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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
public final class BoundedDeque implements Consumer<Integer>
{
    private final int maxSize;
    private final Deque<Integer> deque;
    private LocalDateTime lastTime;
    private int lowAlarm = 0;
    private int highAlarm = Integer.MAX_VALUE;
    private int lastValue;
    private int total;
    private final Function<BoundedDeque, Integer> aggregator;

    public BoundedDeque( int maxSize )
    {
        this( maxSize,
              BoundedDeque::getTotal );
    }

    public BoundedDeque( int maxSize, Function<BoundedDeque, Integer> aggregator )
    {
        this.maxSize = maxSize;
        this.aggregator = aggregator;
        this.deque = new ConcurrentLinkedDeque<>();
        // Ensures we have some data
        accept( 0 );
    } // Ensures we have some data

    @Override
    public synchronized void accept( Integer t )
    {
        lastTime = TimeUtils.getLocalDateTime();
        lastValue = t;
        total += t;
        deque.offerLast( t );
        while( deque.size() > maxSize ) {
            deque.pollFirst();
        }
    }

    public JsonArrayBuilder toArray()
    {
        JsonArrayBuilder a = Json.createArrayBuilder();
        deque.forEach( a::add );
        return a;
    }

    public synchronized LocalDateTime getLastTime()
    {
        return lastTime;
    }

    public synchronized int getTotal()
    {
        return total;
    }

    public synchronized void reset()
    {
        total = 0;
    }

    public synchronized void reset( Consumer<Integer> c )
    {
        c.accept( aggregator.apply( this ) );
        total = 0;
    }

    public synchronized int getHighAlarm()
    {
        return highAlarm;
    }

    public synchronized void setHighAlarm( int highAlarm )
    {
        this.highAlarm = highAlarm;
    }

    public synchronized int getLowAlarm()
    {
        return lowAlarm;
    }

    public synchronized void setLowAlarm( int lowAlarm )
    {
        this.lowAlarm = lowAlarm;
    }

    public synchronized boolean isLow()
    {
        return lastValue < lowAlarm;
    }

    public synchronized boolean isHigh()
    {
        return lastValue > highAlarm;
    }

    public synchronized int getLastValue()
    {
        return lastValue;
    }

    public int get( BinaryOperator<Integer> f )
    {
        return deque.stream().reduce( f ).orElse( 0 );
    }

    public synchronized JsonObjectBuilder toJsonObjectBuilder()
    {
        return Json.createObjectBuilder().add( "time", lastTime.toString() ).add( "millis", lastTime.toInstant( ZoneOffset.UTC ).toEpochMilli() ).
                add( "low", isLow() ).add( "high", isHigh() ).add( "lowValue", getLowAlarm() ).add( "highvalue", getHighAlarm() ).add( "values", toArray() ).
                add( "current", getLastValue() ).
                add( "min",
                     get( Math::min ) ).
                add( "max",
                     get( Math::max ) );
    }
    
}
