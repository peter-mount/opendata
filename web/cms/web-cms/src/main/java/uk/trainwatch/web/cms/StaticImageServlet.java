/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "StaticImageServlet", urlPatterns = "/staticImage/*")
public class StaticImageServlet
        extends AbstractStaticServlet
{

    @Override
    protected void doGet( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        StaticContentManager.INSTANCE.getImage( path, request.getResponse() );
    }

    @Override
    protected void doHead( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        // For now doGet until we implement this
        doGet( request, path );
    }

    
}
