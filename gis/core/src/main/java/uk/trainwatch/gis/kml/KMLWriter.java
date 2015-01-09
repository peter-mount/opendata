/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.kml;

import javax.xml.stream.XMLStreamException;
import uk.trainwatch.util.xml.UncheckedXMLStreamException;
import uk.trainwatch.util.xml.XMLSaxWriter;
import uk.trainwatch.util.xml.XMLStreamWriterConsumer;

/**
 *
 * @author peter
 */
public class KMLWriter
        implements XMLStreamWriterConsumer<Placemark>
{

    private final String name;
    private boolean first = true;

    public KMLWriter( String name )
    {
        this.name = name;
    }

    @Override
    public void accept( XMLSaxWriter w, Placemark p )
    {
        try
        {
            if( first )
            {
                first = false;

                w.writeStartDocument( "utf-8", "1.0" );
                w.setPrefix( "kml", "http://www.opengis.net/kml/2.2" );
                w.writeStartElement( "http://www.opengis.net/kml/2.2", "kml" );
                w.writeNamespace( "kml", "http://www.opengis.net/kml/2.2" );
                w.writeStartElement( "Document" );
                w.writeStartElement( "Folder" );
                w.writeElement( "name", name );

                Schema schema = new Schema();
                schema.setName( name );
                schema.add( p );
                Schema.toXML.accept( w, schema );
            }

            Placemark.toXML.accept( w, p );
        }
        catch( XMLStreamException ex )
        {
            throw new UncheckedXMLStreamException( ex );
        }
    }

}
