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
        extends AbstractWayImporter
        implements SQLConsumer<Object>
{

    public NodeImporter( Connection con )
    {
        super( con );
    }

    @Override
    public void accept( Object o )
            throws SQLException
    {
        Node n = (Node) o;

        if( n != null && n.getPoint() != null ) {
            try( PreparedStatement ps = SQL.prepareInsert( con,
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
            ) ) {
                ps.executeUpdate();
            }
        }

        con.commit();
    }

}
