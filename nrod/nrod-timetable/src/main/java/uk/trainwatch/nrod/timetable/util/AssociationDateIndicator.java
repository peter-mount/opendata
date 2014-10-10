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
public enum AssociationDateIndicator
{
    /**
     * Standard (same day)
     */
    STANDARD("S","Standard (same day)"),
    /**
     * Over next midnight
     */
    OVER_NEXT_MIDNIGHT("N","Over next midnight"),
    /**
     * Over previous midnight
     */
    OVER_PREV_MIDNIGHT("P","Over previous midnight"),
    /**
     * Unknown
     */
    UNKNOWN( " ", "Unknown" );

    private static final Map<String, AssociationDateIndicator> CODES = new HashMap<>();

    static
    {
        for( AssociationDateIndicator bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static AssociationDateIndicator lookup( String s )
    {
        return CODES.getOrDefault(s, AssociationDateIndicator.UNKNOWN );
    }
    private final String code;
    private final String description;

    private AssociationDateIndicator( String code, String description )
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
