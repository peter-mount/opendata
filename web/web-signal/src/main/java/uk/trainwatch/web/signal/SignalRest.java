/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.td.berth.BerthMap;
import uk.trainwatch.util.sql.KeyValue;

/**
 *
 * @author peter
 */
@Path("/rail/1/signal")
public class SignalRest
{

    private static final Logger LOG = Logger.getLogger( SignalRest.class.getName() );

    /**
     * Returns a JSON Object defining the signal berths within a signalling area that is currently occupied.
     * <p>
     * @param area
     *             <p>
     * @return
     */
    @Path("/occupied/{area}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response occupied( @PathParam("area") String area )
    {
        try {
            Optional<SignalArea> signalArea = Optional.empty();
            if( area != null ) {
                signalArea = SignalManager.INSTANCE.getSignalArea( area );
            }

            if( signalArea.isPresent() ) {
                BerthMap map = SignalManager.INSTANCE.getArea( area );
                if( map != null ) {
                    return Response.ok( map.stream().
                            collect( Collectors.toMap( KeyValue::getKey, KeyValue::getValue ) ) ).
                            build();
                }
            }
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, ex, () -> "Signal area " + area );
            return Response.serverError().
                    entity( ex ).
                    build();
        }

        return Response.status( Response.Status.NOT_FOUND ).
                build();
    }
}
