/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter T Mount
 */
public enum TrainStatus
{
    /**
     * Bus (Permanent)
     */
    BUS( "B" ),
    /**
     * Freight (Permanent - WTT)
     */
    FREIGHT( "F" ),
    /**
     * Passenger &amp; Parcels (Permanent - WTT)
     */
    PASSENGER_PARCELS( "P" ),
    /**
     * Ship (Permanent)
     */
    SHIP( "S" ),
    /**
     * Trip (Permanent)
     */
    TRIP( "T" ),
    /**
     * Passenger &amp; Parcels (Short Term Planning)
     */
    STP_PASSENGER_PARCELS( "1" ),
    /**
     * Freight (Short Term Planning)
     */
    STP_FREIGHT( "2" ),
    /**
     * Trip (Short Term Planning)
     */
    STP_TRIP( "3" ),
    /**
     * Ship (Short Term Planning)
     */
    STP_SHIP( "4" ),
    /**
     * Bus (Short Term Planning)
     */
    STP_BUS( "5" ),
    /**
     * Unknown
     */
    UNKNOWN( " " );

    private static final Map<String, TrainStatus> CODES = new HashMap<>();

    static
    {
        for( TrainStatus bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static TrainStatus lookup( String s )
    {
        return CODES.getOrDefault( s, TrainStatus.UNKNOWN );
    }
    private final String code;

    private TrainStatus( String code )
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

}
