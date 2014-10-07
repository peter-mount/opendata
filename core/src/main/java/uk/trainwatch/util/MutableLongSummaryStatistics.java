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
package uk.trainwatch.util;

import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import uk.trainwatch.io.format.PsvReader;
import uk.trainwatch.io.format.PsvWritable;
import uk.trainwatch.io.format.PsvWriter;

/**
 * A version of {@link java.util.LongSummaryStatistics} which can be initialized from a remote source, specifically we
 * can use {@link PsvReader} to read and {@link PsvWriter} to write to storage.
 * <p>
 * @author Peter T Mount
 */
public class MutableLongSummaryStatistics
        implements LongConsumer,
                   IntConsumer,
                   PsvWritable
{

    private final long key;
    private final int key2;
    private long count;
    private long sum;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    /**
     * Construct an empty instance with zero count, zero sum, {@code Long.MAX_VALUE} min, {@code Long.MIN_VALUE} max and
     * zero average.
     * <p>
     * @param key Primary key for this instance
     */
    public MutableLongSummaryStatistics( long key )
    {
        this( key, Integer.MIN_VALUE );
    }

    public MutableLongSummaryStatistics( long key, int key2 )
    {
        this.key = key;
        this.key2 = key2;
    }

    public long getKey()
    {
        return key;
    }

    public int getKey2()
    {
        return key2;
    }

    /**
     * Create an instance based on values previously written by the {@link PsvWritable} interface.
     * <p>
     * You can pass this to the {@link PsvReader#load(java.io.Reader, java.util.function.Function) } methods as the
     * function by using {@code MutableLongSummaryStatistics::fromString}
     * <p>
     * @param r
     * <p>
     * @return
     */
    public static MutableLongSummaryStatistics fromString( String[] r )
    {
        MutableLongSummaryStatistics s = new MutableLongSummaryStatistics( Long.valueOf( r[0] ) );
        s.count = Long.valueOf( r[1] );
        s.min = Long.valueOf( r[2] );
        s.max = Long.valueOf( r[3] );
        // Ignore average
        s.sum = Long.valueOf( r[5] );
        return s;
    }

    //<editor-fold defaultstate="collapsed" desc="PsvWritable interface">
    @Override
    public void addPsvHeaders( PsvWriter w )
    {
        w.setHeaders( "key", "count", "min", "max", "average", "sum" );
    }

    @Override
    public void toPsvWriter( PsvWriter w )
    {
        w.add( getKey(), getCount(), getMin(), getMax(), getAverage(), getSum() );
    }
    //</editor-fold>

    /**
     * Records a new {@code int} value into the summary information.
     * <p>
     * @param value the input value
     */
    @Override
    public void accept( int value )
    {
        accept( (long) value );
    }

    /**
     * Records a new {@code long} value into the summary information.
     * <p>
     * @param value the input value
     */
    @Override
    public void accept( long value )
    {
        ++count;
        sum += value;
        min = Math.min( min, value );
        max = Math.max( max, value );
    }

    /**
     * Returns the count of values recorded.
     * <p>
     * @return the count of values
     */
    public final long getCount()
    {
        return count;
    }

    /**
     * Returns the sum of values recorded, or zero if no values have been recorded.
     * <p>
     * @return the sum of values, or zero if none
     */
    public final long getSum()
    {
        return sum;
    }

    /**
     * Returns the minimum value recorded, or {@code Long.MAX_VALUE} if no values have been recorded.
     * <p>
     * @return the minimum value, or {@code Long.MAX_VALUE} if none
     */
    public final long getMin()
    {
        return min;
    }

    /**
     * Returns the maximum value recorded, or {@code Long.MIN_VALUE} if no values have been recorded
     * <p>
     * @return the maximum value, or {@code Long.MIN_VALUE} if none
     */
    public final long getMax()
    {
        return max;
    }

    /**
     * Returns the arithmetic mean of values recorded, or zero if no values have been recorded.
     * <p>
     * @return The arithmetic mean of values, or zero if none
     */
    public final double getAverage()
    {
        return getCount() > 0 ? (double) getSum() / getCount() : 0.0d;
    }
}
