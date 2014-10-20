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

import uk.trainwatch.util.sql.CUDConsumer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger( AssociationDBUpdate.class.getName() );

    public AssociationDBUpdate( Connection con )
    {
        super( con,
               "INSERT INTO timetable.association (mainuid,assocuid,startdt,enddt,assocdays,assoccat,assocdateind,tiploc,baselocsuff,assoclocsuff,assoctype,stpind) values (?,?,?,?,?,?,?,?,?,?,?,?)",
               null,
               "DELETE FROM timetable.association WHERE mainuid=?, assocuid=?, startdt=?, enddt=?, assocdays=?, assoccat=?, assocdateind=?,tiploc=?"
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
                    insert( t );
                    break;

                case DELETE:
                    delete( t );
                    break;

                case REVISE:
                    // Association's don't update, just create/delete
                    break;
            }
        }
        catch( SQLException ex )
        {
            // Only Throw if it's not a duplicate key, can happen on insert
            String m = ex.getMessage();
            if( m == null || !m.contains( "duplicate key" ) )
            {
                throw new UncheckedSQLException( ex );
            }
            else
            {
                // Log as required
                LOG.log( Level.FINE, null, ex );
            }
        }
    }

    private void insert( Association t )
            throws SQLException
    {
        PreparedStatement s = getInsert();
        
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
        inserted();
    }

    private void delete( Association t )
            throws SQLException
    {
        PreparedStatement s = getInsert();
        
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
        
        s.executeUpdate();
        deleted();
    }
}
