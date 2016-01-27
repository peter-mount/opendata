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

    private static final Logger LOG = Logger.getLogger( RabbitConsumer.class.getName() );
    /**
     * Number of reconnect attempts to post
     */
    private static final int ATTEMPTS = 3;

    private final RabbitConnection connection;
    private final String topic;
    private final String routingKey;
    private final int hashCode;

    public RabbitConsumer( RabbitConnection connection, String routingKey )
    {
        this( connection, RabbitMQ.DEFAULT_TOPIC, routingKey );
    }

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
        if( t != null ) {
            for( int attempt = 0; attempt < ATTEMPTS; attempt++ ) {
                try {
                    LOG.log( Level.FINE,
                             () -> "Publishing " + t.length + " bytes to " + topic + " routing " + routingKey );

                    connection.getChannel( this ).
                            basicPublish( topic, routingKey, null, t );
                    return;
                }
                catch( IOException |
                       UncheckedIOException ex ) {
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
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final RabbitConsumer other = (RabbitConsumer) obj;
        return Objects.equals( this.topic, other.topic )
               || Objects.equals( this.routingKey, other.routingKey );
    }

}
