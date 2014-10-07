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
package uk.trainwatch.archiver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.app.Application;
import static uk.trainwatch.util.app.Application.loadProperties;

/**
 *
 * @author Peter T Mount
 */
public class Main
        extends Application
{

    private RabbitConnection rabbitmq;

    public static void main( String... args )
            throws IOException,
                   InterruptedException
    {
        LOG.log( Level.INFO, "Initialising Rail Analyser" );

        new Main().run();
    }

    @Override
    protected void setupBrokers()
            throws IOException
    {
        Properties p = loadProperties( "rabbit.properties" );
        rabbitmq = new RabbitConnection( p.getProperty( "username" ),
                                         p.getProperty( "password" ),
                                         p.getProperty( "host" )
        );
    }

    @Override
    protected void stop()
    {
        super.stop();
        rabbitmq.close();
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // Record raw data to disk - mvtall
        Function<String, Path> trustmapper = new DatePathMapper( "/usr/local/networkrail", "trust/raw", true );
        RabbitMQ.queueDurableStream( rabbitmq, "nrod.trust.archiver", "nr.trust.mvtall",
                                     s -> s.map( RabbitMQ.toString ).
                                     forEach( Consumers.guard( LOG, FileRecorder.recordTo( trustmapper ) ) )
        );

        // Record raw data to disk - mvtall
        Function<String, Path> rtppmMapper = new DatePathMapper( "/usr/local/networkrail", "rtppm/raw" );
        RabbitMQ.queueDurableStream( rabbitmq, "nrod.rtppm.archiver", "nr.rtppm",
                                     s -> s.map( RabbitMQ.toString ).
                                     forEach( Consumers.guard( LOG, FileRecorder.recordTo( rtppmMapper ) ) )
        );
    }

}
