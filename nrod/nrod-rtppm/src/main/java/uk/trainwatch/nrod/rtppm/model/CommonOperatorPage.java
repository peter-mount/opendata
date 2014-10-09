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
public class CommonOperatorPage
        extends AbstractPage
{

    private List<OperatorPagePPM> operators;

    public List<OperatorPagePPM> getOperators()
    {
        return operators;
    }

    public void setOperators( List<OperatorPagePPM> operators )
    {
        this.operators = operators;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
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
        final CommonOperatorPage other = (CommonOperatorPage) obj;
        return operators.equals( other.operators );
    }
}
