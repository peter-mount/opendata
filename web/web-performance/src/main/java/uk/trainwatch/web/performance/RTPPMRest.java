/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.rtppm.sql.OperatorDailyPerformance;
import uk.trainwatch.nrod.rtppm.sql.OperatorManager;
import uk.trainwatch.nrod.rtppm.sql.PerformanceManager;
import uk.trainwatch.web.rest.Cache;

/**
 *
 * @author peter
 */
@Path( "/rail/1/performance/rtppm" )
@RequestScoped
public class RTPPMRest
{

    @Inject
    private OperatorManager operatorManager;

    @Inject
    private PerformanceManager performanceManager;

    @Path( "/operators" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response operators()
    {
        return Response.ok( operatorManager.getOperators() ).
                build();
    }

    @Path( "/live/{operatorId}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Cache( maxAge = 1, unit = ChronoUnit.MINUTES )
    public Response live( @PathParam( "operatorId" ) int id )
    {
        try
        {
            Optional<OperatorDailyPerformance> ppm = performanceManager.getOperatorPerformance( id, LocalDate.now() );
            if( ppm.isPresent() )
            {
                return Response.ok( ppm.get() ).
                        build();
            }
            return Response.status( Response.Status.NOT_FOUND ).
                    build();
        } catch( SQLException ex )
        {
            Logger.getLogger( RTPPMRest.class.getName() ).
                    log( Level.SEVERE, null, ex );
            return Response.serverError().
                    entity( ex ).
                    build();
        }
    }
}
