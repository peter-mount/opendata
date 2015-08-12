/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.rabbitmq;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.enterprise.event.Event;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
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
        queueConsumer( connection, queueName, topic, routingKey, false, properties, Consumers.consumeIfNotNull( mapper, consumer ) );
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
        queueConsumer( connection, queueName, topic, routingKey, true, properties, Consumers.consumeIfNotNull( mapper, consumer ) );
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
        RabbitSupplier supplier = new RabbitSupplier( connection, realQueue, topic, routingKey, durable, properties );
        Streams.supplierStream( supplier, factory );
        supplier.start();
    }

    private static void queueConsumer( RabbitConnection connection,
                                       String queueName,
                                       String topic, String routingKey,
                                       boolean durable,
                                       Map<String, Object> properties,
                                       Consumer<byte[]> consumer )
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
        return properties == null || properties.containsKey( NO_HOSTNAME ) ? queueName : (queueName + "." + getHostname());
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
}
