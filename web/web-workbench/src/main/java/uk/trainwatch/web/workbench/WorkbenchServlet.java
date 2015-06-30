/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench;

import uk.trainwatch.web.workbench.trust.TrustCache;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Opens the actual workbench, loading the users settings from the db
 * <p>
 * @author peter
 */
@WebServlet( name = "WorkbenchServlet", urlPatterns =
     {
         "/workbench", "/workbench/"
} )
public class WorkbenchServlet
        extends AbstractServlet
{

    private static final Logger LOG = Logger.getLogger( WorkbenchServlet.class.getName() );

    @Inject
    private TrustCache trustCache;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        // TODO add login here, redirect to registration if not
        Map<String, Object> req = request.getRequestScope();
        req.put( "tocs", trustCache.getTocs() );
        request.renderTile( "workbench" );
    }
}
