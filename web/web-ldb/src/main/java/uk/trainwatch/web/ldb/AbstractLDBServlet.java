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
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.web.ldb.cache.LDBDepartureCache;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractLDBServlet
        extends AbstractServlet
{

    @Inject
    protected LDBDepartureCache departureCache;
    @Inject
    protected StationMessageManager stationMessageManager;

    protected abstract String getRenderTile();

    protected void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
                   IOException
    {
        String crs = loc.getCrs();

        Map<String, Object> req = request.getRequestScope();
        req.put( "location", loc );
        req.put( "pageTitle", loc.getLocation() );
        try {
            req.put( "departures", departureCache.getDarwinDepartures( crs ) );
            req.put( "stationMessages", stationMessageManager.getMessages( crs ) );

            // TfL requires 30s whilst NRE/Darwin 60s
            int maxAge = loc.isTfl() ? 30 : 60;
            req.put( "maxAge", maxAge );

            // Force 1 minute cache setting
            long now = System.currentTimeMillis();
            HttpServletResponse response = request.getResponse();
            response.addHeader( "Cache-Control", String.format( "public, max-age=%1$d, s-maxage=%<d, no-transform", maxAge ) );
            response.addDateHeader( "Expires", now + (maxAge * 1000L) );
            response.addDateHeader( "last-modified", System.currentTimeMillis() );

            request.renderTile( getRenderTile() );
        }
        catch( SQLException ex ) {
            log( "show " + loc, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

}
