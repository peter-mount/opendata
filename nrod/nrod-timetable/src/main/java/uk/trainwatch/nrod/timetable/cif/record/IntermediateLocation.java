/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalTime;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.util.Activity;

/**
 *
 * @author Peter T Mount
 */
public class IntermediateLocation
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new IntermediateLocation(
            p.getString( 8 ),
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

    private final String location;
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

    public IntermediateLocation( String location, LocalTime workArrival, LocalTime workDeparture, LocalTime workPass,
                                 LocalTime pubArrival, LocalTime pubDeparture, String platform, String line, String path,
                                 Activity[] activity,
                                 int engAllowance,
                                 int pathingAllowance,
                                 int perfAllowance )
    {
        super( RecordType.LI );
        this.location = location;
        this.workArrival = workArrival;
        this.workDeparture = workDeparture;
        this.workPass = workPass;
        this.pubArrival = pubArrival;
        this.pubDeparture = pubDeparture;
        this.platform = platform;
        this.line = line;
        this.path = path;
        this.activity = activity;
        this.engAllowance = engAllowance;
        this.pathingAllowance = pathingAllowance;
        this.perfAllowance = perfAllowance;
    }

    public String getLocation()
    {
        return location;
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

    public LocalTime getPubArrival()
    {
        return pubArrival;
    }

    public LocalTime getPubDeparture()
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

    public int getPathingAllowance()
    {
        return pathingAllowance;
    }

    public int getPerfAllowance()
    {
        return perfAllowance;
    }

    @Override
    public String toString()
    {
        return "IntermediateLocation{" + "location=" + location + ", workArrival=" + workArrival + ", workDeparture=" + workDeparture + ", workPass=" + workPass + ", pubArrival=" + pubArrival + ", pubDeparture=" + pubDeparture + ", platform=" + platform + ", line=" + line + ", path=" + path + ", activity=" + activity + ", engAllowance=" + engAllowance + ", pathingAllowance=" + pathingAllowance + ", perfAllowance=" + perfAllowance + '}';
    }

}
