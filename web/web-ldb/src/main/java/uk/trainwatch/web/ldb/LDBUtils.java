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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLBiFunction;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.SQLFunction;
import uk.trainwatch.web.ldb.model.Forecast;
import uk.trainwatch.web.ldb.model.ForecastEntry;
import uk.trainwatch.web.ldb.model.Schedule;
import uk.trainwatch.web.ldb.model.ScheduleEntry;
import uk.trainwatch.web.ldb.model.SearchResult;
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
     * @param prefix  <p>
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

        TrainLocation loc = DarwinReferenceManager.INSTANCE.getLocationRefFromCrs( crs );
        if( loc == null )
        {
            // See if they have used an alternate code
            loc = DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( crs );

            if( loc == null )
            {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else
            {
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
    public static Instant getDepartures( Map<String, Object> req, TrainLocation loc, LocalTime time )
            throws SQLException
    {
        // FIXME allow to be shown whilst at the platform. However this might cause issues
        // with a train sitting at the platform but not on the boards?
        LocalTime timeAfter = time.minusMinutes( 2 );
        LocalTime timeBefore = time.plusHours( 1 );
        boolean midnight = timeBefore.isBefore( timeAfter );
        req.put( "midnight", midnight );

        // Filter a LocalTime to fit between the required times, accounting for midnight
        Predicate<LocalTime> filter = midnight
                ? t -> t.isAfter( timeAfter ) || t.isBefore( timeBefore )
                : t -> t.isAfter( timeAfter ) && t.isBefore( timeBefore );

        final String crs = loc.getCrs();

        try( Connection con = LDBContextListener.getDataSource().getConnection() )
        {
            // must have a working departure
            // order by first of actual departure, estimated then working departure
            List<LDB> departures;
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT *,"
                                                     + "   s.toc AS toc,"
                                                     + "   o.name AS origin,"
                                                     + "   d.name AS destination,"
                                                     + "   f.ts AS ts"
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
                                                     loc.getCrs() ) )
            {
                departures = SQL.stream( ps, LDB.fromSQL ).
                        // Filter only public entries
                        filter( LDB::isPublic ).
                        // Filter those that have departed
                        filter( l -> !l.isDeparted() ).
                        // Filter out those out of range, accounting for midnight
                        filter( l -> filter.test( l.getTime() ) ).
                        // Sort to Darwin rules, accounts for midnight
                        sorted( ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( a.getTime(), b.getTime() ) ).
                        collect( Collectors.toList() );
            }

            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT t.tpl,"
                                                     + " COALESCE(e.dep, e.etdep, e.arr, e.etarr) AS time,"
                                                     + " (e.dep IS NOT NULL OR e.arr IS NOT NULL) AS report,"
                                                     + " s.can"
                                                     + " FROM darwin.schedule_entry s"
                                                     + " INNER JOIN darwin.forecast f ON s.schedule=f.schedule"
                                                     + " INNER JOIN darwin.forecast_entry e ON f.id = e.fid AND s.tpl=e.tpl AND e.ldb=true"
                                                     + " INNER JOIN darwin.tiploc t on e.tpl=t.id"
                                                     + " WHERE f.id=?"
                                                     + " ORDER BY s.id"
            ) )
            {
                departures.forEach( SQLConsumer.guard( dep ->
                {
                    ps.setLong( 1, dep.getId() );

                    dep.setPoints( SQL.stream( ps, CallingPoint.fromSQL ).
                            peek( c ->
                                    {
                                        if( DarwinReferenceManager.INSTANCE.isCrs( crs, c.getTpl() ) )
                                        {
                                            dep.setCanc( c.isCanc() );
                                        }
                            } ).
                            collect( Collectors.toList() ) );
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

    private static final SQLBiConsumer<Connection, Train> forecastFilter = ( con, train ) ->
    {
        if( train.isForecastPresent() && train.isSchedulePresent() )
        {
            train.getForecastEntries().forEach( f ->
            {
                train.getScheduleEntries().
                        stream().
                        filter( s -> s.getTpl().equals( f.getTpl() ) ).
                        filter( s -> Objects.equals( s.getPta(), f.getPta() ) ).
                        filter( s -> Objects.equals( s.getPtd(), f.getPtd() ) ).
                        findAny().
                        ifPresent( f::setScheduleEntry );
            } );
        }
    };

    private static final SQLBiConsumer<Connection, Train> schedules = Schedule.populate.
            andThen( ScheduleEntry.populate );

    private static final SQLBiConsumer<Connection, Train> schedulesArc = Schedule.populateArc.
            andThen( ScheduleEntry.populateArc );

    private static final SQLBiFunction<Connection, Train, Train> getSchedule = ( c, t ) ->
    {
        Schedule.populate.accept( c, t );
        if( !t.isSchedulePresent() )
        {
            Schedule.populateArc.accept( c, t );
        }
        return t;
    };

    private static final SQLBiConsumer<Connection, Train> forecast = schedules.
            andThen( Forecast.populate ).
            andThen( ForecastEntry.populate ).
            andThen( forecastFilter );

    private static final SQLBiConsumer<Connection, Train> forecastArc = schedulesArc.
            andThen( Forecast.populateArc ).
            andThen( ForecastEntry.populateArc ).
            andThen( forecastFilter );

    public static Train getSchedule( String rid )
            throws SQLException
    {
        return getTrain( Schedule.populate, rid, false );
    }

    public static Train getArchivedSchedule( String rid )
            throws SQLException
    {
        return getTrain( Schedule.populateArc, rid, true );
    }

    public static Train getTrain( String rid )
            throws SQLException
    {
        return getTrain( forecast, rid, false );
    }

    public static Train getArchivedTrain( String rid )
            throws SQLException
    {
        return getTrain( forecastArc, rid, true );
    }

    private static Train getTrain( SQLBiConsumer<Connection, Train> c, String rid, boolean archive )
            throws SQLException
    {
        Train train = new Train( rid );

        try( Connection con = LDBContextListener.getDataSource().getConnection() )
        {
            c.accept( con, train );

            if( archive )
            {
                train.setArchived( archive );
            }
        }

        return train;
    }

    /**
     * Searches made before 0500 will do an extra check with the previous SSD.
     *
     * We use 0500 not 0200 for the rare cases of long running trains, specifically sleepers.
     *
     * This should be a rare case so the extra overhead should be minimal.
     */
    private static final LocalTime MIDNIGHT_TIME = LocalTime.of( 5, 0 );

    /**
     * Return a collection of Train's with main schedule populated
     *
     * @param start Start LocalDateTime.
     * @param crs
     * @return
     * @throws SQLException
     */
    public static Collection<Train> search( LocalDateTime start, String crs )
            throws SQLException
    {
        // Restrict to top of the hour. This then limits us to within midnight
        start = start.truncatedTo( ChronoUnit.HOURS );
        java.sql.Date ssd = java.sql.Date.valueOf( start.toLocalDate() );
        LocalTime fromTime = start.toLocalTime();
        java.sql.Time from = java.sql.Time.valueOf( fromTime );
        // to is exclusive so add 1 hour then -1s so we search :00:00 to :59:59
        java.sql.Time to = java.sql.Time.valueOf( fromTime.plusHours( 1 ).minusSeconds( 1 ) );

        // If searching in the early hours also check previous SSD to handle trains starting before midnight.
        boolean midnight = fromTime.isBefore( MIDNIGHT_TIME );

        try( Connection con = LDBContextListener.getDataSource().getConnection() )
        {

            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT f.rid"
                                                     + " FROM darwin.forecast f"
                                                     + " INNER JOIN darwin.forecast_entry e ON f.id=e.fid"
                                                     + " INNER JOIN darwin.location l ON e.tpl=l.tpl"
                                                     + " INNER JOIN darwin.crs c on l.crs=c.id"
                                                     + " WHERE c.crs=?"
                                                     + " AND f.ssd = ?"
                                                     + " AND COALESCE(e.wtd,e.wta,e.wtp) BETWEEN ? AND ?"
                                                     + " UNION "
                                                     + "SELECT f.rid"
                                                     + " FROM darwin.forecastarc f"
                                                     + " INNER JOIN darwin.forecast_entryarc e ON f.id=e.fid"
                                                     + " INNER JOIN darwin.location l ON e.tpl=l.tpl"
                                                     + " INNER JOIN darwin.crs c on l.crs=c.id"
                                                     + " WHERE c.crs=?"
                                                     + " AND f.ssd =?"
                                                     + " AND COALESCE(e.wtd,e.wta,e.wtp) BETWEEN ? AND ?",
                                                     crs, ssd, from, to,
                                                     crs, ssd, from, to
            ) )
            {
                System.out.println( ps );
                Stream<String> stream = SQL.stream( ps, SQL.STRING_LOOKUP );

                // Check across previous midnight?
                if( midnight )
                {
                    ssd = java.sql.Date.valueOf( start.minusDays( 1 ).toLocalDate() );
                    SQL.setParameters( ps,
                                       crs, ssd, from, to,
                                       crs, ssd, from, to );
                    System.out.println( ps );
                    stream = Stream.concat( SQL.stream( ps, SQL.STRING_LOOKUP ), stream );
                }

                // Now translate into Train objects
                return stream.
                        sorted().
                        distinct().
                        map( Train::new ).
                        map( t -> SQLBiFunction.guard( getSchedule ).apply( con, t ) ).
                        filter( Train::isValid ).
                        sorted().
                        collect( Collectors.toList() );
            }
        }
    }

}
