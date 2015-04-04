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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.jms.BytesMessage;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinDispatcherBuilder;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
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
        try
        {
            activemq.stop();
        } finally
        {
            rabbitmq.close();
        }
    }

    private Consumer<Pport> forward( RabbitConnection mq, String routingKey )
    {
        Consumer<String> c = RabbitMQ.stringConsumer( mq, routingKey );
        return p -> c.accept( DarwinJaxbContext.toXML.apply( p ) );
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // There's just one queue from NRE so we'll just receive everything, uncompress & publish to rabbit
        Consumer<String> monitor = RateMonitor.log( LOG, "record nre.raw" );

        // Send the raw/all feed - may not be required later
        Consumer<String> publisher = RabbitMQ.stringConsumer( rabbitmq, "nre.push.raw" );

        // Dispatcher to send to individual routing keys
        Consumer<Pport> dispatcher = new DarwinDispatcherBuilder().
                addAlarm( forward( rabbitmq, "nre.push.alarm" ) ).
                addAssociation( forward( rabbitmq, "nre.push.association" ) ).
                addDeactivatedSchedule( forward( rabbitmq, "nre.push.deactivated" ) ).
                addSchedule( forward( rabbitmq, "nre.push.schedule" ) ).
                addStationMessage( forward( rabbitmq, "nre.push.stationmessage" ) ).
                addTrackingID( forward( rabbitmq, "nre.push.trackingid" ) ).
                addTrainAlert( forward( rabbitmq, "nre.push.trainalert" ) ).
                addTrainOrder( forward( rabbitmq, "nre.push.trainorder" ) ).
                addTs( forward( rabbitmq, "nre.push.ts" ) ).
                build();

        // Now register our queue consumer
        activemq.registerQueueConsumer( nreProps.getProperty( "push.queue" ),
                Consumers.guard( msg -> Stream.of( msg ).
                        // The xml is gzipped so decompress it first
                        map( Functions.castTo( BytesMessage.class ) ).
                        map( new GUnZipBytesMessage() ).
                        filter( Objects::nonNull ).
                        // Log raw message count
                        peek( monitor ).
                        // Push raw feed - may remove
                        peek( publisher ).
                        // Now unmarshall the xml & dispatch to the correct routing key
                        map( DarwinJaxbContext.fromXML ).
                        forEach( dispatcher )
                ) );
    }

    public static void main( String... args )
            throws IOException,
            InterruptedException
    {
        LOG.log( Level.INFO, "Initialising National Rail Enquiries Bridge" );

        new Main().run();
    }

}
