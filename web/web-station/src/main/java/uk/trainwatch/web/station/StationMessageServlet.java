/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * A Debug servlet (may become a real one) to show all current Station Messages on the system
 *
 * @author peter
 */
@WebServlet( name = "StationMessageServlet", urlPatterns = "/stationMessages" )
public class StationMessageServlet
        extends AbstractServlet
{

    @Inject
    private StationMessageManager stationMessageManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.getRequestScope().put( "stationMessages", stationMessageManager.getMessages() );
        request.renderTile( "station.messages" );
    }

}
