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
public enum TrainCategory
{

    // ordinary passenger trains
    /**
     * London Underground/Metro Service
     */
    OL( "OL", "London Underground/Metro Service" ),
    /**
     * Unadvertised Ordinary Passenger
     */
    OU( "OU", "Unadvertised Ordinary Passenger" ),
    /**
     * Ordinary Passenger
     */
    OO( "OO", "Ordinary Passenger" ),
    /**
     * Staff train
     */
    OS( "OS", "Staff train" ),
    /**
     * Mixed
     */
    OW( "OW", "Mixed" ),
    // Express Passenger Trains
    /**
     * Channel Tunnel
     */
    XC( "XC", "Channel Tunnel" ),
    /**
     * Sleeper (Europe Night Service)
     */
    XD( "XD", "Sleeper (Europe Night Service)" ),
    /**
     * International
     */
    XI( "XI", "International" ),
    /**
     * Motorail
     */
    XR( "XR", "Motorail" ),
    /**
     * Unadvertised Express
     */
    XU( "XU", "Unadvertised Express" ),
    /**
     * Express Passenger
     */
    XX( "XX", "Express Passenger" ),
    /**
     * Sleeper (Domestic)
     */
    XZ( "XZ", "Sleeper (Domestic)" ),
    // Buses
    /**
     * Bus - Replacement due to engineering work
     */
    BR( "BR", "Bus - Replacement due to engineering work" ),
    /**
     * Bus - WTT Service
     */
    BS( "BS", "Bus - WTT Service" ),
    // Empty Coaching Stock Trains
    /**
     * Empty Coaching Stock (ECS)
     */
    EE( "EE", "Empty Coaching Stock (ECS)" ),
    /**
     * ECS, London Underground/Metro Service
     */
    EL( "EL", "ECS, London Underground/Metro Service" ),
    /**
     * ECS & Staff
     */
    ES( "ES", "ECS & Staff" ),
    // Parcels & Postal trains
    /**
     * Postal
     */
    JJ( "JJ", "Postal" ),
    /**
     * Post Office Controlled Parcels
     */
    PM( "PM", "Post Office Controlled Parcels" ),
    /**
     * Parcels
     */
    PP( "PP", "Parcels" ),
    /**
     * Empty NPCCS
     */
    PV( "PV", "Empty NPCCS" ),
    // Departmental Trains
    /**
     * Departmental
     */
    DD( "DD", "Departmental" ),
    /**
     * Civil Engineer
     */
    DH( "DH", "Civil Engineer" ),
    /**
     * Mechanical & Electrical Engineer
     */
    DI( "DI", "Mechanical & Electrical Engineer" ),
    /**
     * Stores
     */
    DQ( "DQ", "Stores" ),
    /**
     * Test
     */
    DT( "DT", "Test" ),
    /**
     * Signal & Telecommunications Engineer
     */
    DY( "DY", "Signal & Telecommunications Engineer" ),
    // Light Locomotives
    /**
     * Locomotive & Brake van
     */
    ZB( "ZB", "Locomotive & Brake van" ),
    /**
     * Light Locomotive
     */
    ZZ( "ZZ", "Light Locomotive" ),
    // Railfreight Distribution
    /**
     * RfD Automotive (Components)
     */
    J2( "J2", "RfD Automotive (Components)" ),
    /**
     * RfD Automotive (Vehicles)
     */
    H2( "H2", "RfD Automotive (Vehicles)" ),
    /**
     * RfD Edible Products (UK Contracts)
     */
    J3( "J3", "RfD Edible Products (UK Contracts)" ),
    /**
     * RfD Industrial Minerals (UK Contracts)
     */
    J4( "J4", "RfD Industrial Minerals (UK Contracts)" ),
    /**
     * RfD Chemicals (UK Contracts)
     */
    J5( "J5", "RfD Chemicals (UK Contracts)" ),
    /**
     * RfD Building Materials (UK Contracts)
     */
    J6( "J6", "RfD Building Materials (UK Contracts)" ),
    /**
     * RfD General Merchandise (UK Contracts)
     */
    J8( "J8", "RfD General Merchandise (UK Contracts)" ),
    /**
     * RfD European
     */
    H8( "H8", "RfD European" ),
    /**
     * RfD Freightliner (Contracts)
     */
    J9( "J9", "RfD Freightliner (Contracts)" ),
    /**
     * RfD Freightliner (Other)
     */
    H9( "H9", "RfD Freightliner (Other)" ),
    // Trainload Freight
    /**
     * Coal (Distributive)
     */
    A0( "A0", "Coal (Distributive)" ),
    /**
     * Coal (Electricity) MGR
     */
    E0( "E0", "Coal (Electricity) MGR" ),
    /**
     * Coal (Other) and Nuclear
     */
    B0( "B0", "Coal (Other) and Nuclear" ),
    /**
     * Metals
     */
    B1( "B1", "Metals" ),
    /**
     * Aggregates
     */
    B4( "B4", "Aggregates" ),
    /**
     * Domestic and Industrial Waste
     */
    B5( "B5", "Domestic and Industrial Waste" ),
    /**
     * Building Materials (TLF)
     */
    B6( "B6", "Building Materials (TLF)" ),
    /**
     * Petroleum Products
     */
    B7( "B7", "Petroleum Products" ),
    // Railfreight Distribution (Channel Tunnel)
    /**
     * RfD European Channel Tunnel (Mixed Business)
     */
    H0( "H0", "RfD European Channel Tunnel (Mixed Business)" ),
    /**
     * RfD European Channel Tunnel Intermodal
     */
    H1( "H1", "RfD European Channel Tunnel Intermodal" ),
    /**
     * RfD European Channel Tunnel Automotive
     */
    H3( "H3", "RfD European Channel Tunnel Automotive" ),
    /**
     * RfD European Channel Tunnel Contract Services
     */
    H4( "H4", "RfD European Channel Tunnel Contract Services" ),
    /**
     * RfD European Channel Tunnel Haulmark
     */
    H5( "H5", "RfD European Channel Tunnel Haulmark" ),
    /**
     * RfD European Channel Tunnel Joint Venture
     */
    H6( "H6", "RfD European Channel Tunnel Joint Venture" ),

    /**
     * Unknown
     */
    UNKNOWN( "  ", "Unknown" );

    private static final Map<String, TrainCategory> CODES = new HashMap<>();

    static
    {
        for( TrainCategory bhx : values() )
        {
            CODES.put( bhx.code, bhx );
        }
    }

    public static TrainCategory lookup( String s )
    {
        return CODES.getOrDefault( s, TrainCategory.UNKNOWN );
    }
    private final String code;
    private final String description;

    private TrainCategory( String code, String description )
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
