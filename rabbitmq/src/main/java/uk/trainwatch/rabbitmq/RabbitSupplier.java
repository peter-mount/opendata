/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.BlockingSupplier;
import uk.trainwatch.util.BlockingSupplierConsumer;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 * A {@link Supplier} which will provide received messages from a RabbitMQ Queue
 * <p>
 * @author Peter T Mount
 */
public class RabbitSupplier
        implements BlockingSupplier<byte[]>
{

    private static final Logger LOG = Logger.getLogger( RabbitSupplier.class.getName() );
    private final RabbitConnection connection;
    private final String queueName;
    private final String bindTopic;
    private final String bindRoutingKey;
    private final boolean durable;
    private final boolean parked;
    private final long parkDelay;
    private final String parkQueueName;
    private final Map<String, Object> queueProperties;
    private Channel channel;
    private QueueingConsumer consumer;
    private volatile boolean running;
    /**
     * Only allow a single message to be queued
     */
    private final BlockingSupplierConsumer<byte[]> supplier = new BlockingSupplierConsumer<>( 1 );

    RabbitSupplier( RabbitConnection connection, String queueName, String bindTopic, String bindRoutingKey,
                    boolean durable,
                    Map<String, Object> queueProperties )
    {
        this.connection = Objects.requireNonNull( connection );
        this.queueName = Objects.requireNonNull( queueName );
        this.bindTopic = bindTopic;
        this.bindRoutingKey = bindRoutingKey;
        this.durable = durable;

        if( durable )
        {
            this.queueProperties = queueProperties == null ? new HashMap<>() : queueProperties;

            // When durable, enforce a 5 minute ttl for all messages unless specified. This is a safety net so that we
            // don't cause the server to fill up with messages on a dead durable queue
            this.queueProperties.putIfAbsent( "x-message-ttl", 300000 );
        }
        else
        {
            // queue properties are optiona, so null is valid. If empty then replace with null
            this.queueProperties = (queueProperties == null || queueProperties.isEmpty()) ? null : queueProperties;
        }

        // Enable parking
        if( this.queueProperties != null && this.queueProperties.containsKey( "internal-park-queue" ) )
        {
            parked = true;
            parkDelay = (Long) this.queueProperties.remove( "internal-park-queue" );
            parkQueueName = queueName + "$park";
        }
        else
        {
            parked = false;
            parkDelay = 0L;
            parkQueueName = null;
        }
    }

    @Override
    public void setInvalid()
    {
        supplier.setInvalid();
        closeImpl();
    }

    protected final Channel getChannel()
            throws IOException
    {
        if( channel == null || !channel.isOpen() )
        {
            closeImpl();
            connect();
        }
        return channel;
    }

    public final synchronized QueueingConsumer getQueueingConsumer()
            throws IOException
    {
        if( channel == null || !channel.isOpen() )
        {
            closeImpl();
            connect();
        }
        return consumer;
    }

    @SuppressWarnings( "UseSpecificCatch" )
    public final void start()
    {
        DaemonThreadFactory.INSTANCE.newThread( () ->
        {
            running = true;
            while( running )
            {
                try
                {
                    processNextMessage();
                }
                catch( InterruptedException ex )
                {
                    running = false;
                    LOG.log( Level.INFO, "Interrupted so terminating" );
                }
                catch( IOException ex )
                {
                    if( running )
                    {
                        LOG.log( Level.SEVERE, "IOException, attempting to recover", ex );
                        closeImpl();
                    }
                }
                catch( Throwable ex )
                {
                    // Catch all others so the thread doesn't terminate but try to recover by reconnecting.
                    // Reconnecting will automatically nack the message and requeue it
                    if( running )
                    {
                        LOG.log( Level.WARNING, "Error whilst processing message, attempting to recover", ex );
                        closeImpl();
                    }
                    else
                    {
                        LOG.log( Level.SEVERE, "Error whilst processing message, appears to be fatal?", ex );
                    }
                }
            }
            LOG.log( Level.FINE, () -> "Worker " + queueName + " terminated" );

        } ).
                start();
    }

    public final void stop()
    {
        running = false;
        closeImpl();
    }

    private void closeImpl()
    {
        if( channel != null )
        {
            try
            {
                LOG.log( Level.INFO, () -> "Closing channel " + queueName );
                channel.close();
            }
            catch( IOException ex )
            {
                // ignore
            }
            finally
            {
                channel = null;
            }
        }
    }

    private void connect()
            throws IOException
    {
        LOG.log( Level.INFO, () -> "Connecting to " + queueName );

        channel = connection.getChannel( this );

        // When declaring the queue, if we are durable then don't auto-delete, but auto-delete when we're not
        channel.queueDeclare( queueName, durable, false, !durable, queueProperties );
        channel.basicQos( 1 );

        LOG.log( Level.FINE, () -> "Creating consumer on " + queueName );
        consumer = new QueueingConsumer( channel );

        if( bindTopic != null )
        {
            // Create the parking queue
            if( parked )
            {
                Map<String, Object> props = new HashMap<>();
                props.put( "x-message-ttl", parkDelay );
                props.put( "x-dead-letter-exchange", "" );
                props.put( "x-dead-letter-routing-key", queueName );

                channel.queueDeclare( parkQueueName, durable, false, !durable, props );
            }

            // Bind to the correct queue so if parked then it receives from the exchange
            channel.queueBind( parked ? parkQueueName : queueName, bindTopic, bindRoutingKey );
        }

        // Now consume the main queue
        channel.basicConsume( queueName, consumer );

        LOG.log( Level.INFO, () -> "Connected to " + queueName );
    }

    private void processNextMessage()
            throws IOException,
                   InterruptedException
    {
        final QueueingConsumer.Delivery delivery = getQueueingConsumer().
                nextDelivery();

        try
        {
            supplier.accept( delivery.getBody() );

            // ack as we have handled it safely
            channel.basicAck( delivery.getEnvelope().
                    getDeliveryTag(), false );

        }
        catch( Exception ex )
        {
            // nack and don't requeue
            getChannel().
                    basicNack( delivery.getEnvelope().
                            getDeliveryTag(), false, false );
        }
    }

    /**
     * Return a supplier for this queue
     * <p>
     * @return
     */
    @Override
    public byte[] get()
    {

        return supplier.get();
    }

}
