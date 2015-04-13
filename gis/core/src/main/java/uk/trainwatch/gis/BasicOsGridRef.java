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
public class BasicOsGridRef
        implements OsGridRef
{

    private final int easting, northing;

    public BasicOsGridRef( int easting, int northing )
    {
        this.easting = easting;
        this.northing = northing;
    }

    @Override
    public final int getEasting()
    {
        return easting;
    }

    @Override
    public final int getNorthing()
    {
        return northing;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 41 * hash + this.easting;
        hash = 41 * hash + this.northing;
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final BasicOsGridRef other = (BasicOsGridRef) obj;
        return this.easting == other.easting && this.northing == other.northing;
    }

}
