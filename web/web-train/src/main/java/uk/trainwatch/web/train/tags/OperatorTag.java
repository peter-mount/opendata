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
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.TocRef;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 *
 * @author Peter T Mount
 */
public class OperatorTag
        extends BodyTagSupport
{

    private String value;
    private boolean link = false;

    @Override
    public void release()
    {
        value = null;
        link = false;
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        if( value != null ) {

            String name = null;

            TocRef ref = DarwinReferenceManager.INSTANCE.getTocRef( value );
            if( ref != null && ref.isSetTocname() ) {
                name = ref.getTocname();
            }

            if( name == null ) {
                name = value;
            }

            try {
                JspWriter w = pageContext.getOut();

                if( link && ref != null && ref.isSetUrl() ) {
                    w.write( "<a href=\"" );
                    w.write( ref.getUrl() );
                    w.write( "\" target=\"_blank\">" );
                }
                w.write( name );
                if( link && ref != null && ref.isSetUrl() ) {
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
