/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import uk.trainwatch.gis.svg.SvgUtils;
import uk.trainwatch.gis.svg.SvgLineAppender;
import uk.trainwatch.gis.svg.SvgBounds;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.postgis.LineString;
import uk.trainwatch.gis.PostGISUtils;
import uk.trainwatch.gis.svg.SvgDatabaseRenderer;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class GraphicVectorRenderer
        implements SvgDatabaseRenderer
{

    private static final Logger LOG = Logger.getLogger( GraphicVectorRenderer.class.getName() );

    private static class Layer
    {

        public final int layer;
        public final LineString line;

        public Layer( ResultSet rs )
                throws SQLException
        {
            layer = rs.getInt( 1 );
            line = PostGISUtils.getLineString( rs, 2 );
        }

        public int getLayer()
        {
            return layer;
        }

        public LineString getLine()
        {
            return line;
        }

    }

    public void render( XMLStreamWriter w, Connection con, SvgBounds tileBounds )
            throws SQLException,
                   XMLStreamException
    {
        try( PreparedStatement gvPS = SQL.prepare( con,
                                                   "SELECT layer,geom"
                                                   + " FROM tpnm.feat_graphicvector"
                                                   + " WHERE geom && (tpnm.ST_MakeEnvelope(?,?,?,?,4258)::geometry)",
                                                   (int) tileBounds.getMinX(),
                                                   (int) tileBounds.getMinY(),
                                                   (int) tileBounds.getMaxX(),
                                                   (int) tileBounds.getMaxY()
        ) ) {
            LOG.log( Level.INFO, () -> gvPS.toString() );

            // Create a map of each layer
            Map<Integer, List<Layer>> layers = SQL.stream( gvPS, Layer::new ).
                    collect( Collectors.groupingBy( o -> o.layer, TreeMap::new, Collectors.toList() ) );

            if( !layers.isEmpty() ) {
                SvgLineAppender sla = new SvgLineAppender();
                layers.entrySet().
                        forEach( e -> {
                            // Form the SVG Path
                            sla.reset();
                            e.getValue().stream().map( Layer::getLine ).map( tileBounds::transformLineString ).forEach( sla::append );

                            if( !sla.isEmpty() ) {
                                SvgUtils.writePath( w,
                                                    () -> "tpnm_vec tpnm_vec_" + e.getKey(),
                                                    null,//() -> "#ffffff",
                                                    null,//() -> "#000000",
                                                    sla );
                            }

                        } );
            }
        }
    }
}
