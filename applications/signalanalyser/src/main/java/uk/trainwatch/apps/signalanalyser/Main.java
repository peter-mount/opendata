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
package uk.trainwatch.apps.signalanalyser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JMS;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.app.Application;
import static uk.trainwatch.util.app.BaseApplication.loadProperties;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author peter
 */
public class Main
        extends Application
{

    private RemoteActiveMQConnection activemq;

    @Override
    protected void setupBrokers()
            throws IOException
    {
        Properties p = loadProperties( "networkrail.properties" );
        activemq = new RemoteActiveMQConnection(
                p.getProperty( "server" ),
                Integer.parseInt( p.getProperty( "port", "61619" ) ),
                p.getProperty( "clientid", p.getProperty( "username" ) ),
                p.getProperty( "username" ),
                p.getProperty( "password" )
        );
    }

    @Override
    protected void start()
    {
        super.start();
        activemq.start();
    }

    @Override
    protected void stop()
    {
        super.stop();
        activemq.stop();
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        // TD feed
        Consumer<String> rawMonitor = RateMonitor.log( LOG, "receive nr.td.raw" );
        Consumer<? super JsonStructure> tdMonitor = RateMonitor.log( LOG, "receive nr.td.m" );

        Consumer<? super JsonObject> cMonitor = new NetworkMapper().andThen( RateMonitor.log( LOG, "receive nr.td.c" ) );

        Consumer<? super JsonObject> sMonitor = RateMonitor.log( LOG, "receive nr.td.s" );

        Map<String, Consumer<? super JsonObject>> router = new HashMap<>();
        router.put( "CA", cMonitor );
        router.put( "CB", cMonitor );
        router.put( "CC", cMonitor );
        router.put( "CT", cMonitor );
        router.put( "SF", sMonitor );
        router.put( "SG", sMonitor );
        router.put( "SH", sMonitor );

        activemq.registerTopicConsumer( "TD_KENT_MCC_SIG_AREA", Consumers.guard(
                                        msg -> Stream.of( msg ).
                                        map( JMS.toText ).
                                        filter( Objects::nonNull ).
                                        peek( rawMonitor ).
                                        //peek(rawPublisher).
                                        map( JsonUtils.parseJsonArray ).
                                        flatMap( JsonUtils::<JsonObject>stream ).
                                        flatMap( o -> o.values().
                                                stream() ).
                                        map( JsonUtils.getObject ).
                                        filter( Objects::nonNull ).
                                        peek( tdMonitor ).
                                        //
                                        forEach( m -> {
                                            Consumer<? super JsonObject> c = router.get( m.getString( "msg_type" ) );
                                            if( c != null ) {
                                                c.accept( m );
                                            }
                                        } )
                                ) );
    }

    public static void main( String... args )
            throws IOException,
                   InterruptedException
    {
        LOG.log( Level.INFO, "Initialising Network Rail Signal Analyser" );

        new Main().run();
    }

}
