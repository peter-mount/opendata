/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalTime;
import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
public class TerminatingLocation
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new TerminatingLocation(
            p.getString( 8 ),
            p.getTime_hhmmH(),
            p.getTime_hhmm(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getString( 12 )
    );

    private final String location;
    private final LocalTime workArrival;
    private final LocalTime pubArrival;
    private final String platform;
    private final String path;
    private final String activity;

    public TerminatingLocation( String location, LocalTime workArrival, LocalTime pubArrival, String platform,
                                String path, String activity )
    {
        super( RecordType.LT );
        this.location = location;
        this.workArrival = workArrival;
        this.pubArrival = pubArrival;
        this.platform = platform;
        this.path = path;
        this.activity = activity;
    }

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
    }

    public String getLocation()
    {
        return location;
    }

    public LocalTime getWorkArrival()
    {
        return workArrival;
    }

    public LocalTime getPubArrival()
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

    public String getActivity()
    {
        return activity;
    }

    @Override
    public String toString()
    {
        return "TerminatingLocation{" + "location=" + location + ", workArrival=" + workArrival + ", pubArrival=" + pubArrival + ", platform=" + platform + ", path=" + path + ", activity=" + activity + '}';
    }

}
