/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

import java.util.List;

/**
 * The entire RTPPM message as received.
 * <p/>
 * This bean is mainly used for parsing the inbound message
 * <p/>
 * @author peter
 */
public class RTPPMDataMsg
{

    private String owner;
    private long timestamp;
    private String classification;
    private String schemaLocation;
    //
    private NationalPage nationalPage;
    private OOCPage oocPage;
    private CommonOperatorPage commonOperatorPage;
    private List<OperatorPagePPM> operatorPages;

    public String getOwner()
    {
        return owner;
    }

    public void setOwner( String owner )
    {
        this.owner = owner;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( long timestamp )
    {
        this.timestamp = timestamp;
    }

    public String getClassification()
    {
        return classification;
    }

    public void setClassification( String classification )
    {
        this.classification = classification;
    }

    public String getSchemaLocation()
    {
        return schemaLocation;
    }

    public void setSchemaLocation( String schemaLocation )
    {
        this.schemaLocation = schemaLocation;
    }

    public NationalPage getNationalPage()
    {
        return nationalPage;
    }

    public void setNationalPage( NationalPage nationalPage )
    {
        this.nationalPage = nationalPage;
    }

    public OOCPage getOocPage()
    {
        return oocPage;
    }

    public void setOocPage( OOCPage oocPage )
    {
        this.oocPage = oocPage;
    }

    public CommonOperatorPage getCommonOperatorPage()
    {
        return commonOperatorPage;
    }

    public void setCommonOperatorPage( CommonOperatorPage commonOperatorPage )
    {
        this.commonOperatorPage = commonOperatorPage;
    }

    public List<OperatorPagePPM> getOperatorPages()
    {
        return operatorPages;
    }

    public void setOperatorPages( List<OperatorPagePPM> operatorPages )
    {
        this.operatorPages = operatorPages;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + (this.owner != null ? this.owner.hashCode() : 0);
        hash = 23 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 23 * hash + (this.classification != null ? this.classification.hashCode() : 0);
        hash = 23 * hash + (this.schemaLocation != null ? this.schemaLocation.hashCode() : 0);
        hash = 23 * hash + (this.nationalPage != null ? this.nationalPage.hashCode() : 0);
        hash = 23 * hash + (this.oocPage != null ? this.oocPage.hashCode() : 0);
        hash = 23 * hash + (this.commonOperatorPage != null ? this.commonOperatorPage.hashCode() : 0);
        hash = 23 * hash + operatorPages.hashCode();
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
        final RTPPMDataMsg other = (RTPPMDataMsg) obj;
        if( (this.owner == null) ? (other.owner != null) : !this.owner.equals( other.owner ) )
        {
            return false;
        }
        if( this.timestamp != other.timestamp )
        {
            return false;
        }
        if( (this.classification == null) ? (other.classification != null) : !this.classification.equals(
                other.classification ) )
        {
            return false;
        }
        if( (this.schemaLocation == null) ? (other.schemaLocation != null) : !this.schemaLocation.equals(
                other.schemaLocation ) )
        {
            return false;
        }
        if( this.nationalPage != other.nationalPage && (this.nationalPage == null || !this.nationalPage.equals(
                                                        other.nationalPage )) )
        {
            return false;
        }
        if( this.oocPage != other.oocPage && (this.oocPage == null || !this.oocPage.equals( other.oocPage )) )
        {
            return false;
        }
        if( this.commonOperatorPage != other.commonOperatorPage && (this.commonOperatorPage == null || !this.commonOperatorPage.
                                                                    equals( other.commonOperatorPage )) )
        {
            return false;
        }

        return operatorPages.equals( other.operatorPages );
    }
}
