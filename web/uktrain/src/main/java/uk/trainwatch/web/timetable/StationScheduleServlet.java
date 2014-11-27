/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * Shows a schedule for a station either for todays date or a specified date
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "TTStation", urlPatterns = "/timetable/station/*")
public class StationScheduleServlet
        extends AbstractSearchServlet
{

    private static final Pattern STATION_TODAY = Pattern.compile( "^/([a-zA-Z]+)$" );
    private static final Pattern STATION_DATE = Pattern.compile( "^/([a-zA-Z]+)/([0-9]+)/([0-9]+)/([0-9]+)$" );

    /**
     * Validates the path is uppercase as locations are from a mainframe. If not then issue a redirect.
     * <p>
     * We only do this on what appears to be a valid url so if someone puts gibberish in there then they'll get a 404 as usual.
     * <p>
     * @param request
     *                <p>
     * @return
     *         <p>
     * @throws ServletException
     * @throws IOException
     */
    private boolean isValid( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String path = request.getPathInfo();
        String uPath = path.toUpperCase();
        if( !path.equals( uPath ) )
        {
            request.redirect( "/timetable/station/" + uPath );
            return false;
        }
        return true;
    }

    @Override
    protected void doSearch( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        try
        {
            String path = request.getPathInfo();

            Matcher m = STATION_TODAY.matcher( path );
            if( m.matches() )
            {
                if( isValid( request ) )
                {
                    doSearch( request, m.group( 1 ), LocalDate.now() );
                }
                return;
            }

            m = STATION_DATE.matcher( path );
            if( m.matches() )
            {
                if( isValid( request ) )
                {
                    LocalDate date = LocalDate.of( Integer.parseInt( m.group( 2 ) ),
                                                   Month.of( Integer.parseInt( m.group( 3 ) ) ),
                                                   Integer.parseInt( m.group( 4 ) ) );
                    doSearch( request, m.group( 1 ), date );
                }
                return;
            }

        }
        catch( NumberFormatException |
               DateTimeParseException ex )
        {
            // Ignore
        }

        // Respond with a 404
        request.sendError( HttpServletResponse.SC_NOT_FOUND );
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
