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
    JOIN( "JJ", "Train Joins", "Joined by" ),
    /**
     * Train divides
     */
    DIVIDE( "VV", "Train divides", "Divides into" ),
    /**
     * Next train
     */
    NEXT( "NP", "Next", "Becomes" ),
    /**
     * Unknown
     */
    UNKNOWN( "  ", "Unknown", "" ),
    /**
     * Train is linked.
     * <p>
     * Not defined in CIF but it is in Darwin.
     */
    LK( "LK", "Linked", "Linked" );

    private static final Map<String, AssociationCategory> CODES = new HashMap<>();
    private static final Map<Integer, AssociationCategory> IDS = new HashMap<>();

    static {
        for( AssociationCategory bhx: values() ) {
            CODES.put( bhx.code, bhx );
            IDS.put( bhx.ordinal(), bhx );
        }
    }

    public static AssociationCategory lookup( int i )
    {
        return IDS.getOrDefault( i, UNKNOWN );
    }

    public static AssociationCategory lookup( String s )
    {
        return CODES.getOrDefault( s, AssociationCategory.UNKNOWN );
    }
    private final String code;
    private final String description;
    private final String legend;

    private AssociationCategory( String code, String description, String legend )
    {
        this.code = code;
        this.description = description;
        this.legend = legend;
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

    public String getLegend()
    {
        return legend;
    }

}
