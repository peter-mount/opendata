/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author peter
 */
public enum StepType
{

    NULL( "" ),
    BETWEEN( "B" ),
    FROM( "F" ),
    TO( "T" ),
    INTERMEDIATE_FIRST( "D" ),
    CLEAROUT( "C" ),
    INTERPOSE( "I" ),
    INTERMEDIATE( "E" );

    private static final Map<String, StepType> TYPES = new HashMap<>();

    static
    {
        for( StepType e: values() )
        {
            TYPES.put( e.getType(), e );
        }
    }

    public static StepType getType( String t )
    {
        return TYPES.get( t );
    }
    
    private final String type;

    private StepType( String type )
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

}
