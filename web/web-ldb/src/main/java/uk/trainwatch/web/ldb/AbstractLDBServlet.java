/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.TimeUtils;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractLDBServlet
        extends AbstractServlet
{

    @Inject
    protected LDBUtils lDBUtils;

    protected abstract String getRenderTile();

    protected void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "location", loc );
        req.put( "pageTitle", loc.getLocation() );
        try
        {
            LocalTime time = TimeUtils.getLondonDateTime().toLocalTime();
            String otherTime = request.getParam().get( "t" );
            if( otherTime != null )
            {
                time = LocalTime.parse( otherTime );
            }

            // Get the departures and the Instant the last entry was last updated
            Instant lastUpdate = lDBUtils.getDepartures( req, loc, time );

            // Force 1 minute cache setting
            long now = System.currentTimeMillis();
            HttpServletResponse response = request.getResponse();
            response.addHeader( "Cache-Control", "public, max-age=60, s-maxage=60, no-transform" );
            response.addDateHeader( "Expires", now + 60000L );
            response.addDateHeader( "last-modified", Date.from( lastUpdate ).getTime() );

            request.renderTile( getRenderTile() );
        } catch( SQLException ex )
        {
            log( "show " + loc, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

}
