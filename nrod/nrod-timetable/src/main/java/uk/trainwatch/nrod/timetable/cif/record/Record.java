/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;

/**
 *
 * @author Peter T Mount
 */
public abstract class Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    private final RecordType type;

    public Record( RecordType type )
    {
        this.type = type;
    }

    public final RecordType getRecordType()
    {
        return type;
    }

    public abstract void accept( RecordVisitor v );
}
