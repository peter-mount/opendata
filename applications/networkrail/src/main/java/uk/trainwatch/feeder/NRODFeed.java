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
package uk.trainwatch.feeder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.apachemq.JMS;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class NRODFeed
        implements ServletContextListener
{

    @Inject
    private Rabbit rabbit;

    private RemoteActiveMQConnection activemq;

    private final Map<String, Consumer<? super JsonStructure>> tdPublishers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<? super JsonStructure>> trustPublishers = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        activemq = RemoteActiveMQConnection.getJNDIConnection( "nrod" );

        activemq.registerTopicConsumer( "RTPPM_ALL", JMS.toText, rabbit.publishString( "nr.rtppm" ) );

        Consumer<String> rawPublisher = rabbit.publishString( "nr.trust.mvtall" );
        Consumer<? super JsonStructure> mvtPublisher = rabbit.publishJson( "nr.trust.mvt" );
        activemq.registerTopicConsumer( "TRAIN_MVT_ALL_TOC",
                                        JMS.toText,
                                        msg -> Stream.of( msg ).
                                        // peek will submit the raw text to rabbit
                                        peek( rawPublisher ).
                                        // Parse and then unbatch
                                        map( JsonUtils.parseJsonArray ).
                                        flatMap( JsonUtils::stream ).
                                        map( JsonObject.class::cast ).
                                        // Pass to the main route
                                        forEach( o -> {
                                            mvtPublisher.accept( o );
                                            trustDispatch( o );
                                        } )
        );

        Consumer<String> rawTdPublisher = rabbit.publishString( "nr.td.all" );
        activemq.registerTopicConsumer( "TD_ALL_SIG_AREA",
                                        JMS.toText,
                                        msg -> Stream.of( msg ).
                                        // peek will submit the raw text to rabbit
                                        peek( rawTdPublisher ).
                                        // Parse and then unbatch
                                        map( JsonUtils.parseJsonArray ).
                                        // Stream of array elements
                                        flatMap( JsonUtils::<JsonObject>stream ).
                                        // Stream of object property values
                                        flatMap( JsonUtils::<JsonObject>stream ).
                                        map( JsonUtils.getObject ).
                                        filter( Objects::nonNull ).
                                        // Route to the individual signalling area
                                        forEach( td -> tdPublishers.computeIfAbsent( td.getString( "area_id" ),
                                                                                     area -> rabbit.publishJson( "nr.td.area." + area )
                                                ).accept( td )
                                        )
        );

        activemq.start();
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        if( activemq != null ) {
            activemq.stop();
        }
    }

    synchronized void trustDispatch( JsonStructure s )
    {
        if( s instanceof JsonObject ) {
            JsonObject t = (JsonObject) s;
            JsonObject header = t.getJsonObject( "header" );
            if( header == null ) {
                return;
            }

            String msgType = JsonUtils.getString( header, "msg_type" );
            if( msgType == null || msgType.isEmpty() ) {
                return;
            }

            trustPublishers.computeIfAbsent( msgType, k -> rabbit.publishJson( "analyser.trust.mvt." + k ) ).
                    accept( t );
        }
    }

}
