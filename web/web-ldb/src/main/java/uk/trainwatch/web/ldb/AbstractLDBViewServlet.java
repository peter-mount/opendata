/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractLDBViewServlet
        extends AbstractLDBServlet
{

    protected abstract String getServletPrefix();

    @Inject
    private StationMessageManager stationMessageManager;
    
    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = lDBUtils.resolveLocation( request, getServletPrefix() );
        if( loc != null ) {
            Map<String, Object> req = request.getRequestScope();
            req.put( "location", loc );
            req.put( "pageTitle", loc.getLocation() );
            req.put( "stationMessages", stationMessageManager.getMessages( crs ) );
            show( request, loc );
        }
    }

}
