/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonObject;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.ATSCode;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public class BasicScheduleExtras
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    static final Function<CIFParser, Record> factory = p -> new BasicScheduleExtras(
            p.skip( 4 ),
            p.getInt( 5 ),
            p.getATOCCode(),
            p.getATSCode()
    );

    public static final Function<JsonObject, BasicScheduleExtras> fromJson = o -> new BasicScheduleExtras(
            null,
            JsonUtils.getInt( o, "uicCode" ),
            JsonUtils.getEnum( ATOCCode::lookup, o, "atocCode" ),
            JsonUtils.getEnum( ATSCode::lookup, o, "atsCode" )
    );

    public static final Function<BasicScheduleExtras, JsonObject> toJson = s -> Json.createObjectBuilder().
            add( "uicCode", s.getUicCode() ).
            add( "atocCode", s.getAtocCode().getCode() ).
            add( "atsCode", s.getApplicableTimetableCode().getCode() ).
            build();

    // No longer used
    //private final String tractionClass;
    private final int uicCode;
    private final ATOCCode atocCode;
    private final ATSCode applicableTimetableCode;

    public BasicScheduleExtras( Void tractionClass,
                                int uicCode,
                                ATOCCode atocCode,
                                ATSCode applicableTimetableCode )
    {
        super( RecordType.BX );
        this.uicCode = uicCode;
        this.atocCode = atocCode;
        this.applicableTimetableCode = applicableTimetableCode;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

    public int getUicCode()
    {
        return uicCode;
    }

    public ATOCCode getAtocCode()
    {
        return atocCode;
    }

    public ATSCode getApplicableTimetableCode()
    {
        return applicableTimetableCode;
    }

}
