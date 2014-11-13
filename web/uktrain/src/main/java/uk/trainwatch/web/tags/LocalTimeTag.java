/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.tags;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 * @author Peter T Mount
 */
public class LocalTimeTag
        extends BodyTagSupport
{

    private LocalTime value;
    private boolean working;

    @Override
    public int doStartTag()
            throws JspException
    {
        if( value != null )
        {
            try
            {
                JspWriter w = pageContext.getOut();

                boolean half = value.getSecond() != 0;
                w.print( value.truncatedTo( ChronoUnit.MINUTES ).
                        toString() );

                if( working )
                {
                    w.print( half ? "&frac12" : " " );
                }
            }
            catch( IOException ex )
            {
                throw new JspException( ex );
            }

        }

        return SKIP_BODY;
    }

    public void setValue( LocalTime value )
    {
        this.value = value;
    }

    public void setWorking( boolean working )
    {
        this.working = working;
    }

}
