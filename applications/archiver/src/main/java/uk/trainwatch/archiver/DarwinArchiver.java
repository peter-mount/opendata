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
package uk.trainwatch.archiver;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.counter.RateMonitor;

public class DarwinArchiver
{

    protected static final Logger LOG = Logger.getLogger(DarwinArchiver.class.getName() );

    public static void setup( RabbitConnection rabbitmq )
    {
        Consumer<String> monitor = RateMonitor.log( LOG, "record nre.push" );
        Function<String, Path> pathMapper = new DatePathMapper( "/usr/local/networkrail/darwin", "pport", true );
        RabbitMQ.queueDurableStream( rabbitmq, "archiver.nre.push", "nre.push",
                s -> s.map( RabbitMQ.toString ).
                peek( monitor ).
                forEach( Consumers.guard( LOG, FileRecorder.recordTo( pathMapper ) ) )
        );
    }

}
