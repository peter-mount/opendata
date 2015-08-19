/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
public class TrailerRecord
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    static final Function<CIFParser, Record> factory = p -> new TrailerRecord();

    public TrailerRecord()
    {
        super( RecordType.ZZ );
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

}
