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
package uk.trainwatch.apps.signalanalyser.monitor;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import uk.trainwatch.apps.signalanalyser.AbstractApp;
import uk.trainwatch.nrod.td.berth.BerthAreaMap;
import uk.trainwatch.nrod.td.model.BerthCancel;
import uk.trainwatch.nrod.td.model.BerthInterpose;
import uk.trainwatch.nrod.td.model.BerthStep;
import uk.trainwatch.nrod.td.model.Heartbeat;
import uk.trainwatch.nrod.td.model.TDMessage;
import uk.trainwatch.nrod.td.model.TDMessageFactory;
import uk.trainwatch.nrod.td.model.TDVisitor;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author peter
 */
public class Monitor
        extends AbstractApp
{

    private final String signalArea;
    private final String queueName;
    private final String routingKey;

    public Monitor( String signalArea )
    {
        this.signalArea = signalArea;
        routingKey = "nr.td.area." + signalArea;
        queueName = "signal.map." + signalArea;
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        Consumer<TDMessage> rawMonitor = RateMonitor.log( LOG, "Receive " + routingKey );

        Consumer<TDMessage> tdViewer = new TDVisitor()
        {

            @Override
            public void visit( BerthCancel s )
            {
                LOG.log( Level.INFO, () -> "Canc " + s.getDescr() + " " + s.getFrom() );
            }

            @Override
            public void visit( BerthInterpose s )
            {
                LOG.log( Level.INFO, () -> "Put  " + s.getDescr() + " " + s.getTo() );
            }

            @Override
            public void visit( BerthStep s )
            {
                LOG.log( Level.INFO, () -> "Move " + s.getDescr() + " " + s.getFrom() + " -> " + s.getTo() );
            }

            @Override
            public void visit( Heartbeat s )
            {
                LOG.log( Level.INFO, () -> "Heart " + s.getReport_time() );
            }

        };

        RabbitMQ.queueStream( getRabbitmq(),
                              queueName,
                              routingKey,
                              s -> s.map( RabbitMQ.toJsonObject.andThen( TDMessageFactory.INSTANCE ) ).
                              filter( Objects::nonNull ).
                              forEach( rawMonitor.andThen( tdViewer ) )
        );
    }

    public static void main( String... args )
            throws Exception
    {
        if( args.length != 1 ) {
            LOG.log( Level.SEVERE, "A signalling area is required" );
            System.exit( 1 );
        }

        String area = args[0].toUpperCase();
        if( area.isEmpty() || area.length() != 2 ) {
            LOG.log( Level.SEVERE, "Signalling area code is 2 characters only." );
            System.exit( 1 );
        }

        LOG.log( Level.INFO, () -> "Initialising Network Rail Signal Analyser for area " + area );

        new Monitor( area ).run();
    }

}
