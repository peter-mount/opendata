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
package uk.trainwatch.nrod.feed.td;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.apachemq.JMS;
import uk.trainwatch.nrod.feed.NetworkRailConnection;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class TdFeed
        implements Consumer<String>
{

    private static final Logger LOG = Logger.getLogger( TdFeed.class.getName() );

    private final Map<String, Consumer<? super JsonStructure>> tdPublishers = new ConcurrentHashMap<>();

    @Inject
    private NetworkRailConnection networkRailFeed;

    @Inject
    private Rabbit rabbit;

    @Inject
    private TdArchiver tdArchiver;

    private Consumer<String> publisher;
    private Consumer<String> monitor;

    @PostConstruct
    public void start()
    {
        publisher = rabbit.publishString( ALL_ROUTING_KEY );

        monitor = RateMonitor.log( LOG, "rtppm" );

        networkRailFeed.registerTopicConsumer( "TD_ALL_SIG_AREA", JMS.toText, this );
    }
    private static final String ALL_ROUTING_KEY = "nr.td.all";

    @Override
    public void accept( String t )
    {
        if( t == null ) {
            return;
        }

        tdArchiver.accept( t );

        monitor.accept( t );

        // Publish entire message
        publisher.accept( t );

        // Publish to each td area
        JsonUtils.stream( JsonUtils.parseJsonArray.apply( t ) )
                .map( JsonObject.class::cast )
                // Stream of object property values
                .flatMap( JsonUtils::<JsonObject>stream )
                .map( JsonUtils.getObject )
                .filter( Objects::nonNull )
                // Route to the individual signalling area
                .forEach( td -> toArea( td ).accept( td ) );
    }

    private Consumer<? super JsonStructure> toArea( JsonObject td )
    {
        return tdPublishers.computeIfAbsent( td.getString( "area_id" ), this::newPublisher );
    }

    private Consumer<? super JsonStructure> newPublisher( String area )
    {
        String key = "nr.td.area." + area;
        Consumer<? super JsonStructure> pub = rabbit.publishJson( key );
        Consumer<? super JsonStructure> mon = RateMonitor.log( LOG, key );
        return o -> {
            pub.accept( o );
            mon.accept( o );
        };
    }
}
