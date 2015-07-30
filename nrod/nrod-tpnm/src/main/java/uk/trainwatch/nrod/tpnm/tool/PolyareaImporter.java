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
import uk.trainwatch.nrod.tpnm.model.Polyarea;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class PolyareaImporter
        extends AbstractImporter<Object, Polyarea>
        implements SQLConsumer<Object>
{

    private PreparedStatement ps;

    public PolyareaImporter( Connection con )
    {
        super( con, 1000 );
    }

    @Override
    protected boolean filter( Polyarea v )
            throws SQLException
    {
        return !v.getPolyareacorner().isEmpty();
    }

    @Override
    protected void process( Polyarea s )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteIdTable( con, "tpnm", "polyarea" );
        }

        ps = SQL.prepare( ps, con,
                          "INSERT INTO tpnm.polyarea (name,descr,geom) VALUES (?,?, tpnm.ST_SetSRID(tpnm.ST_GeomFromText(?), 4258))",
                          s.getName(),
                          s.getDescription(),
                          s.getPolyareacorner().
                          stream().
                          map( c -> (c.getX()*1.609344) + " " + (c.getY()*-1.609344) ).
                          collect( Collectors.joining( ",", "MULTIPOINT(", ")" ) )
        );

        ps.executeUpdate();

    }
}
