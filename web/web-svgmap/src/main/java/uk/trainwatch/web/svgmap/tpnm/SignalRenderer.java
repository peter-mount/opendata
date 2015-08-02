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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SignalRenderer
        implements SvgDatabaseRenderer
{

    private static final Logger LOG = Logger.getLogger( SignalRenderer.class.getName() );

    @Override
    public void render( XMLStreamWriter w, Connection con, SvgBounds bounds )
            throws SQLException,
                   XMLStreamException
    {
        try( PreparedStatement gvPS = bounds.prepare( con,
                                                      "id, signalid, interlockingsysid, name, zoneid, tmpclosed, vmax, accelerationattail",
                                                      "tpnm.feat_signal",
                                                      "geom" ) ) {
            //LOG.log( Level.INFO, () -> gvPS.toString() );

            SvgLineAppender sla = new SvgLineAppender();

            SQL.stream( gvPS, rs -> new Object()
            {
                long id = rs.getLong( 1 );
                long signalId = rs.getLong( 2 );
                int interlockingsysid = rs.getInt( 3 );
                String name = rs.getString( 4 );
                int zoneid = rs.getInt( 5 );
                boolean tmpclosed = rs.getBoolean( 6 );
                int vmax = rs.getInt( 7 );
                boolean accelerationattail = rs.getBoolean( 8 );
                LineString geom = bounds.transformLineString( PostGISUtils.getLineString( rs, 9 ) );

            } ).
                    forEach( s -> {
                        sla.reset().append( s.geom );
                        if( !sla.isEmpty() ) {
                            if( sla.getCount() == 1 ) {
                                // We have a point
                                SvgUtils.writeCircle( w,
                                                      () -> "tpnm_sig tpnm_sig_point",
                                                      null,
                                                      null,
                                                      s.geom.getFirstPoint()::getX,
                                                      s.geom.getFirstPoint()::getY,
                                                      () -> 5 );
                            }
                            else {
                                // We have a line
                                SvgUtils.writePath( w,
                                                    () -> "tpnm_sig tpnm_sig_area",
                                                    null,
                                                    null,
                                                    sla );
                            }
                        }
                    } );
        }
    }

}
