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
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.json.JsonArray;
import javax.json.JsonObject;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JMS;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.app.Application;

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
        try
        {
            activemq.stop();
        }
        finally
        {
            rabbitmq.close();
        }
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // RTPPM is received and submitted as is to RabbitMQ
        activemq.registerTopicConsumer( "RTPPM_ALL", Consumers.guard( msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        forEach( RabbitMQ.stringConsumer( rabbitmq, "nr.rtppm" ) )
                                ) );

        // Trust movements - here we simply push everything to rabbit, once for the raw message and then we
        // split the message into it's individual components
        activemq.registerTopicConsumer( "TRAIN_MVT_ALL_TOC", Consumers.guard( msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        // peek will submit the raw text to rabbit
                                        peek( RabbitMQ.stringConsumer( rabbitmq, "nr.trust.mvtall" ) ).
                                        // Parse and then unbatch
                                        map( JsonUtils.parseJsonArray ).
                                        filter( Objects::nonNull ).
                                        flatMap( JsonArray::stream ).
                                        map( JsonObject.class::cast ).
                                        // Pass to the main route
                                        forEach( RabbitMQ.jsonConsumer( rabbitmq, "nr.trust.mvt" ) )
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
