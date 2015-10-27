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
package uk.trainwatch.nrod.feed.trust;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.util.JsonUtils;
import javax.json.JsonStructure;

/**
 * This handles inbound MVT feed and republishes them by message type. This allows other consumers to receive just the
 * ones they require by just listening for the specific routing keys.
 * <p>
 * For example, to receive just train movements (type 0003) then use the routing key {@code "analyser.trust.mvt.0003"}.
 * <p>
 * @author Peter T Mount
 */
@ApplicationScoped
public class TrustDispatcher
        implements Consumer<JsonObject>
{

    @Inject
    private Rabbit rabbit;

    private final Map<String, Consumer<? super JsonStructure>> consumers = new ConcurrentHashMap<>();

    @Override
    public void accept( JsonObject t )
    {
        if( t == null ) {
            return;
        }

        JsonObject header = t.getJsonObject( "header" );
        if( header == null ) {
            return;
        }

        String msgType = JsonUtils.getString( header, "msg_type" );
        if( msgType == null || msgType.isEmpty() ) {
            return;
        }

        consumers.computeIfAbsent( msgType, k -> rabbit.publishJson( "analyser.trust.mvt." + k ) ).
                accept( t );
    }

}
