/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.departureboards;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.ldb.LDBUtils;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "TrainServlet", urlPatterns = "/train/*")
public class TrainServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String rid = request.getPathInfo().substring( 1 ).toUpperCase();
        log( "Retrieving train " + rid );

        try {
            Map<String, Object> req = request.getRequestScope();
            req.put( "train", LDBUtils.getTrain( rid ) );
            req.put( "pageTitle", rid );

            // Force 1 minute cache setting
            long now = System.currentTimeMillis();
            
            // FIXME should be last instant in Train
            Instant lastUpdate = TimeUtils.getInstant( now);
            
            HttpServletResponse response = request.getResponse();
            response.addHeader( "Cache-Control", "public, max-age=60, s-maxage=60, no-transform" );
            response.addDateHeader( "Expires", now + 60000L );
            response.addDateHeader( "last-modified", Date.from( lastUpdate ).getTime() );

            request.renderTile( "ldb.train" );
        }
        catch( SQLException ex ) {
            log( "Failed rid " + rid, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

}
