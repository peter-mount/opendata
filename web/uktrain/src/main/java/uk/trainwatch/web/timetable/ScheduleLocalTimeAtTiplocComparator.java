/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.time.LocalTime;
import java.util.Comparator;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.nrod.timetable.model.Schedule;

/**
 * Comparator to order Schedules by LocalTime at a specific tiploc.
 * <p>
 * It's used when ordering train schedules chronologically at a specific location.
 * <p>
 * <p>
 * @author Peter T Mount
 */
public class ScheduleLocalTimeAtTiplocComparator
        implements Comparator<Schedule>
{

    private final String tiploc;
    // FIXME remove
    private final LocalTime midnight = LocalTime.of( 0, 0 );

    /**
     * Constructor
     * <p>
     * @param tiploc The tiploc of interest
     */
    public ScheduleLocalTimeAtTiplocComparator( String tiploc )
    {
        this.tiploc = tiploc;
    }

    @Override
    public int compare( Schedule o1, Schedule o2 )
    {
        Location l1 = findLocation( o1 );
        Location l2 = findLocation( o2 );
        LocalTime t1 = getTime( l1 );
        LocalTime t2 = getTime( l2 );
        if( t1 == null )
        {
            return t2 == null ? 0 : 1;
        }
        else if( t2 == null )
        {
            return -1;
        }
        else
        {
            return t1.compareTo( t2 );
        }
    }

    private Location findLocation( Schedule t )
    {
        for( Location l: t.getLocations() )
        {
            if( tiploc.equals( l.getLocation().
                    getKey() ) && !(l instanceof ChangesEnRoute) )
            {
                return l;
            }
        }
        return null;
    }

    private LocalTime getTime( Location l )
    {
        if( l instanceof OriginLocation )
        {
            return ((OriginLocation) l).getWorkDeparture();
        }
        if( l instanceof IntermediateLocation )
        {
            IntermediateLocation ol = (IntermediateLocation) l;
            LocalTime t = ol.getWorkArrival();
            if( t == null )
            {
                t = ol.getWorkPass();
            }
            if( t == null )
            {
                t = midnight;
            }
            return t;
        }
        if( l instanceof TerminatingLocation )
        {
            return ((TerminatingLocation) l).getWorkArrival();
        }
        return null;
    }

}
