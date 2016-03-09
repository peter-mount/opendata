/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Configuration from a remote http server. Authentication is done
 *
 * @author peter
 */
public abstract class ConfigurationWrapper
        implements Configuration
{

    protected abstract Configuration getConfiguration();

    @Override
    public String getString( String key )
    {
        return getConfiguration().getString( key );
    }

    @Override
    public Collection<String> getKeys()
    {
        return getConfiguration().getKeys();
    }

    @Override
    public Stream<String> keys()
    {
        return getConfiguration().keys();
    }

    @Override
    public String getString( String key, String defaultValue )
    {
        return getConfiguration().getString( key, defaultValue );
    }

    @Override
    public boolean getBoolean( String key )
    {
        return getConfiguration().getBoolean( key );
    }

    @Override
    public boolean getBoolean( String key, boolean defaultValue )
    {
        return getConfiguration().getBoolean( key, defaultValue );
    }

    @Override
    public int getInt( String key )
    {
        return getConfiguration().getInt( key );
    }

    @Override
    public int getInt( String key, int defaultValue )
    {
        return getConfiguration().getInt( key, defaultValue );
    }

    @Override
    public long getLong( String key )
    {
        return getConfiguration().getLong( key );
    }

    @Override
    public long getLong( String key, long defaultValue )
    {
        return getConfiguration().getLong( key, defaultValue );
    }

    @Override
    public double getDouble( String key )
    {
        return getConfiguration().getDouble( key );
    }

    @Override
    public double getDouble( String key, double defaultValue )
    {
        return getConfiguration().getDouble( key, defaultValue );
    }

    @Override
    public int size()
    {
        return getConfiguration().size();
    }

    @Override
    public boolean isEmpty()
    {
        return getConfiguration().isEmpty();
    }

    @Override
    public boolean containsKey( Object key )
    {
        return getConfiguration().containsKey( key );
    }

    @Override
    public boolean containsValue( Object value )
    {
        return getConfiguration().containsValue( value );
    }

    @Override
    public Object get( Object key )
    {
        return getConfiguration().get( key );
    }

    @Override
    public Object put( String key, Object value )
    {
        return getConfiguration().put( key, value );
    }

    @Override
    public Object remove( Object key )
    {
        return getConfiguration().remove( key );
    }

    @Override
    public void putAll(
            Map<? extends String, ? extends Object> m )
    {
        getConfiguration().putAll( m );
    }

    @Override
    public void clear()
    {
        getConfiguration().clear();
    }

    @Override
    public Set<String> keySet()
    {
        return getConfiguration().keySet();
    }

    @Override
    public Collection<Object> values()
    {
        return getConfiguration().values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return getConfiguration().entrySet();
    }

    @Override
    public Object getOrDefault( Object key, Object defaultValue )
    {
        return getConfiguration().getOrDefault( key, defaultValue );
    }

    @Override
    public void forEach(
            BiConsumer<? super String, ? super Object> action )
    {
        getConfiguration().forEach( action );
    }

    @Override
    public void replaceAll(
            BiFunction<? super String, ? super Object, ? extends Object> function )
    {
        getConfiguration().replaceAll( function );
    }

    @Override
    public Object putIfAbsent( String key, Object value )
    {
        return getConfiguration().putIfAbsent( key, value );
    }

    @Override
    public boolean remove( Object key, Object value )
    {
        return getConfiguration().remove( key, value );
    }

    @Override
    public boolean replace( String key, Object oldValue, Object newValue )
    {
        return getConfiguration().replace( key, oldValue, newValue );
    }

    @Override
    public Object replace( String key, Object value )
    {
        return getConfiguration().replace( key, value );
    }

    @Override
    public Object computeIfAbsent( String key,
                                   Function<? super String, ? extends Object> mappingFunction )
    {
        return getConfiguration().computeIfAbsent( key, mappingFunction );
    }

    @Override
    public Object computeIfPresent( String key,
                                    BiFunction<? super String, ? super Object, ? extends Object> remappingFunction )
    {
        return getConfiguration().computeIfPresent( key, remappingFunction );
    }

    @Override
    public Object compute( String key,
                           BiFunction<? super String, ? super Object, ? extends Object> remappingFunction )
    {
        return getConfiguration().compute( key, remappingFunction );
    }

    @Override
    public Object merge( String key, Object value,
                         BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction )
    {
        return getConfiguration().merge( key, value, remappingFunction );
    }

}
