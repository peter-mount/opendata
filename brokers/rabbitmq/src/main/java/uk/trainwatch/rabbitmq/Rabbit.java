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

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.JsonStructure;
import org.apache.commons.configuration.Configuration;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.config.PrivateConfiguration;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class Rabbit
{

    private volatile RabbitConnection connection;

    @Inject
    @PrivateConfiguration("rabbit")
    private Configuration configuration;

    public boolean isDev()
    {
        String localHost = RabbitMQ.getHostname();
        return "europa".equals( localHost ) || "phoebe".equals( localHost );
    }

    public RabbitConnection getConnection()
    {
        if( connection == null ) {
            synchronized( this ) {
                if( connection == null ) {
                    //connection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );
                    connection = new RabbitConnection( configuration.getString( "username" ),
                                                       configuration.getString( "password" ),
                                                       configuration.getString( "host" ) );
                }
            }
        }
        return connection;
    }

    void close()
    {
        if( connection != null ) {
            try {
                connection.close();
            }
            catch( IllegalStateException ex ) {
            }
        }
    }

    /**
     * Create a queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Event to fire
     */
    public <T> void queueConsumer( String queueName, String routingKey, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueConsumer( getConnection(), queueName, routingKey, mapper, consumer );
    }

    /**
     * Create a queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Event to fire
     */
    public <T> void queueConsumer( String queueName, String routingKey, Map<String, Object> properties, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueConsumer( getConnection(), queueName, routingKey, properties, mapper, consumer );
    }

    /**
     * Create a queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Consumer to fire
     */
    public <T> void queueConsumer( String queueName, String topic, String routingKey, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueConsumer( getConnection(), queueName, topic, routingKey, mapper, consumer );
    }

    /**
     * Create a durable queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Consumer to fire
     */
    public <T> void queueDurableConsumer( String queueName, String routingKey, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueDurableConsumer( getConnection(), queueName, routingKey, mapper, consumer );
    }

    /**
     * Create a durable queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Consumer to fire
     */
    public <T> void queueDurableConsumer( String queueName, String routingKey, Map<String, Object> properties, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueDurableConsumer( getConnection(), queueName, routingKey, properties, mapper, consumer );
    }

    /**
     * Create a durable queue and send all messages to a consumer
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param topic
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param consumer   Consumer to fire
     */
    public <T> void queueDurableConsumer( String queueName, String topic, String routingKey, Function<byte[], T> mapper, Consumer<T> consumer )
    {
        RabbitMQ.queueDurableConsumer( getConnection(), queueName, topic, routingKey, mapper, consumer );
    }

    /**
     * Create a queue and send all messages to a CDI Event
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public <T> void queueEvent( String queueName, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        RabbitMQ.queueEvent( getConnection(), queueName, routingKey, mapper, event );
    }

    /**
     * Create a queue and send all messages to a CDI Event
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public <T> void queueEvent( String queueName, String topic, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        RabbitMQ.queueEvent( getConnection(), queueName, topic, routingKey, mapper, event );
    }

    /**
     * Create a queue and send all messages to a CDI Event
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public <T> void queueDurableEvent( String queueName, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        RabbitMQ.queueDurableEvent( getConnection(), queueName, routingKey, mapper, event );
    }

    /**
     * Create a queue and send all messages to a CDI Event
     * <p>
     * @param <T>        Type of message
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public <T> void queueDurableEvent( String queueName, String topic, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        RabbitMQ.queueDurableEvent( getConnection(), queueName, topic, routingKey, mapper, event );
    }

    public Consumer<byte[]> publisher( String routingKey )
    {
        return publish( RabbitMQ.DEFAULT_TOPIC, routingKey );
    }

    public Consumer<byte[]> publish( String topic, String routingKey )
    {
        return new RabbitConsumer( getConnection(), topic, routingKey );
    }

    public <T> Consumer<T> publish( String routingKey, Function<T, byte[]> mapper )
    {
        return Consumers.consumeIfNotNull( mapper, publisher( routingKey ) );
    }

    public <T> Consumer<T> publish( String topic, String routingKey, Function<T, byte[]> mapper )
    {
        return Consumers.consumeIfNotNull( mapper, publish( topic, routingKey ) );
    }

    public Consumer<String> publishString( String routingKey )
    {
        return publishString( RabbitMQ.DEFAULT_TOPIC, routingKey );
    }

    public Consumer<String> publishString( String topic, String routingKey )
    {
        return publish( topic, routingKey, s -> s != null && !s.isEmpty() ? s.getBytes( RabbitMQ.UTF8 ) : null );
    }

    public Consumer<? super JsonStructure> publishJson( String routingKey )
    {
        return publishJson( RabbitMQ.DEFAULT_TOPIC, routingKey );
    }

    public Consumer<? super JsonStructure> publishJson( String topic, String routingKey )
    {
        return publish( topic, routingKey, j -> j == null ? null : RabbitMQ.jsonToBytes.apply( j ) );
    }
}
