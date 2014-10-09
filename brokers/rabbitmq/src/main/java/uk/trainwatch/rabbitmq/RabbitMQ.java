/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.rabbitmq;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.json.JsonStructure;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.Streams;

/**
 *
 * @author Peter T Mount
 */
public class RabbitMQ
{

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

    /**
     * Convenience method that returns a {@link Consumer} of String that will publish to a remote topic
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
     * Convenience method that returns a {@link Consumer} of String that will publish to a remote topic
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
        return s -> c.accept( s.getBytes( UTF8 ) );
    }

    /**
     * Convenience method that returns a {@link Consumer} of {@link JsonStructure} that will publish JSON to a remote
     * topic.
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
     * Convenience method that returns a {@link Consumer} of {@link JsonStructure} that will publish JSON to a remote
     * topic.
     * <p>
     * @param connection Connection manager
     * @param topic      topic to publish to
     * @param routingKey routing key
     * <p>
     * @return consumer
     */
    public static Consumer<? super JsonStructure> jsonConsumer( RabbitConnection connection, String topic,
                                                                String routingKey )
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
        RabbitSupplier supplier = new RabbitSupplier( connection, queueName, topic, routingKey, durable, properties );
        Streams.supplierStream( supplier, factory );
        supplier.start();
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
}
