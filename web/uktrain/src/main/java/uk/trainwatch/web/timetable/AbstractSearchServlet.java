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
import java.util.Map;
import javax.servlet.ServletException;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractSearchServlet
        extends AbstractServlet
{

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

        TrainLocation loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( station );
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
                Map<String, Object> req = request.getRequestScope();
                req.put( "pageTitle", "UK Time Table" );
                req.put( "station", loc );
                req.put( "searchDate", date );
                req.put( "schedules", ScheduleSQL.getSchedules( loc, date ) );

                request.renderTile( "timetable.search" );
            }
            catch( SQLException ex )
            {
                log( "Search " + loc + " " + date, ex );
                request.getRequestScope().
                        put( "msg", "Something unexpected just went wrong, please try again later." );
                showHome( request );
            }
        }
    }

    protected abstract void showHome( ApplicationRequest request );

}
