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
public enum BankHolidayRunning
{

    /**
     * Does not run on specified Bank Holiday Mondays
     */
    X( "X", "Not on Bank Holiday" ),
    /**
     * Does not run on specified Edinburgh Holiday dates (no longer used)
     */
    E( "E", "Not on Edinburgh Holiday" ),
    /**
     * Does not run on specified Glasgow Holiday dates
     */
    G( "G", "Not on Glasgow Holidays" ),
    /**
     * All other values
     */
    UNKNOWN( " ", "" );

    private static final Map<String, BankHolidayRunning> CODES = new HashMap<>();

    static
    {
        for( BankHolidayRunning bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static BankHolidayRunning lookup( String s )
    {
        return CODES.getOrDefault( s, BankHolidayRunning.UNKNOWN );
    }
    private final String code;
    private final String desc;

    private BankHolidayRunning( String code, String desc )
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
