/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author Peter T Mount
 */
public abstract class AbstractLocationTag
        extends BodyTagSupport
{

    private String value;
    private boolean link = true;

    @Override
    public void release()
    {
        value = null;
        link = true;
    }

    protected abstract TrainLocation getLocationRef( String value );

    @Override
    public int doStartTag()
            throws JspException
    {
        if( value != null ) {

            String name = null;

            TrainLocation ref = getLocationRef( value );
            if( ref != null && ref.isSetLocation()) {
                name = ref.getLocation();
            }

            // Try our own, has depot's etc in there
            // FIXME reimplement
//            if( name == null || name.equals( value ) ) {
//                TrainLocation loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( value );
//                if( loc != null ) {
//                    name = loc.getLocation();
//                }
//            }

            if( name == null ) {
                name = value;
            }

            try {
                JspWriter w = pageContext.getOut();

                if( link && ref != null && ref.isSetCrs() ) {
                    w.write( "<a href=\"/station/" );
                    w.write( ref.getCrs() );
                    w.write( "\">" );
                }
                w.write( name );
                if( link && ref != null && ref.isSetCrs() ) {
                    w.write( "</a>" );
                }
            }
            catch( IOException ex ) {
                throw new JspException( ex );
            }
        }

        return SKIP_BODY;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public void setLink( boolean link )
    {
        this.link = link;
    }

}
