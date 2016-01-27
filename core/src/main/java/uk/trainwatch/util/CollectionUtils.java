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
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
     *               <p>
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
        return ( m1, m2 ) -> {
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
        if( v != null ) {
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
        if( v != null ) {
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
        if( !m.containsKey( k ) ) {
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
        if( !m.containsKey( k ) ) {
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
    public static <T, K, V> Consumer<T> consumeIfPresent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, Consumer<V> c )
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
    public static <T, K, V> Consumer<T> consumeIfPresent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, BiConsumer<K, V> c )
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
    public static <T, K, V> Consumer<T> consumeIfAbsent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, Consumer<K> c )
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
    public static <T, K, V> Consumer<T> consumeIfAbsent( Map<? extends K, ? extends V> m, Function<T, K> kMapper, BiConsumer<Map<? extends K, ? extends V>, K> c )
    {
        return t -> ifAbsent( m, kMapper.apply( t ), c );
    }

    /**
     * Inverts a map by generating a new map who's keys are the values in the original map and it's values the keys.
     * <p>
     * @param <K> Type of Key in original map
     * @param <V> Type of value in original map
     * @param m   Map to invert
     * <p>
     * @return Map who's keys and values are swapped
     */
    public static <K, V> Map<V, K> invertMap( Map<K, V> m )
    {
        return m.entrySet().
                stream().
                collect( Collectors.toMap( Map.Entry::getValue, Map.Entry::getKey ) );
    }

    public static <T> void forEach( Collection<T> col, Consumer<T> c )
    {
        if( col != null && !col.isEmpty() ) {
            col.forEach( c );
        }
    }

    /**
     * Wrap a legacy {@link Enumeration} into an Iterable.
     * <p>
     * @param <T>
     * @param e
     *            <p>
     * @return
     */
    public static <T> Iterable<T> iterable( Enumeration<T> e )
    {
        return () -> iterator( e );
    }

    /**
     * Wrap a legacy {@link Enumeration} into an Iterator
     * <p>
     * @param <T>
     * @param e
     *            <p>
     * @return
     */
    public static <T> Iterator<T> iterator( Enumeration<T> e )
    {
        return new Iterator<T>()
        {

            @Override
            public boolean hasNext()
            {
                return e.hasMoreElements();
            }

            @Override
            public T next()
            {
                return e.nextElement();
            }
        };
    }

    /**
     * An unmodifiable list of a generic collection
     *
     * @param <T>
     * @param c Collection
     *
     * @return Empty list if c is null or empty, unmodifiable if c is a list otherwise a simple wrapper around c
     */
    public static <T> List<T> unmodifiableList( Collection<T> c )
    {
        if( c == null || c.isEmpty() ) {
            return Collections.emptyList();
        }

        if( c instanceof List ) {
            return Collections.unmodifiableList( ((List<T>) c) );
        }

        return new List<T>()
        {
            @Override
            public int size()
            {
                return c.size();
            }

            @Override
            public boolean isEmpty()
            {
                return c.isEmpty();
            }

            @Override
            public boolean contains( Object o )
            {
                return c.contains( o );
            }

            @Override
            public Iterator<T> iterator()
            {
                Iterator<T> it = c.iterator();
                return new Iterator<T>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public T next()
                    {
                        return it.next();
                    }
                };
            }

            @Override
            public Object[] toArray()
            {
                return c.toArray();
            }

            @Override
            public <T> T[] toArray( T[] a )
            {
                return c.toArray( a );
            }

            @Override
            public boolean add( T e )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean remove( Object o )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean containsAll( Collection<?> c1 )
            {
                return c.contains( c1 );
            }

            @Override
            public boolean addAll( Collection<? extends T> c )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll( int index, Collection<? extends T> c )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll( Collection<?> c )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll( Collection<?> c )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public T get( int index )
            {
                // Inefficient
                Iterator<T> t = c.iterator();
                for( int i = 0; t.hasNext() && i < index; i++ ) {
                    T v = t.next();
                    if( i == index ) {
                        return v;
                    }
                }
                throw new IndexOutOfBoundsException();
            }

            @Override
            public T set( int index, T element )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add( int index, T element )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public T remove( int index )
            {
                throw new UnsupportedOperationException();
            }

            /**
             *
             * @param o    Object for equality
             * @param f    function (i,r) -> returns new value of r
             * @param slow true then run through entire list, false then first r >0
             *
             * @return
             */
            private int scan( Object o, IntBinaryOperator f, boolean slow )
            {
                int r = -1;
                Iterator<T> t = c.iterator();
                for( int i = 0; t.hasNext() && (slow || r == -1); i++ ) {
                    T v = t.next();
                    if( (v == null && o == null) || (v != null && v.equals( o )) ) {
                        r = f.applyAsInt( i, r );
                    }
                }
                return r;
            }

            @Override
            public int indexOf( Object o )
            {
                return scan( o, ( i, r ) -> i, true );
            }

            @Override
            public int lastIndexOf( Object o )
            {
                return scan( o, Integer::max, false );
            }

            @Override
            public ListIterator<T> listIterator()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public ListIterator<T> listIterator( int index )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<T> subList( int fromIndex, int toIndex )
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
