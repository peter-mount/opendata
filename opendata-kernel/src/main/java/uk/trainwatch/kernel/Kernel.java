/*
 * Copyright 2015 peter.
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
package uk.trainwatch.kernel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import uk.trainwatch.util.MapBuilder;
import uk.trainwatch.util.ParserUtils;
import uk.trainwatch.util.sql.DataSourceProducer;

/**
 * The injectable Kernel.
 *
 * @author peter
 */
@ApplicationScoped
public class Kernel
{

    private static final Semaphore SEMAPHORE = new Semaphore( 0 );
    private int returnCode = 0;

    @Inject
    Event<CommandArguments> startEvent;

    private CommandArguments commandArguments;
    private File homeDir;

    private FileSystem config;

    /**
     * Called by main to initialise the Kernel
     *
     * @param args Command line arguments
     *
     * @throws IOException if failed to read property files
     */
    void init( List<String> args )
            throws IOException
    {
        commandArguments = () -> args;

        File userHome = new File( System.getProperty( "user.home" ) );

        homeDir = new File( userHome, ".area51" );

        homeDir.mkdirs();

        config = FileSystems.newFileSystem( URI.create( "local://config" ),
                                            MapBuilder.<String, Object>builder()
                                            .add( "baseDirectory", new File( homeDir, "config" ).getAbsolutePath() )
                                            .build()
        );

        Path dbProperties = config.getPath( "/database.properties" );
        if( Files.exists( dbProperties, LinkOption.NOFOLLOW_LINKS ) ) {
            // If database.properties exists then read that for DB config
            DataSourceProducer.setFactory( ParserUtils.readProperties( dbProperties ) );
            DataSourceProducer.setUseJndi( false );
        }
    }

    /**
     * Runs the main Kernel thread. This will not return until one of {@link #exit()} or {@link #exit(int)} is called.
     *
     * @return
     *
     * @throws InterruptedException
     */
    int run()
            throws InterruptedException
    {
        // Fire the startup event
        startEvent.fire( commandArguments );

        // Now wait on the semaphore
        SEMAPHORE.acquire();
        return returnCode;
    }

    public FileSystem getConfig()
    {
        return config;
    }

    public Properties getProperties( String name )
            throws IOException
    {
        try {
            Path p = config.getPath( name + ".properties" );
            if( Files.exists( p, LinkOption.NOFOLLOW_LINKS ) && Files.isRegularFile( p, LinkOption.NOFOLLOW_LINKS ) ) {
                return ParserUtils.readProperties( p );
            }
            throw new FileNotFoundException( "Unknown properties " + name );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    public File getHomeDir()
    {
        return homeDir;
    }

    public CommandArguments getCommandArguments()
    {
        return commandArguments;
    }

    public void exit( int returnCode )
    {
        this.returnCode = returnCode;
        SEMAPHORE.release( 10 );
    }
}
