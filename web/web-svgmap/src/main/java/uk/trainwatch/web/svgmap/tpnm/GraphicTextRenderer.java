/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import uk.trainwatch.gis.svg.SvgUtils;
import uk.trainwatch.gis.svg.SvgBounds;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.postgis.Point;
import uk.trainwatch.gis.PostGISUtils;
import uk.trainwatch.gis.svg.SvgDatabaseRenderer;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class GraphicTextRenderer
        implements SvgDatabaseRenderer
{

    private static final Logger LOG = Logger.getLogger( GraphicTextRenderer.class.getName() );

    @Override
    public void render( XMLStreamWriter w, Connection con, SvgBounds tileBounds )
            throws SQLException,
                   XMLStreamException
    {
        try( PreparedStatement gvPS = SQL.prepare( con,
                                                   "SELECT layer,text,angle,size,geom"
                                                   + " FROM tpnm.feat_graphictext"
                                                   + " WHERE geom && (tpnm.ST_MakeEnvelope(?,?,?,?,4258)::geometry)",
                                                   (int) tileBounds.getMinX(),
                                                   (int) tileBounds.getMinY(),
                                                   (int) tileBounds.getMaxX(),
                                                   (int) tileBounds.getMaxY()
        ) ) {
            LOG.log( Level.INFO, () -> gvPS.toString() );

            // Create a map of each layer
            SQL.stream( gvPS, rs -> new Object()
            {
                int layer = rs.getInt( 1 );
                String text = rs.getString( 2 );
                int angle = rs.getInt( 3 );
                int size = (int) (rs.getInt( 4 ) / tileBounds.getScale());
                Point point = tileBounds.transformPoint( PostGISUtils.getPoint( rs, 5 ) );
            } ).
                    forEach( o -> SvgUtils.writeText(
                                    w,
                                    () -> "tpnm_tl_" + o.layer + " tpnm_ts_" + o.size,
                                    o.point::getX,
                                    o.point::getY,
                                    () -> o.angle,
                                    null,//() -> "#000000",
                                    null,
                                    () -> o.text )
                    );
        }
    }
}
