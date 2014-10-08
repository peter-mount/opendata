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
package uk.trainwatch.analysis;

import java.util.Objects;
import java.util.function.Consumer;
import uk.trainwatch.analysis.performance.TocStanoxAllClassDelayAnalyzer;
import uk.trainwatch.analysis.performance.TocStanoxClassDelayAnalyzer;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementFactory;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author Peter T Mount
 */
public class TocPerformance
{

    public static void setup( RabbitConnection rabbitmq )
    {
        toc_stanox_all( rabbitmq );
    }

    /**
     * Handles the toc/stanox reports by monitoring the delay reported at each stanox
     * <p>
     * @param rabbitmq
     */
    private static void toc_stanox_all( RabbitConnection rabbitmq )
    {
        Consumer<TrainMovement> tocStanoxAllClasses = new TocStanoxAllClassDelayAnalyzer(
                RabbitMQ.jsonConsumer( rabbitmq, "report.perf.toc.stanox" ),
                RabbitMQ.jsonConsumer( rabbitmq, "rt.perf.toc.stanox.all" )
        );

        Consumer<TrainMovement> tocStanoxByClasses = new TocStanoxClassDelayAnalyzer(
                RabbitMQ.jsonConsumer( rabbitmq, "report.perf.toc.stanox.class" ),
                RabbitMQ.jsonConsumer( rabbitmq, "rt.perf.toc.stanox.class" )
        );

        Consumer<TrainMovement> statisticRecorder = RateMonitor.log( TocStanoxAllClassDelayAnalyzer.class,
                                                                     "performance toc stanox all classes" );

        // Now link it all up & start listening for movement messages type 0003
        Consumer<TrainMovement> consumer = Consumers.andThenGuarded(
                tocStanoxAllClasses,
                tocStanoxByClasses,
                statisticRecorder
        );

        RabbitMQ.queueDurableStream( rabbitmq, "analyser.performance.toc.stanox", "analyser.trust.mvt.0003",
                                     s -> s.map( RabbitMQ.toString.andThen( JsonUtils.parseJsonObject ) ).
                                     map( TrustMovementFactory.INSTANCE ).
                                     map( Functions.castTo( TrainMovement.class ) ).
                                     filter( Objects::nonNull ).
                                     forEach( consumer )
        );
    }
}
