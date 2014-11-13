/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "TTHome", urlPatterns = "/timetable/" )
public class HomeServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();

        req.put( "stations", TrainLocationFactory.INSTANCE.getStations() );

        request.renderTile( "timetable.home" );
    }

}
