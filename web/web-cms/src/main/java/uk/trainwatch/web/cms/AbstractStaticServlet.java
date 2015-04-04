/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
public abstract class AbstractStaticServlet
        extends AbstractServlet
{

    // FIXME remove this hardcoding
    protected final File baseDirectory = new File( "/var/www/uktra.in" );

    @Override
    protected final void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String path = request.getPathInfo().substring( 1 );

        if( path.startsWith( "." ) || path.contains( "/." ) ) {
            request.sendError( HttpServletResponse.SC_BAD_REQUEST, path );
        }
        else {
            doGet( request, path );
        }
    }

    protected abstract void doGet( ApplicationRequest request, String path )
            throws ServletException,
                   IOException;

    /**
     * Disallow posts to static content
     * <p>
     * @param request
     * @param response
     *                 <p>
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected final void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        // Message here is due to seeing requests to various MediaWiki url's in the server logs.
        // It appears that some detect it's originating from a MediaWiki but that's not whats public!
        request.sendError( HttpServletResponse.SC_NOT_FOUND, "Are you thinking this is MediaWiki?" );
    }

}
