/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.time.LocalTime;
import uk.trainwatch.nrod.location.Tiploc;

/**
 * Represents a location in a schedule.
 * <p>
 * It's used to organise entries within the schedule so they are in the correct sequence chronologically.
 * <p>
 * @author Peter T Mount
 */
public abstract class Location
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    private final Tiploc location;

    /**
     *
     * @param type     RecordType
     * @param location Tiploc
     * @param workTime LocalTime for this location
     */
    public Location( RecordType type, Tiploc location )
    {
        super( type );
        this.location = location;
    }

    /**
     * The Tiploc for this location
     * <p>
     * @return
     */
    public final Tiploc getLocation()
    {
        return location;
    }

    public boolean isPass()
    {
        return false;
    }

    public abstract LocalTime getTime();
}
