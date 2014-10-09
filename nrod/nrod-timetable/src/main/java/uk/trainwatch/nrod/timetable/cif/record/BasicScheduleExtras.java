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
public class BasicScheduleExtras
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new BasicScheduleExtras(
            p.getString( 4 ),
            p.getInt( 5 ),
            p.getString( 2 ),
            p.getString( 1 ),
            p.getString( 8 ),
            p.getString( 1 )
    );

    private final String tractionClass;
    private final int uicCode;
    private final String atocCode;
    private final String applicableTimetableCode;
    private final String reserved1;
    private final String reserved2;

    public BasicScheduleExtras( String tractionClass, int uicCode, String atocCode, String applicableTimetableCode,
                                String reserved1, String reserved2 )
    {
        super( RecordType.BX );
        this.tractionClass = tractionClass;
        this.uicCode = uicCode;
        this.atocCode = atocCode;
        this.applicableTimetableCode = applicableTimetableCode;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
    }

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
    }

    public String getTractionClass()
    {
        return tractionClass;
    }

    public int getUicCode()
    {
        return uicCode;
    }

    public String getAtocCode()
    {
        return atocCode;
    }

    public String getApplicableTimetableCode()
    {
        return applicableTimetableCode;
    }

    public String getReserved1()
    {
        return reserved1;
    }

    public String getReserved2()
    {
        return reserved2;
    }

    @Override
    public String toString()
    {
        return "BasicScheduleExtras{" + "tractionClass=" + tractionClass + ", uicCode=" + uicCode + ", atocCode=" + atocCode + ", applicableTimetableCode=" + applicableTimetableCode + ", reserved1=" + reserved1 + ", reserved2=" + reserved2 + '}';
    }

}
