/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This defines the days of the week a train runs.
 * <p>
 * @author Peter T Mount
 */
public class DaysRun
{

    private static final Day DAYS[] = Day.values();

    private final int days;

    public DaysRun( int days )
    {
        this.days = days;
    }

    public DaysRun( String s )
    {
        Objects.requireNonNull( s );
        if( s.length() != 7 )
        {
            throw new IllegalArgumentException( "Invalid days run \"" + s + "\"" );
        }

        int d = 0;
        for( int i = 0; i < 7; i++ )
        {
            if( s.charAt( i ) == '1' )
            {
                d = d | (1 << i);
            }
        }
        days = d;
    }

    public boolean isOnDay( Day dow )
    {
        return (days & 1 << dow.ordinal()) != 0;
    }

    public boolean isOnDay( DayOfWeek dow )
    {
        return (days & 1 << dow.ordinal()) != 0;
    }

    /**
     * Convenience, returns
     * <p>
     * @param dow
     */
    public DaysRun( DayOfWeek dow )
    {
        this( 1 << dow.ordinal() );
    }

    /**
     * The days this train runs.
     * <p>
     * Each bit represents a day, bit 0 for Monday, 7 for Sunday.
     * <p>
     * @return days train runs
     */
    public int getDaysRunning()
    {
        return days;
    }

    /**
     * Returns a {@link Collection} of {@link Day} that this train runs
     * <p>
     * @return Collection, never null
     */
    public Collection<Day> getWeek()
    {
        if( days == 0 )
        {
            return Collections.emptyList();
        }

        List<Day> l = new ArrayList<>();
        for( Day day : DAYS )
        {
            if( day.runsToday( days ) )
            {
                l.add( day );
            }
        }
        return l;
    }

    /**
     * Enum which can be used to determine the days this train runs
     */
    public static enum Day
    {

        MONDAY( 0 ),
        TUESDAY( 1 ),
        WEDNESDAY( 2 ),
        THURSDAY( 3 ),
        FRIDAY( 4 ),
        SATURDAY( 5 ),
        SUNDAY( 6 );

        private final int bit;
        private final int mask;

        private Day( int bit )
        {
            this.bit = bit;
            mask = 1 << bit;
        }

        public int getBit()
        {
            return bit;
        }

        /**
         * Does a train run today
         * <p>
         * @param days definition of the week
         * <p>
         * @return true if it runs today
         */
        public boolean runsToday( int days )
        {
            return (days & mask) == mask;
        }
    }
}
