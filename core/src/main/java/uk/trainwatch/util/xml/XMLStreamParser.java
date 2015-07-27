/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parses a large xml file via streaming
 *
 * @author peter
 */
public class XMLStreamParser
        implements Runnable
{

    private static final Logger LOG = Logger.getLogger( XMLStreamParser.class.getName() );

    private final InputStream inputStream;
    private final int triggerDepth;
    private final Consumer<Node> receiver;
    private final Consumer<Exception> errorHandler;
    private final Runnable closeHandler;
    private final Map<String, String> nameSpaceMap;

    private final Document document;
    private Node node;
    private XMLStreamReader xmlStreamReader;
    private volatile boolean running = true;
    private int depth;

    XMLStreamParser( InputStream inputStream,
                     int triggerDepth,
                     Consumer<Node> receiver,
                     Consumer<Exception> errorHandler,
                     Runnable closeHandler,
                     Map<String, String> nameSpaceMap,
                     Document document )
    {
        this.inputStream = inputStream;
        this.triggerDepth = triggerDepth;
        this.receiver = receiver;
        this.errorHandler = errorHandler;
        this.closeHandler = closeHandler;
        this.nameSpaceMap = nameSpaceMap;
        this.document = document;
    }

    @Override
    public void run()
    {
        try
        {
            LOG.log( Level.FINE, "Starting background thread" );
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty( XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE );
            xmlif.setProperty( XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE );
            xmlif.setProperty( XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE );
            xmlif.setProperty( XMLInputFactory.IS_COALESCING, Boolean.TRUE );
            xmlStreamReader = xmlif.createXMLStreamReader( new BufferedInputStream( inputStream ) );

            while( running && xmlStreamReader.hasNext() )
            {
                switch( xmlStreamReader.next() )
                {
                    case XMLStreamConstants.START_ELEMENT:
                        depth++;

                        Element e;
                        if( xmlStreamReader.getNamespaceURI() == null )
                        {
                            e = document.createElement( xmlStreamReader.getLocalName() );
                        }
                        else
                        {
                            String ns = nameSpaceMap.get( xmlStreamReader.getNamespaceURI() );
                            e = document.createElementNS( ns == null ? xmlStreamReader.getNamespaceURI() : ns,
                                                          xmlStreamReader.getLocalName() );
                        }
                        //e.setPrefix( xmlStreamReader.getPrefix() );

                        for( int i = 0; i < xmlStreamReader.getAttributeCount(); i++ )
                        {
                            e.setAttributeNS( xmlStreamReader.getAttributeNamespace( i ),
                                              xmlStreamReader.getAttributeLocalName( i ),
                                              xmlStreamReader.getAttributeValue( i ) );
                        }

                        if( depth >= triggerDepth && node != null )
                        {
                            node.appendChild( e );
                        }

                        node = e;
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if( node == null )
                        {
                            // Do nothing
                        }
                        else if( depth == triggerDepth )
                        {
                            receiver.accept( node );
                            node = null;
                        }
                        else
                        {
                            node = node.getParentNode();
                        }
                        
                        if( depth > 0 )
                        {
                            depth--;
                        }
                        
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if( node != null )
                        {
                            node.appendChild( document.createTextNode( xmlStreamReader.getText() ) );
                        }
                        break;

                    default:
                        break;
                }

                if( Thread.interrupted() )
                {
                    setRunning( false );
                }
            }
        } catch( IllegalArgumentException | XMLStreamException | DOMException ex )
        {
            LOG.log( Level.SEVERE, ex.getMessage(), ex );
            errorHandler.accept( ex );
        }
        finally
        {
            setRunning( false );
        }

        if( closeHandler != null )
        {
            closeHandler.run();
        }
        LOG.log( Level.FINE, "Background thread end" );
    }

    boolean isRunning()
    {
        return running;
    }

    void setRunning( boolean running )
    {
        this.running = running;
    }

}
