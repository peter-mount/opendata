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
    BUS( "B", "Bus (Permanent)" ),
    /**
     * Freight (Permanent - WTT)
     */
    FREIGHT( "F", "Freight (Permanent - WTT)" ),
    /**
     * Passenger &amp; Parcels (Permanent - WTT)
     */
    PASSENGER_PARCELS( "P", "Passenger &amp; Parcels (Permanent - WTT)" ),
    /**
     * Ship (Permanent)
     */
    SHIP( "S", "Ship (Permanent)" ),
    /**
     * Trip (Permanent)
     */
    TRIP( "T", "Trip (Permanent)" ),
    /**
     * Passenger &amp; Parcels (Short Term Planning)
     */
    STP_PASSENGER_PARCELS( "1", "Passenger &amp; Parcels (Short Term Planning)" ),
    /**
     * Freight (Short Term Planning)
     */
    STP_FREIGHT( "2", "Freight (Short Term Planning)" ),
    /**
     * Trip (Short Term Planning)
     */
    STP_TRIP( "3", "Trip (Short Term Planning)" ),
    /**
     * Ship (Short Term Planning)
     */
    STP_SHIP( "4", "Ship (Short Term Planning)" ),
    /**
     * Bus (Short Term Planning)
     */
    STP_BUS( "5", "Bus (Short Term Planning)" ),
    /**
     * Unknown
     */
    UNKNOWN( " ", "" );

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
    private final String desc;

    private TrainStatus( String code, String desc )
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return desc;
    }

}
