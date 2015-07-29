/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.nrod.tpnm.model.Directed;
import uk.trainwatch.nrod.tpnm.model.Point;
import uk.trainwatch.nrod.tpnm.model.Way;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public abstract class AbstractWayImporter
{

    protected final Connection con;

    public AbstractWayImporter( Connection con )
    {
        this.con = con;
    }

    protected final void importWay( Way way, String type, Object... args )
            throws SQLException
    {
        // Don't create null/empty ways
        if( way != null && !way.getPoint().isEmpty() ) {
            try( PreparedStatement wayps = SQL.prepare( con, "SELECT tpnm.create" + type + "way(?)", args );
                 CallableStatement pointps = SQL.prepareCall( con, "{call tpnm.waypoint(?,?)}" ) ) {

                long wayId = SQL.stream( wayps, SQL.LONG_LOOKUP ).findAny().get();

                way.getPoint().
                        forEach( SQLConsumer.guard( pt -> SQL.executeBatch( pointps, wayId, (long) pt.getNodeid() ) ) );
                pointps.executeBatch();
            }
        }
    }

    protected final Long importDirected( Directed d )
            throws SQLException
    {
        if( d == null ) {
            return null;
        }
        else {
            Point st = d.getStart().getPoint();
            Point ed = d.getEnd().getPoint();
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "INSERT INTO tpnm.directed (startid,endid) VALUES (?,?)",
                                                     st.getNodeid(),
                                                     ed.getNodeid()
            ) ) {
                ps.executeUpdate();
            }

            return SQL.currval( con, "tpnm.directed_id_seq" );
        }
    }

}
