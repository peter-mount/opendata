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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.sql.ScheduleResultSetFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author Peter T Mount
 */
@WebServlet( name = "TTSchedule", urlPatterns = "/timetable/schedule/*" )
public class ScheduleServlet
        extends AbstractSearchServlet
{

    private final Pattern pattern = Pattern.compile(
            "/([A-Za-z0-9 ]+)/([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])" );

    @Override
    protected void doSearch( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String pathInfo = request.getPathInfo();

        Matcher m = pattern.matcher( pathInfo );
        if( m.matches() )
        {

            String trainUid = m.group( 1 );
            LocalDate date = LocalDate.parse( m.group( 2 ) );

            try
            {
                search( request, date, trainUid );
            }
            catch( SQLException ex )
            {
                throw new ServletException( ex );
            }
        }
        else
        {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "no match " + request.getPathInfo() );
        }
    }

    protected final void search( ApplicationRequest request, LocalDate date, String uid )
            throws SQLException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "searchDate", date );

        // Filter to today
        DayOfWeek dow = date.getDayOfWeek();

        Schedule schedule;
        try( Connection con = getConnection() )
        {
            try( PreparedStatement s = con.prepareStatement(
                    "SELECT s.schedule FROM timetable.schedule s"
                    + " INNER JOIN timetable.trainuid i ON s.trainuid=i.id"
                    + " WHERE ? BETWEEN s.runsfrom AND s.runsto"
                    + " AND i.uid=?" ) )
            {
                TimeUtils.setDate( s, 1, date );
                s.setString( 2, uid );

                List<Schedule> schedules = SQL.stream( s, ScheduleResultSetFactory.INSTANCE ).
                        filter( Objects::nonNull ).
                        filter( sh -> sh.getDaysRun().
                                isOnDay( dow ) ).
                        collect( Collectors.toList() );

                // Get the active schedule
                schedule = ActiveScheduleFilter.INSTANCE.apply( schedules );
                req.put( "schedule", schedule );
            }
        }

        if( schedule == null )
        {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "Unable to find train " + uid + " on " + date );
        }
        else
        {
            getAssociations( request, date, uid );

            request.renderTile( "timetable.schedule" );
        }
    }

    private void getAssociations( ApplicationRequest request, LocalDate date, String uid )
            throws SQLException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();

        // Filter to today
        DayOfWeek dow = date.getDayOfWeek();

        try( Connection con = getConnection() )
        {
            try( PreparedStatement s = con.prepareStatement(
                    "SELECT t1.uid as mainuid, t2.uid as assocuid, a.startdt, a.enddt, a.assocdays, a.assoccat, a.assocdateind,"
                    + " l.tiploc as tiploc,"
                    + " a.baselocsuff, a.assoclocsuff,"
                    + " a.assoctype, a.stpind"
                    + " FROM timetable.association a"
                    + " INNER JOIN timetable.trainuid t1 ON a.mainuid=t1.id"
                    + " INNER JOIN timetable.trainuid t2 ON a.assocuid=t2.id"
                    + " INNER JOIN timetable.tiploc l ON a.tiploc=l.id"
                    + " WHERE ? BETWEEN a.startdt AND a.enddt"
                    + " AND t1.uid=?" ) )
            {
                TimeUtils.setDate( s, 1, date );
                s.setString( 2, uid );

                List<Association> associations = SQL.stream( s, Association.fromSql ).
                        filter( Objects::nonNull ).
                        // Filter out those that don't apply today
                        filter( sh -> sh.getAssocDays().
                                isOnDay( dow ) ).
                        // Group by assoc uid so we can handle overlays
                        collect( Collectors.groupingBy( a -> a.getAssocTrainUID() ) ).
                        // Now for each group sort out the applicable entry
                        values().
                        stream().
                        map( AssociationFilter.INSTANCE ).
                        collect( Collectors.toList() );

                req.put( "associations", associations );
            }
        }
    }
}
