/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import uk.trainwatch.nrod.location.Tiploc;

/**
 *
 * @author Peter T Mount
 */
public abstract class TIPLOCAction
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Tiploc tiploc;

    public TIPLOCAction( RecordType type, Tiploc tiploc )
    {
        super( type );
        this.tiploc = tiploc;
    }

    public final Tiploc getTiploc()
    {
        return tiploc;
    }

}
