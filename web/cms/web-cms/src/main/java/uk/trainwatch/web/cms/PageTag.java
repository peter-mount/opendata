/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Allows a CMS page to be embedded within a jsp
 * <p>
 * @author Peter T Mount
 */
public class PageTag
        extends BodyTagSupport
{

    private static final String STRIP_START = "<article id=\"er-article-body\"><div id=\"mw-content-text\" lang=\"en-GB\" dir=\"ltr\" class=\"mw-content-ltr\">";
    private static final int STRIP_START_LENGTH = STRIP_START.length();
    private static final String STRIP_END = "</div></article>";
    private static final int STRIP_END_LENGTH = STRIP_END.length();
    private String page;
    private boolean strip = true;

    private static final Logger LOG = Logger.getLogger( PageTag.class.getName() );

    @Override
    public void release()
    {
        page = null;
        strip = true;
    }

    @Override
    public int doStartTag()
            throws JspException
    {
        try {
            JspWriter w = pageContext.getOut();

            Map<String, Object> req = new HashMap<>();
            if( StaticContentManager.INSTANCE.getPage( page, req ) ) {
                String content = Objects.toString( req.get( StaticContentManager.PAGE ), "" );

                // Usually enabled, strip article elements as this is usually not the default article in the page.
                if( strip ) {
                    if( content.startsWith( STRIP_START ) ) {
                        content = content.substring( STRIP_START_LENGTH );
                    }
                    if( content.endsWith( STRIP_END ) ) {
                        content = content.substring( 0, content.length() - STRIP_END_LENGTH );
                    }
                }

                LOG.log( Level.INFO, content );

                w.write( content );
            }
        }
        catch( IOException ex ) {
            throw new JspException( ex );
        }

        return SKIP_BODY;
    }

    /**
     * The cms page name to include
     * <p>
     * @param page
     */
    public void setPage( String page )
    {
        this.page = page;
    }

    /**
     * Strip outer article/div tags? Default is true
     * <p>
     * @param strip
     */
    public void setStrip( boolean strip )
    {
        this.strip = strip;
    }

}
