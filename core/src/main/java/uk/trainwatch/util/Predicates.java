/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.function.Predicate;

/**
 *
 * @author Peter T Mount
 */
public class Predicates
{

    /**
     * Always true
     */
    public static final Predicate<?> TRUE = o -> true;
    /**
     * Always false
     */
    public static final Predicate<?> FALSE = o -> false;

    /**
     * Equivalent to {@code o-> o instanceof class}
     * <p>
     * @param <T>
     * @param clazz <p>
     * @return
     */
    public static final <T> Predicate<T> instanceOf( Class clazz )
    {
        return o -> o == null ? null : clazz.isAssignableFrom( o.getClass() );
    }

    public static final <T> Predicate<T> and( Predicate<T> a, Predicate<T> b )
    {
        return a == null ? b : b == null ? a : a.and( b );
    }

    public static final <T> Predicate<T> and( Predicate<T>... p )
    {
        Predicate<T> a = null;
        for( Predicate<T> b: p ) {
            a = and( a, b );
        }
        return a;
    }

    public static final <T> Predicate<T> or( Predicate<T> a, Predicate<T> b )
    {
        return a == null ? b : b == null ? a : a.or( b );
    }

    public static final <T> Predicate<T> or( Predicate<T>... p )
    {
        Predicate<T> a = null;
        for( Predicate<T> b: p ) {
            a = or( a, b );
        }
        return a;
    }

}
