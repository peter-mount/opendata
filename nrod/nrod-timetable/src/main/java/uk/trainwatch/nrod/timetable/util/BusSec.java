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
public enum BusSec
{

    /**
     * Portion Identifier used by NRS system
     */
    N0( "0", "Portion Identifier used by NRS system" ),
    /**
     * Portion Identifier used by NRS system
     */
    N1( "1", "Portion Identifier used by NRS system" ),
    /**
     * Portion Identifier used by NRS system
     */
    N2( "2", "Portion Identifier used by NRS system" ),
    /**
     * Portion Identifier used by NRS system
     */
    N4( "4", "Portion Identifier used by NRS system" ),
    /**
     * Portion Identifier used by NRS system
     */
    N8( "8", "Portion Identifier used by NRS system" ),
    /**
     * Train may be used to convey Red Star parcels (redundant but may still appear)
     */
    Z( "Z", "Train may be used to convey Red Star parcels (redundant but may still appear)" ),
    /**
     * Unknown
     */
    UNKNOWN( "  ", "Unknown" );

    private static final Map<String, BusSec> CODES = new HashMap<>();

    static
    {
        for( BusSec bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static BusSec lookup( String s )
    {
        return CODES.getOrDefault( s, BusSec.UNKNOWN );
    }
    private final String code;
    private final String description;

    private BusSec( String code, String description )
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
