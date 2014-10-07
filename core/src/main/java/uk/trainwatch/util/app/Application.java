/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter T Mount
 */
public abstract class Application
{

    protected static final Logger LOG = Logger.getLogger( Application.class.getName() );
    private static final Semaphore SEMAPHORE = new Semaphore( 0 );
    private static final File homeDir = new File( System.getProperty( "user.home" ) );
    private static final File confDir = new File( homeDir, ".networkrail" );

    protected void run()
            throws IOException,
                   InterruptedException
    {
        LOG.log( Level.INFO, "Initialising Network Rail Bridge" );

        setupBrokers();
        setupApplication();
        start();

        waitOnSignal();
        stop();
        LOG.log( Level.INFO, "Main thread completed" );
    }

    protected abstract void setupBrokers()
            throws IOException;

    protected void start()
    {
        LOG.log( Level.INFO, "Starting" );
    }

    protected void stop()
    {
        LOG.log( Level.INFO, "Shutting down" );
    }

    protected abstract void setupApplication()
            throws IOException;

    private static void waitOnSignal()
            throws InterruptedException
    {
        Runtime.getRuntime().
                addShutdownHook( new Thread( SEMAPHORE::release ) );
        LOG.log( Level.INFO, "Bridge running" );
        SEMAPHORE.acquire( 1 );
    }

    /**
     * Load {@link Properties} file from ~/networkrail/{name}
     * <p>
     * @param name <p>
     * @return <p>
     * @throws IOException
     */
    public static Properties loadProperties( String name )
            throws IOException
    {
        return loadProperties( new File( confDir, name ) );
    }

    /**
     * Load {@link Properties} from a {@link File}
     * <p>
     * @param file <p>
     * @return <p>
     * @throws IOException
     */
    public static Properties loadProperties( File file )
            throws IOException
    {
        final Properties props = new Properties();
        try( FileReader r = new FileReader( file ) )
        {
            props.load( r );
        }
        return props;
    }

}
