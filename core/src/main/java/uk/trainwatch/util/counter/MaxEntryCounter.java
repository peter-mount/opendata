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
package uk.trainwatch.util.counter;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Utility to maintain a counter in a Map.
 * <p>
 * Every time map.compute(key, maxEntryCounter) is invoked the value in said map is incremented by 1.
 * <p>
 * Once it hits the max value then the consumer is fired and the counter is reset back to 1.
 * <p>
 * @author Peter T Mount
 */
public class MaxEntryCounter
        implements BiFunction<Integer, Integer, Integer>
{

    private final int max;
    private final Consumer<Integer> consumer;

    /**
     * 
     * @param max counter value to trigger the consumer
     * @param consumer Consumer to invoke when the counter hits max
     */
    public MaxEntryCounter( int max, Consumer<Integer> consumer )
    {
        this.max = max;
        this.consumer = consumer;
    }

    @Override
    public Integer apply( Integer key, Integer count )
    {
        if( count == null )
        {
            return 1;
        }
        else if( count < max )
        {
            return count + 1;
        }
        else
        {
            consumer.accept( key );
            return 1;
        }
    }
}
