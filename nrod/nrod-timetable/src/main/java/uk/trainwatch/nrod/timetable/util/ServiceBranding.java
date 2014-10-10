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
public enum ServiceBranding
{

    /**
     * No branding
     */
    N( " ", "None" ),
    /**
     * Eurostar
     */
    EUROSTAR( "E", "Eurostar" ),
    /**
     * Alphaline
     */
    ALPHALINE( "U", "Alphaline" );

    private static final Map<String, ServiceBranding> CODES = new HashMap<>();

    static
    {
        for( ServiceBranding bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static ServiceBranding lookup( String s )
    {
        return CODES.getOrDefault( s, ServiceBranding.N );
    }
    
    public static ServiceBranding[] lookupAll( String s )
    {
        ServiceBranding[] r = new ServiceBranding[s.length()];
        int j = 0;
        for( int i = 0; i < r.length && j < r.length; i++ )
        {
            ServiceBranding oc = lookup( s.substring( i, i + 1 ) );
            if( oc != N )
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

    private ServiceBranding( String code, String description )
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
