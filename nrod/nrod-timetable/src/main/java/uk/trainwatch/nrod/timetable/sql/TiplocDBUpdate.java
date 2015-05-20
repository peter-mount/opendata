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
package uk.trainwatch.nrod.timetable.sql;

import uk.trainwatch.util.sql.CUDConsumer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAction;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAmend;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCDelete;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCInsert;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Handles updating the tiploc table in the database
 * <p>
 * @author peter
 */
public class TiplocDBUpdate
        extends CUDConsumer<TIPLOCAction>
{

    private List<TIPLOCDelete> deletedTiplocs = new ArrayList<>();

    public TiplocDBUpdate( Connection con )
    {
        super( con,
                "INSERT INTO timetable.tiploc (tiploc,caps,nalco,nlccheck,tpsdesc,stanox,crs,description) VALUES (?,?,?,?,?,?,?,?)",
                "UPDATE timetable.tiploc SET caps=?,nalco=?,nlccheck=?,tpsdesc=?,stanox=?,crs=?,description=? WHERE tiploc=?",
                "DELETE FROM timetable.tiploc WHERE tiploc=?"
        );
    }

    @Override
    public void accept( TIPLOCAction t )
            throws SQLException
    {
        totaled();

        if( t instanceof TIPLOCInsert )
        {
            insert( (TIPLOCInsert) t );
        } else if( t instanceof TIPLOCAmend )
        {
            alter( (TIPLOCAmend) t );
        } else if( t instanceof TIPLOCDelete )
        {
            deletedTiplocs.add( (TIPLOCDelete) t );
        }
    }

    private void setStanox( PreparedStatement s, int f, long stanox )
            throws SQLException
    {
        if( stanox == 0 )
        {
            s.setNull( f, Types.BIGINT );
        } else
        {
            s.setLong( f, stanox );
        }
    }

    private void setCrs( PreparedStatement s, int f, String crs )
            throws SQLException
    {
        if( crs == null || crs.isEmpty() || "   ".equals( crs ) )
        {
            s.setNull( f, Types.CHAR );
        } else
        {
            s.setString( f, crs );
        }
    }

    private void insert( TIPLOCInsert t )
            throws SQLException
    {
        PreparedStatement s = getInsert();

        s.setString( 1, t.getTiploc().getKey() );
        s.setInt( 2, t.getCaps() );
        s.setInt( 3, t.getNalco() );
        s.setString( 4, t.getNlcCheck() );
        s.setString( 5, t.getTpsDescription() );
        setStanox( s, 6, t.getStanox() );
        setCrs( s, 7, t.getCrs() );
        s.setString( 8, t.getDescription() );
        s.executeUpdate();

        inserted();
    }

    private void alter( TIPLOCAmend t )
            throws SQLException
    {
        PreparedStatement s = getUpdate();

        s.setInt( 1, t.getCaps() );
        s.setInt( 2, t.getNalco() );
        s.setString( 3, t.getNlcCheck() );
        s.setString( 4, t.getTpsDescription() );
        setStanox( s, 5, t.getStanox() );
        setCrs( s, 6, t.getCrs() );
        s.setString( 7, t.getDescription() );

        s.setString( 8, t.getTiploc().getKey() );
        s.executeUpdate();

        updated();
    }

    public void deletePending()
            throws SQLException
    {
        PreparedStatement s = getDelete();
        deletedTiplocs.forEach( SQLConsumer.guard( t ->
        {
            s.setString( 1, t.getTiploc().getKey() );
            s.executeUpdate();
            deleted();
        } ) );
    }

}
