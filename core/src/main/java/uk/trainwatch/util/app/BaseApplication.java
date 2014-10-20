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
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * @author Peter T Mount
 */
public abstract class BaseApplication
{

    protected static final Logger LOG = Logger.getLogger( Application.class.getName() );
    protected static final File homeDir = new File( System.getProperty( "user.home" ) );
    protected static final File confDir = new File( homeDir, ".networkrail" );

    private static final String appUid = UUID.randomUUID().
            toString().
            substring( 0, 6 );

    /**
     * A Random ID that can be used to make queue names unique
     *
     * @return
     */
    public static String getAppId()
    {
        return appUid;
    }

    public static File getConfDir()
    {
        return confDir;
    }

    public static File getHomeDir()
    {
        return homeDir;
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
        try( FileReader r = new FileReader( file ) ) {
            props.load( r );
        }
        return props;
    }

}
