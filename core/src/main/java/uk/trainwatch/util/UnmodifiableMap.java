/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Abstract {@link Map} implementation with all methods that modify the map throwing {@link UnsupportedOperationException}
 * <p>
 * @author peter
 * @param <K> Type of key
 * @param <V> Type of value
 */
public abstract class UnmodifiableMap<K, V>
        implements Map<K, V>,
                   Serializable
{

    private static final long serialVersionUID = 1L;

    @Override
    public V put( K key, V value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove( Object key )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll( BiFunction<? super K, ? super V, ? extends V> function )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent( K key, V value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove( Object key, Object value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace( K key, V oldValue, V newValue )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V replace( K key, V value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V computeIfAbsent( K key, Function<? super K, ? extends V> mappingFunction )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V computeIfPresent( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V compute( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V merge( K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction )
    {
        throw new UnsupportedOperationException();
    }
}
