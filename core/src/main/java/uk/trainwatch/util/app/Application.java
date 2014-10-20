/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.app;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

/**
 *
 * @author Peter T Mount
 */
public abstract class Application
        extends BaseApplication
{

    private static final Semaphore SEMAPHORE = new Semaphore( 0 );

    protected void run()
            throws IOException,
                   InterruptedException
    {
        setupBrokers();
        setupApplication();
        start();

        waitOnSignal();
        stop();
        LOG.log( Level.FINE, "Main thread completed" );
    }

    protected void setupBrokers()
            throws IOException
    {
    }

    protected void start()
    {
        LOG.log( Level.INFO, "Starting" );
    }

    protected void stop()
    {
        LOG.log( Level.INFO, "Shutting down" );
    }

    protected void setupApplication()
            throws IOException
    {
    }

    protected void waitOnSignal()
            throws InterruptedException
    {
        Runtime.getRuntime().
                addShutdownHook( new Thread( SEMAPHORE::release ) );
        LOG.log( Level.INFO, "Bridge running" );
        SEMAPHORE.acquire( 1 );
    }

}
