/*
 * Copyright 2014 peter.
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
package uk.trainwatch.web.signal;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.nrod.td.model.TDMessage;
import uk.trainwatch.nrod.td.model.TDMessageFactory;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.config.JNDIConfig;

/**
 * Ensures that, if enabled, we start receiving signal messages and populate our caches
 * <p>
 * @author peter
 */
@ApplicationScoped
@WebListener
public class SignalContextListener
        implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger( SignalContextListener.class.getName() );

    private static final String QUEUE_NAME = "signal.map";

    @Inject
    private Event<TDMessage> event;

    @Inject
    private Rabbit rabbit;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        if( JNDIConfig.INSTANCE.getBoolean( "signal.disabled" ) ) {
            LOG.log( Level.INFO, "Signalling has been disabled for this instance" );
            return;
        }

        // Dev, connect to prod for data but only request a single area to keep volumne down.
        String localHost = RabbitMQ.getHostname();
        boolean dev = "europa".equals( localHost ) || "phoebe".equals( localHost );

        // Now pass all messages to an event
        rabbit.queueDurableEvent( QUEUE_NAME,
                                  dev ? "nr.td.area.A3" : "nr.td.area.#",
                                  RabbitMQ.toJsonObject.andThen( TDMessageFactory.INSTANCE ),
                                  event );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

}
