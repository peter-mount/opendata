/*
 * Copyright 2014 peter.
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
package uk.trainwatch.web.train;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.forecast.ForecastRecorder;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.config.JNDIConfig;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.DBContextListener;

/**
 *
 * @author peter
 */
@WebListener
public class TrainContextListener
        extends DBContextListener
{

    private RabbitConnection rabbitConnection;

    @Override
    protected void init( ServletContextEvent sce )
            throws SQLException
    {
        // Always initialise the manager
        DataSource dataSource = getRailDataSource();

        ForecastManager.INSTANCE.setDataSource( dataSource );

        if( !JNDIConfig.INSTANCE.getBoolean( "trainInfo.disabled" ) )
        {
            log.log( Level.INFO, "Initialising Train Information" );

            rabbitConnection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );

            Consumer<Pport> monitor = RateMonitor.log( log, "station.nre.forecast" );

            // Pass deactivated & TS messages to forecast
            RabbitMQ.queueDurableStream( rabbitConnection,
                    "station.nre.forecast",
                    "nre.push.deactivated,nre.push.ts",
                    s -> s.map( RabbitMQ.toString ).
                    map( DarwinJaxbContext.fromXML ).
                    flatMap( DarwinJaxbContext.messageSplitter ).
                    peek( monitor ).
                    forEach( new ForecastRecorder( dataSource ) )
            );
        }
    }

    @Override
    protected void shutdown( ServletContextEvent sce )
    {
        if( rabbitConnection != null )
        {
            rabbitConnection.close();
            rabbitConnection = null;
        }
    }

}
