/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 *
 * @author peter
 */
public class GIS
{

    /**
     * An approximation of converting miles in latitude into degrees
     */
    public static final double MILES_TO_DEGREES = 0.014492;
    /**
     * Conversion from km to Miles
     */
    public static final double KM_TO_MILES = 0.621371;
    /**
     * The earth's radius in km
     */
    public static final double EARTH_RADIUS = 6371.0;

    /**
     * {@link BiFunction} that will return a {@link Distance} between two {@link Coordinate}'s.
     */
    public static BiFunction<Coordinate, Coordinate, Distance> DISTANCE = ( a, b ) -> new DistanceImpl( GIS.distance( a, b ) );

    /**
     * Using the haversine formula, calculate the great-circle distance between two points
     * <p>
     * @param c1 Coordinate 1
     * @param c2 Coordinate 2
     * <p>
     * @return Distance in km
     */
    public static double distance( Coordinate c1, Coordinate c2 )
    {
        double φ1 = Math.toRadians( c1.getLatitude() );
        double φ2 = Math.toRadians( c2.getLatitude() );
        double Δφ = φ2 - φ1;
        double Δλ = Math.toRadians( c2.getLongitude() - c1.getLongitude() );
        double a = Math.sin( Δφ / 2.0 ) * Math.sin( Δφ / 2.0 ) + Math.cos( φ1 ) * Math.cos( φ2 ) * Math.sin( Δλ / 2.0 ) * Math.sin( Δλ / 2.0 );
        double c = 2.0 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1.0 - a ) );
        return EARTH_RADIUS * c;
    }

    /**
     * Creates an {@link UnaryOperator} which will set the distance of some coordinate from the supplied one
     * <p>
     * @param <T>
     * @param origin origin coordinate
     * <p>
     * @return function to set the distance from the origin
     */
    public static <T extends DistanceCoordinate> UnaryOperator<T> setDistance( Coordinate origin )
    {
        return coord ->
        {
            coord.setDistance( KM_TO_MILES * distance( origin, coord ) );
            return coord;
        };
    }

    /**
     * Returns a {@link Predicate} that will be true if a {@link Distance} is within range (in some specified unit).
     * <p>
     * Note: this will filter out distances of 0.0.
     * <p>
     * @param <T>
     * @param range
     *              <p>
     * @return
     */
    public static <T extends Distance> Predicate<T> getRange( double range )
    {
        return d -> d.getDistance() > 0.0 && d.getDistance() <= range;

    }

    private static class DistanceImpl
            implements Distance
    {

        final double distance;

        public DistanceImpl( double distance )
        {
            this.distance = distance;
        }

        @Override
        public double getDistance()
        {
            return distance;
        }
    }

}
