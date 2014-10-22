/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.timetable;

import in.uktra.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.sql.ScheduleResultSetFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "TTSearch", urlPatterns = "/timetable/search" )
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

                search( request, loc, date );
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

    protected final void search( ApplicationRequest request, TrainLocation station, LocalDate date )
            throws SQLException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "station", station );
        req.put( "searchDate", date );

        // Filter to today
        DayOfWeek dow = date.getDayOfWeek();

        try( Connection con = getConnection() )
        {
            try( PreparedStatement s = con.prepareStatement(
                    "SELECT s.schedule FROM timetable.schedule s"
                    + " INNER JOIN timetable.schedule_loc l ON s.id=l.scheduleid"
                    + " INNER JOIN timetable.tiploc t ON l.tiploc=t.id"
                    + " WHERE t.crs=?"
                    + " AND ? BETWEEN s.runsfrom AND s.runsto" ) )
            {
                s.setString( 1, station.getCrs() );
                TimeUtils.setDate( s, 2, date );

                // Get our schedules grouped by TrainUID
                List<Schedule> schedules = SQL.stream( s, ScheduleResultSetFactory.INSTANCE ).
                        filter( Objects::nonNull ).
                        // Filter out those that don't run today
                        filter( sh -> sh.getDaysRun().
                                isOnDay( dow ) ).
                        // Group by trainUid so we can handle overlays
                        collect( Collectors.groupingBy( sh -> sh.getTrainUid() ) ).
                        // Now for each grouped list sort so that the first one will be the active (most recent) schedule
                        values().
                        stream().
                        map( ActiveScheduleFilter.INSTANCE ).
                        collect( Collectors.toList() );

                // Sort so they are in chronological order
                Collections.sort( schedules, new ScheduleLocalTimeAtTiplocComparator( station.getTiploc() ) );

                req.put( "schedules", schedules );
            }
        }

        request.renderTile(
                "timetable.search" );
    }

    private void showHome( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "stations", TrainLocationFactory.INSTANCE.getStations() );

        request.renderTile( "timetable.home" );
    }
}
