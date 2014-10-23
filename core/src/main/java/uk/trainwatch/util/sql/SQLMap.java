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
import java.util.Map;

/**
 * A SQL aware map
 * <p>
 * @param <K>
 * @param <V> <p>
 * @author Peter T Mount
 */
public interface SQLMap<K, V>
        extends Map<K, V>
{

    default void forEachSQL( SQLBiConsumer<? super K, ? super V> action )
            throws SQLException
    {
        try
        {
            forEach( SQLBiConsumer.guard( action ) );
        }
        catch( UncheckedSQLException ex )
        {
            throw ex.getCause();
        }
    }

    default void forEachSQL( SQLConsumer<? super V> action )
            throws SQLException
    {
        forEachSQL( SQLConsumer.composeBiConsumer( action ) );
    }

    /**
     * Variant of {@link #computeIfAbsent(java.lang.Object, java.util.function.Function)} which allows for a SQL
     * statement to be executed but allows for the passing of any {@link SQLException}
     * <p>
     * @param key
     * @param mappingFunction <p>
     * @return <p>
     * @throws SQLException
     */
    default V computeSQLIfAbsent( K key, SQLSupplier<? extends V> mappingFunction )
            throws SQLException
    {
        try
        {
            return computeIfAbsent( key, k -> SQLSupplier.guard( mappingFunction ).
                                    get() );
        }
        catch( UncheckedSQLException ex )
        {
            throw ex.getCause();
        }
    }

    default V computeSQLIfPresent( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return computeIfPresent( key, SQLBiFunction.guard( remappingFunction ) );
        }
        catch( UncheckedSQLException ex )
        {
            throw ex.getCause();
        }
    }

    default V computeSQL( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return compute( key, SQLBiFunction.guard( remappingFunction ) );
        }
        catch( UncheckedSQLException ex )
        {
            throw ex.getCause();
        }
    }

    default V mergeSQL( K key, V value, SQLBiFunction<? super V, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return merge( key, value, SQLBiFunction.guard( remappingFunction ) );
        }
        catch( UncheckedSQLException ex )
        {
            throw ex.getCause();
        }
    }

}
