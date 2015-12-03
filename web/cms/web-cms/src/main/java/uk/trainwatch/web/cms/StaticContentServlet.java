/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.util.CacheControl;
import uk.trainwatch.web.util.ImageUtils;

/**
 *
 * @author peter
 */
@WebServlet(name = "StaticContentServlet", urlPatterns = "/staticContent/*", loadOnStartup = 0)
public class StaticContentServlet
        extends AbstractStaticServlet
{

    @Override
    public void init( ServletConfig config )
            throws ServletException
    {
        super.init( config );
        StaticContentManager.INSTANCE.init( config );
    }

    @Override
    protected void doGet( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        if( StaticContentManager.INSTANCE.getPage( path, req ) ) {
            HttpServletResponse response = request.getResponse();

            // Cache page for 1 hour
            CacheControl.HOUR.addHeaders( response );

            // Include the last modified header based on when it was published
            File file = (File) req.get( StaticContentManager.PAGE_FILE );
            if( file != null ) {
                ImageUtils.addLastModified( file, response );
            }

            // breadcrumb
            request.getRequestScope().put( "breadcrumb", path.split( "/" ) );

            request.renderTile( "cms.page" );
        }
        else {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, path );
        }
    }

    @Override
    protected void doHead( ApplicationRequest request, String path )
            throws ServletException,
                   IOException
    {
        // For now doGet until we implement this
        doGet( request, path );
    }

}
