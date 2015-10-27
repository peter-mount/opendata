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
package uk.trainwatch.nre.darwin;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;

@ApplicationScoped
public class DarwinArchiver
        implements Consumer<String>
{

    protected static final Logger LOG = Logger.getLogger( DarwinArchiver.class.getName() );

    @Inject
    private Rabbit rabbit;

    private Consumer<String> logger;

    @PostConstruct
    public void setup()
    {
        Function<String, Path> pathMapper = new DatePathMapper( "/usr/local/networkrail/darwin", "pport", true );
        logger = Consumers.guard( LOG, FileRecorder.recordTo( pathMapper ) );

        rabbit.queueDurableConsumer( "archiver.nre.push", "nre.push", RabbitMQ.toString, this );
    }

    @Override
    public void accept( String t )
    {
        if( t != null ) {
            logger.accept( t );
        }
    }

}
