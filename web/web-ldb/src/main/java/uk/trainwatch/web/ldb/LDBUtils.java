/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.tfl.model.cache.TflLocationCache;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.ldb.cache.LDBCallingPointCache;
import uk.trainwatch.web.ldb.cache.LDBDepartureCache;
import uk.trainwatch.web.ldb.cache.LocationTimeKey;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class LDBUtils
{

    @Inject
    private LDBCallingPointCache callingPointCache;

    @Inject
    private LDBDepartureCache departureCache;

    @Inject
    private DarwinReferenceManager darwinReferenceManager;

    @Inject
    private StationMessageManager stationMessageManager;

    @Inject
    protected TflLocationCache tflLocationCache;

    /**
     * Resolves a CRS, issuing a not found or redirect to the correct one as needed
     *
     * @param request
     * @param prefix  <p>
     * @return TrainLocation or null if none
     * <p>
     * @throws ServletException
     * @throws IOException
     */
    public TrainLocation resolveLocation( ApplicationRequest request, String prefix )
            throws ServletException,
                   IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = darwinReferenceManager.getLocationRefFromCrs( crs );

        // No CRS then check for tfl, this will be LU### or DL###
        if( loc == null && crs.length() == 5 ) {
            loc = tflLocationCache.get( crs );
        }

        if( loc == null ) {
            // See if they have used an alternate code
            loc = darwinReferenceManager.getLocationRefFromTiploc( crs );

            if( loc == null ) {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else {
                // Redirect to the correct page
                request.getResponse().
                        sendRedirect( prefix + loc.getCrs() );
            }

            // Force redirect or error
            return null;
        }
        return loc;
    }

    /**
     * Timetabled departures
     * <p>
     * @param req
     * @param loc  <p>
     * @param time <p>
     * @return <p>
     * @throws SQLException
     */
    public Instant getDepartures( Map<String, Object> req, TrainLocation loc, LocalTime time )
            throws SQLException
    {
        // FIXME allow to be shown whilst at the platform. However this might cause issues
        // with a train sitting at the platform but not on the boards?
        LocalTime timeAfter = time.minusMinutes( 2 );
        LocalTime timeBefore = time.plusHours( 1 );
        boolean midnight = timeBefore.isBefore( timeAfter );
        req.put( "midnight", midnight );

        final String crs = loc.getCrs();

        Collection<LDB> departures = departureCache.getDepartures( new LocationTimeKey( crs, time ) );

        // get calling points for each departure and flag it if its been canceled
//        departures.forEach( SQLConsumer.guard( dep -> {
//            Collection<CallingPoint> points = callingPointCache.getCallingPoints( dep.getId() );
//            dep.setPoints( points );
//            points.forEach( c -> {
//                if( darwinReferenceManager.isCrs( crs, c.getTpl() ) ) {
//                    dep.setCanc( c.isCanc() );
//                }
//            } );
//        } ) );

        req.put( "departures", departures );

        req.put( "stationMessages",
                 stationMessageManager.getMessages( loc.getCrs() ).
                 collect( Collectors.toList() ) );

        // Return the last update time
        Timestamp ts = departures.stream().
                map( LDB::getTs ).
                sorted( ( a, b ) -> b.compareTo( a ) ).
                findAny().
                orElse( new Timestamp( System.currentTimeMillis() ) );

        LocalDateTime dt = ts.toLocalDateTime().truncatedTo( ChronoUnit.MINUTES );
        req.put( "lastUpdated", dt );
        return ZonedDateTime.of( dt, TimeUtils.LONDON ).toInstant();
    }

}
