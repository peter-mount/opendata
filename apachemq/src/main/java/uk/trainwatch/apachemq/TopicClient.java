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
import javax.jms.Session;
import javax.jms.Topic;
import uk.trainwatch.util.Consumers;

/**
 * Manages a Topic and when a message is received it then hands it to the Consumer
 */
class TopicClient
{

    private static final Logger LOG = Logger.getLogger( TopicClient.class.getName() );

    private final String topicName;
    private final Consumer<Message> consumer;
    private final int hashCode;
    private Session session;
    private Topic topic;
    private MessageConsumer messageConsumer;
    private Thread thread;
    private volatile boolean running;
    private final RemoteActiveMQConnection connection;

    public TopicClient( final RemoteActiveMQConnection connection,
                        final String topicName,
                        final Consumer<Message> consumer )
    {
        this.connection = Objects.requireNonNull( connection );
        this.topicName = Objects.requireNonNull( topicName );
        this.consumer = Consumers.guard( LOG, Objects.requireNonNull( consumer ));
        hashCode = topicName.hashCode();
    }

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
        final TopicClient other = (TopicClient) obj;
        return Objects.equals( this.topicName, other.topicName );
    }

    void start()
    {
        LOG.info( () -> "Client " + topicName + " starting" );
        try
        {
            session = connection.getConnection().
                    createSession( false, Session.AUTO_ACKNOWLEDGE );
            topic = session.createTopic( topicName );
            messageConsumer = session.createDurableSubscriber( topic, topicName + connection.getUsername() );
            if( thread == null )
            {
                thread = new Thread( () ->
                {
                    LOG.info( () -> "Starting to receive from " + topicName );
                    running = true;
                    try
                    {
                        while( connection.isRunning() && running )
                        {
                            Message message = messageConsumer.receive( 1000L );
                            if( message != null )
                            {
                                consumer.accept( message );
                            }
                        }
                    }
                    catch( Exception ex )
                    {
                        LOG.log( Level.SEVERE, "Exception in topic " + topicName, ex );
                        if( running )
                        {
                            connection.reconnect();
                        }
                    }
                    finally
                    {
                        thread = null;
                    }
                    LOG.info( () -> "Receive thread for " + topicName + " terminated" );
                } );
                thread.start();
            }
        }
        catch( JMSException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    TopicClient stop()
    {
        LOG.info( () -> "Client " + topicName + " stopping" );
        if( consumer != null && thread != null )
        {
            try
            {
                try
                {
                    try
                    {
                        messageConsumer.close();
                    }
                    catch( JMSException ex )
                    {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
                finally
                {
                    try
                    {
                        session.close();
                    }
                    catch( JMSException ex )
                    {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
            }
            finally
            {
                running = false;
            }
        }
        return null;
    }

}
