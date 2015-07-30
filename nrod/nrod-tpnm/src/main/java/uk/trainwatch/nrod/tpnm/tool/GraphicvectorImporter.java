/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import uk.trainwatch.nrod.tpnm.model.Graphicvector;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class GraphicvectorImporter
        extends AbstractImporter<Object, Graphicvector>
        implements SQLConsumer<Object>
{

    private PreparedStatement ps;

    public GraphicvectorImporter( Connection con )
    {
        super( con, 100000 );
    }

    @Override
    protected void process( Graphicvector s )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteTable( con, "tpnm", "graphicvector" );
        }

        ps = SQL.prepareInsert( ps, con,
                          "tpnm.graphicvector",
                          s.getLayers(),
                          s.getX0().intValue(),
                          s.getY0().intValue(),
                          s.getX1().intValue(),
                          s.getY1().intValue()
        );

        ps.executeUpdate();
        
    }
}
