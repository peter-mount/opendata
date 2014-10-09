/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
public class TrailerRecord
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new TrailerRecord();

    public TrailerRecord()
    {
        super( RecordType.ZZ );
    }

}
