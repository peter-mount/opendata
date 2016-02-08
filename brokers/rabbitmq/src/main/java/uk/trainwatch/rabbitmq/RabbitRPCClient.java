/*
 * Copyright 2016 peter.
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

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.utility.BlockingCell;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Based loosely around {@link com.rabbitmq.client.RpcClient} an optimised client for a RPC client using RabbitMQ
 *
 * @author peter
 */
public final class RabbitRPCClient
        implements Closeable
{

    private final Channel channel;
    private final String exchange;
    private final String routingKey;
    private final int timeout;
    protected final static int NO_TIMEOUT = -1;

    private final Map<String, BlockingCell<Object>> continuationMap = new ConcurrentHashMap<>();

    private String replyQueue;
    private DefaultConsumer consumer;

    public static Map<String, Object> mapCall( RabbitConnection con, String routingKey, int timeout, Map<String, Object> args )
            throws IOException,
                   ShutdownSignalException,
                   TimeoutException
    {
        try( RabbitRPCClient client = new RabbitRPCClient( con, RabbitMQ.DEFAULT_TOPIC, routingKey, timeout ) ) {
            return client.mapCall( args );
        }
    }

    public static void execute( RabbitConnection con, String routingKey, int timeout, Map<String, Object> args, Consumer<Map<String, Object>> action )
            throws IOException,
                   ShutdownSignalException,
                   TimeoutException
    {
        try( RabbitRPCClient client = new RabbitRPCClient( con, RabbitMQ.DEFAULT_TOPIC, routingKey, timeout ) ) {
            client.execute( args, action );
        }
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public RabbitRPCClient( RabbitConnection con, String exchange, String routingKey, int timeout )
            throws IOException
    {
        channel = con.getChannel( this );
        this.exchange = exchange;
        this.routingKey = routingKey;
        if( timeout < NO_TIMEOUT ) {
            throw new IllegalArgumentException( "Timeout arguument must be NO_TIMEOUT(-1) or non-negative." );
        }
        this.timeout = timeout;

        replyQueue = setupReplyQueue();
        consumer = setupConsumer();
    }

    private void checkConsumer()
            throws IOException
    {
        if( consumer == null ) {
            throw new EOFException( "RpcClient is closed" );
        }
    }

    @Override
    public void close()
            throws IOException
    {
        if( consumer != null ) {
            channel.basicCancel( consumer.getConsumerTag() );
            consumer = null;
        }
    }

    private String setupReplyQueue()
            throws IOException
    {
        return channel.queueDeclare( "", false, true, true, null ).getQueue();
    }

    private DefaultConsumer setupConsumer()
            throws IOException
    {
        DefaultConsumer con = new DefaultConsumer( channel )
        {
            @Override
            public void handleShutdownSignal( String consumerTag,
                                              ShutdownSignalException signal )
            {
                continuationMap.values().forEach( c -> c.set( signal ) );
                consumer = null;
            }

            @Override
            public void handleDelivery( String consumerTag,
                                        Envelope envelope,
                                        AMQP.BasicProperties properties,
                                        byte[] body )
                    throws IOException
            {
                continuationMap.computeIfPresent( properties.getCorrelationId(),
                                                  ( replyId, blocker ) -> {
                                                      blocker.set( body );
                                                      return null;
                                                  } );
            }
        };
        channel.basicConsume( replyQueue, true, con );
        return con;
    }

    private void publish( AMQP.BasicProperties props, byte[] message )
            throws IOException
    {
        channel.basicPublish( exchange, routingKey, props, message );
    }

    private byte[] primitiveCall( AMQP.BasicProperties props, byte[] message )
            throws IOException,
                   ShutdownSignalException,
                   TimeoutException
    {
        checkConsumer();
        BlockingCell<Object> k = new BlockingCell<>();

        String replyId = RabbitMQ.newCorrelationId();
        props = ((props == null) ? new AMQP.BasicProperties.Builder() : props.builder())
                .correlationId( replyId )
                .replyTo( replyQueue )
                .build();
        continuationMap.put( replyId, k );

        publish( props, message );

        Object reply = k.uninterruptibleGet( timeout );
        if( reply instanceof ShutdownSignalException ) {
            ShutdownSignalException sig = (ShutdownSignalException) reply;
            ShutdownSignalException wrapper = new ShutdownSignalException( sig.isHardError(),
                                                                           sig.isInitiatedByApplication(),
                                                                           sig.getReason(),
                                                                           sig.getReference() );
            wrapper.initCause( sig );
            throw wrapper;
        }
        else {
            return (byte[]) reply;
        }
    }

    public Map<String, Object> mapCall( Map<String, Object> message )
            throws IOException,
                   ShutdownSignalException,
                   TimeoutException
    {
        byte[] reply = primitiveCall( null, RabbitMQ.toAMQPTable( message ) );
        return RabbitMQ.fromAMQPTable( reply );
    }

    public void execute( Map<String, Object> args, Consumer<Map<String, Object>> action )
            throws IOException,
                   ShutdownSignalException,
                   TimeoutException
    {
        Map<String, Object> ret = mapCall( args );
        if( ret != null ) {
            action.accept( ret );
        }
    }
}
