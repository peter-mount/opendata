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
public enum AssociationType
{

    /**
     * Passenger use
     */
    PASSENGER_USE( "P", "Passenger use" ),
    /**
     * Operating use only
     */
    OPERATING_USE( "O", "Operating use only" ),
    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown" );

    private static final Map<String, AssociationType> CODES = new HashMap<>();
    private static final Map<Integer, AssociationType> IDS = new HashMap<>();

    static
    {
        for( AssociationType bhx : values() )
        {
            CODES.put( bhx.code, bhx );
            IDS.put( bhx.ordinal(), bhx );
        }
    }

    public static AssociationType lookup( int i )
    {
        return IDS.getOrDefault( i, UNKNOWN );
    }

    public static AssociationType lookup( String s )
    {
        return CODES.getOrDefault( s, AssociationType.UNKNOWN );
    }
    private final String code;
    private final String description;

    private AssociationType( String code, String description )
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
