/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.tags;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author Peter T Mount
 */
public class LocalTimeTag
        extends BodyTagSupport
{

    private Object value;
    private boolean working;
    private char modifier;

    @Override
    public void release()
    {
        value = null;
        working = false;
        modifier = 0;
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        LocalTime time = null;
        if( value instanceof LocalTime )
        {
            time = (LocalTime) value;
        }
        else if( value instanceof LocalDateTime )
        {
            time = ((LocalDateTime) value).toLocalTime();
        }
        else if( value instanceof String )
        {
            time = TimeUtils.getLocalTime( (String) value );
        }

        if( time != null )
        {
            try
            {
                JspWriter w = pageContext.getOut();

                boolean half = time.getSecond() != 0;
                String s = time.truncatedTo( ChronoUnit.MINUTES ).toString();

                if( modifier != 0 )
                {
                    s = s.replace( ':', modifier );
                }

                w.print( s );

                if( working )
                {
                    w.print( half ? "&frac12;" : "&emsp;" );
                }
            } catch( IOException ex )
            {
                throw new JspException( ex );
            }

        }

        return SKIP_BODY;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public void setWorking( boolean working )
    {
        this.working = working;
    }

    public void setModifier( String modifier )
    {
        if( modifier == null || modifier.isEmpty() )
        {
            this.modifier = 0;
        }
        else
        {
            this.modifier = modifier.charAt( 0 );
        }
    }

}
