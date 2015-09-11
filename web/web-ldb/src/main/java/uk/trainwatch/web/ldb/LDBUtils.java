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
import java.time.LocalDate;
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
import uk.trainwatch.web.ldb.cache.LDBDepartureCache;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class LDBUtils
{


    @Inject
    private DarwinReferenceManager darwinReferenceManager;

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

}
