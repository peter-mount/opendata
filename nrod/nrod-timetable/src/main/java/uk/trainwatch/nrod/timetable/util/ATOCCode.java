/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Two-character codes devised by the Association of Train Operating Companies to identify the operator of individual trains.
 * <p>
 * These codes do not appear on TSDB, but are generated in CIF from each train’s Train Service Code using a look-up table.
 * <p>
 * @author Peter T Mount
 */
public enum ATOCCode
{

    /**
     * /* ARRIVA Trains Wales
     */
    AW( "AW", "ARRIVA Trains Wales" ),
    /**
     * /* c2c
     */
    CC( "CC", "c2c" ),
    /**
     * /* Chiltern Railway Co.
     */
    CH( "CH", "Chiltern Railway" ),
    /**
     * /* East Midlands Trains
     */
    EM( "EM", "East Midlands Trains" ),
    /**
     * /* Eurostar (UK)
     */
    ES( "ES", "Eurostar" ),
    /**
     * /* First Capital Connect
     */
    FC( "FC", "First Capital Connect (Defunct)" ),
    /**
     * /* Grand Central
     */
    GC( "GC", "Grand Central" ),
    /**
     * /* Great Northern
     * <p>
     * This is a new operator since 2014 so not in the CIF End User Specification dated 2010
     */
    GN( "GN", "Govia Thameslink Railway (Great Northern)" ),
    /**
     * /* Grand Central
     */
    GR( "GR", "East Coast" ),
    /**
     * /* First Great Western
     */
    GW( "GW", "First Great Western" ),
    /**
     * /* Gatwick Express
     */
    GX( "GX", "Gatwick Express" ),
    /**
     * /* Heathrow Connect
     */
    HC( "HC", "Heathrow Connect" ),
    /**
     * /* First Hull Trains
     */
    HT( "HT", "First Hull Trains" ),
    /**
     * /* Heathrow Express
     */
    HX( "HX", "Heathrow Express" ),
    /**
     * /* Island Line
     */
    IL( "IL", "Island Lines" ),
    /**
     * /* National Express East Anglia
     * <p>
     * TODO check as this has been Abellio Greater Anglia since 2012?
     */
    LE( "LE", "Abelio Greater Anglia" ),
    /**
     * /* London Midland
     */
    LM( "LM", "London Midland" ),
    /**
     * /* London Overground
     */
    LO( "LO", "London Overground" ),
    /**
     * /* London Underground
     */
    LT( "LT", "London Underground" ),
    /**
     * /* Merseyrail
     */
    ME( "ME", "Merseyrail" ),
    /**
     * /* Northern
     */
    NT( "NT", "Northern Rail" ),
    /**
     * /* North Yorkshire Moors Railway
     */
    NY( "NY", "North Yorkshire Moors Railway" ),
    /**
     * /* Southeastern
     */
    SE( "SE", "Southeastern" ),
    /**
     * /* Southern
     */
    SN( "SN", "Southern" ),
    /**
     * /* ScotRail
     */
    SR( "SR", "ScotRail" ),
    /**
     * /* South West Trains
     */
    SW( "SW", "South West Trains" ),
    /**
     * /* ThamesLink
     * <p>
     * TODO confirm, this is not in the CIR EUS 2010
     */
    TL( "TL", "Govia Thameslink Railway (Thameslink)" ),
    /**
     * /* First TransPennine Express
     */
    TP( "TP", "First TransPennine Express" ),
    /**
     * /* Tyne & Wear Metro
     */
    TW( "TW", "Nexus (Tyne & Wear Metro)" ),
    /**
     * /* Virgin Trains
     */
    VT( "VT", "Virgin Trains" ),
    /**
     * /* West Coast Railway Co.
     */
    WR( "WR", "West Coast Railway" ),
    /**
     * /* Cross Country
     */
    XC( "XC", "Cross Country" ),
    /**
     * Any Train Service Code without a valid mapping to an existing two letter code
     */
    ZZ( "ZZ", "No valid mapping" ),
    /**
     * Unknown. Usualy caused by a new operator
     */
    UNKNOWN( "??", "Unknown" ),
    // New entries post the last full import must be added last otherwise it will break the DB
    DC( "DC", "Devon and Cornwall Railway" );

    private static final Map<String, ATOCCode> CODES = new HashMap<>();

    static {
        for( ATOCCode bhx : values() ) {
            CODES.put( bhx.code, bhx );
        }
    }

    public static ATOCCode lookup( String s )
    {
        return CODES.getOrDefault( s, ATOCCode.UNKNOWN );
    }
    private final String code;
    private final String description;

    private ATOCCode( String code, String description )
    {
        this.code = code;
        this.description = description;
    }

    /**
     * The code within the timetable
     * <p>
     * @return
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Human readable description
     * <p>
     * @return
     */
    public String getDescription()
    {
        return description;
    }

}
