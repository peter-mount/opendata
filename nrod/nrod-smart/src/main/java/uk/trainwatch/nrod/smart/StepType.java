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
    private static final StepType IDS[] = values();

    static {
        for( StepType e: values() ) {
            TYPES.put( e.getType(), e );
        }
    }

    public static StepType getId( int i )
    {
        return i < 0 || i >= IDS.length ? null : IDS[i];
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
