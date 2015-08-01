/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import uk.trainwatch.gis.svg.SvgBounds;
import java.io.Writer;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import uk.trainwatch.gis.svg.SvgUtils;

/**
 *
 * @author peter
 */
@RequestScoped
public class TPNMWriter
        implements BiConsumer<SvgBounds, Writer>
{

    private static final Logger LOG = Logger.getLogger(TPNMWriter.class.getName() );

    @Inject
    private TPNMRenderer tpnmRenderer;

    @Override
    public void accept( SvgBounds bounds, Writer fw )
    {
        try {
            XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter( fw );

            SvgUtils.writeSvgDocument( w, bounds::getSvgWidth, bounds::getSvgHeight, tpnmRenderer, bounds );
        }
        catch( ClassNotFoundException |
               SQLException |
               XMLStreamException ex ) {
            throw new RuntimeException( ex );
        }
    }

}
