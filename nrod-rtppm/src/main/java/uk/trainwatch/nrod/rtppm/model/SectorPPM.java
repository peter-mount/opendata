/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 *
 * @author peter
 */
public class SectorPPM
{

    private String sectorDesc;
    private String sectorCode;
    private NationalPPM sectorPPM;

    public String getSectorDesc()
    {
        return sectorDesc;
    }

    public void setSectorDesc( String sectorDesc )
    {
        this.sectorDesc = sectorDesc;
    }

    public String getSectorCode()
    {
        return sectorCode;
    }

    public void setSectorCode( String sectorCode )
    {
        this.sectorCode = sectorCode;
    }

    public NationalPPM getSectorPPM()
    {
        return sectorPPM;
    }

    public void setSectorPPM( NationalPPM sectorPPM )
    {
        this.sectorPPM = sectorPPM;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 31 * hash + (this.sectorDesc != null ? this.sectorDesc.hashCode() : 0);
        hash = 31 * hash + (this.sectorCode != null ? this.sectorCode.hashCode() : 0);
        hash = 31 * hash + (this.sectorPPM != null ? this.sectorPPM.hashCode() : 0);
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
        final SectorPPM other = (SectorPPM) obj;
        if( (this.sectorDesc == null) ? (other.sectorDesc != null) : !this.sectorDesc.equals( other.sectorDesc ) )
        {
            return false;
        }
        if( (this.sectorCode == null) ? (other.sectorCode != null) : !this.sectorCode.equals( other.sectorCode ) )
        {
            return false;
        }
        if( this.sectorPPM != other.sectorPPM && (this.sectorPPM == null || !this.sectorPPM.equals( other.sectorPPM )) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "SectorPPM{" + "sectorDesc=" + sectorDesc + ", sectorCode=" + sectorCode + ", sectorPPM=" + sectorPPM
               + ',' + super.toString() + '}';
    }
}
