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
package uk.trainwatch.app.util.timetable;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.util.UncheckedSQLException;
import uk.trainwatch.util.sql.CUDConsumer;

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

    public ScheduleLocUpdate( Connection con )
    {
        super( con,
               "INSERT INTO timetable.schedule_loc (scheduleid,tiploc) values (?,timetable.tiploc(?))",
               null,
               "DELETE FROM timetable.schedule_loc WHERE scheduleid=? AND tiploc=timetable.tiploc(?)"
        );
    }

    @Override
    public void accept( Schedule t )
    {
        long scheduleId;
        try
        {
            scheduleId = getScheduleId( t );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }

        switch( t.getTransactionType() )
        {
            case NEW:
                insert( scheduleId, t );
                break;

            case REVISE:
                // For revising just delete then insert
                delete( scheduleId );
                insert( scheduleId, t );
                break;

            case DELETE:
                delete( scheduleId );
                break;
        }
    }

    private long getScheduleId( Schedule t )
            throws SQLException
    {
        try( PreparedStatement s = getConnection().
                prepareStatement(
                        "SELECT id FROM timetable.schedule WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND stpIndicator=?"
                ) )
        {
            s.setString( 1, t.getTrainUid().
                         getId() );
            s.setDate( 2, Date.valueOf( t.getRunsFrom() ) );
            s.setInt( 3, t.getStpInd().
                      ordinal() );
            try( ResultSet rs = s.executeQuery() )
            {
                if( rs == null || !rs.next() )
                {
                    return 0;
                }
                return rs.getLong( 1 );
            }
        }
    }

    private void delete( long scheduleId )
    {
        try
        {
            PreparedStatement s = getDelete();
            s.setLong( 1, scheduleId );
            s.executeUpdate();
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    private void insert( long scheduleId, Schedule t )
    {
        t.stream().
                map( Location::getLocation ).
                map( Tiploc::getKey ).
                // distinct(). causes duplicate keys so use a set
                collect( Collectors.toSet()).
                forEach( tiploc ->
                        {
                            try
                            {
                                PreparedStatement s = getInsert();
                                s.setLong( 1, scheduleId );
                                s.setString( 2, tiploc );
                                s.executeUpdate();
                            }
                            catch( SQLException ex )
                            {
                                throw new UncheckedSQLException( ex );
                            }
                } );
    }
}
