/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.cache;

import java.util.Objects;

/**
 * A simple key object that can be used with {@link Cache}
 * <p>
 * @param <A> first object type
 * @param <B> second object type
 * <p>
 * @author Peter T Mount
 */
public class CacheKey<A, B>
{

    private final A a;
    private final B b;
    private final int hashCode;

    public CacheKey( A a, B b )
    {
        this.a = a;
        this.b = b;
        hashCode = (79 * Objects.hashCode( a )) + Objects.hashCode( b );
    }

    public A getA()
    {
        return a;
    }

    public B getB()
    {
        return b;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final CacheKey other = (CacheKey) obj;
        if( !Objects.equals( this.a, other.a ) )
        {
            return false;
        }
        if( !Objects.equals( this.b, other.b ) )
        {
            return false;
        }
        return true;
    }

}
