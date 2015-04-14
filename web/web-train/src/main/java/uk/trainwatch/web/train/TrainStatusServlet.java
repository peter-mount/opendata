/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.forecast.ForecastManager;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Display the current forecast &amp; other information about a train
 * <p>
 * @author peter
 */
@WebServlet(name = "TrainStatusServlet", urlPatterns = "/train/*")
public class TrainStatusServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String rid = request.getPathInfo().substring( 1 );
        TS ts = ForecastManager.INSTANCE.get( rid );
        if( ts == null ) {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, rid );
        }
        else {
            Map<String, Object> req = request.getRequestScope();
            req.put( "pageTitle", "Train: " + rid );
            req.put( "rid", rid );
            req.put( "ts", ts );
            //req.put( "stationMessages", StationMessageManager.INSTANCE.getMessages() );
            request.renderTile( "train.info" );

        }
    }

}
