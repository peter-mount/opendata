/*
 * Copyright 2015 peter.
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
package uk.trainwatch.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A representation of a connection to RabbitMQ
 * <p>
 * @author Peter T Mount
 */
public class RabbitConnection
{

    private static final Logger LOG = Logger.getLogger( RabbitConnection.class.getName() );
    private final String username;
    private final String password;
    private final String virtualHost;
    private final String host;
    private final int portNumber;
    /**
     * The current connection
     */
    private Connection connection;
    /**
     * Map of currently open channels
     */
    private final Map<Object, Channel> channels = new ConcurrentHashMap<>();

    RabbitConnection()
    {
        this.username = null;
        this.password = null;
        this.virtualHost = null;
        this.host = null;
        this.portNumber = 0;
    }

    
    /**
     *
     * @param username    remote username
     * @param password    remote password
     * @param host        remote server
     * @param portNumber  server port
     * @param virtualHost virtual host
     */
    public RabbitConnection( final String username, final String password,
                             final String host, final int portNumber, String virtualHost )
    {
        this.username = Objects.requireNonNull( username );
        this.password = Objects.requireNonNull( password );
        this.virtualHost = virtualHost;
        this.host = Objects.requireNonNull( host );
        this.portNumber = portNumber;
    }

    /**
     *
     * @param username remote username
     * @param password remote password
     * @param host     remote server
     */
    public RabbitConnection( final String username, final String password, final String host )
    {
        this( username, password, host, 0, null );
    }

    /**
     *
     * @param username   remote username
     * @param password   remote password
     * @param host       remote server
     * @param portNumber server port
     */
    public RabbitConnection( final String username, final String password,
                             final String host, final int portNumber )
    {
        this( username, password, host, portNumber, null );
    }

    /**
     *
     * @param username    remote username
     * @param password    remote password
     * @param host        remote server
     * @param virtualHost virtual host
     */
    public RabbitConnection( final String username, final String password,
                             final String host, String virtualHost )
    {
        this( username, password, host, 0, virtualHost );
    }

    /**
     * Get the current connection, creating one as needed
     * <p>
     * @return <p>
     * @throws IOException
     */
    public synchronized Connection getConnection()
            throws IOException
    {
        if( connection == null ) {
            createConnection();
        }
        return connection;
    }

    private void createConnection()
            throws IOException
    {
        if( connection != null ) {
            close();
        }

        LOG.log( Level.FINE, "building connection" );
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername( username );
        factory.setPassword( password );
        if( virtualHost != null ) {
            factory.setVirtualHost( virtualHost );
        }
        factory.setHost( host );
        if( portNumber > 0 ) {
            factory.setPort( portNumber );
        }

        channels.clear();

        LOG.log( Level.FINE, "creating connection" );
        connection = factory.newConnection();
    }

    /**
     * Get a {@link Channel} for the specified key. If the key does not exist then a channel is created.
     * <p>
     * Note: Do not cache the returned Channel, as it could become invalid if the underlying connection is closed. The
     * key is usually an object that's requiring a channel.
     * <p>
     * @param key client specific key, usually a reference to themselves
     * <p>
     * @return Channel for this key, can change over time
     */
    public synchronized Channel getChannel( Object key )
    {
        Channel channel = null;
        do {
            channel = channels.computeIfAbsent( key, k -> {
                try {
                    LOG.log( Level.FINE, "creating channel" );
                    return getConnection().
                            createChannel();
                }
                catch( IOException ex ) {
                    throw new UncheckedIOException( ex );
                }
            } );
            if( channel != null && !channel.isOpen() ) {
                LOG.log( Level.FINE, "discarding dead channel" );

                channels.remove( key );
                channel = null;
            }
        }
        while( channel == null );

        return channel;
    }

    public void close( Object key, Channel channel )
    {
        if( channels.remove( key, channel ) ) {
            try {
                channel.close();
            }
            catch( ShutdownSignalException ex ) {
                // Ignore
            }
            catch( IOException ex ) {
                Logger.getLogger( RabbitConnection.class.getName() ).
                        log( Level.SEVERE, null, ex );
            }
        }
    }

    /**
     * Close the underlying connection and all Channels to it
     */
    public synchronized void close()
    {
        LOG.log( Level.FINE, "closing connection" );
        try {
            try {
                channels.forEach( ( n, c ) -> {
                    try {
                        c.close();
                    }
                    catch( Throwable t ) {
                        // Ignore as we are cleaning up
                    }
                } );
            }
            finally {
                if( connection != null ) {
                    connection.close();
                }
            }
        }
        catch( Throwable t ) {
            // Ignore as we are cleaning up
        }
        finally {
            channels.clear();
            connection = null;
        }
    }

}
