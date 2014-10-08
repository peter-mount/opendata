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
     * 
     * @param <T> Type of class
     * @param clazz Class of type T to cast to
     * @return casted object or null if null or not an instance of clazz
     */
    public static <T> Function<? super Object, T> castTo( Class<T> clazz )
    {
        return o -> o == null || !clazz.isAssignableFrom( clazz ) ? null : clazz.cast( o );
    }
}
