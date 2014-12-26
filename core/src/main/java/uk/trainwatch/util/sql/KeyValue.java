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
package uk.trainwatch.util.sql;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A helper class for handling Key,Value pairs from a database.
 * <p>
 * @author peter
 */
public class KeyValue<K, V>
{

    /**
     * Mapping function that returns a {@link KeyValue} keyed by an integer and a String value
     */
    public static final SQLResultSetHandler<KeyValue<Integer, String>> INTEGER_STRING = rs -> new KeyValue<>( rs.getInt( 1 ), rs.getString( 2 ) );

    /**
     * Mapping function that returns a {@link KeyValue} keyed by a long and a String value
     */
    public static final SQLResultSetHandler<KeyValue<Long, String>> LONG_STRING = rs -> new KeyValue<>( rs.getLong( 1 ), rs.getString( 2 ) );

    private final K key;
    private final int hashCode;
    private final V value;

    public KeyValue( Map.Entry<K, V> e )
    {
        this( e.getKey(), e.getValue() );
    }

    public KeyValue( K key, V value )
    {
        this.key = key;
        this.value = value;
        hashCode = 13 * 7 + Objects.hashCode( this.key );
    }

    public K getKey()
    {
        return key;
    }

    public V getValue()
    {
        return value;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final KeyValue<?, ?> other = (KeyValue<?, ?>) obj;
        return Objects.equals( this.key, other.key );
    }

    /**
     * Returns a collector that will produce a {@link Map} from a Stream of KeyValue's.
     * <p>
     * @param <K> Type of Key
     * @param <U> Type of Value
     * <p>
     * @return Collector
     */
    public static <K, U> Collector<KeyValue<K, U>, ?, Map<K, U>> toMap()
    {
        return Collectors.toMap( KeyValue::getKey, KeyValue::getValue );
    }

    /**
     * Returns a collector that will produce a {@link ConcurrentMap} from a Stream of KeyValue's.
     * <p>
     * @param <K> Type of Key
     * @param <U> Type of Value
     * <p>
     * @return Collector
     */
    public static <K, U> Collector<KeyValue<K, U>, ?, ConcurrentMap<K, U>> toConcurrentMap()
    {
        return Collectors.toConcurrentMap( KeyValue::getKey, KeyValue::getValue );
    }
}
