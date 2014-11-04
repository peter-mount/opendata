/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.nrod.timetable.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.sql.CUDConsumer;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Imports mapping of a schedule into the schedule_loc table which maps every tiploc within a schedule to the schedule.
 * <p>
 * We can then use that table to retrieve all schedules for a specific location.
 * <p>
 * @author Peter T Mount
 */
public class ScheduleLocUpdate
        extends CUDConsumer<Schedule>
{

    private final PreparedStatement getScheduleId;
    private final PreparedStatement getScheduleIdShort;

    private final SQLBiConsumer<Long, String> delete;
    private final SQLBiConsumer<Long, String> insert;

    private final BiConsumer<Long, String> newSchedules;
    private final BiConsumer<Long, String> updateSchedules;
    private final BiConsumer<Long, String> deleteSchedules;

    public ScheduleLocUpdate( Connection con )
    {
        super( con,
               "INSERT INTO timetable.schedule_loc (scheduleid,tiploc) values (?,timetable.tiploc(?))",
               null,
               "DELETE FROM timetable.schedule_loc WHERE scheduleid=? AND tiploc=timetable.tiploc(?)"
        );

        // used to retrieve the scheduleId(s) for a Schedule
        try
        {
            // For updates
            getScheduleId = con.prepareStatement(
                    "SELECT id FROM timetable.schedule WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND runsto=? AND dayrun=?"
            );

            // Delete
            getScheduleIdShort = con.prepareStatement(
                    "SELECT id FROM timetable.schedule WHERE trainuid=timetable.trainuid(?) AND runsfrom=?"
            );

        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }

        // Delete a schedule
        delete = (scheduleId, tiploc) ->
        {
            PreparedStatement s = getDelete();
            s.setLong( 1, scheduleId );
            s.setString( 2, tiploc );
            s.executeUpdate();
        };

        insert = (scheduleId, tiploc) ->
        {
            PreparedStatement s = getInsert();
            s.setLong( 1, scheduleId );
            s.setString( 2, tiploc );
            s.executeUpdate();
        };

        newSchedules = SQLBiConsumer.guard( insert.andThen( (i, l) -> inserted() ) );

        updateSchedules = SQLBiConsumer.guard( delete.andThen( insert ).
                andThen( (i, l) -> updated() )
        );

        deleteSchedules = SQLBiConsumer.guard( delete.
                andThen( (i, l) -> deleted() )
        );

    }

    @Override
    public void accept( Schedule schedule )
            throws SQLException
    {
        TransactionType tx = schedule.getTransactionType();

        if( tx == TransactionType.NEW || tx == TransactionType.REVISE )
        {
            Consumer<String> func = null;
            if( tx == TransactionType.NEW )
            {
                // Use getCurrentScheduleId as we've just inserted it so no need to do a select
                // to get at the id, it'll be in the sequence.
                long scheduleId = SQL.currval( getConnection(), "timetable.schedule_id_seq" );
                func = tiploc -> newSchedules.accept( scheduleId, tiploc );
            }
            else if( tx == TransactionType.REVISE )
            {
                // For revising just delete then insert
                long scheduleId = getScheduleIdUpdate( schedule );
                func = tiploc -> updateSchedules.accept( scheduleId, tiploc );
            }

            tiplocs( schedule ).
                    forEach( func );
        }
        else if( tx == TransactionType.DELETE )
        {
            if( schedule.getRunsTo() == null )
            {
                // As we are deleting multiple schedules (i.e. truncated record when STPInd is C)
                // run through each scheduleId returned
                getScheduleIdDelete( schedule ).
                        forEach( SQLConsumer.guard(
                                        sid -> tiplocs( schedule ).
                                        forEach( tiploc -> deleteSchedules.accept( sid, tiploc ) )
                                ) );
            }
            else
            {
                // We have a schedule so just delete it
                long scheduleId = getScheduleIdUpdate( schedule );
                if( scheduleId != 0 )
                {
                    tiplocs( schedule ).
                            forEach( tiploc -> deleteSchedules.accept( scheduleId, tiploc ) );
                }
            }
        }

        totaled();
    }

    /**
     * Get the schedule's id.
     * <p>
     * This is valid for updates only as we have to search the table for it and it's specific to the schedule's range
     * <p>
     * @param t <p>
     * @return
     */
    private long getScheduleIdUpdate( Schedule t )
            throws SQLException
    {
        getScheduleId.setString( 1, t.getTrainUid().
                                 toString() );
        getScheduleId.setDate( 2, Date.valueOf( t.getRunsFrom() ) );
        getScheduleId.setDate( 3, Date.valueOf( t.getRunsTo() ) );
        getScheduleId.setInt( 4, t.getDaysRun().
                              getDaysRunning() );

        try( ResultSet rs = getScheduleId.executeQuery() )
        {
            if( rs == null || !rs.next() )
            {
                return 0;
            }
            return rs.getLong( 1 );
        }
    }

    /**
     * Get the schedule's id.
     * <p>
     * This is valid for update and deletes only as we have to search the table for it.
     * <p>
     * @param t <p>
     * @return
     */
    private Stream<Long> getScheduleIdDelete( Schedule t )
            throws SQLException
    {
        // Are we a full or short
        PreparedStatement s;
        if( t.getRunsTo() == null )
        {
            s = getScheduleIdShort;
        }
        else
        {
            s = getScheduleId;
            s.setDate( 3, Date.valueOf( t.getRunsTo() ) );
            s.setInt( 4, t.getDaysRun().
                      getDaysRunning() );
        }

        s.setString( 1, t.getTrainUid().
                     toString() );
        s.setDate( 2, Date.valueOf( t.getRunsFrom() ) );

        return SQL.stream( s, SQL.LONG_LOOKUP );
    }

    private Stream<String> tiplocs( Schedule t )
            throws SQLException
    {
        return t.stream().
                map( Location::getLocation ).
                map( Tiploc::getKey ).
                // distinct(). causes duplicate keys so use a set
                collect( Collectors.toSet() ).
                stream();
    }

}
