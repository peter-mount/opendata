/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.rtppm.sql.OperatorManager;
import uk.trainwatch.nrod.rtppm.sql.PerformanceManager;
import uk.trainwatch.util.MinMaxStatistics;
import uk.trainwatch.util.TimeUtils;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "PPMHistoryServlet", urlPatterns = {
    "/performance/ppm", "/performance/ppm/*"
})
public class PPMHistoryServlet
        extends AbstractServlet
{

    private static final LocalTime DAY_START = LocalTime.of( 2, 0 );

    @Inject
    private PerformanceManager performanceManager;

    @Inject
    private OperatorManager operatorManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String path = request.getPathInfo();

        if( path == null || path.trim().isEmpty() ) {
            show( request, LocalDate.now(), true );
        }
        else {
            // Autorefresh disabled
            request.getRequestScope().put( "refresh", false );

            // This will always be >1 as it leads with a /
            String comp[] = path.split( "/" );
            switch( comp.length ) {
                case 0:
                    showYears( request );
                    break;
                case 2:
                    show( request, Integer.parseInt( comp[1] ) );
                    break;
                case 3:
                    show( request, Integer.parseInt( comp[1] ), Integer.parseInt( comp[2] ) );
                    break;
                // year/month/day
                case 4:
                    show( request, Integer.parseInt( comp[1] ), Integer.parseInt( comp[2] ), Integer.parseInt( comp[3] ) );
                    break;
                default:
                    request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
        }
    }

    private void showYears( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        try {
            Map<String, Object> req = request.getRequestScope();
            req.put( "years", performanceManager.getYears() );
            request.renderTile( "performance.ppm.years" );
        }
        catch( NoSuchElementException |
               SQLException ex ) {
            Logger.getLogger( PPMHistoryServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

    private void show( ApplicationRequest request, int year )
            throws ServletException,
                   IOException
    {
        try {
            MinMaxStatistics stat = performanceManager.getMonths( year );
            if( stat.getMin() == 0 && stat.getMax() == 0 ) {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else {
                Map<String, Object> req = request.getRequestScope();
                req.put( "year", year );
                req.put( "months", stat );
                request.renderTile( "performance.ppm.months" );
            }
        }
        catch( NoSuchElementException |
               SQLException ex ) {
            Logger.getLogger( PPMHistoryServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

    private LocalDate addCalendar( Map<String, Object> req, LocalDate date, MinMaxStatistics stat )
    {
        LocalDate startDate = date.withDayOfMonth( 1 );
        setDate( req, startDate );

        req.put( "prevMonth", startDate.minusMonths( 1 ) );
        req.put( "nextMonth", startDate.plusMonths( 1 ) );

        // Setup the calendar
        req.put( "dow", DayOfWeek.values() );
        // Day names
        req.put( "down", Stream.of( DayOfWeek.values() ).
                 map( d -> d.getDisplayName( TextStyle.SHORT, Locale.ENGLISH ) ).
                 collect( Collectors.toList() ) );

        // Calendar
        LocalDate dateEnd = startDate.plusMonths( 1 ).minusDays( 1 );
        int end = dateEnd.getDayOfMonth();

        List<LocalDate> days = new ArrayList<>();
        for( int i = 1; i <= end; i++ ) {
            days.add( LocalDate.of( startDate.getYear(), startDate.getMonth(), i ) );
        }
        req.put( "calendar", days );

        req.put( "days", stat );

        return dateEnd;
    }

    private void show( ApplicationRequest request, int year, int month )
            throws ServletException,
                   IOException
    {
        try {
            // Unlike other pages we'll allow date of 0 as we'll show just the calendar
            MinMaxStatistics stat = performanceManager.getDays( year, month );

            Map<String, Object> req = request.getRequestScope();
            LocalDate startDate = LocalDate.of( year, month, 1 );
            LocalDate dateEnd = addCalendar( req, startDate, stat );

            // Needed for the graphs - only valid if we have data
            if( stat.getMin() != 0 && stat.getMax() != 0 ) {
                req.put( "operators", operatorManager.getOperators() );
                req.put( "monthppm", performanceManager.getMonthsDailyPPM( year, month ) );
                req.put( "startDate", TimeUtils.getLocalDateTime( startDate ).
                         toInstant( ZoneOffset.UTC ).
                         toEpochMilli() );
                req.put( "endDate", TimeUtils.getLocalDateTime( dateEnd ).
                         toInstant( ZoneOffset.UTC ).
                         toEpochMilli() );
            }

            request.renderTile( "performance.ppm.days" );
        }
        catch( NoSuchElementException |
               SQLException ex ) {
            Logger.getLogger( PPMHistoryServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

    private void show( ApplicationRequest request, int year, int month, int day )
            throws ServletException,
                   IOException
    {
        show( request, LocalDate.of( year, month, day ), false );
    }

    private void show( ApplicationRequest request, LocalDate date, boolean refresh )
            throws ServletException,
                   IOException
    {
        try {
            Map<String, Object> req = request.getRequestScope();

            addCalendar( req, date, performanceManager.getDays( date.getYear(), date.getMonthValue() ) );

            setDate( req, date );

            req.put( "operators", operatorManager.getOperatorMap() );

            req.put( "perfdate", LocalDate.now( ZoneId.of( "Europe/London" ) ) );

            // Ensure we have the correct cache headers
            req.put( "refresh", refresh );
            if( refresh ) {
                req.put( "performance", performanceManager.getCurrentPerformance() );
                // Cache for only 1 minute
                request.expiresIn( 1, ChronoUnit.MINUTES );
                request.lastModified( Instant.now() );
                request.maxAge( 1, ChronoUnit.MINUTES );
            }
            else {
                req.put( "performance", performanceManager.getPerformance( date ) );
                // Cache for a day
                request.expiresIn( 1, ChronoUnit.DAYS );
                request.maxAge( 1, ChronoUnit.DAYS );

                // Last modified becomes the end of the rail day which is 0200 the following day
                request.lastModified( LocalDateTime.of( date.plus( 1, ChronoUnit.DAYS ), DAY_START ) );
            }

            request.renderTile( "performance.ppm.view" );
        }
        catch( NoSuchElementException |
               SQLException ex ) {
            Logger.getLogger( PPMHistoryServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

    private void setDate( Map<String, Object> req, LocalDate date )
    {
        req.put( "date", date );
        req.put( "day", date.getDayOfWeek().
                 getDisplayName( TextStyle.FULL, Locale.ENGLISH ) );
        req.put( "month", date.getMonth().
                 getDisplayName( TextStyle.FULL, Locale.ENGLISH ) );
    }
}
