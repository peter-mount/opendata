/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Applicable Timetable Schedule (ATS)
 * <p>
 * @author Peter T Mount
 */
public enum ATSCode
{

    /**
     * Train is subject to performance monitoring (Applicable Timetable Service)
     */
    Y( "Y", "Train is subject to performance monitoring" ),
    /**
     * Train is not subject to performance monitoring (Not Applicable Timetable Service)
     */
    N( "N", "Train is not subject to performance monitoring" ),
    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown performance monitoring" );

    private static final Map<String, ATSCode> CODES = new HashMap<>();

    static
    {
        for( ATSCode bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static ATSCode lookup( String s )
    {
        return CODES.getOrDefault( s, ATSCode.N );
    }
    private final String code;
    private final String description;

    private ATSCode( String code, String description )
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
