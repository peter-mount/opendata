/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalTime;
import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.util.Activity;
import uk.trainwatch.util.JsonUtils;

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

}
