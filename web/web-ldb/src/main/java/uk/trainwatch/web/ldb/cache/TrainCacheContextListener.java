/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;

/**
 * Listens to the inbound push port feed and ensures that any train that appears in the feed
 * is removed from the cache.
 * <p>
 * We do this so that we limit reads to the database only when we don't have an entry, so on
 * departure boards if a train doesn't change we don't refresh, improving performance,
 * <p>
 * @author peter
 */
@WebListener
@ApplicationScoped
public class TrainCacheContextListener
        implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger( TrainCacheContextListener.class.getName() );

    private static final String NS[][] = {
        {"pport", "http://www.thalesgroup.com/rtti/PushPort/v12"},
        {"alarm", "http://www.thalesgroup.com/rtti/PushPort/Alarms/v1"},
        {"ct", "http://www.thalesgroup.com/rtti/PushPort/CommonTypes/v1"},
        {"fcst", "http://www.thalesgroup.com/rtti/PushPort/Forecasts/v2"},
        {"sched", "http://www.thalesgroup.com/rtti/PushPort/Schedules/v1"},
        {"msg", "http://www.thalesgroup.com/rtti/PushPort/StationMessages/v1"},
        {"status", "http://thalesgroup.com/RTTI/PushPortStatus/root_1"},
        {"tddata", "http://www.thalesgroup.com/rtti/PushPort/TDData/v1"},
        {"alert", "http://www.thalesgroup.com/rtti/PushPort/TrainAlerts/v1"},
        {"tord", "http://www.thalesgroup.com/rtti/PushPort/TrainOrder/v1"},
        {"ref", "http://www.thalesgroup.com/rtti/XmlRefData/v3"}
    };

    @Inject
    private TrainCache trainCache;

    @Inject
    private Rabbit rabbit;

    private final Set<String> set = new HashSet<>();
    private DocumentBuilder builder;
    private XPath xpath;
    private InputSource inputSource;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        trainCache.init();

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch( ParserConfigurationException ex ) {
            throw new RuntimeException( ex );
        }

        Map<String, String> nsUri = new HashMap<>();
        Map<String, String> nsPrefix = new HashMap<>();
        for( String[] s: NS ) {
            nsUri.put( s[0], s[1] );
            nsPrefix.put( s[1], s[0] );
        }

        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext( new NamespaceContext()
        {

            @Override
            public String getNamespaceURI( String prefix )
            {
                return nsUri.get( prefix );
            }

            @Override
            public String getPrefix( String namespaceURI )
            {
                return nsPrefix.get( namespaceURI );
            }

            @Override
            public Iterator getPrefixes( final String namespaceURI )
            {
                return new Iterator()
                {
                    boolean next = true;

                    @Override
                    public boolean hasNext()
                    {
                        return next;
                    }

                    @Override
                    public Object next()
                    {
                        if( next ) {
                            next = false;
                            return getPrefix( namespaceURI );
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        } );

        inputSource = new InputSource();

        rabbit.queueDurableConsumer( "cache.darwin.train",
                                     "nre.push",
                                     RabbitMQ.toString,
                                     Consumers.guard(
                                             s -> {
                                                 try {
                                                     consume( s );
                                                 }
                                                 catch( SAXException |
                                                        IOException |
                                                        XPathExpressionException ex ) {
                                                     LOG.log( Level.SEVERE, null, ex );
                                                 }
                                             }
                                     )
        );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

    private void consume( String s )
            throws SAXException,
                   IOException,
                   XPathExpressionException
    {
        inputSource.setCharacterStream( new StringReader( s ) );
        Document doc = builder.parse( inputSource );

        // Clear all RID entries for schedules and forecasts
        // Set to only do it once per rid
        set.clear();
        getRid( set, (NodeList) xpath.evaluate( "//schedule/@rid", doc, XPathConstants.NODESET ) );
        getRid( set, (NodeList) xpath.evaluate( "//TS/@rid", doc, XPathConstants.NODESET ) );
        set.forEach( trainCache::clear );
    }

    private void getRid( Set<String> l, NodeList nodes )
    {
        if( nodes != null && nodes.getLength() > 0 ) {
            for( int i = 0; i < nodes.getLength(); i++ ) {
                Attr t = (Attr) nodes.item( i );
                String rid = t.getValue();
                l.add( rid );
            }
        }
    }
}
