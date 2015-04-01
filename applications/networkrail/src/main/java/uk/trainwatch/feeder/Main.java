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
package uk.trainwatch.feeder;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.apachemq.JMS;
import uk.trainwatch.util.JsonUtils;
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

    private final Map<String, Consumer<? super JsonStructure>> tdPublishers = new ConcurrentHashMap<>();

    @Override
    protected void setupBrokers()
            throws IOException
    {
        Properties p = loadProperties( "rabbit.properties" );
        rabbitmq = new RabbitConnection( p.getProperty( "username" ),
                                         p.getProperty( "password" ),
                                         p.getProperty( "host" )
        );

        p = loadProperties( "networkrail.properties" );
        activemq = new RemoteActiveMQConnection(
                p.getProperty( "server" ),
                Integer.parseInt( p.getProperty( "port", "61619" ) ),
                p.getProperty( "clientid", p.getProperty( "username" ) ),
                p.getProperty( "username" ),
                p.getProperty( "password" )
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
        } finally {
            rabbitmq.close();
        }
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // RTPPM is received and submitted as is to RabbitMQ
        Consumer<String> rtppmMonitor = RateMonitor.log( LOG, "record nr.trust.mvtall" );
        Consumer<String> rtppmPublisher = RabbitMQ.stringConsumer( rabbitmq, "nr.rtppm" );
        activemq.registerTopicConsumer( "RTPPM_ALL", Consumers.guard( msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        peek( rtppmMonitor ).
                                        forEach( rtppmPublisher )
                                ) );

        // Trust movements - here we simply push everything to rabbit, once for the raw message and then we
        // split the message into it's individual components
        Consumer<String> rawMonitor = RateMonitor.log( LOG, "recieve nr.trust.mvt raw" );
        Consumer<String> rawPublisher = RabbitMQ.stringConsumer( rabbitmq, "nr.trust.mvtall" );
        Consumer<JsonObject> mvtMonitor = RateMonitor.log( LOG, "recieve nr.trust.mvt movement" );
        Consumer<? super JsonStructure> mvtPublisher = RabbitMQ.jsonConsumer( rabbitmq, "nr.trust.mvt" );
        activemq.registerTopicConsumer( "TRAIN_MVT_ALL_TOC", Consumers.guard( msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        // peek will submit the raw text to rabbit
                                        peek( rawMonitor ).
                                        peek( rawPublisher ).
                                        // Parse and then unbatch
                                        map( JsonUtils.parseJsonArray ).
                                        flatMap( JsonUtils::stream ).
                                        map( JsonObject.class::cast ).
                                        // Pass to the main route
                                        peek( mvtMonitor ).
                                        forEach( mvtPublisher )
                                ) );

        // TD feed
        Consumer<String> rawTdMonitor = RateMonitor.log( LOG, "recieve nr.td raw" );
        Consumer<String> rawTdPublisher = RabbitMQ.stringConsumer( rabbitmq, "nr.td.all" );
        Consumer<JsonObject> tdMonitor = RateMonitor.log( LOG, "recieve nr.td.area" );
        activemq.registerTopicConsumer( "TD_ALL_SIG_AREA", Consumers.guard( msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        // peek will submit the raw text to rabbit
                                        peek( rawTdMonitor ).
                                        peek( rawTdPublisher ).
                                        // Parse and then unbatch
                                        map( JsonUtils.parseJsonArray ).
                                        // Stream of array elements
                                        flatMap( JsonUtils::<JsonObject>stream ).
                                        // Stream of object property values
                                        flatMap( JsonUtils::<JsonObject>stream ).
                                        map( JsonUtils.getObject ).
                                        filter( Objects::nonNull ).
                                        peek( tdMonitor ).
                                        // Route to the individual signalling area
                                        forEach( td -> tdPublishers.computeIfAbsent( td.getString( "area_id" ),
                                                                                     area -> RabbitMQ.jsonConsumer( rabbitmq, "nr.td.area." + area )
                                                ).
                                                accept( td )
                                        )
                                ) );
    }

    public static void main( String... args )
            throws IOException,
                   InterruptedException
    {
        LOG.log( Level.INFO, "Initialising Network Rail Bridge" );

        new Main().run();
    }

}
