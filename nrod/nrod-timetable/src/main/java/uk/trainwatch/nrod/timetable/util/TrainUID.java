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
        switch( s.length() )
        {
            // Most train uid's start with a letter
            case 6:
                type = s.charAt( 0 );
                id = s.substring( 1 );
                break;

            // Some start with a ' ' so appear in schedules as 5 digits
            case 5:
                type = ' ';
                id = s;
                break;

            default:
                throw new IllegalArgumentException( "Invalid TrainUID \"" + s + "\"" );
        }
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
