/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Enum of the various CIF record types.
 * <p>
 * It also maps against the various factories to the model
 * <p>
 * @author Peter T Mount
 */
public enum RecordType
{

    AA( Association.factory ),
    BS( BasicSchedule.factory ),
    BX( BasicScheduleExtras.factory ),
    CR( ChangesEnRoute.factory ),
    HD( Header.factory ),
    LI( IntermediateLocation.factory ),
    LO( OriginLocation.factory ),
    LT( TerminatingLocation.factory ),
    TI( TIPLOCInsert.factory ),
    ZZ( TrailerRecord.factory ),
    /*
     * According to the CIF End User Specification these have been defined but not implemented & will not appear in
     * extract files.
     *
     * We have them here so in case they do appear CIFParser will ignore them when running in strict mode
     */
    TN( null ),
    LN( null );
    private static final Map<String, RecordType> TYPES = new ConcurrentHashMap<>();

    static
    {
        for( RecordType t : values() )
        {
            TYPES.put( t.toString(), t );
        }
    }

    /**
     * Lookup the CIFRecordType by id.
     * <p>
     * @param id <p>
     * @return
     */
    public static RecordType lookup( String id )
    {
        return id == null ? null : TYPES.get( id );
    }

    private final Function<CIFParser, Record> factory;

    private RecordType( Function<CIFParser, Record> factory )
    {
        this.factory = factory;
    }

    public Function<CIFParser, Record> getFactory()
    {
        return factory;
    }

}
