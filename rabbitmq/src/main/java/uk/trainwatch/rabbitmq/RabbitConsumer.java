/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.rabbitmq;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Consumer which will publish to a RabbitMQ Topic
 * <p>
 * @author Peter T Mount
 */
public class RabbitConsumer
        implements Consumer<byte[]>
{

    private static final Logger LOG = Logger.getLogger(RabbitConsumer.class.getName() );
    /**
     * Number of reconnect attempts to post
     */
    private static final int ATTEMPTS = 3;

    private final RabbitConnection connection;
    private final String topic;
    private final String routingKey;
    private final int hashCode;

    public RabbitConsumer( RabbitConnection connection, String topic, String routingKey )
    {
        this.connection = Objects.requireNonNull( connection );
        this.topic = Objects.requireNonNull( topic );
        this.routingKey = Objects.requireNonNull( routingKey );
        hashCode = Objects.hash( topic, routingKey );
    }

    @Override
    public synchronized void accept( byte[] t )
    {
        if( t != null )
        {
            for( int attempt = 0; attempt < ATTEMPTS; attempt++ )
            {
                try
                {
                    LOG.log( Level.FINE,
                             () -> "Publishing " + t.length + " bytes to " + topic + " routing " + routingKey );

                    connection.getChannel( this ).
                            basicPublish( topic, routingKey, null, t );
                    return;
                }
                catch( IOException |
                       UncheckedIOException ex )
                {
                    LOG.log( Level.SEVERE, "On topic " + topic + " routing " + routingKey, ex );
                    connection.close();
                }
            }
            LOG.log( Level.SEVERE,
                     () -> "Failed after " + ATTEMPTS + " attempts to post to topic " + topic + " routing " + routingKey );
        }
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final RabbitConsumer other = (RabbitConsumer) obj;
        return Objects.equals( this.topic, other.topic )
               || Objects.equals( this.routingKey, other.routingKey );
    }

}
