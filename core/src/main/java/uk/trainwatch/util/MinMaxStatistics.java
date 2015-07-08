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
package uk.trainwatch.util;

import java.io.Serializable;
import java.util.function.IntConsumer;

/**
 *
 * @author peter
 */
public class MinMaxStatistics
        implements IntConsumer,
                   Serializable
{

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    /**
     * Records a new value into the summary information
     *
     * @param value the input value
     */
    @Override
    public void accept( int value )
    {
        min = Math.min( min, value );
        max = Math.max( max, value );
    }

    /**
     * Combines the state of another {@code IntSummaryStatistics} into this one.
     *
     * @param other another {@code IntSummaryStatistics}
     * <p>
     * @throws NullPointerException if {@code other} is null
     */
    public void combine( MinMaxStatistics other )
    {
        min = Math.min( min, other.min );
        max = Math.max( max, other.max );
    }

    /**
     * Returns the minimum value recorded, or {@code Integer.MAX_VALUE} if no
     * values have been recorded.
     *
     * @return the minimum value, or {@code Integer.MAX_VALUE} if none
     */
    public final int getMin()
    {
        return min;
    }

    /**
     * Returns the maximum value recorded, or {@code Integer.MIN_VALUE} if no
     * values have been recorded.
     *
     * @return the maximum value, or {@code Integer.MIN_VALUE} if none
     */
    public final int getMax()
    {
        return max;
    }

    /**
     * {@inheritDoc}
     *
     * Returns a non-empty string representation of this object suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     */
    @Override
    public String toString()
    {
        return String.format(
                "%s[min=%d, max=%d}",
                this.getClass().getSimpleName(),
                getMin(),
                getMax() );
    }
}
