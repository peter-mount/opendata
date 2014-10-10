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
public class OriginLocation
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new OriginLocation(
            p.getString( 8 ),
            p.getTime_hhmmH(),
            p.getTime_hhmm(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getAllowance(),
            p.getAllowance(),
            p.getActivity(),
            p.getAllowance()
    );

    private final String location;
    private final LocalTime workDeparture;
    private final LocalTime publicDeparture;
    private final String platform;
    private final String line;
    private final int engAllowance;
    private final int pathAllowance;
    private final Activity[] activity;
    private final int perfAllowance;

    public OriginLocation( String location, LocalTime workDeparture, LocalTime publicDeparture, String platform,
                           String line,
                           int engAllowance,
                           int pathAllowance,
                           Activity[] activity,
                           int perfAllowance )
    {
        super( RecordType.LO );
        this.location = location;
        this.workDeparture = workDeparture;
        this.publicDeparture = publicDeparture;
        this.platform = platform;
        this.line = line;
        this.engAllowance = engAllowance;
        this.pathAllowance = pathAllowance;
        this.activity = activity;
        this.perfAllowance = perfAllowance;
    }

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
    }

    public String getLocation()
    {
        return location;
    }

    public LocalTime getWorkDeparture()
    {
        return workDeparture;
    }

    public LocalTime getPublicDeparture()
    {
        return publicDeparture;
    }

    public String getPlatform()
    {
        return platform;
    }

    public String getLine()
    {
        return line;
    }

    public int getEngAllowance()
    {
        return engAllowance;
    }

    public int getPathAllowance()
    {
        return pathAllowance;
    }

    public Activity[] getActivity()
    {
        return activity;
    }

    public int getPerfAllowance()
    {
        return perfAllowance;
    }

    @Override
    public String toString()
    {
        return "OriginLocation{" + "location=" + location + ", workDeparture=" + workDeparture + ", publicDeparture=" + publicDeparture + ", platform=" + platform + ", line=" + line + ", engAllowance=" + engAllowance + ", pathAllowance=" + pathAllowance + ", activity=" + activity + ", perfAllowance=" + perfAllowance + '}';
    }

}
