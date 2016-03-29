/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import uk.trainwatch.gis.svg.SvgBounds;
import java.sql.Connection;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.tomcat.dbcp.dbcp2.DelegatingConnection;
import uk.trainwatch.gis.PostGISUtils;
import uk.trainwatch.gis.svg.SvgRenderer;
import uk.trainwatch.util.config.Database;

/**
 *
 * @author peter
 */
@RequestScoped
public class TPNMRenderer
        implements SvgRenderer
{

    @Database("postgis")
    @Inject
    private DataSource dataSource;

    @Inject
    private GraphicVectorRenderer graphicVectorRenderer;

    @Inject
    private GraphicTextRenderer graphicTextRenderer;

    @Inject
    private SignalRenderer signalRenderer;

    @Override
    public void render( XMLStreamWriter w, SvgBounds bounds )
            throws SQLException,
                   XMLStreamException,
                   ClassNotFoundException
    {

        try( Connection con = dataSource.getConnection() ) {
            Connection con2 = ((DelegatingConnection) con).getInnermostDelegate();
            PostGISUtils.postgisEnable( con2 );

            // Signals first so lines render above them
            signalRenderer.render( w, con2, bounds );

            // The main network map layer, lines buildings etc
            graphicVectorRenderer.render( w, con2, bounds );

            // The main text layer last
            graphicTextRenderer.render( w, con2, bounds );
        }
    }

}
