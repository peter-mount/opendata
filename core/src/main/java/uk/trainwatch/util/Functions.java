/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
public class Functions
{

    /**
     * Cast to a specific class
     * <p>
     * @param <T>   Type of class
     * @param clazz Class of type T to cast to
     * <p>
     * @return casted object or null if null or not an instance of clazz
     */
    public static <T> Function<? super Object, T> castTo( Class<T> clazz )
    {
        return o -> o == null || !clazz.isAssignableFrom( o.getClass() ) ? null : clazz.cast( o );
    }

    public static <T> Class<T> forName( String n )
    {
        if( n == null || n.isEmpty() ) {
            return null;
        } else {
            try {
                return (Class<T>) Class.forName( n );
            } catch( ClassNotFoundException ex ) {
                return null;
            }
        }
    }

    public static <T> T newInstance( String n )
    {
        return newInstance( forName( n ) );
    }

    public static <T> T newInstance( Class<T> clazz )
    {
        if( clazz == null ) {
            return null;
        } else {
            try {
                return clazz.newInstance();
            } catch( InstantiationException | IllegalAccessException ex ) {
                return null;
            }
        }
    }
}
