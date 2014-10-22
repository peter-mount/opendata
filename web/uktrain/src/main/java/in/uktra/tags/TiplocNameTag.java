/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.tags;

import in.uktra.timetable.TiplocNames;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.record.Location;

/**
 *
 * @author Peter T Mount
 */
public class TiplocNameTag
        extends BodyTagSupport
{

    private Object loc;

    @Override
    public int doStartTag()
            throws JspException
    {
        if( loc == null )
        {
            return SKIP_BODY;
        }

        Tiploc tiploc = null;
        if( loc instanceof Location )
        {
            tiploc = ((Location) loc).getLocation();
        }
        else if( loc instanceof Tiploc )
        {
            tiploc = (Tiploc) loc;
        }

        if( tiploc == null )
        {
            return SKIP_BODY;
        }

        // Try to convert the name, use tiploc if it doesn't map - should not occur
        String name = TiplocNames.getName( tiploc.getKey() );
        if( name == null )
        {
            name = tiploc.getKey();
        }

        try
        {
            JspWriter w = pageContext.getOut();
            w.print( name );
        }
        catch( IOException ex )
        {
            throw new JspException( ex );
        }

        return SKIP_BODY;
    }

    public void setValue( Object loc )
    {
        this.loc = loc;
    }

}
