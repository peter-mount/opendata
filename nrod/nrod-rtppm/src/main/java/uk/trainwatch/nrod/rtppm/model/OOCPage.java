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
public class OOCPage
        extends AbstractPage
{

    private List<OperatorPPM> operators;

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
        final OOCPage other = (OOCPage) obj;
        return operators.equals( other.operators );
    }
}
