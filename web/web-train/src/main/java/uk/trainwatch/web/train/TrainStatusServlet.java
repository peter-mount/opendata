/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.TplLocation;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.timetable.util.AssociationCategory;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainStatus;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Display the current forecast &amp; other information about a train
 * <p>
 * @author peter
 */
@WebServlet(name = "TrainStatusServlet", urlPatterns = "/train/*")
public class TrainStatusServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String rid = request.getPathInfo().substring( 1 );
        Pport pport = ForecastManager.INSTANCE.get( rid );
        if( pport == null || !pport.isSetUR() ) {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, rid );
            return;
        }

        Train train = new Train( pport );

        // if schedule deleted we cannot show it
        if( train.isSchedulePresent() && train.getSchedule().getDeleted() ) {
            request.sendError( HttpServletResponse.SC_GONE, rid + " has had it's schedule deleted" );
            return;
        }

        // Get the origin location as seen by darwin
        TplLocation originLoc = train.getOrigin();
        LocationRef origin = originLoc == null ? null : DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( originLoc.getTpl() );

        // Get the destination location as seen by darwin
        TplLocation destLoc = train.getOrigin();
        LocationRef dest = destLoc == null ? null : DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( train.getDestination().getTpl() );

        // Any via text
        Via via = null;
        if( origin != null && dest != null && origin.isSetCrs() ) {
            List<String> locs = train.getMovement().stream().map( TplLocation::getTpl ).collect( Collectors.toList() );

            via = DarwinReferenceManager.INSTANCE.getVia( origin.getCrs(), dest.getTpl(), locs );
        }

        // Reason for being late
        Reason lateReason = null;
        if( train.isTsPresent() && train.getTs().isSetLateReason() ) {
            lateReason = DarwinReferenceManager.INSTANCE.getLateReason( train.getTs().getLateReason().getValue() );
        }

        // Reason for being cancelled
        Reason cancReason = null;
        if( train.isSchedulePresent() && train.getSchedule().isSetCancelReason() ) {
            cancReason = DarwinReferenceManager.INSTANCE.getLateReason( train.getSchedule().getCancelReason().getValue() );
        }

        TrainStatus trainStatus = TrainStatus.UNKNOWN;
        if( train.isSchedulePresent() && train.getSchedule().isSetStatus() ) {
            trainStatus = TrainStatus.lookup( train.getSchedule().getStatus() );
        }

        TrainCategory trainCategory = TrainCategory.UNKNOWN;
        if( train.isSchedulePresent() && train.getSchedule().isSetTrainCat() ) {
            trainCategory = TrainCategory.lookup( train.getSchedule().getTrainCat() );
        }

        Map<String, Object> req = request.getRequestScope();
        req.put( "pageTitle", "Train: " + rid );
        req.put( "rid", rid );
        req.put( "train", train );
        req.put( "origin", origin );
        req.put( "dest", dest );
        req.put( "via", via );
        req.put( "lateReason", lateReason );
        req.put( "cancReason", cancReason );
        req.put( "trainCategory", trainCategory );
        req.put( "trainStatus", trainStatus );
        //req.put( "stationMessages", StationMessageManager.INSTANCE.getMessages() );
        request.renderTile( "train.info" );
    }

}
