/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "SignalServlet", urlPatterns =
    {
        "/signal/map",
        "/signal/map/*"
})
public class SignalServlet
        extends AbstractServlet
{

    private static final Logger LOG = Logger.getLogger( SignalServlet.class.getName() );

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String pathInfo = request.getPathInfo();
        try
        {
            if( pathInfo == null || pathInfo.isEmpty() )
            {
                showIndex( request );
            }
            else
            {
                showArea( request, pathInfo.substring( 1 ) );
            }
        }
        catch( SQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed for " + pathInfo );
        }
    }

    protected void showIndex( ApplicationRequest request )
            throws ServletException,
                   IOException,
                   SQLException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "areas", SignalManager.INSTANCE.getSignalAreas() );
        request.renderTile( "signal.home" );
    }

    protected void showArea( ApplicationRequest request, String area )
            throws ServletException,
                   IOException,
                   SQLException
    {
        Optional<SignalArea> signalArea = SignalManager.INSTANCE.getSignalArea( area );

        if( signalArea.isPresent() )
        {
            Map<String, Object> req = request.getRequestScope();
            req.put( "area", signalArea.get() );
            req.put( "berthmap", SignalManager.INSTANCE.getArea( area ) );
            req.put( "recent", SignalManager.INSTANCE.getRecent( area ) );

            request.renderTile( "signal.map" );
        }
        else
        {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "Unknown signaling area " + area );
        }
    }
}
