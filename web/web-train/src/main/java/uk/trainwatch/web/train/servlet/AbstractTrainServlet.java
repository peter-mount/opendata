/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OR;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.PublicDeparture;
import uk.trainwatch.nre.darwin.model.util.TplLocation;
import uk.trainwatch.nre.darwin.model.util.WorkDeparture;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainStatus;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.train.Train;
import uk.trainwatch.web.train.TrainMovement;
import uk.trainwatch.web.util.CacheControl;

/**
 * Display the current forecast &amp; other information about a train
 * <p>
 * @author peter
 */
public abstract class AbstractTrainServlet
        extends AbstractServlet
{

    @Override
    protected final void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Train train = getTrain( request );
        if( train != null ) {
            setHeaders( request, train );
            show( request, train.getRid(), train );
        }
    }

    @Override
    protected void doHead( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Train train = getTrain( request );
        if( train != null ) {
            setHeaders( request, train );
        }
    }

    private void setHeaders( ApplicationRequest request, Train train )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        HttpServletResponse resp = request.getResponse();

        // Last modified is the Pport timestamp
        Calendar cal = train.getPport().getTs().toGregorianCalendar();
        req.put( "timestamp", TimeUtils.getLocalDateTime( cal.toInstant() ) );
        resp.setDateHeader( "last-modified", cal.getTimeInMillis() );

        // Get the next report
        TrainMovement nextReport = train.getNextReport();
        req.put( "nextReport", nextReport );

        // The time until the next report
        Duration untilNextReport = train.getUntilNextReport();
        req.put( "untilNextReport", untilNextReport );

        // Set the cache headers accordingly
        if( untilNextReport == null ) {
            // No max time then cache to 2 hours
            CacheControl.TWO_HOURS.addHeaders( resp );
        }
        else {
            // Cache up until half way to the next expected time
            // This will fail for entries with missing reports but it'll do
            long maxAge = Math.max( 0, untilNextReport.toMillis() >>> 1 );
            resp.addHeader( "Cache-Control",
                            maxAge > 0
                            ? "public, max-age=" + maxAge + ", s-maxage=" + maxAge + ", no-transform"
                            : "public, must-revalidate, no-transform, proxy-revalidate"
            );
            resp.addDateHeader( "Expires", System.currentTimeMillis() + maxAge );
        }
    }

    private Train getTrain( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String rid = request.getPathInfo().substring( 1 );
        Pport pport = ForecastManager.INSTANCE.get( rid );
        Train train = Train.create( pport );
        if( train == null ) {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, rid );
            return null;
        }

        // if schedule deleted we cannot show it
        if( train.isSchedulePresent() && train.getSchedule().getDeleted() ) {
            request.sendError( HttpServletResponse.SC_GONE, rid );
            return null;
        }

        return train;
    }

    protected abstract void show( ApplicationRequest request, String rid, Train train );

    /**
     * Fills in common details about a train like origin, destination, via, name etc
     * <p>
     * @param request
     * @param rid
     * @param train
     */
    protected final void getDetails( ApplicationRequest request, String rid, Train train )
    {
        // Get the origin location as seen by darwin
        TplLocation originLoc = train.getOrigin();
        LocationRef origin = originLoc == null ? null : DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( originLoc.getTpl() );
        TrainMovement originMvt = originLoc == null ? null : train.getMovement( originLoc.getTpl() );
        String originName=Objects.toString( origin.getLocname(), originLoc.getTpl() );

        // Get the destination location as seen by darwin
        TplLocation destLoc = train.getDestination();
        LocationRef dest = destLoc == null ? null : DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( destLoc.getTpl() );
        TrainMovement destMvt = destLoc == null ? null : train.getMovement( destLoc.getTpl() );
        String destName=Objects.toString( dest.getLocname(), destLoc.getTpl() );

        // Any via text
        Via via = null;
        if( origin != null && dest != null && origin.isSetCrs() ) {
            List<String> locs = train.getMovement().stream().map( TplLocation::getTpl ).collect( Collectors.toList() );

            via = DarwinReferenceManager.INSTANCE.getVia( origin.getCrs(), dest.getTpl(), locs );
        }

        // Train name
        String title;
        if( train.isSchedulePresent() ) {
            if( originLoc instanceof OR ) {
                title = ((PublicDeparture) originLoc).getPtd();
            }
            else {
                title = ((WorkDeparture) originLoc).getWtd();
            }
            title = title + " " + originName + " to " + destName;
            if( via != null ) {
                title = title + " " + via.getViatext();
            }
        }
        else {
            title = "Train: " + rid;
        }
        Map<String, Object> req = request.getRequestScope();
        req.put( "pageTitle", title + " - uktra.in" );
        req.put( "name", title );
        req.put( "rid", rid );
        req.put( "train", train );
        req.put( "origin", origin );
        req.put( "originMvt", originMvt );
        req.put( "originName", originName );
        req.put( "dest", dest );
        req.put( "destMvt", destMvt );
        req.put( "destName", destName );
        req.put( "via", via );
    }

    /**
     * Fills in details like why delayed, cancelled and by how much
     * <p>
     * @param request
     * @param rid
     * @param train
     */
    protected final void getDelay( ApplicationRequest request, String rid, Train train )
    {
        Map<String, Object> req = request.getRequestScope();

        // Reason for being late
        Reason lateReason = null;
        if( train.isTsPresent() && train.getTs().isSetLateReason() ) {
            lateReason = DarwinReferenceManager.INSTANCE.getLateReason( train.getTs().getLateReason().getValue() );
        }
        req.put( "lateReason", lateReason );

        // Reason for being cancelled
        Reason cancReason = null;
        if( train.isSchedulePresent() && train.getSchedule().isSetCancelReason() ) {
            cancReason = DarwinReferenceManager.INSTANCE.getLateReason( train.getSchedule().getCancelReason().getValue() );
        }
        req.put( "cancReason", cancReason );

        // Running if we have a last report time
        boolean running = train.isRunning();
        req.put( "running", running );

        // Is it being delayed?
        Duration delay = null;
        if( running ) {
            TrainMovement l = train.getLastReportedMovement();
            if( l != null ) {
                delay = l.getDelay();
            }
        }
        req.put( "delay", delay );
    }

    /**
     * Fills in the status like why it's late or cancelled
     * <p>
     * @param request
     * @param rid
     * @param train
     */
    protected final void getStatus( ApplicationRequest request, String rid, Train train )
    {
        TrainStatus trainStatus = TrainStatus.UNKNOWN;
        if( train.isSchedulePresent() && train.getSchedule().isSetStatus() ) {
            trainStatus = TrainStatus.lookup( train.getSchedule().getStatus() );
        }

        TrainCategory trainCategory = TrainCategory.UNKNOWN;
        if( train.isSchedulePresent() && train.getSchedule().isSetTrainCat() ) {
            trainCategory = TrainCategory.lookup( train.getSchedule().getTrainCat() );
        }

        Map<String, Object> req = request.getRequestScope();
        req.put( "trainCategory", trainCategory );
        req.put( "trainStatus", trainStatus );
    }

}
