/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "StationHome", urlPatterns = "/station/")
public class HomeServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String index = request.getParam().
                getOrDefault( "s", "A" ).
                substring( 0, 1 ).
                toUpperCase();

        Map<String, Object> req = request.getRequestScope();
        req.put( "selected", index );
        req.put( "index", TrainLocationFactory.INSTANCE.getStationIndex() );
        req.put( "stations", TrainLocationFactory.INSTANCE.getStationIndex( index ) );

        request.renderTile( "station.home" );
    }

}
