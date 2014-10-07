/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 *
 * @author peter
 */
public class RollingPPM
        extends PPM
{

    private String trend;
    private boolean display;

    public String getTrend()
    {
        return trend;
    }

    public void setTrend( String trendInd )
    {
        this.trend = trendInd;
    }

    public boolean isDisplay()
    {
        return display;
    }

    public void setDisplay( boolean display )
    {
        this.display = display;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        hash = 41 * hash + (this.trend != null ? this.trend.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !super.equals( obj ) )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final RollingPPM other = (RollingPPM) obj;
        if( (this.trend == null) ? (other.trend != null) : !this.trend.equals( other.trend ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "RollingPPM{" +super.toString()+ ",trend=" + trend + ", display=" + display + '}';
    }
}
