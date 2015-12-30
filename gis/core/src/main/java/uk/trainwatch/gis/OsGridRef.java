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
public interface OsGridRef
{

    int getEasting();

    int getNorthing();

    static OsGridRef of( int easting, int northing )
    {
        return new BasicOsGridRef( easting, northing );
    }

    static OsGridRef from( Coordinate c )
    {
        return GIS.toOsGridRef( c );
    }

    default Coordinate toCoordinate()
    {
        return GIS.osgbToLatLong( this );
    }
}
