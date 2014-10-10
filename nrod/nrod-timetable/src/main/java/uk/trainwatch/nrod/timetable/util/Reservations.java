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
public enum Reservations
{

    /**
     * Seat Reservations Compulsory
     */
    A( "A", "Seat Reservations Compulsory" ),
    /**
     * Reservations for Bicycles Essential
     */
    E( "E", "Reservations for Bicycles Essential" ),
    /**
     * Seat Reservations Recommended
     */
    R( "R", "Seat Reservations Recommended" ),
    /**
     * Seat Reservations possible from any station
     */
    S( "S", "Seat Reservations possible from any station" ),
    /**
     * No reservations
     */
    N( " ", "None" );

    private static final Map<String, Reservations> CODES = new HashMap<>();

    static
    {
        for( Reservations bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static Reservations lookup( String s )
    {
        return CODES.getOrDefault( s, Reservations.N );
    }
    private final String code;
    private final String description;

    private Reservations( String code, String description )
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
