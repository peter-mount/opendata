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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.DBContextListener;

/**
 *
 * @author peter
 */
@WebListener
public class StationContextListener
        extends DBContextListener
{

    private static final Logger LOG = Logger.getLogger( StationContextListener.class.getName() );

    private RabbitConnection rabbitConnection;

    @Override
    protected void init( ServletContextEvent sce )
            throws SQLException
    {
        log.log( Level.INFO, "Initialising Station Information" );

        try {

            rabbitConnection = new RabbitConnection(
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/user" ),
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/password" ),
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/host" )
            );

            // Pass deactivated & TS messages to forecast
            Consumer<Stream<byte[]>> forecast = s -> s.map( RabbitMQ.toString ).
                    map( DarwinJaxbContext.fromXML ).
                    forEach( ForecastManager.INSTANCE::accept );
            RabbitMQ.queueDurableStream( rabbitConnection, "station.nre.forecast", "nre.push.deactivated,nre.push.ts", forecast );

            // Station messages
            Consumer<Stream<byte[]>> stationMessages = s -> s.map( RabbitMQ.toString ).
                    map( DarwinJaxbContext.fromXML ).
                    forEach( StationMessageManager.INSTANCE::accept );
            RabbitMQ.queueDurableStream( rabbitConnection, "station.nre.msg", "nre.push.stationmessage", stationMessages );
        }
        catch( NamingException ex ) {
            LOG.log( Level.SEVERE, null, ex );
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
