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
public enum STPIndicator
{

    /**
     * No sleepers
     */
    N( " ", "Non overlay user?" ),
    /**
     * STP Cancellation of Permanent schedule
     */
    STP_CANCELLATION( "C", "STP Cancellation of Permanent schedule" ),
    /**
     * New STP schedule (not an overlay)
     */
    STP_NEW( "N", "New STP schedule (not an overlay)" ),
    /**
     * STP overlay of Permanent schedule
     */
    STP_OVERLAY( "O", "STP overlay of Permanent schedule" ),
    /**
     * Permanent schedule
     */
    PERMANENT( "P", "Permanent schedule" );

    private static final Map<String, STPIndicator> CODES = new HashMap<>();

    static
    {
        for( STPIndicator bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static STPIndicator lookup( String s )
    {
        return CODES.getOrDefault( s, STPIndicator.N );
    }
    private final String code;
    private final String description;

    private STPIndicator( String code, String description )
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
