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
package uk.trainwatch.util.counter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Consumer;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 *
 * @author peter
 */
public class MinMaxMonitor
        implements Consumer<Long>
{

    private final LongAccumulator min = new LongAccumulator( Long::min, Long.MAX_VALUE );
    private final LongAccumulator max = new LongAccumulator( Long::max, Long.MIN_VALUE );

    /**
     * A basic consumer
     */
    public MinMaxMonitor()
    {
    }

    /**
     * A consumer which will submit the values & then reset to two consumers
     * <p>
     * @param minConsumer
     * @param maxConsumer
     */
    public MinMaxMonitor( Consumer<Integer> minConsumer, Consumer<Integer> maxConsumer )
    {
        DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( () -> {
            minConsumer.accept( toInt( min.getThenReset() ) );
            maxConsumer.accept( toInt( max.getThenReset() ) );
        }, 0L, 1L, TimeUnit.MINUTES );
    }

    @Override
    public void accept( Long value )
    {
        if( value != null ) {
            min.accumulate( value );
            max.accumulate( value );
        }
    }

    private static int toInt( long v )
    {
        return v < Integer.MIN_VALUE ? Integer.MIN_VALUE : v > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) v;
    }

    public long getMin()
    {
        return min.get();
    }

    public long getThenResetMin()
    {
        return min.getThenReset();
    }

    public long getMax()
    {
        return max.get();
    }

    public long getThenResetMax()
    {
        return max.getThenReset();
    }

}
