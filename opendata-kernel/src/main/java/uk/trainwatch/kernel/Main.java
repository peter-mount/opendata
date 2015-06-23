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

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.runlevel.RunLevelController;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jvnet.hk2.annotations.Service;

/**
 *
 * @author peter
 */
@Service
public class Main
{

    private static final Logger LOG = Logger.getLogger( Main.class.getName() );
    private static String args[];
    private static ServiceLocator serviceLocator;

    private static final Semaphore SEMAPHORE = new Semaphore( 0 );

    @Inject
    RunLevelController controller;

    public String[] getArgs()
    {
        return args;
    }

    public ServiceLocator getServiceLocator()
    {
        return serviceLocator;
    }

    public void shutdown()
    {
        SEMAPHORE.release();
    }

    @PostConstruct
    void start()
    {
        LOG.log( Level.INFO, "Starting up services" );

        for( int level = 1; level < 5; level++ )
        {
            LOG.log( Level.FINE, "Entering Runlevel {0}", level );
            controller.proceedTo( level );
        }
    }

    @PreDestroy
    void end()
    {
        LOG.log( Level.INFO, "Shutting down services" );

    }

    public static void main( String... args )
            throws Exception
    {
        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();

        // On VM shutdown, shutdown all services
        Runtime.getRuntime().addShutdownHook( new Thread( () ->
        {
            try
            {
                LOG.log( Level.INFO, "Shutting down" );
                locator.shutdown();
                LOG.log( Level.INFO, "System shutdown" );
            }
            finally
            {
                SEMAPHORE.release();
            }
        } ) );

        // Now locate ourselves. This will then start the lifecycle
        locator.getService( Main.class );

        // Now wait until we shutdown
        SEMAPHORE.acquire( 1 );
    }

}
