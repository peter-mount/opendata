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
public enum PowerType
{

    /**
     * Diesel
     */
    D( "D", "Diesel" ),
    /**
     * Diesel Electric Multiple Unit
     */
    DEM( "DEM", "Diesel Electric Multiple Unit" ),
    /**
     * Diesel Mechanical Multiple Unit
     */
    DMU( "DMU", "Diesel Mechanical Multiple Unit" ),
    /**
     * Electric
     */
    E( "E", "Electric" ),
    /**
     * Electro-Diesel
     */
    ED( "ED", "Electro-Diesel" ),
    /**
     * EMU plus D, E, ED locomotive
     */
    EML( "EML", "EMU plus D, E, ED locomotive" ),
    /**
     * Electric Multiple Unit
     */
    EMU( "EMU", "Electric Multiple Unit" ),
    /**
     * Electric Parcels Unit
     */
    EPU( "EPU", "Electric Parcels Unit" ),
    /**
     * High Speed Train
     */
    HST( "HST", "High Speed Train" ),
    /**
     * Diesel Shunting Locomotive
     */
    LDS( "LDS", "Diesel Shunting Locomotive" ),
    /**
     * Unknown
     */
    UNKNOWN( "  ", "Unknown" );

    private static final Map<String, PowerType> CODES = new HashMap<>();

    static
    {
        for( PowerType bhx : values() )
        {
            CODES.put( bhx.code, bhx );
            CODES.put( bhx.code.trim(), bhx );
        }
    }

    public static PowerType lookup( String s )
    {
        return CODES.getOrDefault( s, PowerType.UNKNOWN );
    }
    private final String code;
    private final String description;

    private PowerType( String code, String description )
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
