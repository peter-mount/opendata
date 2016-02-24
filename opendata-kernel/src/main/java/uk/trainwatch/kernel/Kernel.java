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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import uk.trainwatch.util.ParserUtils;
import uk.trainwatch.util.sql.DataSourceProducer;

/**
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

    void init( List<String> args )
            throws IOException
    {
        commandArguments = () -> args;

        File userHome = new File( System.getProperty( "user.home" ) );

        homeDir = new File( userHome, ".area51" );

        if( homeDir.exists() && homeDir.isDirectory() ) {
            // If database.properties exists then read that for DB config
            File dbFile = new File( homeDir, "database.properties" );
            if( dbFile.exists() && dbFile.isFile() ) {
                DataSourceProducer.setFactory( ParserUtils.readProperties( dbFile ) );
                DataSourceProducer.setUseJndi( false );
            }
        }
    }

    int run()
            throws InterruptedException
    {
        // Fire the startup event
        startEvent.fire( commandArguments );

        // Now wait on the semaphore
        SEMAPHORE.acquire();
        return returnCode;
    }

    public File getHomeDir()
    {
        return homeDir;
    }

    public CommandArguments getCommandArguments()
    {
        return commandArguments;
    }

    public void exit()
    {
        exit( 0 );
    }

    public void exit( int returnCode )
    {
        this.returnCode = returnCode;
        SEMAPHORE.release( 10 );
    }
}
