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
package uk.trainwatch.nre.darwin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.postgresql.ds.PGPoolingDataSource;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.util.app.Application;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Simple standalone application that reads the full messages from Darwin and processes them into the database.
 * <p>
 * This function was part of tomcat but it's been pulled out to make it more efficient.
 * <p>
 * @author Peter T Mount
 */
public class Main
        extends Application
{

    private static final String QUEUE = "darwin.db";
    private static final String ROUTING_KEY = "nre.push";

    protected PGPoolingDataSource dataSource;
    protected Properties darwinProperties;

    private RabbitConnection rabbitmq;
    private Consumer<String> consumer;

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
    protected void setupApplication()
            throws IOException
    {
        darwinProperties = Application.loadProperties( "darwin.properties" );
        dataSource = new PGPoolingDataSource();
        dataSource.setDataSourceName( "Darwin" );
        dataSource.setServerName( darwinProperties.getProperty( "url" ) );
        dataSource.setDatabaseName( "rail" );
        dataSource.setUser( darwinProperties.getProperty( "username" ) );
        dataSource.setPassword( darwinProperties.getProperty( "password" ) );
        dataSource.setMaxConnections( 10 );

        LOG.log( Level.INFO, () -> "Initialising " + QUEUE );

        consumer = SQLConsumer.guard( new DarwinImport( dataSource ) ).
                andThen( RateMonitor.<String>log( LOG, QUEUE ) );
    }

    @Override
    protected void start()
    {
        super.start();

//        RabbitMQ.queueDurableStream( rabbitmq,
//                                     QUEUE, ROUTING_KEY,
//                                     s -> s.map( RabbitMQ.toString ).
//                                     forEach( consumer )
//        );
    }

    @Override
    protected void mainLoop()
            throws Exception
    {
        for( int i = 6; i < 9; i++ )
        {
            Path p = Paths.get( "/home/peter/11", String.valueOf( i ) );
            System.out.println( "Parsing " + p );
            try( Stream<String> s = Files.lines( p ) )
            {
                s.forEach( consumer );
            }
        }
    }

    @Override
    protected void stop()
    {
        super.stop();
        rabbitmq.close();
    }

    public static void main( String... args )
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising Darwin Database Analyser" );

        new Main().run();
    }

}
