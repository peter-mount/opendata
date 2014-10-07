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
package uk.trainwatch.apachemq;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Manages a connection to a remote ApacheMQ instance
 * <p>
 * @author Peter T Mount
 */
public class RemoteActiveMQConnection
{

    private static final Logger LOG = Logger.getLogger( RemoteActiveMQConnection.class.getName() );
    private final Map<String, TopicClient> clients = new ConcurrentHashMap<>();
    private Watchdog watchdog;
    private final String brokerUri;
    private final String username;
    private final String password;
    private final String clientId;
    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;

    /**
     *
     * @param server   Server name
     * @param port     Server port
     * @param username Username
     * @param password Password
     */
    public RemoteActiveMQConnection( final String server, final int port, final String username, final String password )
    {
        this( server, port, username, username, password );
    }

    /**
     *
     * @param server   Server name
     * @param port     Server port
     * @param clientId Unique client id
     * @param username Username
     * @param password Password
     */
    public RemoteActiveMQConnection( final String server, final int port, final String clientId, final String username,
                                     final String password )
    {
        this.username = Objects.requireNonNull( username );
        this.clientId = Objects.requireNonNull( clientId );
        this.password = Objects.requireNonNull( password );

        brokerUri = "tcp://" + Objects.requireNonNull( server )
                    + ":" + port
                    + "?trace=false"
                    + "&daemon=true"
                    + "&soTimeout=30000";

        connectionFactory = new ActiveMQConnectionFactory( brokerUri );

        connection = null;
    }

    Connection getConnection()
    {
        return connection;
    }

    String getUsername()
    {
        return username;
    }

    public boolean isRunning()
    {
        return watchdog.isRunning();
    }

    void reconnect()
    {
        watchdog.reconnect();
    }

    /**
     * Register a {@link Consumer} to a topic.
     * <p>
     * A topic can only have one consumer associated with it
     * <p>
     * @param topicName Topic name
     * @param consumer  Consumer
     */
    public void registerTopicConsumer( String topicName, Consumer<Message> consumer )
    {
        clients.computeIfAbsent( topicName, k -> new TopicClient( this, topicName, consumer ) );
    }

    /**
     * De-Register a topic
     * <p>
     * @param topicName Topic name
     */
    public void deregisterClient( String topicName )
    {
        clients.computeIfPresent( topicName, (k, c) -> c.stop() );
    }

    /**
     * Start the connection
     */
    public void start()
    {
        if( watchdog == null )
        {
            watchdog = new Watchdog( this );
        }
        watchdog.start();
    }

    /**
     * Stop the connection.
     * <p>
     * If we are connected we will be disconnected however the watchdog will also shutdown so this is then permanent.
     */
    public void stop()
    {
        if( watchdog != null )
        {
            try
            {
                watchdog.stop();
            }
            finally
            {
                watchdog = null;
            }
        }
    }

    /**
     * Force a connection (disconnecting if already connected).
     * <p>
     * Normally this is not needed as the watchdog will do that for you
     * <p>
     * @throws JMSException
     * @throws InterruptedException
     */
    public void connect()
            throws JMSException,
                   InterruptedException
    {
        if( disconnect() )
        {
            // Sleep for 1 second so we don't flood the broker
            Thread.sleep( 1000L );
        }

        LOG.info( () -> "Connecting to " + brokerUri );

        connection = connectionFactory.createConnection( username, password );
        connection.setClientID( clientId );
        connection.start();

        LOG.info( () -> "Connected to " + brokerUri );

        clients.forEach( (t, c) -> c.start() );

        LOG.info( () -> clients.size() + " clients started" );
    }

    /**
     * Disconnect from the remote broker.
     * <p>
     * Calling this will not keep the connection down as the watchdog will notice and reconnect
     * <p>
     * @return
     */
    public boolean disconnect()
    {
        if( connection != null )
        {
            LOG.info( () -> "Disconnecting from " + brokerUri );

            try
            {
                clients.forEach( (t, c) -> c.stop() );
            }
            finally
            {

                try
                {
                    connection.close();
                }
                catch( JMSException ex )
                {
                    LOG.log( Level.SEVERE, null, ex );
                }
                finally
                {
                    connection = null;
                }
            }
            return true;
        }

        return false;
    }

}
