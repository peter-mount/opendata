/*
 * Copyright 2014 Peter T Mount.
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
package uk.trainwatch.nrod.feed.td;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.counter.RateMonitor;

@ApplicationScoped
public class TdArchiver
        implements Consumer<String>
{

    protected static final Logger LOG = Logger.getLogger( TdArchiver.class.getName() );

    private static final String ALL_ROUTING_KEY = "nr.td.all";

    private Consumer<String> monitor;

    @Inject
    private Rabbit rabbit;

    private Consumer<String> logger;

    public void start()
    {
        Function<String, Path> pathMapper = new DatePathMapper( "/usr/local/networkrail", "td", true );
        logger = Consumers.guard( LOG, FileRecorder.recordTo( pathMapper ) );

        monitor = RateMonitor.log( LOG, ALL_ROUTING_KEY );
        rabbit.queueDurableConsumer( "archive." + ALL_ROUTING_KEY, ALL_ROUTING_KEY, RabbitMQ.toString, this );
    }

    @Override
    public void accept( String t )
    {
        monitor.accept( t );
        logger.accept( t );
    }

}
