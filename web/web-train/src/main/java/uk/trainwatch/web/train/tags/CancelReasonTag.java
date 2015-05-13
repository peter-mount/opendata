/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;

/**
 *
 * @author Peter T Mount
 */
public class CancelReasonTag
        extends BodyTagSupport
{

    private Integer value;

    @Override
    public void release()
    {
        value = null;
    }

    public void setValue( Integer lateReason )
    {
        this.value = lateReason;
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        if( value != null ) {
            Reason reason = DarwinReferenceManager.INSTANCE.getCancelReason( value );
            if( reason != null ) {
                try {
                    pageContext.getOut().print( reason.getReasontext() );
                }
                catch( IOException ex ) {
                    throw new JspException( ex );
                }
            }
        }
        return SKIP_BODY;
    }

}
