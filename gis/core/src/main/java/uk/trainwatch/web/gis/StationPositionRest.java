/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.gis;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.enterprise.context.RequestScoped;
import javax.json.JsonArrayBuilder;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.xml.stream.XMLStreamException;
//import uk.trainwatch.gis.StationPosition;
//import uk.trainwatch.gis.StationPositionManager;
//import uk.trainwatch.gis.kml.KMLWriter;
//import uk.trainwatch.util.JsonUtils;
//import uk.trainwatch.util.xml.XMLSaxWriter;

/**
 *
 * @author peter
 */
//@Path("/gis/1/stationPosition")
//@RequestScoped
public class StationPositionRest
{
//
//    private static final Logger LOG = Logger.getLogger( StationPositionRest.class.getName() );
//
//    private static Response ok( JsonArrayBuilder b )
//    {
//        return ok( b.build() );
//    }
//
//    private static Response ok( JsonStructure s )
//    {
//        return Response.
//                ok( JsonUtils.toString.apply( s ) ).
//                build();
//    }
//
//    private static Response error( Supplier<String> s, Exception ex )
//    {
//        LOG.log( Level.SEVERE, ex, s );
//        return Response.
//                status( Response.Status.INTERNAL_SERVER_ERROR ).
//                entity( ex ).
//                build();
//    }
//
//    private static Response notFound()
//    {
//        return Response.
//                status( Response.Status.NOT_FOUND ).
//                build();
//    }
//
//    private static Response respond( List<StationPosition> l, Function<StationPosition, JsonObjectBuilder> func, boolean failWhenNoResults )
//    {
//        if( failWhenNoResults && l.isEmpty() ) {
//            return notFound();
//        }
//
//        JsonArrayBuilder b = Json.createArrayBuilder();
//        l.forEach( s -> b.add( func.apply( s ) ) );
//
//        return ok( b );
//    }
//
//    /**
//     * Perform a lookup by TIPLOC
//     * <p>
//     * @param tiploc
//     *               <p>
//     * @return
//     */
//    @Path("tiploc/{tiploc}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response tiploc( @PathParam("tiploc") String tiploc )
//    {
//        try {
//            return respond( StationPositionManager.INSTANCE.findTiploc( tiploc ), StationPosition.toJsonObject, true );
//        }
//        catch( SQLException ex ) {
//            return error( () -> "retrieving tiploc " + tiploc, ex );
//        }
//    }
//
//    /**
//     * Perform a lookup by Station name
//     * <p>
//     * @param name
//     *             <p>
//     * @return
//     */
//    @Path("name/{name}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response name( @PathParam("name") String name )
//    {
//        try {
//            return respond( StationPositionManager.INSTANCE.find( name ), StationPosition.toJsonObject, true );
//        }
//        catch( SQLException ex ) {
//            return error( () -> "retrieving name " + name, ex );
//        }
//    }
//
//    /**
//     * Return all stations within 5 miles of a specific location
//     * <p>
//     * @param longitude
//     * @param latitude
//     *                  <p>
//     * @return
//     */
//    @Path("nearby/{longitude}/{latitude}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response nearby1( @PathParam("longitude") double longitude,
//                             @PathParam("latitude") double latitude )
//    {
//        return nearby( longitude, latitude, 5 );
//    }
//
//    /**
//     * Return all stations within range miles of a specific location
//     * <p>
//     * @param longitude
//     * @param latitude
//     * @param range
//     *                  <p>
//     * @return
//     */
//    @Path("nearby/{longitude}/{latitude}/{range}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response nearby( @PathParam("longitude") double longitude,
//                            @PathParam("latitude") double latitude,
//                            @PathParam("range") int range )
//    {
//        LOG.log( Level.INFO, () -> "nearby " + longitude + "," + latitude + "," + range );
//        // Limit range to 0..15 miles
//        int r = Math.max( 0, Math.min( range, 15 ) );
//        StationPosition pos = new StationPosition( 0, null, latitude, longitude, null );
//
//        try {
//            return respond( StationPositionManager.INSTANCE.nearby( pos, r ), StationPosition.toJsonObjectWithDistance, false );
//        }
//        catch( SQLException ex ) {
//            return error( () -> "retrieving nearby (" + longitude + "," + latitude + ") range " + range + "(" + r + ")", ex );
//        }
//    }
//
//    /**
//     * Generate a JSON list of all stations
//     * <p>
//     * @return
//     */
//    @Path("all.json")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response all_json()
//    {
//        try {
//            return respond( StationPositionManager.INSTANCE.getAll(), StationPosition.toJsonObject, false );
//        }
//        catch( SQLException ex ) {
//            return error( () -> "retrieving all", ex );
//        }
//    }
//
//    /**
//     * Generate a KML file of all stations
//     * <p>
//     * @return
//     */
//    @Path("all.kml")
//    @GET
//    @Produces("application/vnd.google-earth.kml+xml")
//    public Response all_kml()
//    {
//        try( StringWriter r = new StringWriter() ) {
//            try( XMLSaxWriter w = new XMLSaxWriter( r, new KMLWriter( "station_point" ) ) ) {
//                StationPositionManager.INSTANCE.forEach( w );
//            }
//
//            return Response.ok( r.toString() ).
//                    build();
//        }
//        catch( XMLStreamException |
//               IOException |
//               SQLException ex ) {
//            return error( () -> "all_kml", ex );
//        }
//    }
//
//    /**
//     * Generate a CSV file of all stations
//     * <p>
//     * @return
//     */
//    @Path("all.csv")
//    @GET
//    @Produces("text/csv")
//    public Response all_csv()
//    {
//        try( StringWriter r = new StringWriter() ) {
//            try( PrintWriter w = new PrintWriter( r ) ) {
//                w.println( "\"id\",\"name\",\"longitude\",\"latitude\",\"tiploc\"" );
//                StationPositionManager.INSTANCE.forEach( p -> w.printf( "%d,\"%s\",%.6f,%.6f,\"%s\"\n",
//                                                                        p.get( "id" ),
//                                                                        p.getName(),
//                                                                        p.getLongitude(),
//                                                                        p.getLatitude(),
//                                                                        p.get( "tiploc" )
//                ) );
//            }
//            return Response.ok( r.toString() ).
//                    build();
//        }
//        catch( IOException |
//               SQLException ex ) {
//            return error( () -> "all_csv", ex );
//        }
//    }

}
