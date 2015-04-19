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
import java.sql.SQLException;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.model.ScheduleJsonBuilder;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.sql.CUDConsumer;

/**
 * Imports a Schedule into the schedule table
 * <p>
 * @author Peter T Mount
 */
public class ScheduleDBUpdate
        extends CUDConsumer<Schedule>
{

    private final ScheduleJsonBuilder jsonBuilder = new ScheduleJsonBuilder();

    private final PreparedStatement deleteShort;

    public ScheduleDBUpdate( Connection con )
    {
        super( con,
                "INSERT INTO timetable.schedule"
                + " (trainuid,runsfrom,runsto,dayrun, stpindicator,bankholrun,trainstatus,traincategory,trainidentity,headcode,servicecode,atoccode,schedule)"
                + " VALUES (timetable.trainuid(?),?,?,?,?,?,?,?,?,?,?,?,?)",
                "UPDATE timetable.schedule"
                + " SET stpindicator=?, bankholrun=?, trainstatus=?, traincategory=?, trainidentity=?, headcode=?,"
                + " servicecode=?, atoccode=?, schedule=?"
                + " WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND runsto=? AND dayrun=?",
                "DELETE FROM timetable.schedule"
                + " WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND runsto=? AND dayrun=?"
        );

        try
        {
            deleteShort = con.prepareStatement( "DELETE FROM timetable.schedule"
                    + " WHERE trainuid=timetable.trainuid(?) AND runsfrom=?" );
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    /**
     * The key fields common to insert, update & delete
     * <p>
     * @param t
     * @param s
     * @param i
     * <p>
     * @return
     * <p>
     * @throws SQLException
     */
    private int setKey( Schedule t, PreparedStatement s, int i )
            throws SQLException
    {
        s.setString( i++, t.getTrainUid().toString() );
        s.setDate( i++, Date.valueOf( t.getRunsFrom() ) );
        s.setDate( i++, Date.valueOf( t.getRunsTo() ) );
        s.setInt( i++, t.getDaysRun().getDaysRunning() );
        return i;
    }

    /**
     * The rest of the fields used only in insert & update
     * <p>
     * @param t
     * @param s
     * @param i
     * <p>
     * @return
     * <p>
     * @throws SQLException
     */
    private int setVal( Schedule t, PreparedStatement s, int i )
            throws SQLException
    {
        s.setInt( i++, t.getStpInd().ordinal() );
        s.setInt( i++, t.getBankHolidayRunning().ordinal() );
        s.setInt( i++, t.getTrainStatus().ordinal() );
        s.setInt( i++, t.getTrainCategory().ordinal() );
        s.setString( i++, t.getTrainIdentity() );
        s.setString( i++, t.getHeadCode() );
        s.setString( i++, t.getServiceCode() );
        s.setInt( i++, t.getAtocCode().ordinal() );

        // Generate json for the actual schedule
        s.setString( i++, JsonUtils.encode( jsonBuilder.visit( t ) ) );

        return i;
    }

    @Override
    public void accept( Schedule t )
            throws SQLException
    {
        PreparedStatement s;
        int i;

        switch( t.getTransactionType() )
        {
            case NEW:
                s = getInsert();
                i = setKey( t, s, 1 );
                setVal( t, s, i );
                s.executeUpdate();
                inserted();
                break;

            case REVISE:
                s = getInsert();
                i = setKey( t, s, 1 );
                setVal( t, s, i );
                s.executeUpdate();
                updated();
                break;

            case DELETE:
                if( t.getRunsTo() == null )
                {
                    // Handle case where a STPCancellation doesn't have all fields
                    // In this instance it deletes all schedules for a train from a specified date
                    s = deleteShort;
                    s.setString( 1, t.getTrainUid().toString() );
                    s.setDate( 2, Date.valueOf( t.getRunsFrom() ) );
                } else
                {
                    s = getDelete();
                    setKey( t, s, 1 );
                }
                s.executeUpdate();
                deleted();
                break;
        }

        totaled();
    }

}
