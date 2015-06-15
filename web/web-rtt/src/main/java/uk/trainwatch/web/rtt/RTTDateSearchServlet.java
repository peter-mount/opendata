/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rtt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.ldb.LDBUtils;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "RTTDateSearchServlet", urlPatterns = "/rtt/trains/*")
public class RTTDateSearchServlet
        extends RTTHomeServlet
{

    private static final Pattern PATTERN = Pattern.compile( "/([A-Z][A-Z][A-Z])/([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])/([0-9][0-9])" );

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Matcher m = PATTERN.matcher( request.getPathInfo() );
        if( m.matches() ) {
            doSearch( request, m.group( 1 ), m.group( 2 ), m.group( 3 ) );
        }
        else {
            request.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
    }

    private void doSearch( ApplicationRequest request, String crs, String sDate, String sHour )
            throws ServletException,
                   IOException
    {
        LocalDate date = TimeUtils.parseLocalDate( sDate );
        LocalTime time = LocalTime.of( Integer.parseInt( sHour ), 0 );
        TrainLocation loc = DarwinReferenceManager.INSTANCE.getLocationRefFromCrs( crs );

        if( loc == null || !loc.isSetCrs() || date == null || time == null ) {
            request.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
        else {
            search( request, loc, date, time );
        }
    }

    private void search( ApplicationRequest request, TrainLocation loc, LocalDate date, LocalTime time )
            throws ServletException,
                   IOException
    {
        LocalDateTime start = LocalDateTime.of( date, time ).truncatedTo( ChronoUnit.HOURS );
        LocalDateTime now = LocalDateTime.now( TimeUtils.LONDON );
        if( start.isBefore( now.minusDays( 7 ) ) ) {
            request.sendError( HttpServletResponse.SC_GONE, "Requested date/time is too far in the past" );
        }
        else if( now.truncatedTo( ChronoUnit.DAYS ).plusDays( 1 ).isBefore( start ) ) {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, "Requested date/time is in the future" );
        }
        else {
            search( request, loc, start, now );
        }
    }

    private void search( ApplicationRequest request, TrainLocation loc, LocalDateTime start, LocalDateTime now )
            throws ServletException,
                   IOException
    {
        try {
            Map<String, Object> req = request.getRequestScope();
            req.put( "trains", LDBUtils.search( start, loc.getCrs() ) );

            // Cache control, expire in 1m if today otherwise in 1 hour
            boolean today = now.toLocalDate().equals( start.toLocalDate() );
            ChronoUnit unit = today ? ChronoUnit.MINUTES : ChronoUnit.HOURS;
            request.expiresIn( 1, unit );
            request.maxAge( 1, unit );

            // Last modified is either now if it is this hour or when this hour finished
            boolean current = today & now.truncatedTo( ChronoUnit.HOURS ).equals( start );
            request.lastModified( current ? now : start.plusHours( 1 ) );

            req.put( "location", loc );
            req.put( "start", start );

            LocalDateTime back = start.minusHours( 1 );
            if( back.isAfter( now.minusDays( 7 ) ) ) {
                req.put( "back", back );
            }

            // Allow up to 2am tomorrow
            LocalDateTime next = start.plusHours( 1 );
            if( next.isBefore( now.truncatedTo( ChronoUnit.DAYS ).plusDays( 1 ).plusHours( 2 ).minusSeconds( 1 ) ) ) {
                req.put( "next", start.plusHours( 1 ) );
            }

            request.renderTile( "rtt.search" );
        }
        catch( SQLException ex ) {
            log( "Failed to search \"" + loc.getCrs() + "\" for " + start, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }
}
