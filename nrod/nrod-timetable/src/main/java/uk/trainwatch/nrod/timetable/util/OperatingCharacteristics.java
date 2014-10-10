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
public enum OperatingCharacteristics
{

    /**
     * Vacuum Braked
     */
    B( "B", "Vacuum Braked" ),
    /**
     * Timed at 100mph
     */
    C( "C", "Timed at 100mph" ),
    /**
     * DOO (Coaching stock trains)
     */
    D( "D", "DOO (Coaching stock trains)" ),
    /**
     * Conveys Mark 4 Coaches
     */
    E( "E", "Conveys Mark 4 Coaches" ),
    /**
     * Trainman (Guard) required
     */
    G( "G", "Trainman (Guard) required" ),
    /**
     * Timed at 110 mph
     */
    M( "M", "Timed at 110 mph" ),
    /**
     * Push/Pull Train
     */
    P( "P", "Push/Pull Train" ),
    /**
     * Runs as required
     */
    Q( "Q", "Runs as required" ),
    /**
     * Air conditioned with PA system
     */
    R( "R", "Air conditioned with PA system" ),
    /**
     * Steam heated
     */
    S( "S", "Steam heated" ),
    /**
     * Runs to Terminals/Yards as required
     */
    Y( "Y", "Runs to Terminals/Yards as required" ),
    /**
     * May convey traffic to SB1C gauge. Not to be diverted from booked route without authority
     */
    Z( "Z", "May convey traffic to SB1C gauge. Not to be diverted without authority" ),

    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown" );

    private static final Map<String, OperatingCharacteristics> CODES = new HashMap<>();

    static
    {
        for( OperatingCharacteristics bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static OperatingCharacteristics lookup( String s )
    {
        return CODES.getOrDefault( s, OperatingCharacteristics.UNKNOWN );
    }

    public static OperatingCharacteristics[] lookupAll( String s )
    {
        OperatingCharacteristics[] r = new OperatingCharacteristics[s.length()];
        int j = 0;
        for( int i = 0; i < r.length && j < r.length; i++ )
        {
            OperatingCharacteristics oc = lookup( s.substring( i, i + 1 ) );
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

    private OperatingCharacteristics( String code, String description )
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
