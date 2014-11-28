/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.kml;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.xml.stream.XMLStreamException;
import uk.trainwatch.gis.StationPositionManager;
import uk.trainwatch.util.xml.XMLSaxWriter;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Servlet which exports the Station location database in either a human readable format (similar to the station index) or as a KML file.
 * <p>
 * @author peter
 */
@WebServlet(name = "StationLocationKmlServlet", urlPatterns = "/data/gis/stationPosition/all.kml")
public class StationLocationKmlServlet
        extends AbstractServlet
{
    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        try
        {
            try( XMLSaxWriter w = new XMLSaxWriter( request.getWriter(), new KMLWriter( "station_point" ) ) )
            {
                StationPositionManager.INSTANCE.forEach( w );
            }
        }
        catch( XMLStreamException |
               SQLException ex )
        {
            log( "eek", ex);
            Logger.getLogger( StationLocationKmlServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
    }

}
