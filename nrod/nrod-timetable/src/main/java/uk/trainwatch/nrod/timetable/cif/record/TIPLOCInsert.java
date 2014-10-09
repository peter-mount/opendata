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
public class TIPLOCInsert
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new TIPLOCInsert(
            p.getString( 7 ),
            p.getInt( 2 ),
            p.getInt( 6 ),
            p.getString( 1 ),
            p.getString( 26 ),
            p.getLong( 5 ),
            p.getInt( 4 ),
            p.getString( 3 ),
            p.getString( 16 )
    // Spare 8
    );
    private final String tiplocCode;
    private final int caps;
    private final int nalco;
    private final String nlcCheck;
    private final String tpsDescription;
    private final long stanox;
    // Unused
    private final int PO_Loc_Code;
    private final String crs;
    private final String description;

    public TIPLOCInsert( String tiplocCode, int caps, int nalco, String nlcCheck, String tpsDescription, long stanox,
                         int PO_Loc_Code, String crs, String description )
    {
        super( RecordType.TI );
        this.tiplocCode = tiplocCode;
        this.caps = caps;
        this.nalco = nalco;
        this.nlcCheck = nlcCheck;
        this.tpsDescription = tpsDescription;
        this.stanox = stanox;
        this.PO_Loc_Code = PO_Loc_Code;
        this.crs = crs;
        this.description = description;
    }

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
    }

    public String getTiplocCode()
    {
        return tiplocCode;
    }

    public int getCaps()
    {
        return caps;
    }

    public int getNalco()
    {
        return nalco;
    }

    public String getNlcCheck()
    {
        return nlcCheck;
    }

    public String getTpsDescription()
    {
        return tpsDescription;
    }

    public long getStanox()
    {
        return stanox;
    }

    public int getPO_Loc_Code()
    {
        return PO_Loc_Code;
    }

    public String getCrs()
    {
        return crs;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return "TIPLOCInsert{" + "tiplocCode=" + tiplocCode + ", caps=" + caps + ", nalco=" + nalco + ", nlcCheck=" + nlcCheck + ", tpsDescription=" + tpsDescription + ", stanox=" + stanox + ", PO_Loc_Code=" + PO_Loc_Code + ", crs=" + crs + ", description=" + description + '}';
    }

}
