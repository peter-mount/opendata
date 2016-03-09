/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.json.JsonObject;
import uk.trainwatch.util.MapBuilder;

/**
 * Handles common configuration options
 *
 * @author peter
 */
public interface Configuration
        extends Map<String, Object>
{

    String getString( String key );

    Collection<String> getKeys();

    Stream<String> keys();

    default String getString( String key, String defaultValue )
    {
        String s = getString( key );
        return s == null ? defaultValue : s;
    }

    default Configuration getConfiguration( String key )
    {
        return getConfiguration( key, EmptyConfiguration.INSTANCE );
    }

    default Configuration getConfiguration( String key, Configuration defaultValue )
    {
        return getConfiguration( key, () -> defaultValue );
    }

    default Configuration getConfiguration( String key, Supplier<Configuration> defaultValue )
    {
        Object o = get( key );
        if( o instanceof Configuration ) {
            return (Configuration) o;
        }
        if( o instanceof Map ) {
            return new MapConfiguration( ((Map<String, Object>) o) );
        }
        if( o instanceof JsonObject ) {
            return new MapConfiguration( MapBuilder.fromJsonObject( (JsonObject) o ).build() );
        }
        return defaultValue.get();
    }

    default boolean getBoolean( String key )
    {
        return getBoolean( key, false );
    }

    default boolean getBoolean( String key, boolean defaultValue )
    {
        String s = getString( key );
        return s == null ? defaultValue : Boolean.valueOf( getString( key ) );
    }

    default int getInt( String key )
    {
        return getInt( key, 0 );
    }

    default int getInt( String key, int defaultValue )
    {
        String s = getString( key );
        return s == null || s.isEmpty() ? defaultValue : Integer.parseInt( s );
    }

    default long getLong( String key )
    {
        return getInt( key, 0 );
    }

    default long getLong( String key, long defaultValue )
    {
        String s = getString( key );
        return s == null || s.isEmpty() ? defaultValue : Long.parseLong( s );
    }

    default double getDouble( String key )
    {
        return getInt( key, 0 );
    }

    default double getDouble( String key, double defaultValue )
    {
        String s = getString( key );
        return s == null || s.isEmpty() ? defaultValue : Double.parseDouble( s );
    }

}
