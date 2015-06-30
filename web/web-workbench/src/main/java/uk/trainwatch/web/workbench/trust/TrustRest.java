/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench.trust;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordType;
import uk.trainwatch.nrod.timetable.cif.record.RecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.nrod.timetable.model.ScheduleJsonBuilder;
import uk.trainwatch.nrod.trust.model.TrainActivation;
import uk.trainwatch.nrod.trust.model.TrainCancellation;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.workbench.WorkbenchStreams;

/**
 *
 * @author peter
 */
@Path( "/rail/1/workbench/trust" )
@RequestScoped
public class TrustRest
{

    private static final Logger LOG = Logger.getLogger( TrustRest.class.getName() );

    @Inject
    private WorkbenchStreams workbenchStreams;

    @Inject
    protected TrainLocationFactory trainLocationFactory;

    @Path( "/details/{toc}/{id}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response details( @PathParam( "toc" ) int toc, @PathParam( "id" ) String id )
    {
        return getResponse( workbenchStreams.details( toc, id ), true );
    }

    @Path( "/activations/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response activations( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.activations( toc ) );
    }

    @Path( "/movements/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response running( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.movements( toc ) );
    }

    @Path( "/cancellations/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response cancellations( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.cancellations( toc ) );
    }

    @Path( "/delays/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response delays( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.delays( toc ) );
    }

    @Path( "/offroute/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response offroute( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.offroute( toc ) );
    }

    @Path( "/terminated/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response terminated( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.terminated( toc ) );
    }

    @Path( "/issues/{toc}" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response issues( @PathParam( "toc" ) int toc )
    {
        return getResponse( workbenchStreams.issues( toc ) );
    }

    private Response getResponse( Stream<Trust> trains )
    {
        return getResponse( trains, false );
    }

    private Response getResponse( Stream<Trust> trains, boolean detailed )
    {
        JsonArrayBuilder a = Json.createArrayBuilder();
        trains.forEach( t -> add( a, t, detailed ) );
        CacheControl cache = new CacheControl();
        cache.setMaxAge( 60 );
        cache.setSMaxAge( 60 );
        return Response.ok( a.build() ).
                cacheControl( cache ).
                build();
    }

    private void add( JsonArrayBuilder a, Trust t, boolean detailed )
    {
        JsonObjectBuilder o = Json.createObjectBuilder().
                add( "id", t.getId() ).
                add( "toc", t.getToc() ).
                add( "status", t.getStatus().
                     toString() );

        if( t.getActivation() != null )
        {
            TrainActivation v = t.getActivation();
            o.add( "activation", Json.createObjectBuilder().
                   add( "id", v.getSchedule_wtt_id() ).
                   add( "type", v.getSchedule_type() ).
                   add( "time", TimeUtils.toJson( v.getOrigin_dep_timestamp() ) ).
                   add( "location", trainLocationFactory.getJsonByStanox( v.getSched_origin_stanox() ) ) );
        }
        if( t.getCancellation() != null )
        {
            TrainCancellation v = t.getCancellation();
            o.add( "cancellation", Json.createObjectBuilder().
                   add( "type", v.getCanx_type() ).
                   add( "reason", v.getCanx_reason_code() ).
                   add( "time", TimeUtils.toJson( v.getCanx_timestamp() ) ).
                   add( "location", trainLocationFactory.getJsonByStanox( v.getLoc_stanox() ) ) );
        }
        if( t.getMovement() != null )
        {
            TrainMovement v = t.getMovement();
            o.add( "movement", addMovement( Json.createObjectBuilder(), v ) );
        }

        if( detailed )
        {
            JsonObjectBuilder s;

            List<Location> locations = new ArrayList<>();
            if( Trust.isScheduled( t ) )
            {
                s = extractSchedule( locations, t );
            }
            else
            {
                s = Json.createObjectBuilder();
                s.addNull( "schedule" );
            }

            //extractMovements( locations, t );
            s.add( "locations", new Visitor().visit( locations ) );

            o.add( "schedule", s );
        }

        a.add( o );
    }

    private JsonObjectBuilder addMovement( JsonObjectBuilder b, TrainMovement v )
    {
        return b.add( "delay", v.getDelay() ).
                add( "location", trainLocationFactory.getJsonByStanox( v.getLoc_stanox() ) ).
                add( "time", TimeUtils.toJson( v.getActual_timestamp() ) ).
                add( "offroute", v.isOffroute_ind() ).
                add( "terminated", v.isTrain_terminated() ).
                //
                add( "curTrainId", Objects.toString( v.getCurrent_train_id(), "" ) ).
                add( "platform", Objects.toString( v.getPlatform(), "" ) ).
                add( "fileAddress", Objects.toString( v.getTrain_file_address(), "" ) ).
                add( "serviceCode", Objects.toString( v.getTrain_service_code(), "" ) ).
                add( "varStat", Objects.toString( v.getVariation_status(), "" ) );
    }

    private JsonArrayBuilder extractMovements( Map<LocalTime, JsonObjectBuilder> map, Trust t )
    {
        return t.getMovements().
                stream().
                map( m ->
                        {
                    // Create a new entry
                            // FIXME buggy as works on actual time so no delays managed
                            JsonObjectBuilder b = map.computeIfAbsent(
                                    TimeUtils.getLocalDateTime( m.getActual_timestamp() ).toLocalTime(),
                                    k -> Json.createObjectBuilder().
                                    add( "location", trainLocationFactory.getJsonByStanox( m.getLoc_stanox() ) )
                            );

                            return addMovement( b, m );
                } ).
                reduce( Json.createArrayBuilder(), JsonArrayBuilder::add, ( a, b ) -> a );
    }

    private JsonObjectBuilder extractSchedule( List<Location> schedule, Trust t )
    {
        ScheduleJsonBuilder v = new ScheduleJsonBuilder()
        {

            @Override
            public void visit( OriginLocation ol )
            {
                schedule.add( ol );
            }

            @Override
            public void visit( IntermediateLocation il )
            {
                schedule.add( il );
            }

            @Override
            public void visit( TerminatingLocation tl )
            {
                schedule.add( tl );
            }

            @Override
            public void visit( ChangesEnRoute s )
            {
            }

        };
        return v.visitBuilder( t.getSchedule() );
    }

    private static class UnscheduledLocation
            extends Location
    {

        private final TrainMovement mvt;
        private final LocalTime time;

        public UnscheduledLocation( TrainMovement mvt, Tiploc loc )
        {
            super( RecordType.ZZ, loc );
            this.mvt = mvt;
            time = TimeUtils.getLocalDateTime( mvt.getActual_timestamp() ).toLocalTime();
        }

        public TrainMovement getMvt()
        {
            return mvt;
        }

        @Override
        public LocalTime getTime()
        {
            return time;
        }

        @Override
        public void accept( RecordVisitor v )
        {
            ((Visitor) v).visit( this );
        }

    }

    public static class ScheduledLocation
            extends UnscheduledLocation
    {

        private final Location loc;

        public ScheduledLocation( TrainMovement mvt, Location loc )
        {
            super( mvt, loc.getLocation() );
            this.loc = loc;
        }

        public Location getLoc()
        {
            return loc;
        }

        @Override
        public void accept( RecordVisitor v )
        {
            ((Visitor) v).visit( this );
        }

    }

    private class Visitor
            implements RecordVisitor
    {

        private final JsonArrayBuilder locations = Json.createArrayBuilder();

        public JsonArrayBuilder visit( List<Location> locations )
        {
            Visitor v = new Visitor();
            locations.forEach( l -> l.accept( v ) );
            return v.locations;
        }

        public void visit( UnscheduledLocation ul )
        {
            TrainMovement m = ul.getMvt();
            locations.add( addMovement( Json.createObjectBuilder(), m ) );
        }

        public void visit( ScheduledLocation ul )
        {
            TrainMovement m = ul.getMvt();
            JsonObjectBuilder b = null;

            Location loc = ul.getLoc();
            if( loc instanceof OriginLocation )
            {
                b = OriginLocation.toJsonBuilder.apply( (OriginLocation) loc );
            }
            else if( loc instanceof IntermediateLocation )
            {
                b = IntermediateLocation.toJsonBuilder.apply( (IntermediateLocation) loc );
            }
            else if( loc instanceof TerminatingLocation )
            {
                b = TerminatingLocation.toJsonBuilder.apply( (TerminatingLocation) loc );
            }

            // Note: b is null for a Location with types we are filtering out
            if( b != null && loc != null )
            {
                b.add( "location", trainLocationFactory.getJsonByTiploc( loc.getLocation().getKey() ) );
            }
            else
            {
                b = Json.createObjectBuilder();
            }

            locations.add( addMovement( b, m ) );
        }

        @Override
        public void visit( OriginLocation ol )
        {
            locations.add( OriginLocation.toJsonBuilder.apply( ol ).
                    add( "location", trainLocationFactory.getJsonByTiploc( ol.getLocation().getKey() ) )
            );
        }

        @Override
        public void visit( IntermediateLocation il )
        {
            locations.add( IntermediateLocation.toJsonBuilder.apply( il ).
                    add( "location", trainLocationFactory.getJsonByTiploc( il.getLocation().getKey() ) )
            );
        }

        @Override
        public void visit( TerminatingLocation tl )
        {
            locations.add( TerminatingLocation.toJsonBuilder.apply( tl ).
                    add( "location", trainLocationFactory.getJsonByTiploc( tl.getLocation().getKey() ) )
            );
        }

    }
}
