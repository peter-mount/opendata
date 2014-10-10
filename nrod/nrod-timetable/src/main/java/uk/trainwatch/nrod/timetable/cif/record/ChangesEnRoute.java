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
public class ChangesEnRoute
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new ChangesEnRoute(
            p.getString( 8 ),
            p.getString( 2 ),
            p.getString( 4 ),
            p.getString( 4 ),
            p.skip( 1 ),
            p.getString( 8 ),
            p.getString( 1 ),
            p.getString( 3 ),
            p.getString( 4 ),
            p.getString( 3 ),
            p.getString( 6 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.skip( 1 ),
            p.getString( 4 ),
            p.getString( 4 ),
            p.skip( 4 ),
            p.getInt( 5 )
    );

    private final String location;
    private final String trainCat;
    private final String trainIdentity;
    private final String headcode;
    // No longer used
    //private final String courseInd;
    private final String serviceCode;
    private final String portionId;
    private final String powerType;
    private final String timingLoad;
    private final String speed;
    private final String operatingCharacteristics;
    private final String trainClass;
    private final String sleepers;
    private final String reservations;
    // No longer used
    //private final String connectionIndicator;
    private final String cateringCode;
    private final String serviceBranding;
    // No longer used
    //private final String tractionClass;
    private final int uicCode;

    public ChangesEnRoute( String location, String trainCat, String trainIdentity, String headcode,
                           Void courseInd,
                           String serviceCode, String portionId, String powerType, String timingLoad, String speed,
                           String operatingCharacteristics, String trainClass, String sleepers, String reservations,
                           Void connectionIndicator,
                           String cateringCode, String serviceBranding,
                           Void tractionClass,
                           int uicCode )
    {
        super( RecordType.CR );
        this.location = location;
        this.trainCat = trainCat;
        this.trainIdentity = trainIdentity;
        this.headcode = headcode;
        this.serviceCode = serviceCode;
        this.portionId = portionId;
        this.powerType = powerType;
        this.timingLoad = timingLoad;
        this.speed = speed;
        this.operatingCharacteristics = operatingCharacteristics;
        this.trainClass = trainClass;
        this.sleepers = sleepers;
        this.reservations = reservations;
        this.cateringCode = cateringCode;
        this.serviceBranding = serviceBranding;
        this.uicCode = uicCode;
    }

    public String getLocation()
    {
        return location;
    }

    public String getTrainCat()
    {
        return trainCat;
    }

    public String getTrainIdentity()
    {
        return trainIdentity;
    }

    public String getHeadcode()
    {
        return headcode;
    }

    public String getServiceCode()
    {
        return serviceCode;
    }

    public String getPortionId()
    {
        return portionId;
    }

    public String getPowerType()
    {
        return powerType;
    }

    public String getTimingLoad()
    {
        return timingLoad;
    }

    public String getSpeed()
    {
        return speed;
    }

    public String getOperatingCharacteristics()
    {
        return operatingCharacteristics;
    }

    public String getTrainClass()
    {
        return trainClass;
    }

    public String getSleepers()
    {
        return sleepers;
    }

    public String getReservations()
    {
        return reservations;
    }

    public String getCateringCode()
    {
        return cateringCode;
    }

    public String getServiceBranding()
    {
        return serviceBranding;
    }

    public int getUicCode()
    {
        return uicCode;
    }

    @Override
    public String toString()
    {
        return "ChangesEnRoute{" + "location=" + location + ", trainCat=" + trainCat + ", trainIdentity=" + trainIdentity + ", headcode=" + headcode + ", serviceCode=" + serviceCode + ", portionId=" + portionId + ", powerType=" + powerType + ", timingLoad=" + timingLoad + ", speed=" + speed + ", operatingCharacteristics=" + operatingCharacteristics + ", trainClass=" + trainClass + ", sleepers=" + sleepers + ", reservations=" + reservations + ", cateringCode=" + cateringCode + ", serviceBranding=" + serviceBranding + ", uicCode=" + uicCode + +'}';
    }

}
