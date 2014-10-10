/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalTime;
import java.util.function.Function;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.util.Activity;

/**
 *
 * @author Peter T Mount
 */
public class OriginLocation
        extends Location
{

    static final Function<CIFParser, Record> factory = p -> new OriginLocation(
            p.getTiplocSuffix(),
            p.getTime_hhmmH(),
            p.getTime_hhmm(),
            p.getString( 3 ),
            p.getString( 3 ),
            p.getAllowance(),
            p.getAllowance(),
            p.getActivity(),
            p.getAllowance()
    );

    private final LocalTime workDeparture;
    private final LocalTime publicDeparture;
    private final String platform;
    private final String line;
    private final int engAllowance;
    private final int pathAllowance;
    private final Activity[] activity;
    private final int perfAllowance;

    public OriginLocation( Tiploc location,
                           LocalTime workDeparture,
                           LocalTime publicDeparture,
                           String platform,
                           String line,
                           int engAllowance,
                           int pathAllowance,
                           Activity[] activity,
                           int perfAllowance )
    {
        super( RecordType.LO, location );
        this.workDeparture = workDeparture;
        this.publicDeparture = publicDeparture;
        this.platform = platform;
        this.line = line;
        this.engAllowance = engAllowance;
        this.pathAllowance = pathAllowance;
        this.activity = activity;
        this.perfAllowance = perfAllowance;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
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

}
