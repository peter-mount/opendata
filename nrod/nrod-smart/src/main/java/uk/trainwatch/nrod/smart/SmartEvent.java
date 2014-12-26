/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter
 */
@XmlRootElement
public enum SmartEvent
{

    NOP( "" ),
    ARRIVE_UP( "A" ),
    DEPART_UP( "B" ),
    ARRIVE_DOWN( "C" ),
    DEPART_DOWN( "D" );

    private static final Map<String, SmartEvent> TYPES = new HashMap<>();

    private static final SmartEvent IDS[] = values();

    static {
        for( SmartEvent e: values() ) {
            TYPES.put( e.getType(), e );
        }
    }

    public static SmartEvent getId( int i )
    {
        return i < 0 || i >= IDS.length ? null : IDS[i];
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
