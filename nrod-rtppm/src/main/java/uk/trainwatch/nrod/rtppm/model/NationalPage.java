/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

import java.util.List;

/**
 * The national page section of the RTPPM feed
 * <p/>
 * @author peter
 */
public class NationalPage
        extends AbstractPage
{

    private String webMessageOfMoment;
    private boolean staleFlag;
    private NationalPPM nationalPPM;
    private List<SectorPPM> sectors;
    private List<OperatorPPM> operators;

    public String getWebMessageOfMoment()
    {
        return webMessageOfMoment;
    }

    public void setWebMessageOfMoment( String webMessageOfMoment )
    {
        this.webMessageOfMoment = webMessageOfMoment;
    }

    public boolean isStaleFlag()
    {
        return staleFlag;
    }

    public void setStaleFlag( boolean staleFlag )
    {
        this.staleFlag = staleFlag;
    }

    public NationalPPM getNationalPPM()
    {
        return nationalPPM;
    }

    public void setNationalPPM( NationalPPM nationalPPM )
    {
        this.nationalPPM = nationalPPM;
    }

    public List<SectorPPM> getSectors()
    {
        return sectors;
    }

    public void setSectors( List<SectorPPM> sectors )
    {
        this.sectors = sectors;
    }

    public List<OperatorPPM> getOperators()
    {
        return operators;
    }

    public void setOperators( List<OperatorPPM> operators )
    {
        this.operators = operators;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        hash = 37 * hash + (this.webMessageOfMoment != null ? this.webMessageOfMoment.hashCode() : 0);
        hash = 37 * hash + (this.staleFlag ? 1 : 0);
        hash = 37 * hash + (this.nationalPPM != null ? this.nationalPPM.hashCode() : 0);
        hash = 37 * hash + sectors.hashCode();
        hash = 37 * hash + operators.hashCode();
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !(super.equals( obj )) )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final NationalPage other = (NationalPage) obj;
        if( (this.webMessageOfMoment == null) ? (other.webMessageOfMoment != null) : !this.webMessageOfMoment.equals(
                other.webMessageOfMoment ) )
        {
            return false;
        }
        if( this.staleFlag != other.staleFlag )
        {
            return false;
        }
        if( this.nationalPPM != other.nationalPPM && (this.nationalPPM == null || !this.nationalPPM.equals(
                                                      other.nationalPPM )) )
        {
            return false;
        }
        return sectors.equals( other.sectors ) && operators.equals( other.operators );
    }
}
