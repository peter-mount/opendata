/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rtt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.util.Streams;
import uk.trainwatch.web.ldb.LDBUtils;
import uk.trainwatch.web.ldb.cache.TrainCache;
import uk.trainwatch.web.ldb.model.Train;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
public abstract class AbstractTrainServlet
        extends AbstractServlet
{

    @Inject
    private TrainCache trainCache;

    @Inject
    protected LDBUtils lDBUtils;

    protected abstract String getTile();

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String args[] = request.getPathInfo().substring( 1 ).toUpperCase().split( "/" );
        String rid = args.length == 0 ? null : args[0];

        log( "Retrieving train " + rid );

        try {
            Train train = trainCache.get( rid );

            // Still nothing then we didn't find it
            if( !train.isSchedulePresent() && !train.isForecastPresent() ) {
                request.sendError( HttpServletResponse.SC_NOT_FOUND, rid );
                return;
            }

            Map<String, Object> req = request.getRequestScope();
            req.put( "train", train );
            req.put( "pageTitle", rid );

            // Set headers for caching
            ChronoUnit unit = train.isArchived() ? ChronoUnit.HOURS : ChronoUnit.MINUTES;
            request.expiresIn( 1, unit );
            request.maxAge( 1, unit );

            request.lastModified( train.getLastUpdate() );

            request.renderTile( getTile() );
        }
        catch( SQLException ex ) {
            log( "Failed rid " + rid, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

}
