/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.sql.ResultSet;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.util.Comparators;
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

    /**
     * Function to convert a StationPosition into a JsonObjectBuilder.
     * <p>
     * This does not include the distance.
     */
    public static final Function<StationPosition, JsonObjectBuilder> toJsonObject = s -> Json.createObjectBuilder().
            add( "id", s.getId() ).
            add( "name", s.getName() ).
            add( "tiploc", s.getTiploc() ).
            add( "longitude", s.getLongitude() ).
            add( "latitude", s.getLatitude() );
    /**
     * Function to convert a StationPosition into a JsonObjectBuilder.
     * <p>
     * This does include the distance.
     */
    public static final Function<StationPosition, JsonObjectBuilder> toJsonObjectWithDistance = s -> toJsonObject.apply( s ).
            add( "distance", s.getDistance() );

    public static final Comparator<StationPosition> NAME_COMPARATOR = ( a, b ) -> Comparators.compareTo( a,
                                                                                                         b,
                                                                                                         ( a1, b1 ) -> Comparators.compareTo( a1.getName(),
                                                                                                                                              b1.getName() ),
                                                                                                         ( a1, b1 ) -> Comparators.compareTo( a1.getTiploc(),
                                                                                                                                              b1.getTiploc() ) );

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
        this.name = Objects.toString( name, "" );
        this.latitude = latitude;
        this.longitude = longitude;
        this.tiploc = Objects.toString( tiploc, "" );
    }

    StationPosition( StationPosition pos, double distance )
    {
        this( pos.id, pos.name, pos.latitude, pos.longitude, pos.tiploc );
        this.distance = distance;
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
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.latitude ) ^ (Double.doubleToLongBits( this.latitude ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.longitude ) ^ (Double.doubleToLongBits( this.longitude ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.distance ) ^ (Double.doubleToLongBits( this.distance ) >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final StationPosition other = (StationPosition) obj;
        return Double.doubleToLongBits( this.latitude ) == Double.doubleToLongBits( other.latitude )
               || Double.doubleToLongBits( this.longitude ) == Double.doubleToLongBits( other.longitude )
               || Double.doubleToLongBits( this.distance ) == Double.doubleToLongBits( other.distance );
    }

    @Override
    public String toString()
    {
        return "StationPosition[name=\"" + name + "\", tiploc=\"" + tiploc + "\", lat=\"" + latitude + "\", long=\"" + longitude + "\", distance=" + distance + "]";
    }

}
