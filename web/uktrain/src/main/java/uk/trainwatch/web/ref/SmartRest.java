/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ref;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.nrod.smart.Smart;
import uk.trainwatch.nrod.smart.SmartBerthMovement;
import uk.trainwatch.nrod.smart.SmartManager;

/**
 *
 * @author peter
 */
@Path("/rail/1/smart")
public class SmartRest
{

    /**
     * Generates an index of all signalling areas defined in SMART
     * <p>
     * @return
     */
    @Path("/areas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response areas()
    {
        return Response.
                accepted( SmartManager.INSTANCE.getAreas() ).
                build();
    }

    /**
     * Returns a JSON array of berths within a signalling area that exist within SMART.
     * <p>
     * @param area
     *             <p>
     * @return
     */
    @Path("/berths/{area}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response berths( @PathParam("area") String area )
    {
        return Response.
                accepted( SmartManager.INSTANCE.getBerths( area ) ).
                build();
    }

    /**
     * Returns the smart entry for the specified movement. If no such movement exists then a 404 not found will be returned.
     * <p>
     * @param area
     * @param from
     * @param to
     * <p>
     * @return
     */
    @Path("/movement/{area}/{from}/{to}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response area( @PathParam("area") String area,
                          @PathParam("from") String from,
                          @PathParam("to") String to )
    {
        Smart smart = null;
        SmartBerthMovement mvt = SmartManager.INSTANCE.getSmartBerthMovement( area, from, to );
        if( mvt != null ) {
            smart = SmartManager.INSTANCE.getSmart( mvt );
        }
        if( smart == null ) {
            return Response.status( Response.Status.NOT_FOUND ).
                    build();
        }
        return Response.ok( smart ).
                build();
    }
}
