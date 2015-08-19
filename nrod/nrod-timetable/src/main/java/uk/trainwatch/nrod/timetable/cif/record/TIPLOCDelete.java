/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.util.function.Function;
import uk.trainwatch.nrod.location.Tiploc;

/**
 *
 * @author Peter T Mount
 */
public class TIPLOCDelete
        extends TIPLOCAction
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    static final Function<CIFParser, Record> factory = p -> new TIPLOCDelete( p.getTiploc() );

    public TIPLOCDelete( Tiploc tiploc )
    {
        super( RecordType.TD, tiploc );
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

}
