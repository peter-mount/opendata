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
package uk.trainwatch.nrod.feed.trust;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
public class TrustFeed
        implements Consumer<String>
{

    private static final Logger LOG = Logger.getLogger( TrustFeed.class.getName() );

    @Inject
    private Rabbit rabbit;

    @Inject
    private NetworkRailConnection networkRailFeed;

    @Inject
    private TrustDispatcher trustDispatcher;

    @Inject
    private TrustLogger trustLogger;

    private Consumer<String> rawPublisher;
    private Consumer<? super JsonObject> mvtPublisher;

    private Consumer<String> monitor;

    @PostConstruct
    public void start()
    {
        rawPublisher = rabbit.publishString( "nr.trust.mvtall" );
        mvtPublisher = rabbit.publishJson( "nr.trust.mvt" );
        monitor = RateMonitor.log( LOG, "nr.trust" );

        networkRailFeed.registerTopicConsumer( "TRAIN_MVT_ALL_TOC", JMS.toText, this );
    }

    @Override
    public void accept( String t )
    {
        if( t != null ) {
            try {
                trustLogger.accept( t );
                rawPublisher.accept( t );

                JsonArray a = JsonUtils.parseJsonArray.apply( t );
                JsonUtils.forEachJsonObject( a, trustDispatcher );
                JsonUtils.forEachJsonObject( a, mvtPublisher );

                monitor.accept( t );
            }
            catch( SQLException ex ) {
                LOG.log( Level.SEVERE, null, ex );
            }
        }
    }

}
