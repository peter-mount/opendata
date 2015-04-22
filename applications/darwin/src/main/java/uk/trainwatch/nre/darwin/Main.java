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
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.postgresql.ds.PGPoolingDataSource;
import uk.trainwatch.nre.darwin.forecast.rec.TSRecorder;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageRecorder;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
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

    private PGPoolingDataSource dataSource;
    private RabbitConnection rabbitmq;

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
    }

    @Override
    protected void start()
    {
        super.start();
    }

    @Override
    protected void stop()
    {
        super.stop();
        rabbitmq.close();
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
        Properties p = Application.loadProperties( "darwin.properties" );
        dataSource = new PGPoolingDataSource();
        dataSource.setDataSourceName( "Darwin" );
        dataSource.setServerName( p.getProperty( "url" ) );
        dataSource.setDatabaseName( "rail" );
        dataSource.setUser( p.getProperty( "username" ) );
        dataSource.setPassword( p.getProperty( "password" ) );
        dataSource.setMaxConnections( 10 );

        setupTrainStatus();
        setupStationMessages();
    }

    private void setupTrainStatus()
    {
        String queue = "darwin.forecast";

        LOG.log( Level.INFO, () -> "Initialising " + queue );

        Consumer<Pport> consumer = RateMonitor.<Pport>log( LOG, queue ).
                andThen(new TSRecorder( dataSource ) );

        // Pass deactivated & TS messages to forecast
        RabbitMQ.queueDurableStream( rabbitmq,
                queue,
                "nre.push.activated,nre.push.deactivated,nre.push.ts",
                s -> s.map( RabbitMQ.toString ).
                map( DarwinJaxbContext.fromXML ).
                flatMap( DarwinJaxbContext.messageSplitter ).
                forEach( consumer )
        );

    }

    private void setupStationMessages()
    {
        String queue = "darwin.station.message";

        LOG.log( Level.INFO, () -> "Initialising " + queue );

        Consumer<Pport> consumer = RateMonitor.<Pport>log( LOG, queue ).
                andThen( new StationMessageRecorder( dataSource ) );

        // Station messages
        RabbitMQ.queueDurableStream( rabbitmq,
                queue,
                "nre.push.stationmessage",
                s -> s.map( RabbitMQ.toString ).
                map( DarwinJaxbContext.fromXML ).
                flatMap( DarwinJaxbContext.messageSplitter ).
                forEach( consumer )
        );

    }

    public static void main( String... args )
            throws IOException,
            InterruptedException
    {
        LOG.log( Level.INFO, "Initialising National Rail Enquiries Bridge" );

        new Main().run();
    }

}
