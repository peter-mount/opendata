/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Handles the display of integer times, measured in seconds
 * <p>
 * @author Peter T Mount
 */
public class DelayTag
        extends BodyTagSupport
{

    private int value;

    @Override
    public int doStartTag()
            throws JspException
    {
        try
        {
            JspWriter w = pageContext.getOut();

            boolean half = (value % 60) != 0;
            int mins = (value / 60) % 60;
            int hours = value / 3600;

            w.print( String.format(
                    hours > 0 ? "%1$d:%2$02d%3$s" : mins > 0 ? "%2$d%3$s" : "%3$s",
                    hours,
                    mins,
                    half ? "&frac12;" : "&emsp;"
            ) );
        }
        catch( IOException ex )
        {
            throw new JspException( ex );
        }

        return SKIP_BODY;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

}
