/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.gis.StationPosition;
import uk.trainwatch.gis.StationPositionManager;
import uk.trainwatch.nre.darwin.stationmsg.StationMessageManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "StationServlet", urlPatterns = "/station/*")
public class StationServlet
        extends AbstractServlet
{

    @Inject
    protected TrainLocationFactory trainLocationFactory;

    @Inject
    private StationMessageManager stationMessageManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = trainLocationFactory.getTrainLocationByCrs( crs );
        if( loc == null ) {
            // See if they have used an alternate code
            loc = trainLocationFactory.resolveTrainLocation( crs );

            if( loc == null ) {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            else {
                // Redirect to the correct page
                request.getResponse().
                        sendRedirect( "/station/" + loc.getCrs() );
            }
        }
        else {
            show( request, loc );
        }
    }

    private void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "location", loc );
        req.put( "pageTitle", loc.getLocation() );
        try {
            getMessages( req, loc );
            showMap( req, loc );

            request.renderTile( "station.info" );
        }
        catch( SQLException ex ) {
            log( "show " + loc, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

    /**
     * Station Messages
     * <p>
     * @param req
     * @param loc <p>
     * @throws SQLException
     */
    private void getMessages( Map<String, Object> req, TrainLocation loc )
            throws SQLException
    {
        req.put( "stationMessages", stationMessageManager.getMessages( loc.getCrs() ) );
    }

    /**
     * is it possible to display a map? Not everywhere is in the db (some with wrong names)
     * <p>
     * @param req
     * @param loc <p>
     * @throws ServletException
     * @throws IOException
     */
    private void showMap( Map<String, Object> req, TrainLocation loc )
            throws SQLException
    {

        List<StationPosition> stationPosition = StationPositionManager.INSTANCE.findCrs( loc.getCrs() );
        if( !stationPosition.isEmpty() ) {
            StationPosition station = stationPosition.get( 0 );
            req.put( "stationPosition", station );
            req.put( "nearBy", StationPositionManager.INSTANCE.nearby( station, 3 ) );
        }
    }
}
