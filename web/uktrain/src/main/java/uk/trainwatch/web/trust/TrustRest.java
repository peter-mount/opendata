/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.nrod.trust.model.TrainActivation;
import uk.trainwatch.nrod.trust.model.TrainCancellation;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
@Path("/rail/1/trust/dashboard")
public class TrustRest
{

    private static final Logger LOG = Logger.getLogger( TrustRest.class.getName() );

    @Path("/details/{toc}/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response details( @PathParam("toc") int toc, @PathParam("id") String id )
    {
        return getResponse( TrustStreams.details( toc, id ) );
    }

    @Path("/activations/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response activations( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.activations( toc ) );
    }

    @Path("/movements/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response running( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.movements( toc ) );
    }

    @Path("/cancellations/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancellations( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.cancellations( toc ) );
    }

    @Path("/delays/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response delays( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.delays( toc ) );
    }

    @Path("/offroute/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response offroute( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.offroute( toc ) );
    }

    @Path("/terminated/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response terminated( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.terminated( toc ) );
    }

    @Path("/issues/{toc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response issues( @PathParam("toc") int toc )
    {
        return getResponse( TrustStreams.issues( toc ) );
    }

    /*
     @Path( "/current/{toc}" )
     @GET
     @Produces( MediaType.APPLICATION_JSON )
     public Response current( @PathParam( "toc" ) int toc )
     {
     return getResponse( TrustCache.INSTANCE.getTrains( toc ).stream() );
     }

     @Path( "/delay/low/{toc}" )
     @GET
     @Produces( MediaType.APPLICATION_JSON )
     public Response delayLow( @PathParam( "toc" ) int toc )
     {
     return getResponse( TrustCache.INSTANCE.getTrains( toc ).stream().
     filter( Trust::hasMovement ).
     sorted( ( a, b ) -> Long.compare( a.getDelay(), b.getDelay() ) )
     );
     }

     @Path( "/delay/high/{toc}" )
     @GET
     @Produces( MediaType.APPLICATION_JSON )
     public Response delayHigh( @PathParam( "toc" ) int toc )
     {
     return getResponse( TrustCache.INSTANCE.getTrains( toc ).stream().
     filter( Trust::hasMovement ).
     sorted( ( a, b ) -> -Long.compare( a.getDelay(), b.getDelay() ) )
     );
     }
     */
    private Response getResponse( Stream<Trust> trains )
    {
        JsonArrayBuilder a = Json.createArrayBuilder();
        trains.forEach( t -> add( a, t ) );
        CacheControl cache = new CacheControl();
        cache.setMaxAge( 60 );
        cache.setSMaxAge( 60 );
        return Response.ok( a.build() ).
                cacheControl( cache ).
                build();
    }

    private void add( JsonArrayBuilder a, Trust t )
    {
        JsonObjectBuilder o = Json.createObjectBuilder().
                add( "id", t.getId() ).
                add( "toc", t.getToc() ).
                add( "status", t.getStatus().
                     toString() );

        if( t.getActivation() != null ) {
            TrainActivation v = t.getActivation();
            o.add( "activation", Json.createObjectBuilder().
                   add( "id", v.getSchedule_wtt_id() ).
                   add( "type", v.getSchedule_type() ).
                   add( "time", TimeUtils.toJson( v.getOrigin_dep_timestamp() ) ).
                   add( "location", TrainLocationFactory.getJsonByStanox( v.getSched_origin_stanox() ) ) );
        }
        if( t.getCancellation() != null ) {
            TrainCancellation v = t.getCancellation();
            o.add( "cancellation", Json.createObjectBuilder().
                   add( "type", v.getCanx_type() ).
                   add( "reason", v.getCanx_reason_code() ).
                   add( "time", TimeUtils.toJson( v.getCanx_timestamp() ) ).
                   add( "location", TrainLocationFactory.getJsonByStanox( v.getLoc_stanox() ) ) );
        }
        if( t.getMovement() != null ) {
            TrainMovement v = t.getMovement();
            o.add( "movement", Json.createObjectBuilder().
                   add( "delay", v.getDelay() ).
                   add( "location", TrainLocationFactory.getJsonByStanox( v.getLoc_stanox() ) ).
                   add( "time", TimeUtils.toJson( v.getActual_timestamp() ) ).
                   add( "offroute", v.isOffroute_ind() ).
                   add( "terminated", v.isTrain_terminated() )
            );
        }
        a.add( o );
    }
}
