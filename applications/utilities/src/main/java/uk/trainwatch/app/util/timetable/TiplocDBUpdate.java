/*
 * Copyright 2014 peter.
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAction;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAmend;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCDelete;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCInsert;

/**
 * Handles updating the tiploc table in the database
 *
 * @author peter
 */
public class TiplocDBUpdate
        implements Consumer<TIPLOCAction>
{

    private static final Logger LOG = Logger.getLogger( TiplocDBUpdate.class.getName() );

    private final Connection con;
    private PreparedStatement insStat;
    private PreparedStatement updStat;
    private PreparedStatement delStat;
    private int inserted, updated, deleted, total;

    public TiplocDBUpdate( Connection con )
    {
        this.con = con;
    }

    @Override
    public void accept( TIPLOCAction t )
    {
        try {
            total++;

            if( t instanceof TIPLOCInsert ) {
                insert( (TIPLOCInsert) t );
            } else if( t instanceof TIPLOCAmend ) {
                alter( (TIPLOCAmend) t );
            } else if( t instanceof TIPLOCDelete ) {
                delete( (TIPLOCDelete) t );
            }
        } catch( SQLException ex ) {
            // Only log if it's not a duplicate key - only occurs if we try to insert an existing key & we'll treat that as fine
            String m = ex.getMessage();
            if( m == null || !m.contains( "duplicate key" ) ) {
                LOG.log( Level.SEVERE, null, ex );
            }
        }
    }

    private void insert( TIPLOCInsert t )
            throws SQLException
    {
        if( insStat == null ) {
            insStat = con.
                    prepareStatement( "INSERT INTO timetable.tiploc (tiploc,caps,nalco,nlccheck,tpsdesc,stanox,crs,description) VALUES (?,?,?,?,?,?,?,?)" );
        }

        insStat.setString( 1, t.getTiploc().
                           getKey() );
        insStat.setInt( 2, t.getCaps() );
        insStat.setInt( 3, t.getNalco() );
        insStat.setString( 4, t.getNlcCheck() );
        insStat.setString( 5, t.getTpsDescription() );
        insStat.setLong( 6, t.getStanox() );
        insStat.setString( 7, t.getCrs() );
        insStat.setString( 8, t.getDescription() );
        insStat.executeUpdate();

        inserted++;
    }

    private void alter( TIPLOCAmend t )
            throws SQLException
    {
        if( updStat == null ) {
            updStat = con.
                    prepareStatement( "ALTER timetable.tiploc SET caps=?,nalco=?,nlccheck=?,tpsdesc=?,stanox=?,crs=?,description=? WHERE tiploc=?" );
        }

        updStat.setInt( 1, t.getCaps() );
        updStat.setInt( 2, t.getNalco() );
        updStat.setString( 3, t.getNlcCheck() );
        updStat.setString( 4, t.getTpsDescription() );
        updStat.setLong( 5, t.getStanox() );
        updStat.setString( 6, t.getCrs() );
        updStat.setString( 7, t.getDescription() );

        updStat.setString( 8, t.getTiploc().
                           getKey() );
        updStat.executeUpdate();

        updated++;
    }

    private void delete( TIPLOCDelete t )
            throws SQLException
    {
        if( delStat == null ) {
            delStat = con.
                    prepareStatement( "DELETE FROM timetable.tiploc WHERE tiploc=?" );
        }

        delStat.setString( 1, t.getTiploc().
                           getKey() );
        updStat.executeUpdate();

        deleted++;
    }

    public void log()
    {
        LOG.log( Level.INFO, () -> total + " Tiplocs processed. " + inserted + " new, " + updated + " amended, " + deleted + " deleted." );
    }

}
