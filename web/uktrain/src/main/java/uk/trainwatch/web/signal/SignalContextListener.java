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
package uk.trainwatch.web.signal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.nrod.td.model.TDMessage;
import uk.trainwatch.nrod.td.model.TDMessageFactory;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.DBContextListener;

/**
 *
 * @author peter
 */
@WebListener
public class SignalContextListener
        extends DBContextListener
{

    private static final Logger LOG = Logger.getLogger( SignalContextListener.class.getName() );

    private RabbitConnection rabbitConnection;

    @Override
    protected void init( ServletContextEvent sce )
            throws SQLException
    {
        log.log( Level.INFO, "Initialising SignalManager" );
        SignalManager.INSTANCE.setDataSource( getRailDataSource() );

        try
        {
            String localHost = InetAddress.getLocalHost().
                    getHostName();

            boolean dev = "europa".equals( localHost ) || "phoebe".equals( localHost );
            // Dev, connect to prod for data but only request a single area to keep volumne down.
            configure( dev ? "nr.td.area.A3" : "nr.td.area.#", "signal.map", true );
        } catch( NamingException |
                UnknownHostException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
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

    private void configure( String routingKey, String queueName, boolean durable )
            throws NamingException
    {
        LOG.log( Level.INFO, () -> "Requesting " + routingKey + " on " + queueName );

        rabbitConnection = new RabbitConnection(
                InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/user" ),
                InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/password" ),
                InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/host" )
        );

        Consumer<Stream<byte[]>> consumer = s
                -> s.map( RabbitMQ.toString.
                        andThen( JsonUtils.parseJsonObject ).
                        andThen( TDMessageFactory.INSTANCE ) ).
                filter( Objects::nonNull ).
                forEach(
                        SignalManager.INSTANCE.getTDConsumer().
                        andThen( RateMonitor.log( LOG, "Receive " + routingKey ) )
                );

        if( durable )
        {
            RabbitMQ.queueDurableStream( rabbitConnection, queueName, routingKey, consumer );
        } else
        {
            RabbitMQ.queueStream( rabbitConnection, queueName, routingKey, consumer );
        }
    }

}
