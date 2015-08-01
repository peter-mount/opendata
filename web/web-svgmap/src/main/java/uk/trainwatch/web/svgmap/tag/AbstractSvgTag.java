/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tag;

import java.io.Writer;
import java.util.function.BiConsumer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.gis.svg.SvgBounds;
import uk.trainwatch.util.CDIUtils;

/**
 *
 * @author peter
 */
public abstract class AbstractSvgTag
        extends BodyTagSupport
{

    private int x;
    private int y;
    private double scale;
    private int width;
    private int height;

    @SuppressWarnings("LeakingThisInConstructor")
    public AbstractSvgTag()
    {
        super();
        CDIUtils.inject( this );
    }

    protected abstract BiConsumer<SvgBounds, Writer> getConsumer();

    @Override
    public int doStartTag()
            throws JspException
    {
        getConsumer().accept( new SvgBounds( scale, x, y, width, height ),
                              pageContext.getOut() );

        return SKIP_BODY;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public void setScale( double scale )
    {
        this.scale = scale == 0.0 ? 1.0 : scale;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }

}
