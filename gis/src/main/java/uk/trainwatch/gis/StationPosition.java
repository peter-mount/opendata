/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.sql.ResultSet;
import java.util.Objects;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class StationPosition
        implements DistanceCoordinate
{

    public static final SQLFunction<ResultSet, StationPosition> fromSQL = rs -> new StationPosition(
            rs.getLong( 1 ),
            rs.getString( 2 ),
            rs.getDouble( 3 ),
            rs.getDouble( 4 ),
            rs.getString( 5 )
    );

    private long id;
    private String name;
    private double latitude;
    private double longitude;
    private String tiploc;
    private double distance;

    public StationPosition()
    {
    }

    public StationPosition( long id, String name, double latitude, double longitude, String tiploc )
    {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tiploc = tiploc;
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude( double latitude )
    {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude( double longitude )
    {
        this.longitude = longitude;
    }

    public String getTiploc()
    {
        return tiploc;
    }

    public void setTiploc( String tiploc )
    {
        this.tiploc = tiploc;
    }

    @Override
    public double getDistance()
    {
        return distance;
    }

    @Override
    public void setDistance( double distance )
    {
        this.distance = distance;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Objects.hashCode( this.name );
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.latitude ) ^ (Double.doubleToLongBits( this.latitude ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.longitude ) ^ (Double.doubleToLongBits( this.longitude ) >>> 32));
        hash = 53 * hash + Objects.hashCode( this.tiploc );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final StationPosition other = (StationPosition) obj;
        return this.id == other.id
               || Objects.equals( this.name, other.name )
               || Objects.equals( this.name, other.tiploc )
               || Double.doubleToLongBits( this.latitude ) == Double.doubleToLongBits( other.latitude )
               || Double.doubleToLongBits( this.longitude ) == Double.doubleToLongBits( other.longitude );
    }

}
