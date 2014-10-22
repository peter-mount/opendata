/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter T Mount
 */
public enum TimingLoad
{

    // DMU types
    /**
     * Class 14x series 2-axle
     */
    A( "A", "Class 14x series 2-axle" ),
    /**
     * Class 158
     */
    E( "E", "Class 158" ),
    /**
     * Class 165/0
     */
    N( "N", "Class 165/0" ),
    /**
     * Class 150, 153, 155 or 156
     */
    S( "S", "Class 150, 153, 155 or 156" ),
    /**
     * Class 165/1 or 166
     */
    T( "T", "Class 165/1 or 166" ),
    /**
     * Virgin Voyager Class 220/221
     */
    V( "V", "Virgin Voyager Class 220/221" ),
    /**
     * Class 159
     */
    X( "X", "Class 159" ),

    // DMU/DPU power weight codes
    /**
     * Diesel Mechanical Multiple Units (Vacuum Brake)
     */
    D1( "D1", "Diesel Mechanical Multiple Units (Vacuum Brake)" ),
    /**
     * Diesel Mechanical Multiple Units (Vacuum Brake)
     */
    D2( "D2", "Diesel Mechanical Multiple Units (Vacuum Brake)" ),
    /**
     * Diesel Mechanical Multiple Units (Vacuum Brake)
     */
    D3( "D3", "Diesel Mechanical Multiple Units (Vacuum Brake)" ),
    /**
     * Diesel Mechanical Multiple Units (Vacuum Brake)
     */
    D4( "D4", "Diesel Mechanical Multiple Units (Vacuum Brake)" ),
    /**
     * Diesel Mechanical Multiple Units (Vacuum Brake)
     */
    D1T( "D1T", "Diesel Mechanical Multiple Units (Vacuum Brake)" ),

    // EMU codes
    /**
     * Accelerated Timings
     */
    AT( "AT", "Accelerated Timings" ),

    /**
     * three numerics - 0-999 indicates the specific type of EMU
     * <p>
     * Note: This will change over time as new types are introduced.
     * <p>
     * For types we don't know about this will use UNKNOWN until we add them.
     * <p>
     * The findUnsupportedTimingLoads() tool can generate these.
     */
    /**
     * /* Class 140
     */
    C140( "140", "Class 140" ),
    /**
     * /* Class 210
     */
    C210( "210", "Class 210" ),
    /**
     * /* Class 245
     */
    C245( "245", "Class 245" ),
    /**
     * /* Class 280
     */
    C280( "280", "Class 280" ),
    /**
     * /* Class 313
     */
    C313( "313", "Class 313" ),
    /**
     * /* Class 315
     */
    C315( "315", "Class 315" ),
    /**
     * /* Class 317
     */
    C317( "317", "Class 317" ),
    /**
     * /* Class 319
     */
    C319( "319", "Class 319" ),
    /**
     * /* Class 321
     */
    C321( "321", "Class 321" ),
    /**
     * /* Class 322
     */
    C322( "322", "Class 322" ),
    /**
     * /* Class 323
     */
    C323( "323", "Class 323" ),
    /**
     * /* Class 325
     */
    C325( "325", "Class 325" ),
    /**
     * /* Class 333
     */
    C333( "333", "Class 333" ),
    /**
     * /* Class 350
     */
    C350( "350", "Class 350" ),
    /**
     * /* Class 357
     */
    C357( "357", "Class 357" ),
    /**
     * /* Class 360
     */
    C360( "360", "Class 360" ),
    /**
     * /* Class 365
     */
    C365( "365", "Class 365" ),
    /**
     * /* Class 375
     */
    C375( "375", "Class 375" ),
    /**
     * /* Class 385
     */
    C385( "385", "Class 385" ),
    /**
     * /* Class 390
     */
    C390( "390", "Class 390" ),
    /**
     * /* Class 395
     */
    C395( "395", "Class 395" ),
    /**
     * /* Class 400
     */
    C400( "400", "Class 400" ),
    /**
     * /* Class 410
     */
    C410( "410", "Class 410" ),
    /**
     * /* Class 420
     */
    C420( "420", "Class 420" ),
    /**
     * /* Class 455
     */
    C455( "455", "Class 455" ),
    /**
     * /* Class 483
     */
    C483( "483", "Class 483" ),
    /**
     * /* Class 506
     */
    C506( "506", "Class 506" ),
    /**
     * /* Class 535
     */
    C535( "535", "Class 535" ),
    /**
     * /* Class 595
     */
    C595( "595", "Class 595" ),
    /**
     * /* Class 600
     */
    C600( "600", "Class 600" ),
    /**
     * /* Class 715
     */
    C715( "715", "Class 715" ),
    /**
     * /* Class 790
     */
    C790( "790", "Class 790" ),
    /**
     * /* Class 800
     */
    C800( "800", "Class 800" ),
    /**
     * /* Class 970
     */
    C970( "970", "Class 970" ),
    /**
     * /* Class 975
     */
    C975( "975", "Class 975" ),
    /**
     * Unknown. For types D, E or ED this is returned for the planned load, which we are not interested in
     */
    UNKNOWN( "  ", "Unknown" );

    private static final Map<String, TimingLoad> CODES = new HashMap<>();

    static
    {
        for( TimingLoad bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static TimingLoad lookup( String s )
    {
        return CODES.getOrDefault( s.trim(), TimingLoad.UNKNOWN );
    }
    
    private final String code;
    private final String description;

    private TimingLoad( String code, String description )
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
