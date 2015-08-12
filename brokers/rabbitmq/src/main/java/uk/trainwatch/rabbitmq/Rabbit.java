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

/**
 *
 * @author peter
 */
@ApplicationScoped
public class Rabbit
{

    private volatile RabbitConnection connection;

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
                    connection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );
                }
            }
        }
        return connection;
    }

    void close()
    {
        if( connection != null ) {
            connection.close();
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

}
