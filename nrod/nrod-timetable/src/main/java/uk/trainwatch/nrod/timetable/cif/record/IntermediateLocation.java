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
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.util.Activity;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public class IntermediateLocation
        extends Location
{

    static final Function<CIFParser, Record> factory = p -> new IntermediateLocation(
            p.getTiplocSuffix(),
            p.getTime_hhmmH(),
            p.getTime_hhmmH(),
            p.getTime_hhmmH(),
            p.getTime_hhmm(),
            p.getTime_hhmm(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getActivity(),
            p.getAllowance(),
            p.getAllowance(),
            p.getAllowance()
    );

    public static final Function<JsonObject, IntermediateLocation> fromJson = o -> new IntermediateLocation(
            new Tiploc( o.getString( "tiploc" ) ),
            JsonUtils.getLocalTime( o, "workArrival" ),
            JsonUtils.getLocalTime( o, "workDeparture" ),
            JsonUtils.getLocalTime( o, "workPass" ),
            JsonUtils.getLocalTime( o, "pubArrival" ),
            JsonUtils.getLocalTime( o, "pubDeparture" ),
            JsonUtils.getString( o, "platform" ),
            JsonUtils.getString( o, "line" ),
            JsonUtils.getString( o, "path" ),
            JsonUtils.getEnumArray( Activity.class, o, "activity" ),
            JsonUtils.getInt( o, "engAllowance" ),
            JsonUtils.getInt( o, "pathAllowance" ),
            JsonUtils.getInt( o, "perfAllowance" )
    );

    public static final Function<IntermediateLocation, JsonObject> toJson = l -> Json.createObjectBuilder().
            add( "type", l.getRecordType().
                 toString() ).
            add( "tiploc", l.getLocation().
                 getKey() ).
            add( "workArrival", l.getWorkArrival().
                 toString() ).
            add( "workDeparture", l.getWorkDeparture().
                 toString() ).
            add( "workPass", l.getWorkPass().
                 toString() ).
            add( "pubArrival", l.getPublicArrival().
                 toString() ).
            add( "pubDeparture", l.getPublicDeparture().
                 toString() ).
            add( "platform", l.getPlatform() ).
            add( "line", l.getLine() ).
            add( "path", l.getPath() ).
            add( "engAllowance", l.getEngAllowance() ).
            add( "pathAllowance", l.getPathAllowance() ).
            add( "perfAllowance", l.getPerfAllowance() ).
            add( "activity", JsonUtils.getArray( l.getActivity() ) ).
            build();

    private final LocalTime workArrival;
    private final LocalTime workDeparture;
    private final LocalTime workPass;
    private final LocalTime pubArrival;
    private final LocalTime pubDeparture;
    private final String platform;
    private final String line;
    private final String path;
    private final Activity[] activity;
    private final int engAllowance;
    private final int pathingAllowance;
    private final int perfAllowance;

    public IntermediateLocation( Tiploc location,
                                 LocalTime workArrival, LocalTime workDeparture, LocalTime workPass,
                                 LocalTime pubArrival, LocalTime pubDeparture, String platform, String line, String path,
                                 Activity[] activity,
                                 int engAllowance,
                                 int pathingAllowance,
                                 int perfAllowance )
    {
        super( RecordType.LI, location );
        this.workArrival = workArrival;
        this.workDeparture = workDeparture;
        this.workPass = workPass;
        this.pubArrival = pubArrival;
        this.pubDeparture = pubDeparture;
        this.platform = platform.trim();
        this.line = line.trim();
        // FIXME current db has no path in the json, so account for this until the next full reload
        this.path = path == null ? "" : path.trim();
        this.activity = activity;
        this.engAllowance = engAllowance;
        this.pathingAllowance = pathingAllowance;
        this.perfAllowance = perfAllowance;
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

    public LocalTime getWorkDeparture()
    {
        return workDeparture;
    }

    public LocalTime getWorkPass()
    {
        return workPass;
    }

    public LocalTime getPublicArrival()
    {
        return pubArrival;
    }

    public LocalTime getPublicDeparture()
    {
        return pubDeparture;
    }

    public String getPlatform()
    {
        return platform;
    }

    public String getLine()
    {
        return line;
    }

    public String getPath()
    {
        return path;
    }

    public Activity[] getActivity()
    {
        return activity;
    }

    public int getEngAllowance()
    {
        return engAllowance;
    }

    public int getPathAllowance()
    {
        return pathingAllowance;
    }

    public int getPerfAllowance()
    {
        return perfAllowance;
    }

}
