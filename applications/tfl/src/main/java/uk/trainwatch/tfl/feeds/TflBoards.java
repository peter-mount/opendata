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
package uk.trainwatch.tfl.feeds;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.rabbitmq.Publisher;
import uk.trainwatch.scheduler.Cron;
import uk.trainwatch.util.counter.RateMonitor;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class TflBoards
        extends TflFeed
{

    /**
     * TfL Tube, DLR & Air lines
     */
    private static final String LINES[] = {
        "bakerloo",
        "central",
        "circle",
        "district",
        "dlr",
        "emirates-air-line",
        "hammersmith-city",
        "jubilee",
        "metropolitan",
        "northern",
        "piccadilly",
        "victoria",
        "waterloo-city"
    };

    @Inject
    @Publisher("tfl.boards")
    private Consumer<String> publisher;

    private final Consumer<String> monitor = RateMonitor.consumer( "tfl.boards" );

    @Override
    protected String getEndPoint()
    {
        return Stream.of( LINES ).collect( Collectors.joining( ",", "/line/", "/Arrivals" ) );
    }

    @Override
    protected Supplier<String> getRetriever( Supplier<String> s )
    {
        return RateMonitor.supplier( "tfl.boards.fetch", s );
    }

    @Override
    protected Consumer<String> getPublisher()
    {
        return RateMonitor.consumer( "tfl.boards", publisher );
    }

    @Cron("5/30 * * * * ?")
    @Override
    public void retrieve()
    {
        super.retrieve();
    }

}
