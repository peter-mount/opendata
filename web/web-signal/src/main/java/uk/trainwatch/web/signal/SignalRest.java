/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.td.berth.BerthMap;
import uk.trainwatch.util.sql.KeyValue;
import uk.trainwatch.web.rest.Cache;
import uk.trainwatch.web.rest.Rest;

/**
 *
 * @author peter
 */
@Path("/rail/1/signal")
@RequestScoped
public class SignalRest
{

    @Inject
    private SignalManager signalManager;

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
    @Cache(maxAge = 10, expires = 10, unit = ChronoUnit.SECONDS)
    public Response occupied( @PathParam("area") String area )
    {
        return Rest.invoke( response -> {
            Optional<SignalArea> signalArea = Optional.empty();
            if( area != null ) {
                signalArea = signalManager.getSignalArea( area );
            }

            if( signalArea.isPresent() ) {
                BerthMap map = signalManager.getArea( area );
                if( map != null ) {
                    response.entity( map.stream().
                            collect( Collectors.toMap( KeyValue::getKey, KeyValue::getValue ) ) ).
                            lastModified( Instant.ofEpochMilli( map.getLastUpdate() ) );
                }
            }
        } );
    }
}
