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
            p.getString( 1 ),
            p.getString( 8 ),
            p.getString( 1 ),
            p.getString( 3 ),
            p.getString( 4 ),
            p.getString( 3 ),
            p.getString( 6 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.getString( 4 ),
            p.getString( 4 ),
            p.getString( 4 ),
            p.getInt( 5 ),
            p.getString( 8 )
    );
    private final String location;
    private final String trainCat;
    private final String trainIdentity;
    private final String headcode;
    private final String courseInd;
    private final String serviceCode;
    private final String portionId;
    private final String powerType;
    private final String timingLoad;
    private final String speed;
    private final String operatingCharacteristics;
    private final String trainClass;
    private final String sleepers;
    private final String reservations;
    private final String connectionIndicator;
    private final String cateringCode;
    private final String serviceBranding;
    private final String tractionClass;
    private final int uicCode;
    private final String reserved;

    public ChangesEnRoute( String location, String trainCat, String trainIdentity, String headcode, String courseInd,
                           String serviceCode, String portionId, String powerType, String timingLoad, String speed,
                           String operatingCharacteristics, String trainClass, String sleepers, String reservations,
                           String connectionIndicator, String cateringCode, String serviceBranding, String tractionClass,
                           int uicCode, String reserved )
    {
        super( RecordType.CR );
        this.location = location;
        this.trainCat = trainCat;
        this.trainIdentity = trainIdentity;
        this.headcode = headcode;
        this.courseInd = courseInd;
        this.serviceCode = serviceCode;
        this.portionId = portionId;
        this.powerType = powerType;
        this.timingLoad = timingLoad;
        this.speed = speed;
        this.operatingCharacteristics = operatingCharacteristics;
        this.trainClass = trainClass;
        this.sleepers = sleepers;
        this.reservations = reservations;
        this.connectionIndicator = connectionIndicator;
        this.cateringCode = cateringCode;
        this.serviceBranding = serviceBranding;
        this.tractionClass = tractionClass;
        this.uicCode = uicCode;
        this.reserved = reserved;
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

    public String getCourseInd()
    {
        return courseInd;
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

    public String getConnectionIndicator()
    {
        return connectionIndicator;
    }

    public String getCateringCode()
    {
        return cateringCode;
    }

    public String getServiceBranding()
    {
        return serviceBranding;
    }

    public String getTractionClass()
    {
        return tractionClass;
    }

    public int getUicCode()
    {
        return uicCode;
    }

    public String getReserved()
    {
        return reserved;
    }

    @Override
    public String toString()
    {
        return "ChangesEnRoute{" + "location=" + location + ", trainCat=" + trainCat + ", trainIdentity=" + trainIdentity + ", headcode=" + headcode + ", courseInd=" + courseInd + ", serviceCode=" + serviceCode + ", portionId=" + portionId + ", powerType=" + powerType + ", timingLoad=" + timingLoad + ", speed=" + speed + ", operatingCharacteristics=" + operatingCharacteristics + ", trainClass=" + trainClass + ", sleepers=" + sleepers + ", reservations=" + reservations + ", connectionIndicator=" + connectionIndicator + ", cateringCode=" + cateringCode + ", serviceBranding=" + serviceBranding + ", tractionClass=" + tractionClass + ", uicCode=" + uicCode + ", reserved=" + reserved + '}';
    }

}
