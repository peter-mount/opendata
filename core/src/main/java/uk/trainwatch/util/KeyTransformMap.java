/*
 * Copyright 2016 peter.
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A map which can transform keys during lookup
 *
 * @author peter
 * @param <K> type of public key
 * @param <V> type of value
 */
public class KeyTransformMap<K, V>
        implements Map<K, V>
{

    private final Map<K, V> map;
    private final Function<K, K> f;

    /**
     *
     * @param f   Function to transform keys
     * @param map
     */
    public KeyTransformMap( Function<K, K> f, Map<K, V> map )
    {
        this.map = map;
        this.f = f;
    }

    @Override
    public boolean containsKey( Object key )
    {
        return map.containsKey( f.apply( (K) key ) );
    }

    @Override
    public boolean containsValue( Object value )
    {
        return map.containsValue( value );
    }

    @Override
    public V get( Object key )
    {
        return map.get( f.apply( (K) key ) );
    }

    @Override
    public V put( K key, V value )
    {
        return map.put( f.apply( key ), value );
    }

    @Override
    public V remove( Object key )
    {
        return map.remove( f.apply( (K) key ) );
    }

    @Override
    public void putAll(
            Map<? extends K, ? extends V> m )
    {
        map.putAll( m );
    }

    @Override
    public V getOrDefault( Object key, V defaultValue )
    {
        return map.getOrDefault( f.apply( (K) key ), defaultValue );
    }

    @Override
    public V putIfAbsent( K key, V value )
    {
        return map.putIfAbsent( f.apply( key ), value );
    }

    @Override
    public boolean remove( Object key, Object value )
    {
        return map.remove( f.apply( (K) key ), value );
    }

    @Override
    public boolean replace( K key, V oldValue, V newValue )
    {
        return map.replace( f.apply( key ), oldValue, newValue );
    }

    @Override
    public V replace( K key, V value )
    {
        return map.replace( f.apply( key ), value );
    }

    @Override
    public V computeIfAbsent( K key, Function<? super K, ? extends V> mappingFunction )
    {
        return map.computeIfAbsent( f.apply( key ), mappingFunction );
    }

    @Override
    public V computeIfPresent( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        return map.computeIfPresent( f.apply( key ), remappingFunction );
    }

    @Override
    public V compute( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        return map.compute( f.apply( key ), remappingFunction );
    }

    @Override
    public V merge( K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction )
    {
        return map.merge( f.apply( key ), value, remappingFunction );
    }

    //<editor-fold defaultstate="collapsed" desc="Unchanged Delegates">
    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    public Set<K> keySet()
    {
        return map.keySet();
    }

    @Override
    public Collection<V> values()
    {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet()
    {
        return map.entrySet();
    }

    @Override
    public void forEach( BiConsumer<? super K, ? super V> action )
    {
        map.forEach( action );
    }

    @Override
    public void replaceAll( BiFunction<? super K, ? super V, ? extends V> function )
    {
        map.replaceAll( function );
    }
    //</editor-fold>

}
