/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.svg;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public class TileToSVG
        implements IntFunction<String>
{

    private final Connection con;
    private final int nx, ox, oy;
    private final int tileSize;

    private PreparedStatement gvPS;

    public TileToSVG( Connection con, int nx, int ox, int oy, int tileSize )
    {
        this.con = con;
        this.nx = nx;
        this.ox = ox;
        this.oy = oy;
        this.tileSize = tileSize;
    }

    @Override
    public String apply( int tileId )
    {
        try {
            int x = tileId % nx, y = tileId / nx;

            final Rectangle2D tileBounds = new Rectangle2D.Double( (x * tileSize) - ox, (y * tileSize) - oy, tileSize, tileSize );

            try( StringWriter fw = new StringWriter() ) {

                // Flag to set if we have anything other than the prolog to persist
                boolean notEmpty = false;

                XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter( fw );
                try {
                    w.setDefaultNamespace( "http://www.w3.org/2000/svg" );
                    w.writeStartElement( "http://www.w3.org/2000/svg", "svg" );
                    w.writeAttribute( "version", "1.1" );

                    w.writeAttribute( "height", String.valueOf( tileSize ) );
                    w.writeAttribute( "width", String.valueOf( tileSize ) );

                    w.writeStartElement( "desc" );
                    w.writeAttribute( "style", "-webkit-tap-highlight-color: rgba(0, 0, 0, 0);" );
                    w.writeCharacters( "Created by TrainWatch uktra.in" );
                    w.writeEndElement();

                    w.writeStartElement( "defs" );
                    w.writeAttribute( "style", "-webkit-tap-highlight-color: rgba(0, 0, 0, 0);" );
                    w.writeEndElement();

                    notEmpty |= exportGraphicVector( w, con, tileBounds );

                    w.writeEndElement();
                }
                finally {
                    w.writeEndDocument();
                    w.close();
                }

                // Only store tiles that have content
                return notEmpty ? fw.toString() : null;
            }
        }
        catch( SQLException |
               IOException |
               XMLStreamException ex ) {
            throw new RuntimeException( ex );
        }
    }

    private boolean exportGraphicVector( XMLStreamWriter w, Connection con, Rectangle2D tileBounds )
            throws SQLException,
                   XMLStreamException
    {
        gvPS = SQL.prepare( gvPS, con,
                            "SELECT layer,geom"
                            + " FROM tpnm.feat_graphicvector"
                            + " WHERE geom && (tpnm.ST_MakeEnvelope(?,?,?,?,4258)::geometry)",
                            (int) tileBounds.getMinX(),
                            (int) tileBounds.getMinY(),
                            (int) tileBounds.getMaxX(),
                            (int) tileBounds.getMaxY()
        );

        // Create a map of each layer
        Map<Integer, String> layers = SQL.stream( gvPS, rs -> new Object()
        {
            int layer = rs.getInt( 1 );
            LineString line = (LineString) ((PGgeometry) rs.getObject( 2 )).getGeometry();
        } ).
                collect( Collectors.toMap( o -> o.layer,
                                           o -> lineStringToString( o.line, tileBounds ),
                                           ( a, b ) -> a + b ) );

        // Bail out if nothing to do, notifying caller
        if( layers.isEmpty() ) {
            return false;
        }

        layers.entrySet().
                forEach( e -> {
                    try {
                        int layer = e.getKey();

                        w.writeStartElement( "path" );
                        w.writeAttribute( "fill", "#ffffff" );
                        w.writeAttribute( "stroke", "#000000" );
                        w.writeAttribute( "d", e.getValue() + "Z" );
                        w.writeEndElement();
                    }
                    catch( XMLStreamException ex ) {
                        throw new RuntimeException( ex );
                    }

                } );

        return true;
    }

    private String lineStringToString( LineString line, Rectangle2D tileBounds )
    {
        return Stream.of( line.getPoints() ).
                map( p -> (int) (p.getX() - tileBounds.getMinX()) + "," + (int) (p.getY() - tileBounds.getMinY()) ).
                collect( Collectors.joining( "L", "M", "" ) );
    }

}
