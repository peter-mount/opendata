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
import uk.trainwatch.util.UncheckedSQLException;
import uk.trainwatch.util.sql.CUDConsumer;

/**
 * Imports a Schedule into the schedule table
 * <p>
 * @author Peter T Mount
 */
public class ScheduleDBUpdate
        extends CUDConsumer<Schedule>
{

    public ScheduleDBUpdate( Connection con )
    {
        super( con,
               "INSERT INTO timetable.schedule"
               + " (trainuid,runsfrom,stpindicator,runsto,dayrun,bankholrun,trainstatus,traincategory,trainidentity,headcode,servicecode,atoccode,schedule)"
               + " VALUES (timetable.trainuid(?),?,?,?,?,?,?,?,?,?,?,?,?)",
               "UPDATE timetable.schedule"
               + " SET runsto=?, dayrun=?, bankholrun=?, trainstatus=?, traincategory=?, trainidentity=?, headcode=?,"
               + " servicecode=?, atoccode=?, schedule=?"
               + " WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND stpindicator=?",
               "DELETE FROM timetable.schedule"
               + " WHERE trainuid=timetable.trainuid(?) AND runsfrom=? AND stpindicator=?"
        );
    }

    private int setKey( Schedule t, PreparedStatement s, int i )
            throws SQLException
    {
        s.setString( i++, t.getTrainUid().
                     getId() );
        s.setDate( i++, Date.valueOf( t.getRunsFrom() ) );
        s.setInt( i++, t.getStpInd().
                  ordinal() );
        s.setDate( i++, Date.valueOf( t.getRunsTo() ) );
        s.setInt( i++, t.getDaysRun().
                  getDaysRunning() );
        s.setInt( i++, t.getBankHolidayRunning().
                  ordinal() );
        return i;
    }

    private int setVal( Schedule t, PreparedStatement s, int i )
            throws SQLException
    {
        s.setInt( i++, t.getTrainStatus().
                  ordinal() );
        s.setInt( i++, t.getTrainCategory().
                  ordinal() );
        s.setString( i++, t.getTrainIdentity() );
        s.setString( i++, t.getHeadCode() );
        s.setString( i++, t.getServiceCode() );
        s.setInt( i++, t.getAtocCode().
                  ordinal() );
        s.setString( i++, "{}" );
        return i;
    }

    @Override
    public void accept( Schedule t )
    {
        try
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
                    s = getDelete();
                    setKey( t, s, 1 );
                    s.executeUpdate();
                    deleted();
                    break;
            }
            
            totaled();
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

}
