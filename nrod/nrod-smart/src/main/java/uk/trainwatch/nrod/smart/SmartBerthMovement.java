/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

/**
 *
 * @author peter
 */
public class SmartBerthMovement
{

    private final long areaId;
    private final long fromBerthId;
    private final long toBerthId;

    public SmartBerthMovement( long areaId, long fromBerthId, long toBerthId )
    {
        this.areaId = areaId;
        this.fromBerthId = fromBerthId;
        this.toBerthId = toBerthId;
    }

    public long getAreaId()
    {
        return areaId;
    }

    public long getFromBerthId()
    {
        return fromBerthId;
    }

    public long getToBerthId()
    {
        return toBerthId;
    }

    public String getArea()
    {
        SmartArea area = SmartManager.INSTANCE.getArea( areaId );
        return area == null ? null : area.getArea();
    }

    public String getFromBerth()
    {
        return SmartManager.INSTANCE.getBerth( fromBerthId );
    }

    public String getToBerth()
    {
        return SmartManager.INSTANCE.getBerth( toBerthId );
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + (int) (this.areaId ^ (this.areaId >>> 32));
        hash = 47 * hash + (int) (this.fromBerthId ^ (this.fromBerthId >>> 32));
        hash = 47 * hash + (int) (this.toBerthId ^ (this.toBerthId >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final SmartBerthMovement other = (SmartBerthMovement) obj;
        if( this.areaId != other.areaId )
        {
            return false;
        }
        if( this.fromBerthId != other.fromBerthId )
        {
            return false;
        }
        if( this.toBerthId != other.toBerthId )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "SmartBerthMovement[area=\"" + getArea() + "\", fromBerth=\"" + getFromBerth() + "\", toBerth=\"" + getToBerth() + "\"]";
    }

}
