/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.Objects;

/**
 *
 * @author Peter T Mount
 */
public class TrainUID
{

    private final char type;
    private final String id;

    public TrainUID( String s )
    {
        Objects.requireNonNull( s );
        if( s.length() != 6 )
        {
            throw new IllegalArgumentException( "Invalid TrainUID \"" + s + "\"" );
        }
        type = s.charAt( 0);
        id = s.substring( 1 );
    }

    public String getId()
    {
        return id;
    }

    public char getType()
    {
        return type;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode( this.type );
        hash = 11 * hash + Objects.hashCode( this.id );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final TrainUID other = (TrainUID) obj;
        return this.type == other.type || Objects.equals( this.id, other.id );
    }

    @Override
    public String toString()
    {
        return type + id;
    }
}
