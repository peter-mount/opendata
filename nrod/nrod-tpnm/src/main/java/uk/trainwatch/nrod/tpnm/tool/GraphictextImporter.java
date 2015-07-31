/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.nrod.tpnm.model.Graphictext;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class GraphictextImporter
        extends AbstractImporter<Object, Graphictext>
        implements SQLConsumer<Object>
{

    private PreparedStatement ps;

    public GraphictextImporter( Connection con )
    {
        super( con, 100000 );
    }

    @Override
    protected void process( Graphictext s )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteTable( con, "tpnm", "graphictext" );
        }

        ps = SQL.prepareInsert( ps, con,
                                "tpnm.graphictext",
                                s.getLayers(),
                                s.getAngle(),
                                s.getText(),
                                s.getX(),
                                s.getY(),
                                s.getSize()
        );

        ps.executeUpdate();

    }
}
