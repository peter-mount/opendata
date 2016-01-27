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

import com.rabbitmq.client.LongString;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.impl.MethodArgumentReader;
import com.rabbitmq.client.impl.MethodArgumentWriter;
import com.rabbitmq.client.impl.ValueReader;
import com.rabbitmq.client.impl.ValueWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.enterprise.event.Event;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.util.CollectionUtils;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.config.Configuration;
import uk.trainwatch.util.config.JNDIConfig;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author Peter T Mount
 */
public class RabbitMQ
{

    private static final Logger LOG = Logger.getLogger( RabbitMQ.class.getName() );
    private static final AtomicInteger CORRELATION_ID = new AtomicInteger();

    public static final String DEFAULT_TOPIC = "amq.topic";
    public static final Charset UTF8 = Charset.forName( "UTF-8" );

    /**
     * Function to convert a byte[] to a String using UTF-8
     */
    public static final Function<byte[], String> toString = b -> new String( b, UTF8 );
    /**
     * Function to conert a String to a byte[] using UTF-8
     */
    public static final Function<String, byte[]> toBytes = s -> s.getBytes( UTF8 );

    /**
     * Composed function to encode a {@link JsonStructure} into a byte[] using UTF-8
     */
    public static final Function<? super JsonStructure, byte[]> jsonToBytes = JsonUtils.toString.andThen( toBytes );

    public static final Function<byte[], JsonObject> toJsonObject = toString.andThen( JsonUtils.parseJsonObject );

    public static final Function<byte[], JsonArray> toJsonArray = toString.andThen( JsonUtils.parseJsonArray );

    /**
     * Convenience method that returns a {@link Consumer} of String that will publish to the remote default topic
     * <p>
     * Note: If the string being consumed is null or empty then no action will be taken. This will mean that only messages with
     * content will be sent.
     * <p>
     * @param connection Connection manager
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<String> stringConsumer( RabbitConnection connection, String routingKey )
    {
        return stringConsumer( connection, DEFAULT_TOPIC, routingKey );
    }

    /**
     * Convenience method that returns a {@link Consumer} of String that will publish to a remote topic.
     * <p>
     * Note: If the string being consumed is null or empty then no action will be taken. This will mean that only messages with
     * content will be sent.
     * <p>
     * @param connection Connection manager
     * @param topic      topic to publish to
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<String> stringConsumer( RabbitConnection connection, String topic, String routingKey )
    {
        Consumer<byte[]> c = rabbitConsumer( connection, topic, routingKey );
        return s -> {
            if( s != null && !s.isEmpty() ) {
                c.accept( s.getBytes( UTF8 ) );
            }
        };
    }

    /**
     * Convenience method that returns a {@link Consumer} of {@link JsonStructure} that will publish JSON to a remote topic.
     * <p>
     * @param connection Connection manager
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<? super JsonStructure> jsonConsumer( RabbitConnection connection, String routingKey )
    {
        return jsonConsumer( connection, DEFAULT_TOPIC, routingKey );
    }

    /**
     * Convenience method that returns a {@link Consumer} of {@link JsonStructure} that will publish JSON to a remote topic.
     * <p>
     * @param connection Connection manager
     * @param topic      topic to publish to
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<? super JsonStructure> jsonConsumer( RabbitConnection connection, String topic, String routingKey )
    {
        Consumer<byte[]> c = rabbitConsumer( connection, topic, routingKey );
        return s -> c.accept( jsonToBytes.apply( s ) );
    }

    /**
     * Convenience method that returns a {@link Consumer} of byte[] that will publish to a remote topic.
     * <p>
     * This is identical to using the constructor
     * {@link RabbitConsumer#RabbitConsumer(uk.trainwatch.rabbitmq.RabbitConnection, java.lang.String, java.lang.String)}
     * <p>
     * @param connection Connection manager
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<byte[]> rabbitConsumer( RabbitConnection connection, String routingKey )
    {
        return rabbitConsumer( connection, DEFAULT_TOPIC, routingKey );
    }

    /**
     * Convenience method that returns a {@link Consumer} of byte[] that will publish to a remote topic.
     * <p>
     * This is identical to using the constructor
     * {@link RabbitConsumer#RabbitConsumer(uk.trainwatch.rabbitmq.RabbitConnection, java.lang.String, java.lang.String)}
     * <p>
     * @param connection Connection manager
     * @param topic      topic to publish to
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<byte[]> rabbitConsumer( RabbitConnection connection, String topic, String routingKey )
    {
        return new RabbitConsumer( connection, topic, routingKey );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName, Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, null, null, false, null, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName, String routingKey,
                                    Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, DEFAULT_TOPIC, routingKey, false, null, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName, String topic, String routingKey,
                                    Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, topic, routingKey, false, null, factory );
    }

    public static <T> void queueConsumer( RabbitConnection connection, String queueName, String routingKey,
                                          Map<String, Object> properties,
                                          Function<byte[], T> mapper,
                                          Consumer<T> consumer )
    {
        queueConsumer( connection, queueName, DEFAULT_TOPIC, routingKey, properties, mapper, consumer );
    }

    public static <T> void queueConsumer( RabbitConnection connection, String queueName, String routingKey,
                                          Function<byte[], T> mapper,
                                          Consumer<T> consumer )
    {
        queueConsumer( connection, queueName, DEFAULT_TOPIC, routingKey, null, mapper, consumer );
    }

    public static <T> void queueConsumer( RabbitConnection connection, String queueName, String topic, String routingKey,
                                          Function<byte[], T> mapper,
                                          Consumer<T> consumer )
    {
        queueConsumer( connection, queueName, DEFAULT_TOPIC, routingKey, null, mapper, consumer );
    }

    public static <T> void queueConsumer( RabbitConnection connection, String queueName, String topic, String routingKey,
                                          Map<String, Object> properties,
                                          Function<byte[], T> mapper,
                                          Consumer<T> consumer )
    {
        Consumer<byte[]> c = Consumers.consumeIfNotNull( mapper, consumer );
        queueConsumer( connection, queueName, topic, routingKey, false, properties, d -> c.accept( d.getBody() ) );
    }

    public static <T> void queueDurableConsumer( RabbitConnection connection, String queueName, String routingKey,
                                                 Function<byte[], T> mapper,
                                                 Consumer<T> consumer )
    {
        queueDurableConsumer( connection, queueName, DEFAULT_TOPIC, routingKey, null, mapper, consumer );
    }

    public static <T> void queueDurableConsumer( RabbitConnection connection, String queueName, String routingKey,
                                                 Map<String, Object> properties,
                                                 Function<byte[], T> mapper,
                                                 Consumer<T> consumer )
    {
        queueDurableConsumer( connection, queueName, DEFAULT_TOPIC, routingKey, properties, mapper, consumer );
    }

    public static <T> void queueDurableConsumer( RabbitConnection connection, String queueName, String topic, String routingKey,
                                                 Function<byte[], T> mapper,
                                                 Consumer<T> consumer )
    {
        queueDurableConsumer( connection, queueName, topic, routingKey, null, mapper, consumer );
    }

    public static <T> void queueDurableConsumer( RabbitConnection connection, String queueName, String topic, String routingKey,
                                                 Map<String, Object> properties,
                                                 Function<byte[], T> mapper,
                                                 Consumer<T> consumer )
    {
        Consumer<byte[]> c = Consumers.consumeIfNotNull( mapper, consumer );
        queueConsumer( connection, queueName, topic, routingKey, true, properties, d -> c.accept( d.getBody() ) );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param <T>        Type of message
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public static <T> void queueEvent( RabbitConnection connection, String queueName, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        queueConsumer( connection, queueName, routingKey, mapper, event::fire );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param <T>        Type of message
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public static <T> void queueEvent( RabbitConnection connection, String queueName, String topic, String routingKey,
                                       Function<byte[], T> mapper,
                                       Event<T> event )
    {
        queueConsumer( connection, queueName, topic, routingKey, mapper, event::fire );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, null, null, true, null, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName, String routingKey,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, DEFAULT_TOPIC, routingKey, true, null, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param <T>        Type of message
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public static <T> void queueDurableEvent( RabbitConnection connection, String queueName, String routingKey, Function<byte[], T> mapper, Event<T> event )
    {
        queueDurableConsumer( connection, queueName, routingKey, mapper, event::fire );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param <T>        Type of message
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic
     * @param routingKey Routing Key to bind to
     * @param mapper     Mapping function to form T from a byte[]
     * @param event      CDI Event to fire
     */
    public static <T> void queueDurableEvent( RabbitConnection connection, String queueName, String topic, String routingKey, Function<byte[], T> mapper,
                                              Event<T> event )
    {
        queueDurableConsumer( connection, queueName, topic, routingKey, mapper, event::fire );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName, String topic,
                                           String routingKey,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, topic, routingKey, true, null, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName,
                                    Map<String, Object> properties,
                                    Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, null, null, false, properties, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName, String routingKey,
                                    Map<String, Object> properties,
                                    Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, DEFAULT_TOPIC, routingKey, false, properties, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueStream( RabbitConnection connection, String queueName, String topic, String routingKey,
                                    Map<String, Object> properties,
                                    Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, topic, routingKey, false, properties, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName,
                                           Map<String, Object> properties,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, null, null, true, properties, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param routingKey Routing Key to bind to
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName, String routingKey,
                                           Map<String, Object> properties,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, DEFAULT_TOPIC, routingKey, true, properties, factory );
    }

    /**
     * Create an infinite Stream fed by the broker. This stream will run on a background thread.
     * <p>
     * @param connection Broker connection
     * @param queueName  Queue name
     * @param topic      Topic to bind to
     * @param routingKey Routing Key to bind to
     * @param properties Queue properties
     * @param factory    Consumer that will take the final stream and consume it
     */
    public static void queueDurableStream( RabbitConnection connection, String queueName, String topic,
                                           String routingKey,
                                           Map<String, Object> properties,
                                           Consumer<Stream<byte[]>> factory )
    {
        queueStream( connection, queueName, topic, routingKey, true, properties, factory );
    }

    private static void queueStream( RabbitConnection connection,
                                     String queueName,
                                     String topic, String routingKey,
                                     boolean durable,
                                     Map<String, Object> properties,
                                     Consumer<Stream<byte[]>> factory )
    {
        String realQueue = getRealQueue( queueName, properties );
        RabbitSupplier supplier = new RabbitSupplier( connection, realQueue, topic, routingKey, null, durable, properties );
        Streams.supplierStream( supplier, factory );
        supplier.start();
    }

    public static void queueConsumer( RabbitConnection connection,
                                      String queueName,
                                      String topic, String routingKey,
                                      boolean durable,
                                      Map<String, Object> properties,
                                      Consumer<QueueingConsumer.Delivery> consumer )
    {
        new RabbitSupplier( connection,
                            getRealQueue( queueName, properties ),
                            topic,
                            routingKey,
                            consumer.andThen( RateMonitor.log( LOG, routingKey == null ? queueName : (queueName + "[" + routingKey + "]") ) ),
                            durable, properties ).
                start();
    }

    public static final String NO_HOSTNAME = "no.host.name";

    private static String getRealQueue( String queueName, Map<String, Object> properties )
    {
        return properties != null && properties.containsKey( NO_HOSTNAME ) ? queueName : (queueName + "." + getHostname());
    }

    public static String getHostname()
    {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch( UnknownHostException ex ) {
            return "localHost";
        }
    }

    /**
     * Create properties to mark this queue as being parked
     * <p>
     * @param delay Delay in milliseconds to hold a message before delivery
     * <p>
     * @return Map
     */
    public static Map<String, Object> parkQueue( long delay )
    {
        return parkQueue( new HashMap<>(), delay );
    }

    /**
     * Create properties to mark this queue as being parked
     * <p>
     * @param properties Existing properties map
     * @param delay      Delay in milliseconds to hold a message before delivery
     * <p>
     * @return Map
     */
    public static Map<String, Object> parkQueue( Map<String, Object> properties, long delay )
    {
        Objects.requireNonNull( properties );
        properties.put( "internal-park-queue", delay );
        return properties;
    }

    /**
     * Set the queue Time To Live value.
     * <p>
     * By default if this is not set the queue will default to 5 minutes (set in our code, RabbitMQ's default is no timeout).
     * <p>
     * @param delay time to live in milliseconds
     * <p>
     * @return
     */
    public static Map<String, Object> queueTTL( long delay )
    {
        return queueTTL( new HashMap<>(), delay );
    }

    /**
     * Set the queue Time To Live value.
     * <p>
     * By default if this is not set the queue will default to 5 minutes (set in our code, RabbitMQ's default is no timeout).
     * <p>
     * @param properties Properties
     * @param delay      time to live in milliseconds
     * <p>
     * @return
     */
    public static Map<String, Object> queueTTL( Map<String, Object> properties, long delay )
    {
        Objects.requireNonNull( properties );
        properties.put( "x-message-ttl", delay );
        return properties;
    }

    public static RabbitConnection createJNDIConnection( String jndiPrefix )
    {
        Configuration config = JNDIConfig.INSTANCE;
        return new RabbitConnection(
                config.get( jndiPrefix + "/user" ),
                config.get( jndiPrefix + "/password" ),
                config.get( jndiPrefix + "/host" )
        );
    }

    /**
     * Convert a map to an AMQP table
     *
     * @param args
     *
     * @return
     */
    public static byte[] toAMQPTable( Map<String, Object> args )
    {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            MethodArgumentWriter writer = new MethodArgumentWriter( new ValueWriter( new DataOutputStream( buffer ) ) );

            writer.writeTable( fixAMQPTable( args ) );
            writer.flush();
            return buffer.toByteArray();
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    /**
     * Convert an AMQP table to a map
     *
     * @param reply
     *
     * @return
     */
    public static Map<String, Object> fromAMQPTable( byte[] reply )
    {
        try {
            return fixAMQPTable(
                    new MethodArgumentReader( new ValueReader( new DataInputStream( new ByteArrayInputStream( reply ) ) ) )
                    .readTable()
            );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    /**
     * Fixes a map so that we have valid data present
     *
     * @param m
     *
     * @return
     */
    public static Map<String, Object> fixAMQPTable( Map<String, Object> m )
    {
        if( m == null ) {
            return Collections.emptyMap();
        }

        if( !m.isEmpty() ) {
            Iterator<Map.Entry<String, Object>> it = m.entrySet().iterator();
            while( it.hasNext() ) {
                Map.Entry<String, Object> e = it.next();
                Object value = e.getValue();
                // Convert certain types to supported ones otherwise
                if( value instanceof LongString ) {
                    e.setValue( value.toString() );
                }
                else if( value instanceof BigInteger ) {
                    // wrap in a decimal
                    e.setValue( new BigDecimal( (BigInteger) value ) );
                }
                else if( value instanceof Map ) {
                    // Recurse
                    e.setValue( fixAMQPTable( (Map<String, Object>) value ) );
                }
                else if( value instanceof Collection ) {
                    e.setValue( CollectionUtils.unmodifiableList( (Collection) value ) );
                }
                else if( !(value == null
                           || value instanceof String
                           || value instanceof Integer
                           || value instanceof BigDecimal
                           || value instanceof Date
                           || value instanceof Byte
                           || value instanceof Double
                           || value instanceof Float
                           || value instanceof Long
                           || value instanceof Short
                           || value instanceof Boolean
                           || value instanceof byte[]
                           || value instanceof List
                           || value instanceof Object[]) ) {
                    // Not one of the remaining valid types then remove it
                    it.remove();
                }
            }
        }

        return m;
    }

    public static void rpcConsumer( RabbitConnection connection, String topic, Map<String, Object> properties,
                                    String callName,
                                    UnaryOperator<Map<String, Object>> call )
    {
        // Queue name must start with "rpc". We also use the same for the routing key
        Objects.requireNonNull( callName );
        String queueName = "rpc." + callName;

        Map<String, Object> props = properties == null ? new HashMap<>() : properties;

        // Ensure we don't have a host name - i.e. one server responds to a message
        props.put( NO_HOSTNAME, true );

        new RabbitSupplier( connection, queueName, topic, queueName, new RabbitRPCInvoker( connection, call ), false, null ).
                start();
    }

    public static String newCorrelationId()
    {
        return String.valueOf( CORRELATION_ID.incrementAndGet() );
    }
}
