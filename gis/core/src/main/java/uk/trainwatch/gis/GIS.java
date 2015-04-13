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
    public static final double DEGREES_TO_MILES = 1.0 / MILES_TO_DEGREES;
    /**
     * Conversion from km to Miles
     */
    public static final double KM_TO_MILES = 0.621371;
    public static final double MILES_TO_KM = 1.0 / KM_TO_MILES;
    /**
     * The earth's radius in km
     */
    public static final double EARTH_RADIUS = 6371.0;

    /**
     * {@link BiFunction} that will return a {@link Distance} between two {@link Coordinate}'s.
     */
    public static BiFunction<Coordinate, Coordinate, Distance> DISTANCE = ( a, b ) -> new BasicDistance( GIS.distance( a, b ) );

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
     * <p>
     * @return
     */
    public static <T extends Distance> Predicate<T> getRange( double range )
    {
        return d -> d.getDistance() > 0.0 && d.getDistance() <= range;

    }

    private static final double a = 6377563.396, b = 6356256.909;              // Airy 1830 major & minor semi-axes
    private static final double F0 = 0.9996012717;                             // NatGrid scale factor on central meridian
    private static final double φ0 = 49 * Math.PI / 180, λ0 = -2 * Math.PI / 180;      // NatGrid true origin
    private static final double N0 = -100000, E0 = 400000;                     // northing & easting of true origin, metres
    private static final double e2 = 1 - (b * b) / (a * a);                          // eccentricity squared
    private static final double n = (a - b) / (a + b), n2 = n * n, n3 = n * n * n;         // n, n², n³

    public static Coordinate osgbToLatLong( OsGridRef gridref )
    {
        double E = gridref.getEasting();
        double N = gridref.getNorthing();

        double φ = φ0, M = 0;
        do
        {
            φ = (N - N0 - M) / (a * F0) + φ;

            double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (φ - φ0);
            double Mb = (3 * n + 3 * n * n + (21 / 8) * n3) * Math.sin( φ - φ0 ) * Math.cos( φ + φ0 );
            double Mc = ((15 / 8) * n2 + (15 / 8) * n3) * Math.sin( 2 * (φ - φ0) ) * Math.cos( 2 * (φ + φ0) );
            double Md = (35 / 24) * n3 * Math.sin( 3 * (φ - φ0) ) * Math.cos( 3 * (φ + φ0) );
            M = b * F0 * (Ma - Mb + Mc - Md);              // meridional arc

        } while( N - N0 - M >= 0.00001 );  // ie until < 0.01mm

        double cosφ = Math.cos( φ ), sinφ = Math.sin( φ );
        double ν = a * F0 / Math.sqrt( 1 - e2 * sinφ * sinφ );            // nu = transverse radius of curvature
        double ρ = a * F0 * (1 - e2) / Math.pow( 1 - e2 * sinφ * sinφ, 1.5 ); // rho = meridional radius of curvature
        double η2 = ν / ρ - 1;                                    // eta = ?

        double tanφ = Math.tan( φ );
        double tan2φ = tanφ * tanφ, tan4φ = tan2φ * tan2φ, tan6φ = tan4φ * tan2φ;
        double secφ = 1 / cosφ;
        double ν3 = ν * ν * ν, ν5 = ν3 * ν * ν, ν7 = ν5 * ν * ν;
        double VII = tanφ / (2 * ρ * ν);
        double VIII = tanφ / (24 * ρ * ν3) * (5 + 3 * tan2φ + η2 - 9 * tan2φ * η2);
        double IX = tanφ / (720 * ρ * ν5) * (61 + 90 * tan2φ + 45 * tan4φ);
        double X = secφ / ν;
        double XI = secφ / (6 * ν3) * (ν / ρ + 2 * tan2φ);
        double XII = secφ / (120 * ν5) * (5 + 28 * tan2φ + 24 * tan4φ);
        double XIIA = secφ / (5040 * ν7) * (61 + 662 * tan2φ + 1320 * tan4φ + 720 * tan6φ);

        double dE = (E - E0), dE2 = dE * dE, dE3 = dE2 * dE, dE4 = dE2 * dE2, dE5 = dE3 * dE2, dE6 = dE4 * dE2, dE7 = dE5 * dE2;
        φ = φ - VII * dE2 + VIII * dE4 - IX * dE6;
        double λ = λ0 + X * dE - XI * dE3 + XII * dE5 - XIIA * dE7;

        return new BasicCoordinate( Math.toDegrees( λ ), Math.toDegrees( φ ) );
    }

    public static OsGridRef toOsGridRef( Coordinate point )
    {
        double φ = Math.toRadians( point.getLatitude() );
        double λ = Math.toRadians( point.getLongitude() );

        double cosφ = Math.cos( φ ), sinφ = Math.sin( φ );
        double ν = a * F0 / Math.sqrt( 1 - e2 * sinφ * sinφ );            // nu = transverse radius of curvature
        double ρ = a * F0 * (1 - e2) / Math.pow( 1 - e2 * sinφ * sinφ, 1.5 ); // rho = meridional radius of curvature
        double η2 = ν / ρ - 1;                                    // eta = ?

        double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (φ - φ0);
        double Mb = (3 * n + 3 * n * n + (21 / 8) * n3) * Math.sin( φ - φ0 ) * Math.cos( φ + φ0 );
        double Mc = ((15 / 8) * n2 + (15 / 8) * n3) * Math.sin( 2 * (φ - φ0) ) * Math.cos( 2 * (φ + φ0) );
        double Md = (35 / 24) * n3 * Math.sin( 3 * (φ - φ0) ) * Math.cos( 3 * (φ + φ0) );
        double M = b * F0 * (Ma - Mb + Mc - Md);              // meridional arc

        double cos3φ = cosφ * cosφ * cosφ;
        double cos5φ = cos3φ * cosφ * cosφ;
        double tan2φ = Math.tan( φ ) * Math.tan( φ );
        double tan4φ = tan2φ * tan2φ;

        double I = M + N0;
        double II = (ν / 2) * sinφ * cosφ;
        double III = (ν / 24) * sinφ * cos3φ * (5 - tan2φ + 9 * η2);
        double IIIA = (ν / 720) * sinφ * cos5φ * (61 - 58 * tan2φ + tan4φ);
        double IV = ν * cosφ;
        double V = (ν / 6) * cos3φ * (ν / ρ - tan2φ);
        double VI = (ν / 120) * cos5φ * (5 - 18 * tan2φ + tan4φ + 14 * η2 - 58 * tan2φ * η2);

        double Δλ = λ - λ0;
        double Δλ2 = Δλ * Δλ, Δλ3 = Δλ2 * Δλ, Δλ4 = Δλ3 * Δλ, Δλ5 = Δλ4 * Δλ, Δλ6 = Δλ5 * Δλ;

        double N = I + II * Δλ2 + III * Δλ4 + IIIA * Δλ6;
        double E = E0 + IV * Δλ + V * Δλ3 + VI * Δλ5;

        return new BasicOsGridRef( (int) E, (int) N ); // gets truncated to SW corner of 1m grid square
    }


}
