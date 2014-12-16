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

import java.util.Spliterator;
import static java.util.Spliterator.IMMUTABLE;

/**
 * Common base for a {@link Spliterator} of unknown size.
 * <p>
 * @author peter
 * @param <T> Type
 */
public abstract class AbstractSpliterator<T>
        implements Spliterator<T>
{

    /**
     * This cannot be split
     * <p>
     * @return
     */
    @Override
    public Spliterator<T> trySplit()
    {
        return null;
    }

    /**
     * We don't know the size
     * <p>
     * @return
     */
    @Override
    public long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    /**
     * We are immutable as the ResultSet is (sort of)
     * <p>
     * @return
     */
    @Override
    public int characteristics()
    {
        return IMMUTABLE;
    }

}
