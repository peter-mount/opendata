/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractLDBViewServlet
        extends AbstractLDBServlet
{

    protected abstract String getServletPrefix();

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
            IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = LDBUtils.resolveLocation( request, getServletPrefix() );
        TrainLocationFactory.INSTANCE.getTrainLocationByCrs( crs );
        if( loc != null )
        {
            Map<String, Object> req = request.getRequestScope();
            req.put( "location", loc );
            req.put( "pageTitle", loc.getLocation() );
            show( request, loc );
        }
    }

}
