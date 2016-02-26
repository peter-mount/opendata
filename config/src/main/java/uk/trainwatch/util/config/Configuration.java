/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

/**
 * Handles common configuration options
 *
 * @author peter
 */
public interface Configuration
{

    String get( String key );

    default String get( String key, String defaultValue )
    {
        String s = get( key );
        return s == null ? defaultValue : s;
    }

    default boolean getBoolean( String key )
    {
        return getBoolean( key, false );
    }

    default boolean getBoolean( String key, boolean defaultValue )
    {
        String s = get( key );
        return s == null ? defaultValue : Boolean.valueOf( get( key ) );
    }

    default int getInt( String key )
    {
        return getInt( key, 0 );
    }

    default int getInt( String key, int defaultValue )
    {
        String s = get( key );
        return s == null || s.isEmpty() ? defaultValue : Integer.parseInt( s );
    }

    default long getLong( String key )
    {
        return getInt( key, 0 );
    }

    default long getLong( String key, long defaultValue )
    {
        String s = get( key );
        return s == null || s.isEmpty() ? defaultValue : Long.parseLong( s );
    }

    default double getDouble( String key )
    {
        return getInt( key, 0 );
    }

    default double getDouble( String key, double defaultValue )
    {
        String s = get( key );
        return s == null || s.isEmpty() ? defaultValue : Double.parseDouble( s );
    }

}
