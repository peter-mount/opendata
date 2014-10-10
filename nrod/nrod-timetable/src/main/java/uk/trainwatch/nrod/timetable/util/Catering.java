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
public enum Catering
{

    /**
     * Buffet Service
     */
    BUFFET( "C", "Buffet Service" ),
    /**
     * Restaurant Car available for First Class passengers
     */
    RESTAURANT_CAR( "F", "Restaurant Car available for First Class passengers" ),
    /**
     * Service of hot food available
     */
    HOT_FOOD( "H", "Service of hot food available" ),
    /**
     * Meal included for First Class passengers
     */
    MEAL_INCLUDED( "M", "Meal included for First Class passengers" ),
    /**
     * Wheelchair only reservations
     */
    WHEELCHAIR( "P", "Wheelchair only reservations" ),
    /**
     * Restaurant
     */
    RESTAURANT( "R", "Restaurant" ),
    /**
     * Trolley Service
     */
    TROLLEY_SERVICE( "T", "Trolley Service" ),
    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown" );

    private static final Map<String, Catering> CODES = new HashMap<>();

    static
    {
        for( Catering bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static Catering lookup( String s )
    {
        return CODES.getOrDefault( s, Catering.UNKNOWN );
    }

    public static Catering[] lookupAll( String s )
    {
        Catering[] r = new Catering[s.length()];
        int j = 0;
        for( int i = 0; i < r.length && j < r.length; i++ )
        {
            Catering oc = lookup( s.substring( i, i + 1 ) );
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

    private Catering( String code, String description )
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
