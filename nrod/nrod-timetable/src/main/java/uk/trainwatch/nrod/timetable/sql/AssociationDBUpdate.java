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

import uk.trainwatch.util.sql.CUDConsumer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.UncheckedSQLException;

/**
 *
 * @author Peter T Mount
 */
public class AssociationDBUpdate
        extends CUDConsumer<Association>
{

    public AssociationDBUpdate( Connection con )
    {
        super( con,
               "INSERT INTO timetable.association"
               + " (mainuid,assocuid,startdt,enddt,assocdays,assoccat,assocdateind,tiploc,baselocsuff,assoclocsuff,assoctype,stpind)"
               + " values"
               + " (timetable.trainuid(?),timetable.trainuid(?),?,?,?,?,?,timetable.tiploc(?),?,?,?,?)",
               null,
               "DELETE FROM timetable.association"
               + " WHERE mainuid=timetable.trainuid(?) AND assocuid=timetable.trainuid(?) AND startdt=? AND enddt=? AND assocdays=?"
               + " AND assoccat=? AND assocdateind=? AND tiploc=tiploc(?)"
               + " AND baselocsuff=? AND assoclocsuff=? AND assoctype=? AND stpind=?"
        );
    }

    @Override
    public void accept( Association t )
    {
        try
        {
            totaled();

            switch( t.getTransactionType() )
            {
                case NEW:
                    execute( t, getInsert() );
                    inserted();
                    break;

                case DELETE:
                    execute( t, getDelete() );
                    deleted();
                    break;

                case REVISE:
                    // Association's don't update, just create/delete
                    break;
            }
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    private void execute( Association t, PreparedStatement s )
            throws SQLException
    {
        int i = 1;
        s.setString( i++, t.getMainTrainUID() );
        s.setString( i++, t.getAssocTrainUID() );
        TimeUtils.setDate( s, i++, t.getStartDate() );
        TimeUtils.setDate( s, i++, t.getEndDate() );
        s.setInt( i++, t.getAssocDays().
                  getDaysRunning() );
        s.setInt( i++, t.getAssociationCategory().
                  ordinal() );
        s.setInt( i++, t.getAssocDateInd().
                  ordinal() );
        s.setString( i++, t.getAssocLocation().
                     getKey() );
        s.setString( i++, t.getBaseLocSuffix() );
        s.setString( i++, t.getAssocLocSuffix() );
        s.setInt( i++, t.getAssocType().
                  ordinal() );
        s.setInt( i++, t.getStpIndicator().
                  ordinal() );

        s.executeUpdate();
    }
}
