/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.tags;

import java.io.IOException;
import java.time.Duration;
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

    private Object value;
    private boolean ontime;
    private boolean absolute;
    private boolean early;

    @Override
    public void release()
    {
        value = null;
        ontime = false;
        absolute = false;
        early = false;
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        if( value != null )
        {

            Integer delayValue = null;

            if( value instanceof Number )
            {
                delayValue = ((Number) value).intValue();
            }
            else if( value instanceof Duration )
            {
                delayValue = (int) ((Duration) value).getSeconds();
            }

            if( delayValue != null )
            {
                try
                {
                    JspWriter w = pageContext.getOut();

                    boolean isEarly = delayValue < 0;
                    boolean showEarly = isEarly && !(absolute || early);
                    int delay = Math.abs( delayValue );
                    boolean half = (delay % 60) >= 30;
                    int mins = (delay / 60) % 60;
                    int hours = delay / 3600;

                    if( ontime && delay == 0 )
                    {
                        w.print( "OT" );
                    }
                    else
                    {
                        w.print( String.format(
                                hours > 0 ? "%1$s%2$dh%3$02d%4$s" : mins > 0 ? "%1$s%3$d%4$s" : "%1$s%4$s",
                                showEarly ? "-" : "",
                                hours,
                                mins,
                                // Show 1/2 or a space if not unless absolute mode is enabled
                                half ? "&frac12;" : absolute ? "" : "&emsp;"
                        ) );

                        if( early && isEarly )
                        {
                            w.print( " E" );
                        }
                    }
                } catch( IOException ex )
                {
                    throw new JspException( ex );
                }
            }
        }

        return SKIP_BODY;
    }

    public void setValue( Object value )
    {
        this.value = value;
    }

    public void setOntime( boolean ontime )
    {
        this.ontime = ontime;
    }

    public void setAbsolute( boolean absolute )
    {
        this.absolute = absolute;
    }

    public void setEarly( boolean early )
    {
        this.early = early;
    }

}
