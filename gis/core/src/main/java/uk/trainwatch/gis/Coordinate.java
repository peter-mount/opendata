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
public interface Coordinate
{

    /**
     * The longitude in degrees, positive is East.
     * <p>
     * @return
     */
    double getLongitude();

    /**
     * The latitude in degrees, positive is North.
     * <p>
     * @return
     */
    double getLatitude();

    /**
     * Return an instance
     * <p>
     * @param λ Longitude
     * @param φ Latitude
     * <p>
     * @return
     */
    static Coordinate of( double λ, double φ )
    {
        return new BasicCoordinate( λ, φ );
    }

    default OsGridRef toOsGridRef()
    {
        return GIS.toOsGridRef( this );
    }

    default DistanceCoordinate distanceFrom( Coordinate origin )
    {
        return new BasicDistanceCoordinate( getLongitude(), getLatitude(), GIS.KM_TO_MILES * GIS.distance( origin, this ) );
    }

}
