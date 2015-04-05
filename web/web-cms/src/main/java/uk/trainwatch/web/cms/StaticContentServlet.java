/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "StaticContentServlet", urlPatterns = "/staticContent/*")
public class StaticContentServlet
        extends AbstractStaticServlet
{

    @Override
    protected void doGet( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        if( StaticContentManager.INSTANCE.getPage( path, request.getRequestScope() ) ) {
            request.renderTile( "cms.page" );
        }
        else {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, path );
        }
    }

}
