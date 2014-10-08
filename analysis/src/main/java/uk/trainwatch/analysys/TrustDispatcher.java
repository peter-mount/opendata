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
package uk.trainwatch.analysys;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 * This handles inbound MVT feed and republishes them by message type. This allows other consumers to receive just the
 * ones they require by just listening for the specific routing keys.
 * <p>
 * For example, to receive just train movements (type 0003) then use the routing key {@code "analyser.trust.mvt.0003"}.
 * <p>
 * @author Peter T Mount
 */
public class TrustDispatcher
        implements Consumer<JsonObject>
{

    private final RabbitConnection rabbitmq;
    private final Map<String, Consumer<? super JsonStructure>> consumers = new ConcurrentHashMap<>();

    private TrustDispatcher( RabbitConnection rabbitmq )
    {
        this.rabbitmq = rabbitmq;
    }

    public static void setup( RabbitConnection rabbitmq )
    {
        Consumer<JsonObject> dispatcher = Consumers.guard(
                new TrustDispatcher( rabbitmq ).
                andThen( RateMonitor.log( TrustDispatcher.class, "Trust mvt dispatch" ) )
        );

        RabbitMQ.queueDurableStream( rabbitmq, "analyser.trust.dispatch", "nr.trust.mvt",
                                     s -> s.map( RabbitMQ.toString.andThen( JsonUtils.parseJsonObject ) ).
                                     filter( Objects::nonNull ).
                                     forEach( dispatcher ) );
    }

    @Override
    public synchronized void accept( JsonObject t )
    {
        JsonObject header = t.getJsonObject( "header" );
        if( header == null )
        {
            return;
        }

        String msgType = JsonUtils.getString( header, "msg_type" );
        if( msgType == null || msgType.isEmpty() )
        {
            return;
        }

        consumers.computeIfAbsent( msgType, k -> RabbitMQ.jsonConsumer( rabbitmq, "analyser.trust.mvt." + k ) ).
                accept( t );
    }

}
