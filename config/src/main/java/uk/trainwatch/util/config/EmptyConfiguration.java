/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author peter
 */
public class EmptyConfiguration
        implements Configuration
{

    public static Configuration INSTANCE = new EmptyConfiguration();

    @Override
    public String getString( String key )
    {
        return null;
    }

    @Override
    public String getString( String key, String defaultValue )
    {
        return defaultValue;
    }

    @Override
    public boolean getBoolean( String key, boolean defaultValue )
    {
        return defaultValue;
    }

    @Override
    public double getDouble( String key, double defaultValue )
    {
        return defaultValue;
    }

    @Override
    public int getInt( String key, int defaultValue )
    {
        return defaultValue;
    }

    @Override
    public long getLong( String key, long defaultValue )
    {
        return defaultValue;
    }

    @Override
    public Collection<String> getKeys()
    {
        return Collections.emptyList();
    }

    @Override
    public Stream<String> keys()
    {
        return Stream.empty();
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public boolean containsKey( Object key )
    {
        return false;
    }

    @Override
    public boolean containsValue( Object value )
    {
        return false;
    }

    @Override
    public Object get( Object key )
    {
        return null;
    }

    @Override
    public Object put( String key, Object value )
    {
        return null;
    }

    @Override
    public Object remove( Object key )
    {
        return null;
    }

    @Override
    public void putAll( Map<? extends String, ? extends Object> m )
    {
    }

    @Override
    public void clear()
    {
    }

    @Override
    public Set<String> keySet()
    {
        return Collections.emptySet();
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<Object> values()
    {
        return Collections.emptyList();
    }

}
