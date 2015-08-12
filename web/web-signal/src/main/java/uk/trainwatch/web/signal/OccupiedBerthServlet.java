/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import uk.trainwatch.nrod.td.model.BerthCancel;
import uk.trainwatch.nrod.td.model.BerthInterpose;
import uk.trainwatch.nrod.td.model.BerthStep;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Simple servlet that returns a JSON Object who's keys are berth names and values are the occupying head codes.
 * <p>
 * @author peter
 */
@WebServlet(name = "CurrentSignalData", urlPatterns = "/signal/data/occupiedBerths/*")
public class OccupiedBerthServlet
        extends AbstractServlet
{

    private static final Logger LOG = Logger.getLogger( SignalServlet.class.getName() );

    @Inject
    private SignalManager signalManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String pathInfo = request.getPathInfo();
        try {
            String area = "";
            Optional<SignalArea> signalArea = Optional.empty();

            if( pathInfo != null && !pathInfo.isEmpty() ) {
                area = pathInfo.substring( 1 ).
                        toUpperCase();
                signalArea = signalManager.getSignalArea( area );
            }

            if( signalArea.isPresent() ) {
                JsonObjectBuilder berths = Json.createObjectBuilder();
                signalManager.getArea( area ).
                        forEach( ( b, n ) -> berths.add( b, n ) );

                JsonArrayBuilder recent = Json.createArrayBuilder();
                signalManager.getRecent( area ).
                        forEach( t -> {
                            LocalTime time = TimeUtils.getLocalDateTime( t.getTime() ).
                            toLocalTime();

                            JsonObjectBuilder ob = Json.createObjectBuilder().
                            add( "type", t.getMsg_type().
                                 getType() ).
                            add( "time", time.toString() );

                            switch( t.getMsg_type() ) {
                                case CA: {
                                    BerthStep bs = (BerthStep) t;
                                    recent.add( ob.
                                            add( "descr", bs.getDescr() ).
                                            add( "from", bs.getFrom() ).
                                            add( "to", bs.getTo() )
                                    );
                                    break;
                                }
                                case CB: {
                                    BerthCancel bs = (BerthCancel) t;
                                    recent.add( ob.
                                            add( "descr", bs.getDescr() ).
                                            add( "from", bs.getFrom() )
                                    );
                                    break;
                                }
                                case CC: {
                                    BerthInterpose bs = (BerthInterpose) t;
                                    recent.add( ob.
                                            add( "descr", bs.getDescr() ).
                                            add( "to", bs.getTo() )
                                    );
                                    break;
                                }
                            }
                        } );

                request.expiresIn( 10, ChronoUnit.SECONDS );
                request.maxAge( 10, ChronoUnit.SECONDS );
                request.lastModified( Instant.now() );

                request.getResponseHeaders().
                        setValue( "Content-Type", MediaType.APPLICATION_JSON );
                Writer w = request.getWriter();
                w.write( JsonUtils.toString.apply(
                        Json.createObjectBuilder().
                        add( "berths", berths ).
                        add( "recent", recent ).
                        build()
                ) );
                w.flush();
            }
            else {
                request.sendError( HttpServletResponse.SC_NOT_FOUND, "Unknown signaling area " + area );
            }
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, ex, () -> "Failed for " + pathInfo );
        }
    }

}
