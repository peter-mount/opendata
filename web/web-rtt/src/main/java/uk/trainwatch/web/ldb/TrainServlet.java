/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.IOException;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.ldb.model.Train;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "TrainServlet", urlPatterns = "/rtt/train/*")
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
            // Unlike mobile we retrieve the entire train here & show in one go.
            Train train = LDBUtils.getTrain( rid );

            // Also we lookup from the archive as necessary
            if( !train.isSchedulePresent() && !train.isForecastPresent() ) {
                train = LDBUtils.getArchivedTrain( rid );
            }

            Map<String, Object> req = request.getRequestScope();
            req.put( "train", train );
            req.put( "pageTitle", rid );

            // Set headers for caching
            ChronoUnit unit = train.isArchived() ? ChronoUnit.HOURS : ChronoUnit.MINUTES;
            request.expiresIn( 1, unit );
            request.maxAge( 1, unit );

            if( train.isSchedulePresent() ) {
                request.lastModified( train.getLastUpdate() );
            }

            request.renderTile( "rtt.details" );
        }
        catch( SQLException ex ) {
            log( "Failed rid " + rid, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

}
