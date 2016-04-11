/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.util.Objects;

/**
 * Simple bounds
 * @author peter
 */
public interface Bounds<T>
{

    T getTopLeft();

    T getTopRight();

    T getBottomLeft();

    T getBottomRight();

    static <T> Bounds<T> of( T tl, T tr, T bl, T br )
    {
        return new Bounds<T>()
        {

            @Override
            public T getTopLeft()
            {
                return tl;
            }

            @Override
            public T getTopRight()
            {
                return tr;
            }

            @Override
            public T getBottomLeft()
            {
                return bl;
            }

            @Override
            public T getBottomRight()
            {
                return br;
            }

            @Override
            public int hashCode()
            {
                return Objects.hash( tl, tr, bl, br );
            }

            @Override
            public boolean equals( Object obj )
            {
                if( obj instanceof Bounds ) {
                    Bounds<?> b = (Bounds<?>) obj;
                    return tl.equals( b.getTopLeft() )
                           && tr.equals( b.getTopRight() )
                           && bl.equals( b.getBottomLeft() )
                           && br.equals( b.getBottomRight() );
                }
                return false;
            }

        };
    }

}
