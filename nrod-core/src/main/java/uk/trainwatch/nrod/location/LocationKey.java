/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.util.Objects;

/**
 *
 * @author peter
 */
public class LocationKey
{

    private final String key;
    private final long id;
    private final int hashCode;

    public LocationKey( String key, long id )
    {
        this.key = key;
        this.id = id;
        hashCode = 43 * +(int) (this.id ^ (this.id >>> 32));
    }

    public LocationKey( String key )
    {
        this( key, Objects.hashCode( key ) );
    }

    public LocationKey( long id )
    {
        this( String.valueOf( id ), id );
    }

    public final long getId()
    {
        return id;
    }

    public final String getKey()
    {
        return key;
    }

    @Override
    public final int hashCode()
    {
        return hashCode;
    }

    @Override
    public final boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final LocationKey other = (LocationKey) obj;
        return Objects.equals( this.key, other.key ) || this.id == other.id;
    }
}
