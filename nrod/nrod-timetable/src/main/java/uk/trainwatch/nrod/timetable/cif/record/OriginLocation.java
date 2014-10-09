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
public class OriginLocation
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new OriginLocation(
            p.getString( 8 ),
            p.getTime2H(),
            p.getTime2(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getString( 2 ),
            p.getString( 2 ),
            p.getString( 12 ),
            p.getString( 2 )
    );

    private final String location;
    private final LocalTime workDeparture;
    private final LocalTime publicDeparture;
    private final String platform;
    private final String line;
    private final String engAllowance;
    private final String pathAllowance;
    private final String activity;
    private final String perfAllowance;

    public OriginLocation( String location, LocalTime workDeparture, LocalTime publicDeparture, String platform,
                           String line, String engAllowance, String pathAllowance, String activity,
                           String perfAllowance )
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

    public String getEngAllowance()
    {
        return engAllowance;
    }

    public String getPathAllowance()
    {
        return pathAllowance;
    }

    public String getActivity()
    {
        return activity;
    }

    public String getPerfAllowance()
    {
        return perfAllowance;
    }

    @Override
    public String toString()
    {
        return "OriginLocation{" + "location=" + location + ", workDeparture=" + workDeparture + ", publicDeparture=" + publicDeparture + ", platform=" + platform + ", line=" + line + ", engAllowance=" + engAllowance + ", pathAllowance=" + pathAllowance + ", activity=" + activity + ", perfAllowance=" + perfAllowance + '}';
    }

}
