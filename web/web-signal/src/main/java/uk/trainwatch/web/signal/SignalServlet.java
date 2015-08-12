/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "SignalServlet",
            loadOnStartup = 1,
            urlPatterns
            = {
                "/signal/map",
                "/signal/map/*"
            })
public class SignalServlet
        extends AbstractServlet
{

    private static final Logger LOG = Logger.getLogger( SignalServlet.class.getName() );

    @Inject
    private SignalManager signalManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String pathInfo = request.getPathInfo();
        try {
            if( pathInfo == null || pathInfo.isEmpty() ) {
                showIndex( request );
            }
            else {
                showArea( request, pathInfo.substring( 1 ) );
            }
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, ex, () -> "Failed for " + pathInfo );
        }
    }

    protected void showIndex( ApplicationRequest request )
            throws ServletException,
                   IOException,
                   SQLException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "areas", signalManager.getSignalAreas() );

        request.expiresIn( 1, ChronoUnit.DAYS );
        request.maxAge( 1, ChronoUnit.DAYS );
        request.lastModified( Instant.now() );

        request.renderTile( "signal.home" );
    }

    protected void showArea( ApplicationRequest request, String area )
            throws ServletException,
                   IOException,
                   SQLException
    {
        Optional<SignalArea> signalArea = signalManager.getSignalArea( area );

        if( signalArea.isPresent() ) {
            Map<String, Object> req = request.getRequestScope();
            req.put( "area", signalArea.get() );
            req.put( "berthmap", signalManager.getArea( area ) );
            req.put( "recent", signalManager.getRecent( area ) );

            request.expiresIn( 1, ChronoUnit.DAYS );
            request.maxAge( 1, ChronoUnit.DAYS );
            request.lastModified( Instant.now() );

            request.renderTile( "signal.map" );
        }
        else {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "Unknown signaling area " + area );
        }
    }
}
