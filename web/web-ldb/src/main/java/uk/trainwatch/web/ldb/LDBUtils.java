/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.web.ldb.model.Forecast;
import uk.trainwatch.web.ldb.model.ForecastEntry;
import uk.trainwatch.web.ldb.model.Schedule;
import uk.trainwatch.web.ldb.model.ScheduleEntry;
import uk.trainwatch.web.ldb.model.Train;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
public class LDBUtils
{

    /**
     * Resolves a CRS, issuing a not found or redirect to the correct one as needed
     *
     * @param request
     * @param prefix
     *                <p>
     * @return TrainLocation or null if none
     * <p>
     * @throws ServletException
     * @throws IOException
     */
    public static TrainLocation resolveLocation( ApplicationRequest request, String prefix )
            throws ServletException,
                   IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = TrainLocationFactory.INSTANCE.getTrainLocationByCrs( crs );
        if( loc == null ) {
            // See if they have used an alternate code
            loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( crs );

            if( loc == null ) {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else {
                // Redirect to the correct page
                request.getResponse().
                        sendRedirect( prefix + loc.getCrs() );
            }
        }
        return loc;
    }

    /**
     * Timetabled departures
     * <p>
     * @param req
     * @param loc  <p>
     * @param time
     *             <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public static Instant getDepartures( Map<String, Object> req, TrainLocation loc, LocalTime time )
            throws SQLException
    {
        // FIXME allow to be shown whilst at the platform. However this might cause issues
        // with a train sitting at the platform but not on the boards?
        LocalTime timeAfter = time.minusMinutes( 2 );
        LocalTime timeBefore = timeAfter.plusHours( 1 );
        boolean midnight = time.getHour() > 20;
        req.put( "midnight", midnight );

        try( Connection con = LDBContextListener.getDataSource().getConnection() ) {
            // must have a working departure
            // order by first of actual departure, estimated then working departure
            List<LDB> departures;
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT *,"
                                                     + "   s.toc AS toc,"
                                                     + "   o.name AS origin,"
                                                     + "   d.name AS destination,"
                                                     + "   f.ts AS ts"
                                                     //+ " COALESCE(fe.dep,fe.etdep,fe.wtd,fe.arr,fe.etarr,fe.wta) as time,"
                                                     + " FROM darwin.forecast f"
                                                     + " INNER JOIN darwin.schedule s ON f.schedule=s.id"
                                                     + " INNER JOIN darwin.location d ON s.dest=d.tpl"
                                                     + " INNER JOIN darwin.location o ON s.origin=o.tpl"
                                                     + " INNER JOIN darwin.forecast_entry fe ON f.id=fe.fid"
                                                     + " INNER JOIN darwin.location l ON fe.tpl=l.tpl"
                                                     + " INNER JOIN darwin.crs c ON l.crs=c.id"
                                                     // Only for this station
                                                     + " WHERE c.crs=?"
                                                     // Flagged for display
                                                     + " AND fe.ldb=TRUE"
                                                     // all non-passes required so we can use Terminates/Starts here etc
                                                     + " AND fe.wtp IS NULL",
                                                     // Order by the first valid time
                                                     //+ " ORDER BY COALESCE(fe.dep,fe.etdep,fe.wtd)",
                                                     loc.getCrs() ) ) {
                departures = SQL.stream( ps, LDB.fromSQL ).
                        // Filter only public entries
                        filter( LDB::isPublic ).
                        // Filter those that have departed
                        filter( l -> !l.isDeparted() ).
                        // Filter out those out of range, accounting for midnight
                        filter( l -> {
                            boolean r = l.getTime().isAfter( timeAfter );
                            boolean b = l.getTime().isBefore( timeBefore );

                            return midnight ? r | b : r & b;
                        } ).
                        // Sort to Darwin rules, accounts for midnight
                        sorted( ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( a.getTime(), b.getTime() ) ).
                        collect( Collectors.toList() );
            }

            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT t.tpl, COALESCE(e.dep, e.etdep, e.arr, e.etarr) AS time"
                                                     + " FROM darwin.schedule_entry s"
                                                     + " INNER JOIN darwin.forecast f ON s.schedule=f.schedule"
                                                     + " INNER JOIN darwin.forecast_entry e ON f.id = e.fid AND s.tpl=e.tpl AND e.ldb=true"
                                                     + " INNER JOIN darwin.tiploc t on e.tpl=t.id"
                                                     + " WHERE f.id=?"
                                                     + " ORDER BY s.id"
            ) ) {
                departures.forEach( SQLConsumer.guard( dep -> {
                    ps.setLong( 1, dep.getId() );
                    dep.setPoints( SQL.stream( ps, CallingPoint.fromSQL ).collect( Collectors.toList() ) );
                } ) );
            }

            req.put( "departures", departures );

            req.put( "stationMessages",
                     StationMessageManager.INSTANCE.
                     getMessages( loc.getCrs() ).
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

    private static final SQLBiConsumer<Connection, Train> populator = Schedule.populate.
            andThen( ScheduleEntry.populate ).
            andThen( Forecast.populate ).
            andThen( ForecastEntry.populate );

    public static Train getTrain( String rid )
            throws SQLException
    {
        Train train = new Train( rid );

        try( Connection con = LDBContextListener.getDataSource().getConnection() ) {
            populator.accept( con, train );
        }

        return train;
    }
}
