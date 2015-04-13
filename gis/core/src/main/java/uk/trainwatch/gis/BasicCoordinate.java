/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

/**
 *
 * @author peter
 */
public class BasicCoordinate
        implements Coordinate
{

    private final double longitude, latitude;

    public BasicCoordinate( double longitude, double latitude )
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public final double getLongitude()
    {
        return longitude;
    }

    @Override
    public final double getLatitude()
    {
        return latitude;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 73 * hash + (int) (Double.doubleToLongBits( this.longitude ) ^ (Double.doubleToLongBits( this.longitude ) >>> 32));
        hash = 73 * hash + (int) (Double.doubleToLongBits( this.latitude ) ^ (Double.doubleToLongBits( this.latitude ) >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final BasicCoordinate other = (BasicCoordinate) obj;
        return Double.doubleToLongBits( this.longitude ) == Double.doubleToLongBits( other.longitude )
                && Double.doubleToLongBits( this.latitude ) == Double.doubleToLongBits( other.latitude );
    }

}
