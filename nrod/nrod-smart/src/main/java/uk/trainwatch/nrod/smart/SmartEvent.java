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
public enum SmartEvent
{

    NOP( "" ),
    ARRIVE_UP( "A" ),
    DEPART_UP( "B" ),
    ARRIVE_DOWN( "C" ),
    DEPART_DOWN( "D" );

    private static final Map<String, SmartEvent> TYPES = new HashMap<>();

    static
    {
        for( SmartEvent e: values() )
        {
            TYPES.put( e.getType(), e );
        }
    }

    public static SmartEvent getType( String t )
    {
        return TYPES.get( t );
    }

    private final String type;

    private SmartEvent( String type )
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

}
