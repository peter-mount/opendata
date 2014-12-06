/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

/**
 * A representation of a Smart signalling berth.
 * <p>
 * @author peter
 */
public class SmartBerth
{

    private final long areaId;
    private final long berthId;

    public SmartBerth( long areaId, long berthId )
    {
        this.areaId = areaId;
        this.berthId = berthId;
    }

    public long getAreaId()
    {
        return areaId;
    }

    public long getBerthId()
    {
        return berthId;
    }

    public String getArea()
    {
        SmartArea area = SmartManager.INSTANCE.getArea( areaId );
        return area == null ? null : area.getArea();
    }

    public String getBerth()
    {
        return SmartManager.INSTANCE.getBerth( berthId );
    }

    public boolean isValid()
    {
        return !(areaId == 0L || berthId == 0L);
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 11 * hash + (int) (this.areaId ^ (this.areaId >>> 32));
        hash = 11 * hash + (int) (this.berthId ^ (this.berthId >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final SmartBerth other = (SmartBerth) obj;
        return this.areaId == other.areaId && this.berthId == other.berthId;
    }

    @Override
    public String toString()
    {
        return "SmartBerth[area=\"" + getArea() + "\", berth=\"" + getBerth() + "\"]";
    }

}
