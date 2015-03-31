/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 *
 * @author peter
 */
@Path("/rail/1/station")
public class StationRest
{

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all()
    {
        return Response.ok( TrainLocationFactory.INSTANCE.getStationStream().
                map( StationKeyValue::new ).
                collect( Collectors.toList() ) ).
                build();
    }

    @Path("full")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response full()
    {
        return Response.ok( TrainLocationFactory.INSTANCE.getStationStream().
                collect( Collectors.toList() ) ).
                build();
    }

    @Path("3alpha/{crs}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response talpha( @PathParam("crs") String crs )
    {
        return respond( TrainLocationFactory.INSTANCE.getTrainLocationByCrs( crs ) );
    }

    @Path("crs/{crs}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response crs( @PathParam("crs") String crs )
    {
        return respond( TrainLocationFactory.INSTANCE.getTrainLocationByCrs( crs ) );
    }

    @Path("nlc/{nlc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response nlc( @PathParam("nlc") String nlc )
    {
        return respond( TrainLocationFactory.INSTANCE.getTrainLocationByNlc( nlc ) );
    }

    @Path("stanox/{stanox}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response stanox( @PathParam("stanox") int stanox )
    {
        return respond( TrainLocationFactory.INSTANCE.getTrainLocationByStanox( stanox ) );
    }

    @Path("tiploc/{tiploc}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tiploc( @PathParam("tiploc") String tiploc )
    {
        return respond( TrainLocationFactory.INSTANCE.getTrainLocationByTiploc( tiploc ) );
    }

    private Response respond( TrainLocation l )
    {
        Response.ResponseBuilder r = l == null ? Response.status( Response.Status.NOT_FOUND ) : Response.ok( l );
        return r.build();
    }

}
