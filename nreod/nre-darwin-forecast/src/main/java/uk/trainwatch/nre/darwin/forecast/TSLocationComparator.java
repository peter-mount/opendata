/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast;

import java.time.LocalTime;
import java.util.Comparator;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSLocation;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSTimeData;
import uk.trainwatch.util.TimeUtils;

/**
 * A comparator that will sort {@link TSLocation} into time order
 * <p>
 * @author peter
 */
public enum TSLocationComparator
        implements Comparator<TSLocation>
{

    INSTANCE;

    @Override
    public int compare( TSLocation o1, TSLocation o2 )
    {
        LocalTime t1 = getTime( o1 );
        LocalTime t2 = getTime( o2 );

        // Both null?
        if( t1 == t2 ) {
            return 0;
        }

        // Should not occur unless data is corrupt
        if( t1 == null ) {
            return 1;
        }
        else if( t2 == null ) {
            return -1;
        }

        // Account for crossing midnight? then reverse
        if( Math.abs( t1.getHour() - t2.getHour() ) > 20 ) {
            return t2.compareTo( t1 );
        }

        // If the same then check for a pass, put the pass before the at
        // Sorts out an issue where a station passed at the same minute as an arrival gets put after the station
        int r = t1.compareTo( t2 );
        if( r == 0 ) {
            // Same time, so if we have a pass that comes first before an at
            TSTimeData pass1 = o1.getPass();
            TSTimeData pass2 = o2.getPass();
            if( pass1 != null ) {
                if( pass1 == pass2 ) {
                    r = 0;
                }
                else if( pass2 == null ) {
                    return -1;
                }
                else {
                    r = compare( t2, getTime( pass1 ) );
                }
            }
            else if( pass2 != null ) {
                if( pass1 == null ) {
                    return 1;
                }
                else {
                    r = compare( getTime( pass1 ), t1 );
                }
            }
        }

        return r;
    }

    private int compare( LocalTime a, LocalTime b )
    {
        if( a == b ) {
            return 0;
        }
        if( a == null ) {
            return 1;
        }
        if( b == null ) {
            return -1;
        }
        return a.compareTo( b );
    }

    private LocalTime getTime( TSTimeData td )
    {
        String t = td.getAt();
        if( t == null ) {
            t = td.getEt();
        }
        if( t == null ) {
            t = td.getWet();
        }
        return t == null ? null : TimeUtils.getLocalTime( t );
    }

    private LocalTime getTime( TSLocation o )
    {
        TSTimeData td = o.getArr();
        if( td == null ) {
            td = o.getPass();
        }
        if( td == null ) {
            td = o.getDep();
        }
        if( td == null ) {
            return null;
        }

        return getTime( td );
    }
}
