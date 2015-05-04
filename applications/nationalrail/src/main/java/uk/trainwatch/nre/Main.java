/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.nre;

import uk.trainwatch.apachemq.GUnZipBytesMessage;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Stream;
import javax.jms.BytesMessage;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.app.Application;
import uk.trainwatch.util.counter.RateMonitor;

/**
 * Simple standalone application that acts as a bridge between Network Rail and RabbitMQ
 * <p>
 * @author Peter T Mount
 */
public class Main
        extends Application
{

    private RabbitConnection rabbitmq;

    private RemoteActiveMQConnection activemq;
    private Properties nreProps;

    @Override
    protected void setupBrokers()
            throws IOException
    {
        Properties p = loadProperties( "rabbit.properties" );
        rabbitmq = new RabbitConnection( p.getProperty( "username" ),
                                         p.getProperty( "password" ),
                                         p.getProperty( "host" )
        );

        nreProps = loadProperties( "nationalrail.properties" );
        activemq = new RemoteActiveMQConnection(
                nreProps.getProperty( "push.server" ),
                Integer.parseInt( nreProps.getProperty( "push.port", "61616" ) ),
                //nreProps.getProperty( "clientid", RabbitMQ.getHostname() + ".area51.onl" ),
                nreProps.getProperty( "push.username" ),
                nreProps.getProperty( "push.password" )
        );
    }

    @Override
    protected void start()
    {
        super.start();
        activemq.start();
    }

    @Override
    protected void stop()
    {
        super.stop();
        try {
            activemq.stop();
        }
        finally {
            rabbitmq.close();
        }
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // There's just one queue from NRE so we'll just receive everything, uncompress & publish to rabbit
        Consumer<String> monitor = RateMonitor.log( LOG, "record nre.push" );

        // ISSUE 16: we occasionally stop receiving data
        Consumer<String> issue16 = new Issue16();

        // Consumer to accept the raw feed
        Consumer<String> consumer = RabbitMQ.stringConsumer( rabbitmq, "nre.push" );

        // Now register our queue consumer
        activemq.registerQueueConsumer( nreProps.getProperty( "push.queue" ),
                                        Consumers.guard( msg -> Stream.of( msg ).
                                                // The xml is gzipped so decompress it first
                                                map( Functions.castTo( BytesMessage.class ) ).
                                                map( new GUnZipBytesMessage() ).
                                                filter( Objects::nonNull ).
                                                // Log raw message count
                                                peek( monitor ).
                                                // ISSUE 16: temp fix if no messages after 3 minutes then restart
                                                peek( issue16 ).
                                                // Submit everything to the main queue
                                                forEach( consumer )
                                        ) );
    }

    public static void main( String... args )
            throws Exception
    {
        if( args.length > 0 ) {
            FileHandler fh = new FileHandler( args[0] );
            fh.setFormatter( new SimpleFormatter() );

            // Add to the root logger
            Logger log = LOG;
            while( log.getParent() != null ) {
                log = log.getParent();
            }
            log.addHandler( fh );
        }
        
        LOG.log( Level.INFO, "Initialising National Rail Enquiries Bridge" );

        new Main().run();
    }

}
