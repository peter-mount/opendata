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
package uk.trainwatch.nre.darwin;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import static uk.trainwatch.nre.darwin.DarwinArchiver.LOG;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class DarwinDBImport
        implements ServletContextListener,
                   Consumer<String>
{

    private static final Logger LOG = Logger.getLogger( DarwinDBImport.class.getName() );

    private static final String QUEUE = "darwin.db";
    private static final String ROUTING_KEY = "nre.push";

    @Inject
    private Rabbit rabbit;

    @Inject
    private DarwinImport darwinImport;

    @Inject
    DarwinArchiver darwinArchiver;

    private Consumer<String> monitor;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        LOG.log( Level.WARNING, "******************* Started Darwin ");
        
        try {
            monitor = RateMonitor.log( LOG, "nre.push" );
            rabbit.queueDurableConsumer( QUEUE, ROUTING_KEY, RabbitMQ.toString, this );

            monitor.accept( null );
            darwinArchiver.accept( null );
            darwinImport.accept( null );
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

    @Override
    public void accept( String t )
    {
        try {
            monitor.accept( t );
            darwinArchiver.accept( t );
            darwinImport.accept( t );
        }
        catch( Throwable ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

}
