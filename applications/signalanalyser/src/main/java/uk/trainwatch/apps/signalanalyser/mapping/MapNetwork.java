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
package uk.trainwatch.apps.signalanalyser.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.apps.signalanalyser.AbstractApp;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 * Simple application to receive the entire TD feed and build up a simple map of berths used and the relationships between them when movements happen.
 * <p>
 * This mapping data can then be used to help build up a signalling diagram.
 * <p>
 * @author peter
 */
public class MapNetwork
        extends AbstractApp
{

    @Override
    protected void setupApplication()
            throws IOException
    {
        // TD feed
        Consumer<String> rawMonitor = RateMonitor.log( LOG, "receive nr.td.raw" );
        Consumer<? super JsonStructure> tdMonitor = RateMonitor.log( LOG, "receive nr.td.m" );

        Consumer<? super JsonObject> cMonitor = new NetworkMapper().
                andThen( new NetworkTracker() ).
                andThen( RateMonitor.log( LOG, "receive nr.td.c" ) );

        Consumer<? super JsonObject> sMonitor = RateMonitor.log( LOG, "receive nr.td.s" );

        Map<String, Consumer<? super JsonObject>> router = new HashMap<>();
        router.put( "CA", cMonitor );
        router.put( "CB", cMonitor );
        router.put( "CC", cMonitor );
        router.put( "CT", cMonitor );
        router.put( "SF", sMonitor );
        router.put( "SG", sMonitor );
        router.put( "SH", sMonitor );

        RabbitMQ.queueDurableStream( getRabbitmq(),
                                     "signal.map.all",
                                     "nr.td.area.#",
                                     Consumers.guard(
                                             s -> s.map( RabbitMQ.toString ).
                                             map( JsonUtils.parseJsonObject ).
                                             filter( Objects::nonNull ).
                                             peek( tdMonitor ).
                                             forEach( m -> router.get( m.getString( "msg_type" ) ).
                                                     accept( m ) )
                                     )
        );
    }

    public static void main( String... args )
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising Network Rail Signal Analyser" );

        new MapNetwork().run();
    }

}
