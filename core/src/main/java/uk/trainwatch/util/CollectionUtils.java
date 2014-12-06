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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Some additional utilities to compliment those in {@link Collections}
 * <p>
 * @author peter
 */
public class CollectionUtils
{

    /**
     * Returns a Set containing the specified values
     * <p>
     * @param <E>
     * @param values
     *               <p>
     * @return
     */
    public static <E> Set<E> asSet( E... values )
    {
        Objects.requireNonNull( values );
        return new HashSet<>( Arrays.asList( values ) );
    }

    /**
     * Returns an unmodifiable Set containing the specified values
     * <p>
     * @param <E>
     * @param values
     *               <p>
     * @return
     */
    public static <E> Set<E> asUnmodifiableSet( E... values )
    {
        return Collections.unmodifiableSet( asSet( values ) );
    }

    /**
     * Return an unmodifiable set of enum values
     * <p>
     * @param <E>
     * @param values
     * <p>
     * @return
     */
    public static <E extends Enum<E>> Set<E> asUnmodifiableEnumSet( E... values )
    {
        Objects.requireNonNull( values );
        return Collections.unmodifiableSet( EnumSet.<E>of( values[0], Arrays.copyOfRange( values, 1, values.length ) ) );
    }

    /**
     * Returns a {@link Predicate} that tests to see if a map does not contain a key
     * <p>
     * @param <T> Type of key
     * @param map Map to test against
     * <p>
     * @return Predicate
     */
    public static <T> Predicate<T> mapDoesNotContainKey( Map<? extends T, ?> map )
    {
        return k -> !map.containsKey( k );
    }

    /**
     * Replace the contents of one map with those of another.
     * <p>
     * This only modifies the original map and is useful if the original is in constant use.
     * <p>
     * It works by adding all entries in newMap into the original then removing key's from the original that are not in the new one.
     * <p>
     * Concurrency is down to the implementation of the map being updated
     * <p>
     * @param <K>    Type of key
     * @param <V>    Type of value
     * @param map    Map to update
     * @param newMap Map containing the new data
     */
    public static <K, V> void replace( Map<K, V> map, Map<? extends K, ? extends V> newMap )
    {
        map.putAll( newMap );
        map.keySet().
                removeIf( mapDoesNotContainKey( map ) );
    }

    /**
     * Returns a {@link BinaryOperator} that accepts two maps, merges the second one using the mergeFunction into the first. The result is the first map.
     * <p>
     * @param <K>           Key type
     * @param <V>           Value type
     * @param <M>           Map type
     * @param mergeFunction merge function
     * <p>
     * @return BinaryOperator
     */
    public static <K, V, M extends Map<K, V>> BinaryOperator<M> mergeMaps( BinaryOperator<V> mergeFunction )
    {
        return ( m1, m2 ) ->
        {
            m2.entrySet().
                    forEach( e -> m1.merge( e.getKey(), e.getValue(), mergeFunction ) );
            return m1;
        };
    }

    /**
     * Perform an action by calling a consumer with the value in a map only if it's present.
     * <p>
     * @param <K> Type of Key
     * @param <V> Type of value
     * @param m   Map
     * @param k   Key
     * @param c   Consumer
     */
    public static <K, V> void ifPresent( Map<? extends K, ? extends V> m, K k, Consumer<V> c )
    {
        V v = m.get( k );
        if( v != null )
        {
            c.accept( v );
        }
    }

    /**
     * Perform an action by calling a BiConsumer with the value in a map only if it's present.
     * <p>
     * @param <K> Type of Key
     * @param <V> Type of value
     * @param m   Map
     * @param k   Key
     * @param c   Consumer
     */
    public static <K, V> void ifPresent( Map<? extends K, ? extends V> m, K k, BiConsumer<K, V> c )
    {
        V v = m.get( k );
        if( v != null )
        {
            c.accept( k, v );
        }
    }

    /**
     * Perform an action by calling a Consumer with the key if that key is not present in a map.
     * <p>
     * @param <K> Type of Key
     * @param <V> Type of value
     * @param m   Map
     * @param k   Key
     * @param c   Consumer
     */
    public static <K, V> void ifAbsent( Map<? extends K, ? extends V> m, K k, Consumer<K> c )
    {
        if( !m.containsKey( k ) )
        {
            c.accept( k );
        }
    }

    /**
     * Perform an action by calling a BiConsumer with the key if that key is not present in a map.
     * <p>
     * @param <K> Type of Key
     * @param <V> Type of value
     * @param m   Map
     * @param k   Key
     * @param c   Consumer
     */
    public static <K, V> void ifAbsent( Map<? extends K, ? extends V> m, K k, BiConsumer<Map<? extends K, ? extends V>, K> c )
    {
        if( !m.containsKey( k ) )
        {
            c.accept( m, k );
        }
    }

    /**
     * Returns a consumer which will perform another with the value in a map if it's present
     * <p>
     * @param <T>     Type to consume
     * @param <K>     Map Key type
     * @param <V>     Map Value type
     * @param m       Map
     * @param kMapper Mapping function to convert T to K
     * @param c       Consumer
     * <p>
     * @return Consumer
     */
    public <T, K, V> Consumer<T> consumeIfPresent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, Consumer<V> c )
    {
        return t -> ifPresent( m, kMapper.apply( t ), c );
    }

    /**
     * Returns a consumer which will perform a BiConsumer with the key and value in a map if it's present
     * <p>
     * @param <T>     Type to consume
     * @param <K>     Map Key type
     * @param <V>     Map Value type
     * @param m       Map
     * @param kMapper Mapping function to convert T to K
     * @param c       BiConsumer
     * <p>
     * @return Consumer
     */
    public <T, K, V> Consumer<T> consumeIfPresent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, BiConsumer<K, V> c )
    {
        return t -> ifPresent( m, kMapper.apply( t ), c );
    }

    /**
     * Returns a consumer which will perform another with the map key if it's not present in a map
     * <p>
     * @param <T>     Type to consume
     * @param <K>     Map Key type
     * @param <V>     Map Value type
     * @param m       Map
     * @param kMapper Mapping function to convert T to K
     * @param c       Consumer
     * <p>
     * @return Consumer
     */
    public <T, K, V> Consumer<T> consumeIfAbsent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, Consumer<K> c )
    {
        return t -> ifAbsent( m, kMapper.apply( t ), c );
    }

    /**
     * Returns a consumer which will perform a BiConsumer with the map and map key if it's not present in a map
     * <p>
     * @param <T>     Type to consume
     * @param <K>     Map Key type
     * @param <V>     Map Value type
     * @param m       Map
     * @param kMapper Mapping function to convert T to K
     * @param c       BiConsumer
     * <p>
     * @return Consumer
     */
    public <T, K, V> Consumer<T> consumeIfAbsent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, BiConsumer<Map<? extends K, ? extends V>, K> c )
    {
        return t -> ifAbsent( m, kMapper.apply( t ), c );
    }
}
