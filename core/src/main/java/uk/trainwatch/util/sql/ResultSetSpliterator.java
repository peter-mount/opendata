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
package uk.trainwatch.util.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
class ResultSetSpliterator<T>
        implements Spliterator<T>
{

    private final ResultSet resultSet;
    private final Function<ResultSet, T> factory;

    /**
     *
     * @param resultSet {@link ResultSet} to iterate over
     * @param factory   {@link Function} that will take a ResultSet row and produce some object
     */
    public ResultSetSpliterator( ResultSet resultSet, Function<ResultSet, T> factory )
    {
        this.resultSet = resultSet;
        this.factory = factory;
    }

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

    @Override
    public boolean tryAdvance( Consumer<? super T> action )
    {
        try
        {
            if( resultSet.next() )
            {
                action.accept( factory.apply( resultSet ) );
                return true;
            }
            return false;
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }
}
