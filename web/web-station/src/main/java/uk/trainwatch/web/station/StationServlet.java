/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.gis.StationPosition;
import uk.trainwatch.gis.StationPositionManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.performance.StationPerformance;
import uk.trainwatch.web.timetable.ScheduleSQL;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "StationServlet", urlPatterns = "/station/*")
public class StationServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String tiploc = request.getPathInfo().
                substring( 1 ).
                toUpperCase();
        TrainLocation loc = TrainLocationFactory.INSTANCE.getTrainLocationByTiploc( tiploc );
        if( loc == null )
        {
            // See if they have used an alternate code
            loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( tiploc );

            if( loc == null )
            {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else
            {
                // Redirect to the correct page
                request.getResponse().
                        sendRedirect( "/station/" + loc.getTiploc() );
            }
        }
        else
        {
            show( request, loc );
        }
    }

    private void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "location", loc );
        req.put( "pageTitle", loc.getLocation() );
        try
        {
            showMap( req, loc );
            getDepartures( req, loc );
        }
        catch( SQLException ex )
        {
            ex.printStackTrace();
        }

        request.renderTile( "station.info" );
    }

    private void getDepartures( Map<String, Object> req, TrainLocation loc )
            throws SQLException
    {
        LocalDateTime dateTime = TimeUtils.getLocalDateTime();
        req.put( "dateTime", dateTime );
        LocalDate date = dateTime.toLocalDate();
        req.put( "date", date );

        // Now filter to include just PUBLIC trains that depart in the next hour
        // Note: Don't remove from schedule else timetable in cache is affected
        req.put( "departures",
                 ScheduleSQL.getSchedules( loc, date ).
                 stream().
                 filter( ScheduleSQL.PUBLIC_TRAIN.and( ScheduleSQL.departuresOnly( loc.getTiploc(), dateTime, 1L ) ) ).
                 collect( Collectors.toList() )
        );

    }

    /**
     * is it possible to display a map? Not everywhere is in the db (some with wrong names)
     * <p>
     * @param req
     * @param loc
     *            <p>
     * @throws ServletException
     * @throws IOException
     */
    private void showMap( Map<String, Object> req, TrainLocation loc )
            throws SQLException
    {

        List<StationPosition> stationPosition = StationPositionManager.INSTANCE.find( loc.getLocation() );
        if( !stationPosition.isEmpty() )
        {
            StationPosition station = stationPosition.get( 0 );
            req.put( "stationPosition", station );
            req.put( "nearBy", StationPositionManager.INSTANCE.nearby( station, 3 ) );
        }
    }
}
