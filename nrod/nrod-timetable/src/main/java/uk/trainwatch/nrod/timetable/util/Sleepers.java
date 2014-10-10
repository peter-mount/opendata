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
public enum Sleepers
{

    /**
     * No sleepers
     */
    N( " ", "None" ),
    /**
     * First &amp; Standard seats
     */
    B( "B", "First & Standard class" ),
    /**
     * First Class only
     */
    F( "F", "First Class only" ),
    /**
     * Standard class only
     */
    S( "S", "Standard class only" );

    private static final Map<String, Sleepers> CODES = new HashMap<>();

    static
    {
        for( Sleepers bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static Sleepers lookup( String s )
    {
        return CODES.getOrDefault( s, Sleepers.N );
    }
    private final String code;
    private final String description;

    private Sleepers( String code, String description )
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
