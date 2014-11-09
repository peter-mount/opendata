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
import uk.trainwatch.nrod.trust.perf.TrainReporter;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementFactory;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author Peter T Mount
 */
public class TrainMovementLogger
{

    public static void setup( RabbitConnection rabbitmq )
    {
        Consumer<TrustMovement> reporter = new TrainReporter(
                RabbitMQ.jsonConsumer( rabbitmq, "report.movement" )
        );

        Consumer<TrustMovement> statisticRecorder = RateMonitor.log( TrainMovementLogger.class,
                                                                     "train movements recorded" );

        // Now link it all up & start listening for movement messages type 0003
        Consumer<TrustMovement> consumer = Consumers.andThenGuarded(
                reporter,
                statisticRecorder
        );

        RabbitMQ.queueDurableStream( rabbitmq, "analyser.trust.movement.logger", "nr.trust.mvt",
                                     s -> s.map( RabbitMQ.toString.andThen( JsonUtils.parseJsonObject ) ).
                                     map( TrustMovementFactory.INSTANCE ).
                                     filter( Objects::nonNull ).
                                     forEach( consumer )
        );
    }
}
