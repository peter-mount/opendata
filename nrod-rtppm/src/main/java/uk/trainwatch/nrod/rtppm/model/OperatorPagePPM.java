/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 *
 * @author peter
 */
public class OperatorPagePPM
        extends OperatorPPM
{

    private int onTime;
    private int late;
    private int cancelVeryLate;

    public int getOnTime()
    {
        return onTime;
    }

    public void setOnTime( int onTime )
    {
        this.onTime = onTime;
    }

    public int getLate()
    {
        return late;
    }

    public void setLate( int late )
    {
        this.late = late;
    }

    public int getCancelVeryLate()
    {
        return cancelVeryLate;
    }

    public void setCancelVeryLate( int cancelVeryLate )
    {
        this.cancelVeryLate = cancelVeryLate;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        hash = 23 * hash + this.onTime;
        hash = 23 * hash + this.late;
        hash = 23 * hash + this.cancelVeryLate;
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
        final OperatorPagePPM other = (OperatorPagePPM) obj;
        if( this.onTime != other.onTime )
        {
            return false;
        }
        if( this.late != other.late )
        {
            return false;
        }
        if( this.cancelVeryLate != other.cancelVeryLate )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "OperatorPagePPM[name=" + getName() + ",keySymbol=" + getKeySymbol() + ", total=" + getTotal() + ", onTime=" + onTime + ", late=" + late + ", cancelVeryLate=" + cancelVeryLate + ", ppm=" + getPpm() + ", rollingPpm=" + getRollingPPM() + ']';
    }
}
