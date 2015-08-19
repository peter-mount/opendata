/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.ldb.cache.SearchDepartureKey;
import uk.trainwatch.web.ldb.cache.SearchDeparturesCache;
import uk.trainwatch.web.ldb.model.SearchResult;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.timetable.TimeTableSearch;
import uk.trainwatch.web.timetable.TimeTableSearchResult;

/**
 *
 * @author Peter T Mount
 */
@WebServlet(name = "UKTHomeServlet", urlPatterns = "/search")
public class UKTHomeServlet
        extends AbstractServlet
{

    @Inject
    private DarwinReferenceManager darwinReferenceManager;

    @Inject
    protected TrainLocationFactory trainLocationFactory;

    @Inject
    private TimeTableSearch timeTableSearch;

    @Inject
    private SearchDeparturesCache searchDeparturesCache;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        commonFields( request );
        request.renderTile( "ukt.search" );
    }

    private void commonFields( ApplicationRequest request )
    {
        Map<String, Object> req = request.getRequestScope();

        LocalDateTime now = LocalDateTime.now( TimeUtils.LONDON );
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();
        req.put( "date", nowDate );
        req.put( "nowTime", nowTime );

        // Set the date range from 2 months ago to 1 year ahead
        LocalDate start = nowDate.minus( 2, ChronoUnit.MONTHS ).withDayOfMonth( 1 );
        LocalDate end = nowDate.plus( 1, ChronoUnit.YEARS ).minus( 1, ChronoUnit.DAYS );
        req.put( "start", start );
        req.put( "end", end );

        // The hour of the day, so we can limit views to the current hour
        req.put( "time", nowTime.getHour() );
    }

    @Override
    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        Map<String, String> param = request.getParam();
        commonFields( request );

        String station = param.get( "crs" );
        String dateStr = param.get( "date" );
        String timeStr = param.get( "time" );
        req.put( "crs", station );
        req.put( "date", dateStr );
        req.put( "time", timeStr );

        if( station == null || station.isEmpty()
            || dateStr == null || dateStr.isEmpty()
            || timeStr == null || timeStr.isEmpty() ) {
            req.put( "msg", "Your fields are invalid, please check and try again" );
        }
        else {
            try {
                int hour = Integer.parseInt( timeStr );
                LocalDate now = LocalDate.now();
                LocalDate date = LocalDate.parse( dateStr );

                if( date.isAfter( now ) ) {
                    LocalTime timeStart = LocalTime.of( hour, 0 ).minus( 1, ChronoUnit.MINUTES );
                    show( request, timeStart, timeTableSearch.search( station, date ) );
                }
                else {
                    LocalDateTime dateTime = date.atTime( hour, 0 );
                    show( request, dateTime, station, searchDeparturesCache.search( new SearchDepartureKey( station, dateTime ) ) );
                }
                return;
            }
            catch( SQLException |
                   NumberFormatException |
                   DateTimeParseException ex ) {
                req.put( "msg", "Your fields are invalid, please check and try again" );
            }
        }

        // Show the search page
        request.renderTile( "ukt.search" );
    }

    /**
     * Show the Darwin PushPort data for this station.
     * <p>
     * Later: Add NR data as well
     * <p>
     * @param request
     * @param result
     */
    private void show( ApplicationRequest request, LocalDateTime start, String crs, Collection<SearchResult> results )
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "trains", results );

        LocalDateTime now = LocalDateTime.now( TimeUtils.LONDON );
        // Cache control, expire in 1m if today otherwise in 1 hour
        boolean today = now.toLocalDate().equals( start.toLocalDate() );
        ChronoUnit unit = today ? ChronoUnit.MINUTES : ChronoUnit.HOURS;
        request.expiresIn( 1, unit );
        request.maxAge( 1, unit );

        // Last modified is either now if it is this hour or when this hour finished
        boolean current = today & now.truncatedTo( ChronoUnit.HOURS ).equals( start );
        request.lastModified( current ? now : start.plusHours( 1 ) );

        TrainLocation loc = darwinReferenceManager.getLocationRefFromCrs( crs );
        req.put( "location", loc );
        req.put( "start", start );
        req.put( "startDate", TimeUtils.toDate( start.toLocalDate() ) );

        LocalDateTime back = start.minusHours( 1 );
        if( back.isAfter( now.minusMonths( 2 ).withDayOfMonth( 1 ).minusDays( 1 ) ) ) {
            req.put( "back", back );
        }

        // Allow up to 2am tomorrow
        LocalDateTime next = start.plusHours( 1 );
        if( next.isBefore( now.truncatedTo( ChronoUnit.DAYS ).plusDays( 1 ).plusHours( 2 ).minusSeconds( 1 ) ) ) {
            req.put( "next", start.plusHours( 1 ) );
        }
        request.renderTile( "ukt.train.search" );
    }

    /**
     * Show the NR Timetable for the hour period shown
     * <p>
     * @param request
     * @param timeStart
     * @param result
     */
    private void show( ApplicationRequest request, LocalTime timeStart, TimeTableSearchResult result )
    {
        LocalTime timeEnd = timeStart.plus( 1, ChronoUnit.HOURS ).plus( 2, ChronoUnit.MINUTES );
        Predicate<LocalTime> filter = timeEnd.isAfter( timeStart )
                                      ? s -> s.isAfter( timeStart ) && s.isBefore( timeEnd )
                                      : s -> s.isAfter( timeStart ) || s.isBefore( timeEnd );

        Map<String, Object> req = request.getRequestScope();
        req.put( "pageTitle", "UK Time Table" );
        req.put( "station", result.getStation() );
        req.put( "searchDate", result.getSearchDate() );

        // For this page limit to the current hour
        Tiploc tiploc = new Tiploc( result.getStation().getTiploc() );
        req.put( "schedules", result.getSchedules().
                 stream().
                 filter( s -> filter.test( s.getLocation( tiploc ).getTime() ) ).
                 collect( Collectors.toList() )
        );

        request.renderTile( "ukt.timetable.search" );
    }
}
