/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.apachemq;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;

/**
 * Watchdog which monitors the connection, reconnecting if it's broken.
 * <p>
 * If it cannot reconnect it then increases it's period so that we don't flood the server during an outage. Once it
 * finally reconnects the period is reset to the default.
 */
class Watchdog
        implements Runnable
{

    private static final Logger LOG = Logger.getLogger( Watchdog.class.getName() );

    /**
     * Period watchdog runs normally
     */
    private static final long WATCHDOG_PERIOD = 1000L;
    /**
     * When scaling retries this is the max wait time between attempts
     */
    private static final long WATCHDOG_MAX_RECONNECT = 60000L;

    private long period = WATCHDOG_PERIOD;
    private Thread thread;
    private volatile boolean running;
    private volatile boolean reconnect;
    private final RemoteActiveMQConnection connection;

    Watchdog( final RemoteActiveMQConnection connection )
    {
        this.connection = Objects.requireNonNull( connection );
    }

    public void start()
    {
        if( thread == null )
        {
            running = true;
            reconnect = true;
            period = WATCHDOG_PERIOD;
            thread = new Thread( this, "Watchdog" );
            thread.start();
        }
    }

    public void stop()
    {
        running = false;
        connection.disconnect();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void reconnect()
    {
        reconnect = true;
        if( period < WATCHDOG_MAX_RECONNECT )
        {
            period *= 2L;
        }
        LOG.info( () -> "Retry period now " + period );
    }

    @Override
    @SuppressWarnings( value = "SleepWhileInLoop" )
    public void run()
    {
        LOG.info( () -> "Watchdog started" );
        while( running )
        {
            try
            {
                Thread.sleep( period );
                LOG.fine( () -> "Watchdog run=" + running + " reconnect=" + reconnect );
                if( running && reconnect )
                {
                    doReconnect();
                }
            }
            catch( InterruptedException |
                   JMSException |
                   RuntimeException ex )
            {
                LOG.log( Level.SEVERE, "Watchdog failure", ex );
                reconnect();
            }
        }
        LOG.info( () -> "Watchdog shutdown" );
    }

    private void doReconnect()
            throws JMSException,
                   InterruptedException
    {
        if( running )
        {
            connection.connect();
            // Reset as we are now running
            reconnect = false;
            period = WATCHDOG_PERIOD;
        }
    }

}
