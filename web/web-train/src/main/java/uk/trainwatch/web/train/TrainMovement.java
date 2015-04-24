/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.time.LocalTime;
import java.util.Objects;
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

    private final String tpl;
    private TSLocation ts;
    private TplLocation scheduled;

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

    public String getArrival()
    {
        String t = getPta();
        return t == null ? getWta() : t;
    }

    public String getDeparture()
    {
        String t = getPtd();
        return t == null ? getWtd() : t;
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
        LocalTime ta = getTime( a );
        LocalTime tb = getTime( b );
        if( ta == tb ) {
            return 0;
        }
        else if( ta == null ) {
            return -1;
        }
        else if( tb == null ) {
            return 1;
        }

        // Handle crossing midnight
        if( Math.abs( ta.getHour() - tb.getHour() ) > 18 ) {
            return tb.compareTo( ta );
        }
        else {
            return ta.compareTo( tb );
        }
    }

    private static LocalTime getTime( TrainMovement m )
    {
        String t = m.getWta();
        if( t == null ) {
            t = m.getWtp();
        }
        if( t == null ) {
            t = m.getWtd();
        }
        return t == null ? null : TimeUtils.getLocalTime( t );
    }

    public LocalTime getExpectedTime()
    {
        return getTime( this );
    }

    private LocalTime getTime( LocalTime t, TSTimeData td )
    {
        if( td.isSetAt() ) {
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
            if( ts.isSetArr() && ts.getArr().isSetAt() ) {
                t = getTime( t, ts.getArr() );
            }
            if( ts.isSetDep() && ts.getDep().isSetAt() ) {
                t = getTime( t, ts.getDep() );
            }
            if( ts.isSetPass() && ts.getPass().isSetAt() ) {
                t = getTime( t, ts.getPass() );
            }
        }
        return t;
    }

    @Override
    public int compareTo( TrainMovement o )
    {
        return compare( this, o );
    }

}
