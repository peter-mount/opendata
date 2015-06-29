/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.web.rest.Cache;
import uk.trainwatch.web.rest.Rest;

/**
 *
 * @author peter
 */
@Path( "/rail/1/station" )
@RequestScoped
public class StationRest
{

    @Inject
    protected TrainLocationFactory trainLocationFactory;

    @Inject
    private DarwinReferenceManager darwinReferenceManager;

    @Path( "all" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response all()
    {
        return Response.ok( trainLocationFactory.getStationStream().
                map( StationKeyValue::new ).
                collect( Collectors.toList() ) ).
                build();
    }

    @Path( "full" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response full()
    {
        return Response.ok( trainLocationFactory.getStationStream().
                collect( Collectors.toList() ) ).
                build();
    }

    @Path( "3alpha/{crs}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response talpha( @PathParam( "crs" ) String crs )
    {
        return respond( trainLocationFactory.getTrainLocationByCrs( crs ) );
    }

    @Path( "crs/{crs}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response crs( @PathParam( "crs" ) String crs )
    {
        return respond( trainLocationFactory.getTrainLocationByCrs( crs ) );
    }

    @Path( "nlc/{nlc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response nlc( @PathParam( "nlc" ) String nlc )
    {
        return respond( trainLocationFactory.getTrainLocationByNlc( nlc ) );
    }

    @Path( "stanox/{stanox}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response stanox( @PathParam( "stanox" ) int stanox )
    {
        return respond( trainLocationFactory.getTrainLocationByStanox( stanox ) );
    }

    @Path( "tiploc/{tiploc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response tiploc( @PathParam( "tiploc" ) String tiploc )
    {
        return respond( trainLocationFactory.getTrainLocationByTiploc( tiploc ) );
    }

    private Response respond( TrainLocation l )
    {
        Response.ResponseBuilder r = l == null ? Response.status( Response.Status.NOT_FOUND ) : Response.ok( l );
        return r.build();
    }

    /**
     * Perform a search on Train station names. This can be used with the jquery-ui search component.
     * <p>
     * Note: If the term is 3 characters and a valid CRS code then that result will always be first in the result
     * <p>
     * @param term Search term
     * <p>
     * @return Response
     * <p>
     * @since 2015-06-06
     */
    @Path( "search" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 2, unit = ChronoUnit.HOURS )
    public Response search( @QueryParam( "term" ) String term )
    {
        return Rest.invoke( response -> response.entity(
                darwinReferenceManager.
                searchLocations( term ).
                map( l -> Json.createObjectBuilder().
                        add( "label", l.getLocation() + " [" + l.getCrs() + "]" ).
                        add( "value", l.getLocation() ).
                        add( "crs", l.getCrs() ) ).
                reduce( Json.createArrayBuilder(), JsonArrayBuilder::add, ( a, b ) -> a ) ).
                lastModified( Instant.now() )
        );
    }
}
