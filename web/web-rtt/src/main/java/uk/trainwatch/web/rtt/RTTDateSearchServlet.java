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
@WebServlet( name = "RTTDateSearchServlet", urlPatterns = "/rtt/search" )
public class RTTDateSearchServlet
        extends RTTHomeServlet
{

    private void error( ApplicationRequest request, String error )
            throws ServletException,
                   IOException
    {
        request.getRequestScope().put( "msg", error );
        doGet( request );
    }

    @Override
    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, String> params = request.getParam();

        String crs = params.get( "crs" );
        LocalDate date = TimeUtils.parseLocalDate( params.get( "date" ) );
        LocalTime time = LocalTime.of( Integer.parseInt( params.get( "time" ) ), 0 );
        TrainLocation loc = DarwinReferenceManager.INSTANCE.getLocationRefFromCrs( crs );

        if( loc == null || !loc.isSetCrs() )
        {
            error( request, "No station has been provided" );
        }
        else if( date == null )
        {
            error( request, "Invalid search date" );
        }
        else if( time == null )
        {
            search( request, loc, date, LocalTime.now( TimeUtils.LONDON ) );
        }
        else
        {
            search( request, loc, date, time );
        }
    }

    private void search( ApplicationRequest request, TrainLocation loc, LocalDate date, LocalTime time )
            throws ServletException,
                   IOException
    {
        LocalDateTime start = LocalDateTime.of( date, time ).truncatedTo( ChronoUnit.HOURS );
        LocalDateTime now = LocalDateTime.now( TimeUtils.LONDON );
        if( start.isBefore( now.minusDays( 7 ) ) )
        {
            error( request, "Requested date/time is too far in the past" );
        }
        else if( now.truncatedTo( ChronoUnit.DAYS ).plusDays( 1 ).isBefore( start ) )
        {
            error( request, "Requested date/time is in the future" );
        }
        else
        {
            search( request, loc, start, now );
        }
    }

    private void search( ApplicationRequest request, TrainLocation loc, LocalDateTime start, LocalDateTime now )
            throws ServletException,
                   IOException
    {
        try
        {
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
            if( back.isAfter( now.minusDays( 7 ) ) )
            {
                req.put( "back", back );
            }

            if( !current )
            {
                req.put( "next", start.plusHours( 1 ) );
            }

            request.renderTile( "rtt.search" );
        } catch( SQLException ex )
        {
            log( "Failed to search \"" + loc.getCrs() + "\" for " + start, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }
}
