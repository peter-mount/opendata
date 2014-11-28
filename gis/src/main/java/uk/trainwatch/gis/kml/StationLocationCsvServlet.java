/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.kml;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.gis.StationPositionManager;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Servlet which exports the Station location database in either a human readable format (similar to the station index) or as a KML file.
 * <p>
 * @author peter
 */
@WebServlet(name = "StationLocationCsvServlet", urlPatterns = "/data/gis/stationPosition/all.csv")
public class StationLocationCsvServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        try
        {
            PrintWriter w = request.getPrintWriter();
            w.println( "\"id\",\"name\",\"longitude\",\"latitude\",\"tiploc\"" );
            StationPositionManager.INSTANCE.forEach( p -> w.printf( "%d,\"%s\",%.6f,%.6f,\"%s\"\n",
                                                                    p.get( "id" ),
                                                                    p.getName(),
                                                                    p.getLongitude(),
                                                                    p.getLatitude(),
                                                                    p.get( "tiploc" )
            ) );
        }
        catch( SQLException ex )
        {
            Logger.getLogger( StationLocationCsvServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
    }

}
