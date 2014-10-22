/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.timetable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.model.Schedule;

/**
 * A filter which takes a {@link List} of {@link Schedule}'s and produces the current active one
 * <p>
 * @author Peter T Mount
 */
public enum ActiveScheduleFilter
        implements Function<List<Schedule>, Schedule>,
                   Comparator<Schedule>
{

    INSTANCE;

    /**
     * Mapping function to get the most recent schedule
     * <p>
     * @param t <p>
     * @return
     */
    @Override
    public Schedule apply( List<Schedule> t )
    {
        if( t.isEmpty() )
        {
            return null;
        }
        Collections.sort( t, this );
        return t.get( 0 );
    }

    /**
     * Comparator that orders the most recent runsFrom date first
     * <p>
     * @param a
     * @param b
     * <p>
     * @return
     */
    @Override
    public int compare( Schedule a, Schedule b )
    {
        LocalDate da = a.getRunsFrom();
        LocalDate db = b.getRunsFrom();
        return da.isAfter( db ) ? -1 : 1;
    }

}
