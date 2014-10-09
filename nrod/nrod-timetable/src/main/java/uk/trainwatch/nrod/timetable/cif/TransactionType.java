/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Peter T Mount
 */
public enum TransactionType
{

    NEW( "N" ),
    DELETE( "D" ),
    REVISE( "R" );

    private static final Map<String, TransactionType> CODES = new HashMap<>();

    static
    {
        for( TransactionType tt : values() )
        {
            CODES.put( tt.getCode(), tt );
        }
    }

    public static TransactionType lookup( String code )
    {
        return Objects.requireNonNull( CODES.get( code ), () -> "Invalid TransactionType \"" + code + "\"" );
    }
    
    private final String code;

    private TransactionType( String code )
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

}
