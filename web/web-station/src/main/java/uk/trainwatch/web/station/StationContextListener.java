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
package uk.trainwatch.web.station;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageRecorder;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.config.JNDIConfig;
import uk.trainwatch.util.sql.DBContextListener;

/**
 *
 * @author peter
 */
@WebListener
public class StationContextListener
        extends DBContextListener
{

    private RabbitConnection rabbitConnection;

    @Override
    protected void init( ServletContextEvent sce )
            throws SQLException
    {
        // Always initialise the manager
        StationMessageManager.INSTANCE.setDataSource( getRailDataSource() );

        if( !JNDIConfig.INSTANCE.getBoolean( "stationInfo.disabled" ) ) {
            log.log( Level.INFO, "Initialising Station Information" );

            rabbitConnection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );

            // Pass deactivated & TS messages to forecast
            RabbitMQ.queueDurableStream( rabbitConnection,
                                         "station.nre.forecast",
                                         "nre.push.deactivated,nre.push.ts",
                                         s -> s.map( RabbitMQ.toString ).
                                         map( DarwinJaxbContext.fromXML ).
                                         forEach( ForecastManager.INSTANCE::accept )
            );

            // Station messages
            RabbitMQ.queueDurableStream( rabbitConnection,
                                         "station.nre.msg",
                                         "nre.push.stationmessage",
                                         s -> s.map( RabbitMQ.toString ).
                                         map( DarwinJaxbContext.fromXML ).
                                         forEach( new StationMessageRecorder( getRailDataSource() ) )
            );
        }
    }

    @Override
    protected void shutdown( ServletContextEvent sce )
    {
        if( rabbitConnection != null ) {
            rabbitConnection.close();
            rabbitConnection = null;
        }
    }

}
