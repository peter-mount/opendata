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

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author Peter T Mount
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface SQLBiConsumer<T, U>
{

    /**
     * Performs this operation on the given arguments.
     * <p>
     * @param t the first input argument
     * @param u the second input argument
     * <p>
     * @throws java.sql.SQLException
     */
    void accept( T t, U u )
            throws SQLException;

    /**
     * Returns a composed {@code BiConsumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * <p>
     * @param after the operation to perform after this operation
     * <p>
     * @return a composed {@code BiConsumer} that performs in sequence this operation followed by the {@code after}
     *         operation
     * <p>
     * @throws NullPointerException if {@code after} is null
     */
    default SQLBiConsumer<T, U> andThen( SQLBiConsumer<? super T, ? super U> after )
    {
        Objects.requireNonNull( after );

        return ( l, r ) ->
        {
            accept( l, r );
            after.accept( l, r );
        };
    }

    /**
     * Guards a SQLBiConsumer so that it captures all SQLExceptions
     * <p>
     * @param <T>
     * @param <U>
     * @param c   SQLBiConsumer
     * <p>
     * @return BiConsumer
     */
    static <T, U> BiConsumer<T, U> guard( SQLBiConsumer<T, U> c )
    {
        return ( t, u ) ->
        {
            try
            {
                c.accept( t, u );
            }
            catch( SQLException ex )
            {
                throw new UncheckedSQLException( ex );
            }
        };
    }

    /**
     * Composes a new SQLBiConsumer from a BiConsumer. If an UncheckedSQLException is thrown then the original
     * SQLException is rethrown.
     * <p>
     * @param <T>
     * @param <U>
     * @param c
     *            <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    static <T, U> SQLBiConsumer<T, U> compose( BiConsumer<T, U> c )
            throws SQLException
    {
        return ( t, u ) ->
        {
            try
            {
                c.accept( t, u );
            }
            catch( UncheckedSQLException ex )
            {
                throw ex.getCause();
            }
        };
    }

    default BiConsumer<T, U> guard()
    {
        return guard( this );
    }
}
