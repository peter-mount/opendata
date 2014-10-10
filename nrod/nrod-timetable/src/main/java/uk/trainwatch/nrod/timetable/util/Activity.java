/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter T Mount
 */
public enum Activity
{

    /**
     * Stops or shunts for other trains to pass
     */
    STOP_SHUNT( "A", "Stops or shunts for other trains to pass" ),
    /**
     * Attach/detach assisting locomotive
     */
    ATTACH_DETACH_LOCO( "AE", "Attach/detach assisting locomotive" ),
    /**
     * Stops for banking locomotive
     */
    STOP_BANKING( "BL", "Stops for banking locomotive" ),
    /**
     * Stops to change trainmen
     */
    STOP_CHANGE_STAFF( "C", "Stops to change trainmen" ),
    /**
     * Stops to set down passengers
     */
    STOP_SET_DOWN_PASSENGERS( "D", "Stops to set down passengers" ),
    /**
     * Stops to detach vehicles
     */
    STOP_DETACH_VEHICLES( "-D", "Stops to detach vehicles" ),
    /**
     * Stops for examination
     */
    STOP_EXAM( "E", "Stops for examination" ),
    /**
     * National Rail Timetable data to add
     */
    NRT_DATA_ADD( "G", "National Rail Timetable data to add" ),
    /**
     * Notional activity to prevent WTT timing columns merge
     */
    NOTIONAL( "H", "Notional activity to prevent WTT timing columns merge" ),
    /**
     * Notional activity to prevent WTT timing columns merge
     */
    NOTIONAL_3COL( "HH", "Notional activity to prevent three WTT timing columns merge" ),
    /**
     * Passenger Count Point
     */
    PASSENGER_COUNT( "K", "Passenger Count Point" ),
    /**
     * Ticket collection and examination point
     */
    TICKET_COLLECT_EXAM( "KC", "Ticket collection and examination point" ),
    /**
     * Ticket examination point
     */
    TICKET_EXAM( "KE", "Ticket examination point" ),
    /**
     * Ticket examination point, 1st Class only
     */
    TICKET_EXAM_1ST( "KF", "Ticket examination point, 1st Class only" ),
    /**
     * Selective Ticket examination point
     */
    TICKET_EXAM_SELECTIVE( "KS", "Selective Ticket examination point" ),
    /**
     * Stops to change locomotives
     */
    STOP_CHANGE_LOCO( "L", "Stops to change locomotives" ),
    /**
     * Stop not advertised
     */
    STOP_NOT_ADVERTISED( "N", "Stop not advertised" ),
    /**
     * Stops for other operating reasons
     */
    STOP_OTHER_REASON( "OP", "Stops for other operating reasons" ),
    /**
     * Train Locomotive on rear
     */
    LOCO_ON_REAR( "OR", "Train Locomotive on rear" ),
    /**
     * Propelling between points shown
     */
    PROPELLING( "PR", "Propelling between points shown" ),
    /**
     * Stops when required
     */
    STOP_WHEN_REQUIRED( "R", "Stops when required" ),
    /**
     * Reversing movement or driver changes ends
     */
    REVERSE( "RM", "Reversing movement or driver changes ends" ),
    /**
     * Stops for locomotive to run round train
     */
    STOP_LOCO_RUN_ROUND( "RR", "Stops for locomotive to run round train" ),
    /**
     * Stops for railway personnel only
     */
    STOP_STAFF_ONLY( "S", "Stops for railway personnel only" ),
    /**
     * Stops to take up and set down passengers
     */
    STOP_PASSENGERS( "T", "Stops to take up and set down passengers" ),
    /**
     * Stops to attach and detach vehicles
     */
    STOP_ATTACH_DETACH( "-T", "Stops to attach and detach vehicles" ),
    /**
     * Train begins (Origin)
     */
    BEGIN( "TB", "Train begins (Origin)" ),
    /**
     * Train finishes (Destination)
     */
    FINISH( "TF", "Train finishes (Destination)" ),
    /**
     * Detail Consist for TOPS Direct requested by EWS
     */
    TOPS_DIRECT( "TS", "Detail Consist for TOPS Direct requested by EWS" ),
    /**
     * Stops (or at pass) for tablet, staff or token
     */
    STOP_TABLET_STAFF_TOKEN( "TW", "Stops (or at pass) for tablet, staff or token" ),
    /**
     * Stops to take up passengers
     */
    STOP_PASSENGERS_TAKE_UP( "U", "Stops to take up passengers" ),
    /**
     * Stops to attach vehicles
     */
    STOP_ATTACH( "-U", "Stops to attach vehicles" ),
    /**
     * Stops for watering of coaches
     */
    STOP_WATERING( "W", "Stops for watering of coaches" ),
    /**
     * Passes another train at crossing point on single line
     */
    STOP_PASS_TRAIN( "X", "Passes another train at crossing point on single line" ),
    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown" );

    private static final Map<String, Activity> CODES = new HashMap<>();

    static
    {
        for( Activity bhx : values() )
        {
            CODES.put( bhx.code, bhx );
            if( bhx.code.length() == 1 )
            {
                CODES.put( " " + bhx.code, bhx );
                CODES.put( bhx.code + " ", bhx );
            }
        }
    }

    public static Activity lookup( String s )
    {
        return CODES.getOrDefault( s, Activity.UNKNOWN );
    }

    public static Activity[] lookupAll( String s )
    {
        Activity[] r = new Activity[s.length() >> 1];
        int j = 0;
        for( int i = 0; i < s.length() && j < r.length; i += 2 )
        {
            Activity oc = lookup( s.substring( i, i + 2 ) );
            if( oc != UNKNOWN )
            {
                r[j++] = oc;
            }
        }

        if( j < r.length )
        {
            r = Arrays.copyOf( r, j );
        }
        return r;
    }
    private final String code;
    private final String description;

    private Activity( String code, String description )
    {
        this.code = code;
        this.description = description;
    }

    /**
     * The code within the timetable
     * <p>
     * @return
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Human readable description
     * <p>
     * @return
     */
    public String getDescription()
    {
        return description;
    }

}
