/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 * The national page section of the RTPPM feed
 * <p/>
 * @author peter
 */
public abstract class AbstractPPM
{

    private long timestamp;
    private int total;
    private PPM ppm;
    private RollingPPM rollingPPM;

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( long timestamp )
    {
        this.timestamp = timestamp;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal( int total )
    {
        this.total = total;
    }

    public PPM getPpm()
    {
        return ppm;
    }

    public void setPpm( PPM ppm )
    {
        this.ppm = ppm;
    }

    public RollingPPM getRollingPPM()
    {
        return rollingPPM;
    }

    public void setRollingPPM( RollingPPM rollingPPM )
    {
        this.rollingPPM = rollingPPM;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 41 * hash + this.total;
        hash = 41 * hash + (this.ppm != null ? this.ppm.hashCode() : 0);
        hash = 41 * hash + (this.rollingPPM != null ? this.rollingPPM.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !(obj instanceof AbstractPPM) )
        {
            return false;
        }
        final AbstractPPM other = (AbstractPPM) obj;
        if( this.total != other.total )
        {
            return false;
        }
        if( this.ppm != other.ppm && (this.ppm == null || !this.ppm.equals( other.ppm )) )
        {
            return false;
        }
        if( this.rollingPPM != other.rollingPPM && (this.rollingPPM == null || !this.rollingPPM.
                                                    equals( other.rollingPPM )) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "AbstractPPM{" + "timestamp=" + timestamp + ", total=" + total + ", ppm=" + ppm + ", rollingPPM=" + rollingPPM + '}';
    }
    
}
