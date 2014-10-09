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

/**
 * Archives the raw RTPPM feed to disk
 * <p>
 * @author Peter T Mount
 */
public class RTPPMArchiver
{

    protected static final Logger LOG = Logger.getLogger( RTPPMArchiver.class.getName() );

    public static void setup( RabbitConnection rabbitmq )
    {
        // Record raw data to disk - mvtall
        Consumer<String> rtppmMonitor = RateMonitor.log( LOG, "record nr.rtppm" );

        Function<String, Path> rtppmMapper = new DatePathMapper( "/usr/local/networkrail", "rtppm/raw" );

        RabbitMQ.queueDurableStream( rabbitmq, "archiver.nrod.rtppm", "nr.rtppm",
                                     s -> s.map( RabbitMQ.toString ).
                                     peek( rtppmMonitor ).
                                     forEach( Consumers.guard( LOG, FileRecorder.recordTo( rtppmMapper ) ) )
        );
    }
}
