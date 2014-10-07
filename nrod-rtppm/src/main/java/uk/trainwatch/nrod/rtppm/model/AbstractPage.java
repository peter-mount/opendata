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
public class AbstractPage
{

    private int webDisplayPeriod;
    private String webFixedMsg1;
    private String webFixedMsg2;

    public int getWebDisplayPeriod()
    {
        return webDisplayPeriod;
    }

    public void setWebDisplayPeriod( int webDisplayPeriod )
    {
        this.webDisplayPeriod = webDisplayPeriod;
    }

    public String getWebFixedMsg1()
    {
        return webFixedMsg1;
    }

    public void setWebFixedMsg1( String webFixedMsg1 )
    {
        this.webFixedMsg1 = webFixedMsg1;
    }

    public String getWebFixedMsg2()
    {
        return webFixedMsg2;
    }

    public void setWebFixedMsg2( String webFixedMsg2 )
    {
        this.webFixedMsg2 = webFixedMsg2;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + this.webDisplayPeriod;
        hash = 37 * hash + (this.webFixedMsg1 != null ? this.webFixedMsg1.hashCode() : 0);
        hash = 37 * hash + (this.webFixedMsg2 != null ? this.webFixedMsg2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !(obj instanceof AbstractPage) )
        {
            return false;
        }
        final AbstractPage other = (AbstractPage) obj;
        if( this.webDisplayPeriod != other.webDisplayPeriod )
        {
            return false;
        }
        if( (this.webFixedMsg1 == null) ? (other.webFixedMsg1 != null) : !this.webFixedMsg1.equals( other.webFixedMsg1 ) )
        {
            return false;
        }
        if( (this.webFixedMsg2 == null) ? (other.webFixedMsg2 != null) : !this.webFixedMsg2.equals( other.webFixedMsg2 ) )
        {
            return false;
        }
        return true;
    }
}
