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
package uk.trainwatch.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * A Consumer which calculates the start and end of a range of {@link LocalDateTime}.
 * <p>
 * @author peter
 */
public class LocalDateTimeRange
        implements Consumer<LocalDateTime>
{

    private LocalDateTime min;
    private LocalDateTime max;

    @Override
    public void accept( LocalDateTime t )
    {
        if( min == null )
        {
            min = max = t;
        }
        else if( t.isBefore( min ) )
        {
            min = t;
        }
        else if( t.isAfter( max ) )
        {
            max = t;
        }
    }

    /**
     * Do we have a value present
     * <p>
     * @return
     */
    public boolean isPresent()
    {
        return min != null;
    }

    /**
     * If we have a value present then run the supplied Consumer
     * <p>
     * @param c
     */
    public void ifPresent( Consumer<LocalDateTimeRange> c )
    {
        if( isPresent() )
        {
            c.accept( this );
        }
    }

    /**
     * Rest this consumer as if it was newly constructed
     */
    public void reset()
    {
        min = max = null;
    }

    /**
     * The maximum {@link LocalDateTime}
     * <p>
     * @return max time or null if not present
     */
    public LocalDateTime getMax()
    {
        return max;
    }

    /**
     * The minimum {@link LocalDateTime}
     * <p>
     * @return min time or null if not present
     */
    public LocalDateTime getMin()
    {
        return min;
    }

    /**
     * The duration
     * <p>
     * @return
     * <p>
     * @throws NullPointerException is no value is present
     */
    public Duration getDuration()
    {
        return Duration.between( min, max );
    }
}
