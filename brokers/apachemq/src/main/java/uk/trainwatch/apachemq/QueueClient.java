/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.apachemq;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import uk.trainwatch.util.Consumers;

/**
 * Manages a Topic and when a message is received it then hands it to the Consumer
 */
class QueueClient
        implements MQClient
{

    private static final Logger LOG = Logger.getLogger( QueueClient.class.getName() );

    private final String queueName;
    private final Consumer<Message> consumer;
    private final int hashCode;
    private Session session;
    private Queue queue;
    private MessageConsumer messageConsumer;
    private Thread thread;
    private volatile boolean running;
    private final RemoteActiveMQConnection connection;

    public QueueClient( final RemoteActiveMQConnection connection,
            final String queueName,
            final Consumer<Message> consumer )
    {
        this.connection = Objects.requireNonNull( connection );
        this.queueName = Objects.requireNonNull( queueName );
        this.consumer = Consumers.guard( LOG, Objects.requireNonNull( consumer ) );
        hashCode = queueName.hashCode();
    }

    @Override
    public Session getSession()
    {
        return session;
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
        final QueueClient other = (QueueClient) obj;
        return Objects.equals( this.queueName, other.queueName );
    }

    @Override
    public void start()
    {
        LOG.info( () -> "Client " + queueName + " starting" );
        try
        {
            session = connection.getConnection().
                    createSession( false, Session.AUTO_ACKNOWLEDGE );

            queue = session.createQueue( queueName );

            messageConsumer = session.createConsumer( queue );

            messageConsumer.setMessageListener( consumer::accept );
        } catch( JMSException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public QueueClient stop()
    {
        LOG.info( () -> "Client " + queueName + " stopping" );
        if( consumer != null && thread != null )
        {
            try
            {
                try
                {
                    try
                    {
                        messageConsumer.close();
                    } catch( JMSException ex )
                    {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                } finally
                {
                    try
                    {
                        session.close();
                    } catch( JMSException ex )
                    {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
            } finally
            {
                running = false;
            }
        }
        return null;
    }

}
