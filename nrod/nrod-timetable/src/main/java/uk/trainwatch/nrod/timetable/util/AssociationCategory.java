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
public enum AssociationCategory
{

    /**
     * Train joins another
     */
    JOIN( "JJ", "Train Joins" ),
    /**
     * Train divides
     */
    DIVIDE( "VV", "Train divides" ),
    /**
     * Next train
     */
    NEXT( "NP", "Next" ),
    /**
     * Unknown
     */
    UNKNOWN( "  ", "Unknown" );

    private static final Map<String, AssociationCategory> CODES = new HashMap<>();

    static
    {
        for( AssociationCategory bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static AssociationCategory lookup( String s )
    {
        return CODES.getOrDefault( s, AssociationCategory.UNKNOWN );
    }
    private final String code;
    private final String description;

    private AssociationCategory( String code, String description )
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
