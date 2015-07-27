/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import uk.trainwatch.util.AbstractSpliterator;
import uk.trainwatch.util.Consumers;

public class XMLStreamParserBuilder
{

    private InputStream inputStream;
    private int triggerDepth = 2;
    private Consumer<Node> receiver;
    private Consumer<Exception> errorHandler = null;
    private Runnable closeHandler = null;
    private final Map<String, String> nameSpace = new HashMap<>();

    public XMLStreamParserBuilder()
    {
    }

    public XMLStreamParserBuilder setInputStream( InputStream inputStream )
    {
        this.inputStream = inputStream;
        return this;
    }

    public XMLStreamParserBuilder setTriggerDepth( int triggerDepth )
    {
        this.triggerDepth = triggerDepth;
        return this;
    }

    public XMLStreamParserBuilder setReceiver( Consumer<Node> receiver )
    {
        this.receiver = receiver;
        return this;
    }

    public XMLStreamParserBuilder setErrorHandler( Consumer<Exception> errorHandler )
    {
        this.errorHandler = errorHandler;
        return this;
    }

    public XMLStreamParserBuilder setCloseHandler( Runnable closeHandler )
    {
        this.closeHandler = closeHandler;
        return this;
    }

    public XMLStreamParserBuilder substituteNameSpace( String orig, String newValue )
    {
        nameSpace.put( orig, newValue );
        return this;
    }

    public XMLStreamParser createSTAXProcessor()
            throws ParserConfigurationException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware( true );
        Document document = dbf.newDocumentBuilder().newDocument();

        return new XMLStreamParser( Objects.requireNonNull( inputStream, "Input stream is required" ),
                                    triggerDepth,
                                    Objects.requireNonNull( receiver, "Receiving consumer is required" ),
                                    Consumers.ensureNotNull( errorHandler ),
                                    closeHandler == null ? () ->
                                            {
                                    } : closeHandler,
                                    nameSpace,
                                    document );
    }

    public Spliterator<Node> createSpliterator()
            throws ParserConfigurationException
    {
        SynchronousQueue<Node> q = new SynchronousQueue<>();
        setReceiver( n ->
        {
            try
            {
                q.put( n );
            } catch( InterruptedException ex )
            {
                Thread.currentThread().interrupt();
            }
        } );

        XMLStreamParser p = createSTAXProcessor();

        return new AbstractSpliterator<Node>()
        {
            private boolean started = false;

            @Override
            public boolean tryAdvance( Consumer<? super Node> action )
            {
                if( !started )
                {
                    started = true;
                    Thread t = new Thread( p );
                    t.setDaemon( true );
                    t.start();
                }

                if( p.isRunning() )
                {
                    try
                    {
                        Node n = null;
                        do
                        {
                            n = q.poll( 1, TimeUnit.SECONDS );
                        } while( p.isRunning() && n == null );
                        if( n != null )
                        {
                            action.accept( n );
                        }
                    } catch( InterruptedException ex )
                    {
                        // Ignore
                        p.setRunning( false );
                        return false;
                    }
                }
                return p.isRunning();
            }
        };
    }

    public Stream<Node> createStream()
            throws ParserConfigurationException
    {
        return StreamSupport.stream( createSpliterator(), false );
    }
}
