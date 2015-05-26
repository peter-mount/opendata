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
import uk.trainwatch.nrod.location.TrainLocationFactory;
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
        String args[] = request.getPathInfo().substring( 1 ).toUpperCase().split( "/" );
        String rid = args.length == 0 ? null : args[0];

        log( "Retrieving train " + rid );

        try {
            Map<String, Object> req = request.getRequestScope();
            req.put( "train", LDBUtils.getSchedule( rid ) );
            req.put( "pageTitle", rid );

            if( args.length > 1 ) {
                req.put( "backTo", TrainLocationFactory.INSTANCE.getTrainLocationByCrs( args[1] ) );
            }

            // Force 1 minute cache setting
            long now = System.currentTimeMillis();
            Instant lastUpdate = TimeUtils.getInstant( now );

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
