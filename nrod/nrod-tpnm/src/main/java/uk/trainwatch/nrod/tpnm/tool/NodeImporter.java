/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.nrod.tpnm.model.Node;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class NodeImporter
        extends AbstractWayImporter<Object, Node>
        implements SQLConsumer<Object>
{

    private PreparedStatement ps;

    public NodeImporter( Connection con )
    {
        super( con, 2400 );
    }

    @Override
    protected void process( Node n )
            throws SQLException
    {
        if( n.getPoint() != null ) {

            if( isFirst() ) {
                SQL.deleteTable( con, "tpnm", "feat_node" );
                SQL.deleteTable( con, "tpnm", "node" );
            }

            ps = SQL.prepareInsert( ps, con,
                                    "tpnm.node",
                                    n.getPoint().getNodeid(),
                                    n.getPoint().getLineid(),
                                    n.getNetx(),
                                    n.getNety(),
                                    n.getNetz(),
                                    n.getLinex(),
                                    n.getLiney(),
                                    n.getLinez(),
                                    n.getKmregionid(),
                                    n.getKmvalue(),
                                    n.getSecondkmregionid(),
                                    n.getSecondkmvalue(),
                                    n.getName(),
                                    n.getAngle()
            );

            ps.executeUpdate();
        }
    }

}
