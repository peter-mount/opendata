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
package uk.trainwatch.tfl.feeds;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.postgresql.ds.PGPoolingDataSource;
import uk.trainwatch.tfl.model.feed.TflFeed;
import uk.trainwatch.tfl.model.feed.TflJsonRetriever;
import uk.trainwatch.tfl.model.feed.TflPredictionImport;
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

    protected PGPoolingDataSource dataSource;

    protected Properties tflProperties;

    private TflFeed tflPredictionFeed;

    @Override
    protected void setupBrokers()
            throws IOException
    {
    }

    /**
     * TfL Tube, DLR & Air lines
     */
    private static final String LINES[] =
    {
        "bakerloo",
        "central",
        "circle",
        "district",
        "dlr",
        "emirates-air-line",
        "hammersmith-city",
        "jubilee",
        "metropolitan",
        "northern",
        "piccadilly",
        "victoria",
        "waterloo-city"
    };

    @Override
    protected void setupApplication()
            throws IOException
    {
        try
        {
            tflProperties = Application.loadProperties( "tfl.properties" );
            dataSource = new PGPoolingDataSource();
            dataSource.setDataSourceName( "TfL" );
            dataSource.setServerName( tflProperties.getProperty( "url" ) );
            dataSource.setDatabaseName( "rail" );
            dataSource.setUser( tflProperties.getProperty( "username" ) );
            dataSource.setPassword( tflProperties.getProperty( "password" ) );
            dataSource.setMaxConnections( 10 );

            tflPredictionFeed = new TflFeed(
                    new TflJsonRetriever( Stream.of( LINES ).collect( Collectors.joining( ",", "/line/", "/Arrivals" ) ), tflProperties ),
                    SQLConsumer.guard( new TflPredictionImport( dataSource ) ).andThen( RateMonitor.<String>log( LOG, "tfl.prediction" ) ),
                    1, TimeUnit.MINUTES );
        } catch( URISyntaxException ex )
        {
            throw new IOException( ex );
        }
    }

    @Override
    protected void start()
    {
        super.start();

        tflPredictionFeed.start();
    }

    @Override
    protected void stop()
    {
        super.stop();
        tflPredictionFeed.stop();
    }

    public static void main( String... args )
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising TfL Feeds" );

        new Main().run();
    }

}
