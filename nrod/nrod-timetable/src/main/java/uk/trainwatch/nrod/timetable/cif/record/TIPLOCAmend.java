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
public class TIPLOCAmend
        extends TIPLOCAction
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    static final Function<CIFParser, Record> factory = p -> new TIPLOCAmend(
            p.getTiploc(),
            p.getInt( 2 ),
            p.getInt( 6 ),
            p.getString( 1 ),
            p.getString( 26 ),
            p.getLong( 5 ),
            p.skip( 4 ),
            // StringNull as if '   ' then we insert null into the db to prevent duplicate key constraints
            p.getStringNull( 3 ),
            p.getString( 16 ),
            p.getTiploc()
    );
    private final int caps;
    private final int nalco;
    private final String nlcCheck;
    private final String tpsDescription;
    private final long stanox;
    private final String crs;
    private final String description;
    private final Tiploc newTiploc;

    public TIPLOCAmend( Tiploc tiploc,
                        int caps,
                        int nalco,
                        String nlcCheck,
                        String tpsDescription,
                        long stanox,
                        Void PO_Loc_Code,
                        String crs,
                        String description,
                        Tiploc newTiploc )
    {
        super( RecordType.TI, tiploc );
        this.caps = caps;
        this.nalco = nalco;
        this.nlcCheck = nlcCheck;
        this.tpsDescription = tpsDescription;
        this.stanox = stanox;
        this.crs = crs;
        this.description = description;
        this.newTiploc = newTiploc;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
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

    public String getCrs()
    {
        return crs;
    }

    public String getDescription()
    {
        return description;
    }

    public Tiploc getNewTiploc()
    {
        return newTiploc;
    }

}
