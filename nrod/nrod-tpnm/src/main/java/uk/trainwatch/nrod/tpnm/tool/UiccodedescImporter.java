/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import uk.trainwatch.nrod.tpnm.model.Uiccodedesc;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class UiccodedescImporter
        extends AbstractImporter<Object, Uiccodedesc>
        implements SQLConsumer<Object>
{

    private final PreparedStatement ps;

    public UiccodedescImporter( Connection con )
            throws SQLException
    {
        super( con );
        ps = SQL.prepare( con, "INSERT INTO tpnm.uiccodedesc VALUES (?,?,?,?)" );
    }

    @Override
    protected void process( Uiccodedesc v )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteTable( con, "tpnm", "uiccodedesc" );
        }

        SQL.setParameters( ps,
                           v.getId(),
                           v.getText(),
                           new Timestamp( v.getLastmodified().toGregorianCalendar().toInstant().getEpochSecond() ),
                           v.getVanillatext() )
                .executeUpdate();
    }

}
