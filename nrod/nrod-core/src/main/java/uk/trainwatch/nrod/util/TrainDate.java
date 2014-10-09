/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 * A train date
 * <p/>
 * @author peter
 */
public class TrainDate
{

    /**
     * Factory to retrieve a "timestamp" from a JsonObject
     */
    public static final Function<JsonObject, TrainDate> FACTORY
                                                        = o -> new TrainDate( JsonUtils.getLong( o, "timestamp", 0L ) );

    private long timeInMillis;
    private transient volatile Calendar calendar;

    public TrainDate()
    {
    }

    public TrainDate( long timeInMillis )
    {
        this.timeInMillis = timeInMillis;
    }

    public Calendar getCalendar()
    {
        if( calendar == null )
        {
            synchronized( this )
            {
                if( calendar == null )
                {
                    calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis( timeInMillis );
                }
            }
        }
        return calendar;
    }

    /**
     * The delay in a human format
     * <p/>
     * @return
     */
    public static String getDelayHtml( final long d )
    {
        return getDelayHtml( d, true );
    }

    /**
     * The delay in a human format
     * <p/>
     * @return
     */
    public static String getDelayHtml( final long d, final boolean default0 )
    {
        StringBuilder sb = new StringBuilder();

        long delay = d;
        if( delay < 0 )
        {
            sb.append( '-' );
            delay = -delay;
        }

        // No delay if less than 30s
        if( delay < 30 )
        {
            return default0 ? "0" : "&nbsp;";
        }

        long m = delay / 60L;
        long s = delay % 60L;
        if( m > 0 )
        {
            sb.append( m );
        }
        if( s >= 30 )
        {
            sb.append( "&frac12;" );
        }

        return sb.toString();
    }

    public String getYearHtml()
    {
        return String.format( "%04d", getYear() );
    }

    public String getMonthHtml()
    {
        return String.format( "%04d", getMonth() );
    }

    public String getDayHtml()
    {
        return String.format( "%04d", getDay() );
    }

    public String getDayPath()
    {
        return String.format( "%04d/%02d/%02d", getYear(), getMonth(), getDay() );
    }

    public long getTrainDate()
    {
        long t = timeInMillis / 86400000L;
        if( getHour() <= 3 )
        {
            t--;
        }
        return t;
    }

    public void setTrainDate()
    {
        throw new UnsupportedOperationException( "JAXB only" );
    }

    public java.sql.Date getDate()
    {
        return new java.sql.Date( getTimeInMillis() );
    }

    public final Date getTime()
    {
        return getCalendar().
                getTime();
    }

    public final TrainDate setTime( Date date )
    {
        getCalendar().
                setTime( date );
        return this;
    }

    /**
     * Set's the time to now
     * <p/>
     * @return
     */
    public TrainDate setNow()
    {
        setTimeInMillis( System.currentTimeMillis() );
        return this;
    }

    /**
     * Set's the time to the start of today. For us this is 3am. The date portion is unchanged
     * <p/>
     * @return
     */
    public TrainDate setStartOfDay()
    {
        clearTime();
        setHour( 3 );
        return this;
    }

    public TrainDate clearTime()
    {
        Calendar c = getCalendar();
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        return this;
    }

    public TrainDate setTimeInMillis( long l )
    {
        getCalendar().
                setTimeInMillis( l );
        return this;
    }

    public long getTimeInMillis()
    {
        return getCalendar().
                getTimeInMillis();
    }

    public TrainDate setHour( int y )
    {
        getCalendar().
                set( Calendar.HOUR_OF_DAY, y );
        return this;
    }

    public int getHour()
    {
        return getCalendar().
                get( Calendar.HOUR_OF_DAY );
    }

    public TrainDate setMinute( int y )
    {
        getCalendar().
                set( Calendar.MINUTE, y );
        return this;
    }

    public int getMinute()
    {
        return getCalendar().
                get( Calendar.MINUTE );
    }

    public TrainDate setSecond( int y )
    {
        getCalendar().
                set( Calendar.SECOND, y );
        return this;
    }

    public int getSecond()
    {
        return getCalendar().
                get( Calendar.SECOND );
    }

    public TrainDate setYear( int y )
    {
        getCalendar().
                set( Calendar.YEAR, y );
        return this;
    }

    public int getYear()
    {
        return getCalendar().
                get( Calendar.YEAR );
    }

    public TrainDate setMonth( int m )
    {
        getCalendar().
                set( Calendar.MONTH, m - 1 );
        return this;
    }

    public int getMonth()
    {
        return getCalendar().
                get( Calendar.MONTH ) + 1;
    }

    public TrainDate setDay( int d )
    {
        getCalendar().
                set( Calendar.DAY_OF_MONTH, d );
        return this;
    }

    public int getDay()
    {
        return getCalendar().
                get( Calendar.DAY_OF_MONTH );
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + (int) (this.timeInMillis ^ (this.timeInMillis >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final TrainDate other = (TrainDate) obj;
        if( this.timeInMillis != other.timeInMillis )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "TrainDate[calendar=" + timeInMillis + ']';
    }
}
