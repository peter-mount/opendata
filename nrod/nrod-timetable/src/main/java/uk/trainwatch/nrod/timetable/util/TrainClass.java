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
public enum TrainClass
{

    /**
     * First &amp; Standard seats
     */
    UNKNOWN( " ", "First & Standard seats" ),
    /**
     * First &amp; Standard seats
     */
    B( "B", "First & Standard seats" ),
    /**
     * Standard class only
     */
    S( "S", "Standard class only" );

    private static final Map<String, TrainClass> CODES = new HashMap<>();

    static
    {
        for( TrainClass bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static TrainClass lookup( String s )
    {
        return CODES.getOrDefault( s, TrainClass.UNKNOWN );
    }
    private final String code;
    private final String description;

    private TrainClass( String code, String description )
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
