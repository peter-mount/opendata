/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Train Specific Note
 * <p>
 * Note: The CIF End User Specification states that this is not currently (July 2003) in use.
 * <p>
 * @author Peter T Mount
 */
public class TrainNote
        extends Note
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    static final Function<CIFParser, Record> factory = p -> new TrainNote(
            p.getString( 1 ),
            p.getString( 77 )
    );

    public TrainNote( String type, String note )
    {
        super( RecordType.TN, note, note );
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

}
