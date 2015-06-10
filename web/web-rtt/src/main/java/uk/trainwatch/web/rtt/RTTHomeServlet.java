/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rtt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "RTTHomeServlet", urlPatterns = {"/rtt/", "/rtt"})
public class RTTHomeServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        // Set the date range from 7 days ago to now
        Map<String, Object> req = request.getRequestScope();
        LocalDate end = LocalDate.now();
        LocalDate start = end.minus( 7, ChronoUnit.DAYS );
        req.put( "start", start );
        req.put( "end", end );

        req.put( "time", LocalTime.now().getHour() );

        request.renderTile( "rtt.home" );
    }

}
