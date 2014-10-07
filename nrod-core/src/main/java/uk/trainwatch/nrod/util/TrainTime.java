/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.util;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A representation of the time of day with a resolution of 30s
 * @author peter
 */
public class TrainTime
        implements Comparable<TrainTime>
{

    private static final ConcurrentMap<String, TrainTime> map = new ConcurrentHashMap<>();
    private int hour;
    private int minute;
    private boolean second;

    public static TrainTime lookup( final String code )
    {
        if( code == null || code.isEmpty() )
        {
            return null;
        }

        TrainTime tt = map.get( code );
        if( tt == null )
        {
            tt = getTime(
                    Integer.parseInt( code.substring( 0, 2 ) ),
                    Integer.parseInt( code.substring( 2, 4 ) ),
                    code.contains( "H" ) );

            TrainTime e = map.putIfAbsent( code, tt );
            return e == null ? tt : e;
        }
        else
        {
            return tt;
        }
    }

    public static TrainTime getTime( int hour, int minute, boolean second )
    {
        return new TrainTime( hour, minute, second );
    }

    public static TrainTime getTime( long millis )
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( millis );
        return new TrainTime( c.get( Calendar.HOUR_OF_DAY ), c.get( Calendar.MINUTE ), c.get( Calendar.SECOND ) >= 30 );
    }

    TrainTime()
    {
        this( 0, 0, false );
    }

    private TrainTime( int hour, int minute, boolean second )
    {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int compareTo( TrainTime o )
    {
        // We have to account for a train running across midnight otherwise we'll have the end before the start
        // So use our date boundary of 3am as the day boundary, times before then are +24 hours
        int h1 = hour;
        if( h1 <= 3 )
        {
            h1 += 24;
        }

        int h2 = o.hour;
        if( h2 <= 3 )
        {
            h2 += 24;
        }

        int r = compareTo( h1, h2 );
        if( r == 0 )
        {
            r = compareTo( minute, o.minute );
        }
        if( r == 0 && second != o.second )
        {
            // either we are 30s & other is 0s then 1, otherwise we are 0s and other is 30s
            r = second ? 1 : -1;
        }
        return r;
    }

    private static int compareTo( int thisVal, int anotherVal )
    {
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public boolean isSecond()
    {
        return second;
    }

    public int getSecond()
    {
        return second ? 30 : 0;
    }

    public long getTime()
    {
        long r = (hour * 3600L) + (minute * 60L);
        if( second )
        {
            r += 30L;
        }
        return r * 1000L;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + hour;
        hash = 37 * hash + minute;
        hash = 37 * hash + (second ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final TrainTime other = (TrainTime) obj;
        return hour == other.hour && this.minute == other.minute && this.second == other.second;
    }
}
