/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import uk.trainwatch.util.cache.CacheKey;
import uk.trainwatch.util.cache.Cache;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordType;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.sql.ScheduleResultSetFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiFunction;
import uk.trainwatch.util.sql.SQLFunction;

/**
 * Manages the retrieval of schedules
 * <p>
 * @author Peter T Mount
 */
public class ScheduleSQL
{

    /**
     * Our cache of Schedule's
     */
    private static final Cache<CacheKey<LocalDate, String>, Schedule> scheduleCache = new Cache<>();
    /**
     * Our cache of Schedule's
     */
    private static final Cache<CacheKey<TrainLocation, LocalDate>, List<Schedule>> stationScheduleCache = new Cache<>(
            200 );
    /**
     * Our cache of Associations
     */
    private static final Cache<CacheKey<LocalDate, String>, List<Association>> mainAssociationCache = new Cache<>();
    /**
     * Cache of associated associations, i.e. mapped to assocuid not mainuid
     */
    private static final Cache<CacheKey<LocalDate, String>, List<Association>> assocAssociationCache = new Cache<>();

    private static volatile DataSource dataSource;

    private static DataSource getDataSource()
            throws SQLException
    {
        if( dataSource == null )
        {
            synchronized( ScheduleSQL.class )
            {
                if( dataSource == null )
                {
                    try
                    {
                        Context ctx = new InitialContext();
                        dataSource = (DataSource) ctx.lookup( "java:/comp/env/jdbc/rail" );
                    }
                    catch( NamingException ex )
                    {
                        throw new SQLException( ex );
                    }
                }
            }
        }
        return dataSource;
    }

    private static Connection getConnection()
            throws SQLException
    {
        return getDataSource().
                getConnection();
    }

    /**
     * Retrieves the applicable {@link Schedule} for a trainUid on a specific date
     * <p>
     * @param date
     * @param uid  <p>
     * @return <p>
     * @throws SQLException
     */
    public static Schedule getSchedule( LocalDate date, String uid )
            throws SQLException
    {
        return scheduleCache.computeSQLIfAbsent( new CacheKey<>( date, uid ),
                                                 () ->
                                                 {
                                                     // Filter to today
                                                     DayOfWeek dow = date.getDayOfWeek();

                                                     Schedule schedule;
                                                     try( Connection con = getConnection() )
                                                     {
                                                         try( PreparedStatement s = con.prepareStatement(
                                                                 "SELECT s.schedule FROM timetable.schedule s"
                                                                 + " INNER JOIN timetable.trainuid i ON s.trainuid=i.id"
                                                                 + " WHERE ? BETWEEN s.runsfrom AND s.runsto"
                                                                 + " AND i.uid=?" ) )
                                                         {
                                                             TimeUtils.setDate( s, 1, date );
                                                             s.setString( 2, uid );

                                                             List<Schedule> schedules = SQL.stream( s,
                                                                                                    ScheduleResultSetFactory.INSTANCE ).
                                                             filter( Objects::nonNull ).
                                                             filter( sh -> sh.getDaysRun().
                                                                     isOnDay( dow ) ).
                                                             collect( Collectors.toList() );

                                                             // Get the active schedule
                                                             schedule = ActiveScheduleFilter.INSTANCE.apply( schedules );
                                                             return schedule;
                                                                 }
                                                     }
                                                 } );
    }

    /**
     * Returns a list of trains associated with this one
     * <p>
     * @param date
     * @param uid  <p>
     * @return <p>
     * @throws SQLException
     */
    public static List<Association> getMainAssociations( LocalDate date, String uid )
            throws SQLException
    {
        return getAssociations( date, uid, "main", mainAssociationCache );
    }

    /**
     * Returns a list of trains this train is associated with
     * <p>
     * @param date
     * @param uid  <p>
     * @return <p>
     * @throws SQLException
     */
    public static List<Association> getOtherAssociations( LocalDate date, String uid )
            throws SQLException
    {
        return getAssociations( date, uid, "assoc", assocAssociationCache );
    }

    /**
     *
     * @param date  Date of run
     * @param uid   Train uid
     * @param assoc either main or assoc to determine which uid is used
     * @param cache The cache to use
     * <p>
     * @return <p>
     * @throws SQLException
     */
    private static List<Association> getAssociations( LocalDate date, String uid,
                                                      String assoc,
                                                      Cache<CacheKey<LocalDate, String>, List<Association>> cache )
            throws SQLException
    {
        return cache.computeSQLIfAbsent( new CacheKey<>( date, uid ),
                                         () ->
                                         {
                                             // Filter to today
                                             DayOfWeek dow = date.getDayOfWeek();

                                             try( Connection con = getConnection() )
                                             {
                                                 try( PreparedStatement s = con.prepareStatement(
                                                         "SELECT"
                                                         + " main.uid as mainuid,"
                                                         + " assoc.uid as assocuid,"
                                                         + " a.startdt, a.enddt, a.assocdays, a.assoccat, a.assocdateind,"
                                                         + " l.tiploc as tiploc,"
                                                         + " a.baselocsuff, a.assoclocsuff,"
                                                         + " a.assoctype, a.stpind"
                                                         + " FROM timetable.association a"
                                                         + " INNER JOIN timetable.trainuid main ON a.mainuid=main.id"
                                                         + " INNER JOIN timetable.trainuid assoc ON a.assocuid=assoc.id"
                                                         + " INNER JOIN timetable.tiploc l ON a.tiploc=l.id"
                                                         + " WHERE ? BETWEEN a.startdt AND a.enddt"
                                                         + " AND " + assoc + ".uid=?" ) )
                                                 {
                                                     TimeUtils.setDate( s, 1, date );
                                                     s.setString( 2, uid );

                                                     return SQL.stream( s, Association.fromSql ).
                                                     filter( Objects::nonNull ).
                                                     // Filter out those that don't apply today
                                                     filter( sh -> sh.getAssocDays().
                                                             isOnDay( dow ) ).
                                                     // Group by assoc uid so we can handle overlays
                                                     collect( Collectors.groupingBy( a -> a.
                                                                     getAssocTrainUID() ) ).
                                                     // Now for each group sort out the applicable entry
                                                     values().
                                                     stream().
                                                     map( AssociationFilter.INSTANCE ).
                                                     collect( Collectors.toList() );
                                                         }
                                             }
                                         } );
    }

    /**
     * Generates a map from an association list keyed by the Tiploc of where the associations occur
     * <p>
     * @param associations <p>
     * @return
     */
    public static Map<Tiploc, List<Association>> groupAssociationsByLocation( List<Association> associations )
    {
        return associations.stream().
                collect( Collectors.groupingBy( sh -> sh.getAssocLocation() ) );
    }

    public static Map<Association, Schedule> getMainSchedules( LocalDate date, List<Association> associations )
            throws SQLException
    {
        return SQLBiFunction.<LocalDate, List<Association>, Map<Association, Schedule>>compose(
                ( d, l ) -> l.stream().
                collect( Collectors.toMap( Function.identity(),
                                           SQLFunction.guard( a -> getSchedule( d, a.getAssocTrainUID() ) )
                        ) )
        ).
                apply( date, associations );
    }

    public static Map<Association, Schedule> getAssociatedSchedules( LocalDate date, List<Association> associations )
            throws SQLException
    {
        return SQLBiFunction.<LocalDate, List<Association>, Map<Association, Schedule>>compose(
                ( d, l ) -> l.stream().
                collect( Collectors.toMap( Function.identity(),
                                           SQLFunction.guard( a -> getSchedule( d, a.getMainTrainUID() ) )
                        ) )
        ).
                apply( date, associations );
    }

    /**
     * Return a list of {@link Schedule}'s at a station.
     * <p>
     * FIXME this could be made more memory efficient by storing just the Location entries for start, at this station &
     * the end, not the entire schedule.
     * <p>
     * @param station
     * @param date    <p>
     * @return <p>
     * @throws SQLException
     */
    public static List<Schedule> getSchedules( TrainLocation station, LocalDate date )
            throws SQLException
    {
        return stationScheduleCache.computeSQLIfAbsent(
                new CacheKey<>( station, date ),
                () ->
                {

                    // Filter to today
                    DayOfWeek dow = date.getDayOfWeek();

                    try( Connection con = getConnection() )
                    {
                        try( PreparedStatement s = con.prepareStatement(
                                "SELECT s.schedule FROM timetable.schedule s"
                                + " INNER JOIN timetable.schedule_loc l ON s.id=l.scheduleid"
                                + " INNER JOIN timetable.tiploc t ON l.tiploc=t.id"
                                + " WHERE t.crs=?"
                                + " AND ? BETWEEN s.runsfrom AND s.runsto" ) )
                        {
                            s.setString( 1, station.getCrs() );
                            TimeUtils.setDate( s, 2, date );

                            // Get our schedules grouped by TrainUID
                            List<Schedule> schedules = SQL.stream( s, ScheduleResultSetFactory.INSTANCE ).
                            filter( Objects::nonNull ).
                            // Filter out those that don't run today
                            filter( sh -> sh.getDaysRun().
                                    isOnDay( dow ) ).
                            // Group by trainUid so we can handle overlays
                            collect( Collectors.groupingBy( sh -> sh.getTrainUid() ) ).
                            // Now for each grouped list sort so that the first one will be the active (most recent) schedule
                            values().
                            stream().
                            map( ActiveScheduleFilter.INSTANCE ).
                            collect( Collectors.toList() );

                            // Sort so they are in chronological order
                            Collections.sort( schedules, new ScheduleLocalTimeAtTiplocComparator( station.getTiploc() ) );

                            return schedules;
                                }
                    }
                } );
    }

    /**
     * Predicate to match a Freight train in the schedule
     */
    public static final Predicate<Schedule> FREIGHT = s -> s.getTrainIdentity().
            equals( "    " );
    /**
     * Predicate to match a Class 5 service (i.e. a train operator moving stock)
     */
    public static final Predicate<Schedule> CLASS5 = s -> s.getTrainIdentity().
            startsWith( "5" );

    /**
     * Predicate to match (hopefully) a public train
     */
    public static final Predicate<Schedule> PUBLIC_TRAIN = FREIGHT.
            or( CLASS5 ).
            negate();

    /**
     * Returns a Predicate that will filter a Schedule that runs within a specified number of hours from a date and departs a specified tiploc.
     * <p>
     * @param tiploc   Tiploc the train departs from
     * @param dateTime DateTime of start of period
     * @param hours    Period duration in hours
     * <p>
     * @return Predicate
     */
    public static final Predicate<Schedule> departuresOnly( String tiploc, LocalDateTime dateTime, long hours )
    {
        LocalDate date = dateTime.toLocalDate();

        Predicate<LocalDateTime> withinHour = TimeUtils.isWithin( dateTime, dateTime.plusHours( hours ) );

        return s ->
        {
            for( Location l: s.getLocations() )
            {
                if( l.getLocation().
                        getKey().
                        equals( tiploc ) )
                {
                    if( l.isPass() )
                    {
                        return false;
                    }

                    // Origin - starts here
                    if( l.getRecordType() == RecordType.LO )
                    {
                        OriginLocation ol = (OriginLocation) l;
                        return withinHour.test( LocalDateTime.of( date, ol.getPublicDeparture() ) );
                    }

                    if( l.getRecordType() == RecordType.LI )
                    {
                        IntermediateLocation il = (IntermediateLocation) l;
                        return withinHour.test( LocalDateTime.of( date, il.getPublicDeparture() ) );
                    }

                    // Terminating, then it's not a departure
                    if( l.getRecordType() == RecordType.LT )
                    {
                        return false;
                    }

                    // Don't return false here as this may be a CR entry for this location
                }
            }
            // Should not get here, so filter it out
            return false;
        };
    }
}
