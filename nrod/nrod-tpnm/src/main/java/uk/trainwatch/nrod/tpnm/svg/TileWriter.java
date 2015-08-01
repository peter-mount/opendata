/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.svg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;

/**
 *
 * @author peter
 */
public class TileWriter
        implements SQLBiConsumer<Integer, String>
{

    private final Connection con;
    private final int nx;

    private PreparedStatement tilePS;

    public TileWriter( Connection con, int nx )
    {
        this.con = con;
        this.nx = nx;
    }

    @Override
    public void accept( Integer t, String u )
            throws SQLException
    {
        if( u != null && !u.isEmpty() ) {
            tilePS = SQL.prepareInsert( tilePS, con, "tpnm.tile", t % nx, t / nx, u );
            tilePS.executeUpdate();
        }
    }

}
