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
import uk.trainwatch.nre.darwin.forecast.rec.DeactivationRecorder;
import uk.trainwatch.nre.darwin.forecast.rec.ScheduleRecorder;
import uk.trainwatch.nre.darwin.forecast.rec.TSRecorder;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinDispatcherBuilder;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageRecorder;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.app.Application;
import uk.trainwatch.util.counter.RateMonitor;

public abstract class AbtractMain
        extends Application
{

    protected PGPoolingDataSource dataSource;
    protected Properties darwinProperties;
    protected Consumer<Pport> dispatcher;

    @Override
    protected void start()
    {
        super.start();
    }

    @Override
    protected void stop()
    {
        super.stop();
    }

    private Consumer<Pport> create( String label, Consumer<Pport> c )
    {
        return RateMonitor.<Pport>log( LOG, Level.INFO, label ).andThen( c );
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

        // Forecasts
        Consumer<Pport> forecast = Streams.fork(
                new DarwinDispatcherBuilder().
                addSchedule( new ScheduleRecorder( dataSource ) ).
                addDeactivatedSchedule( new DeactivationRecorder( dataSource ) ).
                addTs( new TSRecorder( dataSource ) ).
                build()
        );

        // Station messages
        Consumer<Pport> messages = Streams.fork( new StationMessageRecorder( dataSource ) );

        // The dispatcher
        dispatcher = RateMonitor.<Pport>log( LOG, Level.INFO, "Dispatched" ).
                andThen( new DarwinDispatcherBuilder().
                        // Forecasts
                        addSchedule( forecast ).
                        addDeactivatedSchedule( forecast ).
                        addTs( forecast ).
                        // Station Messages
                        addStationMessage( messages ).
                        //
                        build()
                );

    }

}
