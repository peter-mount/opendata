/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author peter
 */
public class LocationTimeKey
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String crs;
    private int time;

    public LocationTimeKey()
    {
    }

    public LocationTimeKey( String crs, int time )
    {
        this.crs = crs;
        this.time = time;
    }

    public LocationTimeKey( String crs, LocalTime time )
    {
        this( crs, time.toSecondOfDay() );
    }

    public LocationTimeKey( TrainLocation loc, LocalTime time )
    {
        this( loc.getCrs(), time );
    }

    public String getCrs()
    {
        return crs;
    }

    public void setCrs( String crs )
    {
        this.crs = crs;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime( int time )
    {
        this.time = time;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode( this.crs );
        hash = 67 * hash + this.time;
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final LocationTimeKey other = (LocationTimeKey) obj;
        return Objects.equals( this.crs, other.crs ) && this.time == other.time;
    }

}
