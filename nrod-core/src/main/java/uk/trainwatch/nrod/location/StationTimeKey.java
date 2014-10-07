/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.io.Serializable;

/**
 * A key used for a specific station stanox and a timestamp
 * <p/>
 * @author peter
 */
public class StationTimeKey
        implements Serializable
{

    private final long stanox;
    private final long start;

    public StationTimeKey( long stanox, long start )
    {
        this.stanox = stanox;
        this.start = start;
    }

    public long getStanox()
    {
        return stanox;
    }

    public long getStart()
    {
        return start;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 59 * hash + (int) (this.stanox ^ (this.stanox >>> 32));
        hash = 59 * hash + (int) (this.start ^ (this.start >>> 32));
        return hash;
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
        final StationTimeKey other = (StationTimeKey) obj;
        if( this.stanox != other.stanox )
        {
            return false;
        }
        if( this.start != other.start )
        {
            return false;
        }
        return true;
    }
}
