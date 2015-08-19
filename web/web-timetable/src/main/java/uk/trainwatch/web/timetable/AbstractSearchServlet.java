/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractSearchServlet
        extends AbstractServlet
{

    @Inject
    protected TimeTableSearch timeTableSearch;

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

    protected abstract void doSearch( ApplicationRequest request )
            throws ServletException,
                   IOException;

    protected void doSearch( ApplicationRequest request, String station, LocalDate date )
            throws ServletException,
                   IOException
    {
        TimeTableSearchResult result = timeTableSearch.search( station, date );
        if( result.isError() ) {
            request.getRequestScope().put( "msg", result.getError() );
            showHome( request );
        }
        else {
            Map<String, Object> req = request.getRequestScope();
            req.put( "pageTitle", "UK Time Table" );
            req.put( "station", result.getStation() );
            req.put( "searchDate", result.getSearchDate() );
            req.put( "schedules", result.getSchedules() );

            request.renderTile( "timetable.search" );
        }
    }

    protected abstract void showHome( ApplicationRequest request );

}
