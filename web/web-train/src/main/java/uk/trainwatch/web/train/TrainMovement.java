/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSLocation;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSTimeData;
import uk.trainwatch.nre.darwin.model.util.PublicArrival;
import uk.trainwatch.nre.darwin.model.util.PublicDeparture;
import uk.trainwatch.nre.darwin.model.util.TplLocation;
import uk.trainwatch.nre.darwin.model.util.WorkArrival;
import uk.trainwatch.nre.darwin.model.util.WorkDeparture;
import uk.trainwatch.nre.darwin.model.util.WorkPass;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
public class TrainMovement
        implements Comparable<TrainMovement>,
                   TplLocation,
                   PublicArrival,
                   PublicDeparture,
                   WorkPass
{

    /**
     * Predicate to indicate if this movement has been suppressed &amp; should not be shown to the public
     */
    public static final Predicate<TrainMovement> isSuppressed = m -> m.isTsPresent() && m.getTs().isSetSuppr() && m.getTs().getSuppr();
    public static final Predicate<TrainMovement> isNotSuppressed = isSuppressed.negate();

    public static final Predicate<TrainMovement> isNotOriginOrDestination = m -> !m.isOriginOrDestination();

    private final String tpl;
    private TSLocation ts;
    private TplLocation scheduled;
    private boolean origin;
    private boolean destination;

    public TrainMovement( String tpl )
    {
        this.tpl = tpl;
    }

    /**
     * Get the latest info.
     * <p>
     * i.e. ts takes precedence to schedule as it has the current forecast
     * <p>
     * @return
     */
    public TplLocation getInfo()
    {
        return ts != null ? ts : scheduled != null ? scheduled : null;
    }

    @Override
    public String getTpl()
    {
        return tpl;
    }

    /**
     * Is diverted. This will give false results if we didn't receive the schedule
     * <p>
     * @return
     */
    public boolean isDiverted()
    {
        return !isScheduled() && isTsPresent();
    }

    /**
     * Do we have a schedule
     * <p>
     * @return
     */
    public boolean isScheduled()
    {
        return scheduled != null;
    }

    public boolean isTsPresent()
    {
        return ts != null;
    }

    public boolean isTsNotPresent()
    {
        return !isTsPresent();
    }

    public TSLocation getTs()
    {
        return ts;
    }

    public void setTs( TSLocation ts )
    {
        this.ts = ts;
    }

    public TplLocation getSchedule()
    {
        return scheduled;
    }

    public void setSchedule( TplLocation scheduled )
    {
        this.scheduled = scheduled;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode( this.tpl );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final TrainMovement other = (TrainMovement) obj;
        return Objects.equals( this.tpl, other.tpl );
    }

    @Override
    public String getPta()
    {
        TplLocation l = getInfo();
        return l instanceof PublicArrival ? ((PublicArrival) l).getPta() : null;
    }

    @Override
    public String getPtd()
    {
        TplLocation l = getInfo();
        return l instanceof PublicDeparture ? ((PublicDeparture) l).getPtd() : null;
    }

    @Override
    public String getWta()
    {
        TplLocation l = getInfo();
        return l instanceof WorkArrival ? ((WorkArrival) l).getWta() : null;
    }

    @Override
    public String getWtd()
    {
        TplLocation l = getInfo();
        return l instanceof WorkDeparture ? ((WorkDeparture) l).getWtd() : null;
    }

    @Override
    public String getWtp()
    {
        TplLocation l = getInfo();
        return l instanceof WorkPass ? ((WorkPass) l).getWtp() : null;
    }

    public boolean isPass()
    {
        return getWtp() != null;
    }

    /**
     * The time of arrival, GBTT value takes precedence to WTT
     * <p>
     * @return time or null if no arrival
     */
    public String getArrival()
    {
        String t = getPta();
        return t == null ? getWta() : t;
    }

    /**
     * Is there an arrival time set (GBTT or WTT)
     * <p>
     * @return
     */
    public boolean isSetArrival()
    {
        return getPta() != null || getWta() != null;
    }

    /**
     * The time of departure, GBTT takes precedence to WTT.
     * <p>
     * @return time or null if no departure
     */
    public String getDeparture()
    {
        String t = getPtd();
        return t == null ? getWtd() : t;
    }

    public boolean isSetDeparture()
    {
        return getPtd() != null || getWtd() != null;
    }

    /**
     * Compares {@link TrainMovement} instances using the Darwin ordering scheme.
     * <p>
     * When determining the order of locations when cancelled locations imply an overlapping range of scheduled times, Darwin will sequence the locations based
     * in the working arrival, pass or departure times (as applicable), in that order.
     * <p>
     * For example, given a cancelled location 'A' with a scheduled time of
     * arrival (STA) of 10:00:00 and scheduled time of departure (STD) of 10:05:00, and a second location 'B' with a scheduled passing time (STP) of 10:03:30,
     * location 'A' will be ordered before location 'B'.
     * <p>
     * There is no absolute guarantee that live times are in chronological order, a client must correctly handle the case where a time goes backwards, or just
     * appears to do so because it has crossed a midnight boundary. Darwin uses the following rules to handle these cases:
     * <table>
     * <tr><td>Time difference</td><td>Interpret as</td></tr>
     * <tr><td>Less than -6 hours</td><td>Crossed midnight</td></tr>
     * <tr><td>Between -6 and 0 hours</td><td>Back in time</td></tr>
     * <tr><td>Between 0 and +18 hours</td><td>Normal increasing time</td></tr>
     * <tr><td>Greater than +18 hours</td><td>Back in time and crossed midnight</td></tr>
     * </table>
     * <p>
     * @param a Movement a
     * @param b Movement b
     * <p>
     * @return a negative integer, zero, or a positive integer as a is less than, equal to, or greater than b
     */
    public static int compare( TrainMovement a, TrainMovement b )
    {
        LocalTime ta = getScheduledTime( a );
        LocalTime tb = getScheduledTime( b );
        if( ta == tb ) {
            return 0;
        }
        else if( ta == null ) {
            return -1;
        }
        else if( tb == null ) {
            return 1;
        }

        return TimeUtils.compareLocalTimeDarwin.compare( ta, tb );
    }

    /**
     * Reverse the order defined by {@link TrainMovement#compare(uk.trainwatch.web.train.TrainMovement, uk.trainwatch.web.train.TrainMovement)}.
     * <p>
     * @param a
     * @param b
     *          <p>
     * @return
     *         <p>
     * @see TrainMovement#compare(uk.trainwatch.web.train.TrainMovement, uk.trainwatch.web.train.TrainMovement)
     */
    public static int compareReverse( TrainMovement a, TrainMovement b )
    {
        return -compare( a, b );
    }

    private static LocalTime getScheduledTime( TrainMovement m )
    {
        if( m == null ) {
            return null;
        }
        String t = m.getWta();
        if( t == null ) {
            t = m.getWtp();
        }
        if( t == null ) {
            t = m.getWtd();
        }
        return t == null ? null : TimeUtils.getLocalTime( t );
    }

    public LocalTime getScheduledTime()
    {
        return getScheduledTime( this );
    }

    public LocalTime getExpectedTime()
    {
        LocalTime et = null;

        if( isTsPresent() ) {
            et = getExpectedTime( et, ts.getPass() );
            et = getExpectedTime( et, ts.getArr() );
            et = getExpectedTime( et, ts.getDep() );
        }

        if( et == null ) {
            et = getScheduledTime();
        }

        return et;
    }

    private LocalTime getExpectedTime( LocalTime t, TSTimeData td )
    {
        LocalTime t1 = null;
        if( td != null ) {
            if( td.isSetAt() ) {
                t1 = TimeUtils.getLocalTime( td.getAt() );
            }
            else if( td.isSetEt() ) {
                t1 = TimeUtils.getLocalTime( td.getEt() );
            }
            else if( td.isSetWet() ) {
                t1 = TimeUtils.getLocalTime( td.getWet() );
            }
        }
        return t == null || (t1 != null && t1.isAfter( t )) ? t1 : t;
    }

    private LocalTime getTime( LocalTime t, TSTimeData td )
    {
        if( td != null && td.isSetAt() ) {
            LocalTime t1 = TimeUtils.getLocalTime( td.getAt() );
            if( t == null || (t1 != null && t1.isAfter( t )) ) {
                return t1;
            }
        }
        return t;
    }

    public LocalTime getTime()
    {
        LocalTime t = null;
        if( isTsPresent() ) {
            if( ts.isSetArr() ) {
                t = getTime( t, ts.getArr() );
            }
            if( ts.isSetDep() ) {
                t = getTime( t, ts.getDep() );
            }
            if( ts.isSetPass() ) {
                t = getTime( t, ts.getPass() );
            }
        }
        return t;
    }

    /**
     * Get the delay between the timetabled (GBTT then WTT) and at time for this movement.
     * <p>
     * The search order here for the timetabled time is: ptd, pta, wtd, wta, wtp.
     * <p>
     * @return Duration or null if none
     */
    public Duration getDelay()
    {
        LocalTime at = getTime();
        if( at != null ) {
            String t = ts.getPtd();
            if( t == null ) {
                t = ts.getPta();
            }
            if( t == null ) {
                t = ts.getWtd();
            }
            if( t == null ) {
                t = ts.getWta();
            }
            if( t == null ) {
                t = ts.getWtp();
            }
            LocalTime pt = TimeUtils.getLocalTime( t );
            if( pt != null ) {
                return Duration.between( pt, at );
            }
        }
        return null;
    }

    /**
     * Is a delay available?
     * <p>
     * @return
     */
    public boolean isDelaySet()
    {
        // This should just be isSetAt() but for safety check we have a timetable time
        return isSetAt() && (ts.isSetPta() || ts.isSetPtd() || ts.isSetWta() || ts.isSetWtd() || ts.isSetWtp());
    }

    @Override
    public int compareTo( TrainMovement o )
    {
        return compare( this, o );
    }

    /**
     * Returns the latest TSTimeData for this movement.
     * <p>
     * This is calculated as the most recent value of arr, dep and pass values, allowing for crossing midnight.
     * <p>
     * @return LocalTime or null if none
     */
    public LocalTime getAt()
    {
        if( isSetAt() ) {
            return Stream.<TSTimeData>of( ts.getArr(), ts.getDep(), ts.getPass() ).
                    filter( Objects::nonNull ).
                    map( TSTimeData::getAt ).
                    map( TimeUtils::getLocalTime ).
                    filter( Objects::nonNull ).
                    sorted( TimeUtils.compareLocalTimeDarwin ).
                    findAny().
                    orElse( null );
        }
        else {
            return null;
        }
    }

    /**
     * Does this movement have an AT value set in one of arr, dep or pass
     * <p>
     * @return
     */
    public boolean isSetAt()
    {
        return isTsPresent() && (ts.isSetArr() || ts.isSetDep() || ts.isSetPass());
    }

    public boolean isOrigin()
    {
        return origin;
    }

    void setOrigin( boolean origin )
    {
        this.origin = origin;
    }

    public boolean isDestination()
    {
        return destination;
    }

    void setDestination( boolean destination )
    {
        this.destination = destination;
    }

    public boolean isOriginOrDestination()
    {
        return origin || destination;
    }
}
