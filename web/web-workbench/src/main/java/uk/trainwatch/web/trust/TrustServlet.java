/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Present a real time view of current running trains from the trust feed, broken down by TOC and class
 * <p>
 * @author peter
 */
@WebServlet( name = "TrustServlet", urlPatterns =
{
    "/trust", "/trust/*"
} )
public class TrustServlet
        extends AbstractServlet
{

    private static final Logger LOG = Logger.getLogger( TrustServlet.class.getName() );

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
            IOException
    {
        String pathInfo = request.getPathInfo();
        LOG.log( Level.INFO, () -> "pathInfo \"" + pathInfo + "\"" );

        if( pathInfo == null || "/".equals( pathInfo ) )
        {
            doIndex( request );
        } else
        {
            doToc( request, Integer.parseInt( pathInfo.substring( 1 ) ) );
        }
    }

    /**
     * Show index of available operators
     * <p>
     * @param req
     */
    private void doIndex( ApplicationRequest req )
    {
        Map<String, Object> request = req.getRequestScope();
        request.put( "tocs", TrustCache.INSTANCE.getTocs() );
        req.renderTile( "trust.index" );
    }

    private void doToc( ApplicationRequest req, int toc )
    {
        Map<String, Object> request = req.getRequestScope();
        request.put( "toc", toc );
        request.put( "tocs", TrustCache.INSTANCE.getTocs() );
        req.renderTile( "trust.trains" );
    }
}
