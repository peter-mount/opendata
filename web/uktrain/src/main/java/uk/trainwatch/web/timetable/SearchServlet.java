/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "TTSearch", urlPatterns = "/timetable/search")
public class SearchServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        doSearch( request );
    }

    @Override
    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        doSearch( request );
    }

    protected void doSearch( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, String> param = request.getParam();

        String station = param.get( "station" );
        String dateStr = param.get( "date" );

        if( station == null || station.isEmpty() || dateStr == null || dateStr.isEmpty() )
        {
            showHome( request );
            return;
        }

        TrainLocation loc = TrainLocationFactory.INSTANCE.getTrainLocationByCrs( station );
        if( loc == null )
        {
            request.getRequestScope().
                    put( "msg", "The station you have requested is unknown" );
            showHome( request );
        }
        else
        {
            try
            {
                LocalDate date = LocalDate.parse( dateStr );

                Map<String, Object> req = request.getRequestScope();
                req.put( "pageTitle", "UK Time Table" );
                req.put( "station", loc );
                req.put( "searchDate", date );
                req.put( "schedules", ScheduleSQL.getSchedules( loc, date ) );

                request.renderTile( "timetable.search" );
            }
            catch( DateTimeParseException ex )
            {
                request.getRequestScope().
                        put( "msg", "Your fields are invalid, please check and try again" );
                showHome( request );
            }
            catch( SQLException ex )
            {
                log( "Search " + loc + " " + dateStr, ex );
                request.getRequestScope().
                        put( "msg", "Something unexpected just went wrong, please try again later." );
                showHome( request );
            }
        }
    }

    private void showHome( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "pageTitle", "UK Time Table" );
        req.put( "stations", TrainLocationFactory.INSTANCE.getStations() );

        request.renderTile( "timetable.home" );
    }
}
