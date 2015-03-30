/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalTime;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.util.Activity;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author Peter T Mount
 */
public class TerminatingLocation
        extends Location
{

    static final Function<CIFParser, Record> factory = p -> new TerminatingLocation(
            p.getTiplocSuffix(),
            p.getTime_hhmmH(),
            p.getTime_hhmm(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getActivity()
    );

    public static final Function<JsonObject, TerminatingLocation> fromJson = o -> new TerminatingLocation(
            new Tiploc( o.getString( "tiploc" ) ),
            JsonUtils.getLocalTime( o, "workArrival" ),
            JsonUtils.getLocalTime( o, "pubArrival" ),
            JsonUtils.getString( o, "platform" ),
            JsonUtils.getString( o, "path" ),
            JsonUtils.getEnumArray( Activity.class, o, "activity" )
    );

    public static final Function<TerminatingLocation, JsonObjectBuilder> toJsonBuilder = l -> Json.createObjectBuilder().
            add( "type", l.getRecordType().
                 toString() ).
            add( "tiploc", l.getLocation().
                 getKey() ).
            add( "workArrival", TimeUtils.toJson( l.getWorkArrival() ) ).
            add( "pubArrival", TimeUtils.toJson( l.getPublicArrival() ) ).
            add( "platform", l.getPlatform() ).
            add( "path", l.getPath() ).
            add( "activity", JsonUtils.getArray( l.getActivity() ) );

    public static final Function<TerminatingLocation, JsonObject> toJson = l -> toJsonBuilder.apply( l ).build();

    private final LocalTime workArrival;
    private final LocalTime pubArrival;
    private final String platform;
    private final String path;
    private final Activity[] activity;

    public TerminatingLocation( Tiploc location,
                                LocalTime workArrival,
                                LocalTime pubArrival,
                                String platform,
                                String path,
                                Activity[] activity )
    {
        super( RecordType.LT, location );
        this.workArrival = workArrival;
        this.pubArrival = pubArrival;
        this.platform = platform.trim();
        this.path = path.trim();
        this.activity = activity;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

    public LocalTime getWorkArrival()
    {
        return workArrival;
    }

    public LocalTime getPublicArrival()
    {
        return pubArrival;
    }

    public String getPlatform()
    {
        return platform;
    }

    public String getPath()
    {
        return path;
    }

    public Activity[] getActivity()
    {
        return activity;
    }

    @Override
    public LocalTime getTime()
    {
        return getWorkArrival();
    }
}
