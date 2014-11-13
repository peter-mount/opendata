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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.util.AssociationCategory;

/**
 *
 * @author Peter T Mount
 */
@WebServlet( name = "TTSchedule", urlPatterns = "/timetable/schedule/*" )
public class ScheduleServlet
        extends AbstractServlet
{

    private final Pattern pattern = Pattern.compile(
            "/([A-Za-z0-9 ]+)/([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])" );

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String pathInfo = request.getPathInfo();

        Matcher m = pattern.matcher( pathInfo );
        if( !m.matches() )
        {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "no match " + request.getPathInfo() );
            return;
        }

        String trainUid = m.group( 1 );
        LocalDate date = LocalDate.parse( m.group( 2 ) );

        try
        {
            Schedule schedule = ScheduleSQL.getSchedule( date, trainUid );
            if( schedule == null )
            {
                request.sendError( HttpServletResponse.SC_NOT_FOUND,
                                   "Unable to find train " + trainUid + " on " + date );
                return;
            }

            Map<String, Object> req = request.getRequestScope();
            req.put( "searchDate", date );
            req.put( "schedule", schedule );

            // The trains associated with this one
            List<Association> associations = ScheduleSQL.getMainAssociations( date, trainUid );
            req.put( "associations", associations );
            req.put( "assocMap", ScheduleSQL.groupAssociationsByLocation( associations ) );

            // Map of the associations own schedules
            Map<Association, Schedule> schedules = ScheduleSQL.getAssociatedSchedules( date, associations );
            req.put( "assocSchedules", schedules );

            // The other 
            req.put( "otherSchedules",
                     ScheduleSQL.getMainSchedules( date, associations ) );

            // Look for a NEXT association
            req.put( "nextTrain", associations.stream().
                     filter( a -> a.getAssociationCategory() == AssociationCategory.NEXT ).
                     collect( Collectors.toList() ) );

            // Now look at what trains we are associated with
            List<Association> other = ScheduleSQL.getOtherAssociations( date, trainUid );
            req.put( "prevTrain", other.
                     stream().
                     filter( a -> a.getAssociationCategory() == AssociationCategory.NEXT
                                  || a.getAssociationCategory() == AssociationCategory.DIVIDE ).
                     collect( Collectors.toList() ) );

            // Map of the other associations own schedules
            req.put( "prevSchedules", ScheduleSQL.getAssociatedSchedules( date, other ) );

            // Look for any associations that form this service
            request.renderTile( "timetable.schedule" );
        }
        catch( SQLException ex )
        {
            throw new ServletException( ex );
        }
    }
}
