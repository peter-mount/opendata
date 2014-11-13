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
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import uk.trainwatch.nrod.timetable.model.Schedule;

/**
 *
 * @author Peter T Mount
 */
public class StpIndicatorTag
        extends BodyTagSupport
{

    private Schedule schedule;

    @Override
    public int doStartTag()
            throws JspException
    {
        if( schedule != null )
        {
            try
            {
                JspWriter w = pageContext.getOut();

                switch( schedule.getStpInd() )
                {
                    case STP_NEW:
                    case STP_OVERLAY:
                        w.print( "<span class=\"stpvar\">VAR</span>" );
                        break;
                    case STP_CANCELLATION:
                        w.print( "<span class=\"stpvar\">CAN</span>" );
                        break;
                    case PERMANENT:
                    case N:
                        w.print( "<span class=\"stpwtt\">WTT</span>" );
                        break;
                }
            }
            catch( IOException ex )
            {
                throw new JspException( ex );
            }

        }

        return SKIP_BODY;
    }

    public void setValue( Schedule value )
    {
        this.schedule = value;
    }

    @Override
    public void release()
    {
        schedule = null;
    }

}
