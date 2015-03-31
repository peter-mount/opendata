/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * Search for a schedule
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "TTSearch", urlPatterns = "/timetable/search")
public class SearchServlet
        extends AbstractSearchServlet
{

    @Override
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

        try
        {
            LocalDate date = LocalDate.parse( dateStr );
            doSearch( request, station, date );
        }
        catch( DateTimeParseException ex )
        {
            request.getRequestScope().
                    put( "msg", "Your fields are invalid, please check and try again" );
            showHome( request );
        }

    }

    @Override
    protected void showHome( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "pageTitle", "UK Time Table" );
        req.put( "stations", TrainLocationFactory.INSTANCE.getStations() );

        request.renderTile( "timetable.home" );
    }
}
