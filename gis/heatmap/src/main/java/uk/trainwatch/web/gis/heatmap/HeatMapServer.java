/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.gis.heatmap;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.trainwatch.gis.gheat.HeatMap;
import uk.trainwatch.gis.heatmap.HeatMapManager;
import uk.trainwatch.gis.heatmap.ThemeManager;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
@Path("/1/gis/heatmap")
@RequestScoped
public class HeatMapServer
{

    /**
     * Returns a JSON array of all available heat maps
     * <p>
     * @return
     */
    @GET
    @Path("/maps")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all()
    {
        JsonArrayBuilder b = Json.createArrayBuilder();
        HeatMapManager.INSTANCE.forEachName( b::add );
        return Response.ok( JsonUtils.toString.apply( b.build() ) ).
                build();
    }

    /**
     * Returns a JSON array of all available schemes
     * <p>
     * @return
     */
    @GET
    @Path("/schemes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response schemes()
    {
        JsonArrayBuilder b = Json.createArrayBuilder();
        ThemeManager.INSTANCE.forEachScheme( b::add );
        return Response.ok( JsonUtils.toString.apply( b.build() ) ).
                build();
    }

    @GET
    @Path("/{map}/{scheme}/{z}/{x}/{y}.png")
    @Produces("image/png")
    public Response tile( @PathParam("map") String map,
                          @PathParam("scheme") String scheme,
                          @PathParam("z") int z,
                          @PathParam("x") int x,
                          @PathParam("y") int y
    )
    {
        HeatMap heatMap = HeatMapManager.INSTANCE.getHeatMap( map );
        if( heatMap == null ) {
            return Response.status( Response.Status.NOT_FOUND ).
                    build();
        }

        try {
            BufferedImage tile = heatMap.getTile( scheme, z, x, y, true, 1 );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( tile, "png", baos );
            baos.flush();
            return Response.ok( baos.toByteArray() ).
                    build();
        }
        catch( Exception ex ) {
            Logger.getLogger( HeatMapServer.class.getName() ).
                    log( Level.SEVERE, null, ex );
            return Response.serverError().
                    build();
        }
    }

}
