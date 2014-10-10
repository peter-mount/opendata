/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.util.function.Function;
import uk.trainwatch.nrod.timetable.util.BusSec;
import uk.trainwatch.nrod.timetable.util.Catering;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.PowerType;
import uk.trainwatch.nrod.timetable.util.Reservations;
import uk.trainwatch.nrod.timetable.util.ServiceBranding;
import uk.trainwatch.nrod.timetable.util.Sleepers;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainClass;

/**
 *
 * @author Peter T Mount
 */
public class ChangesEnRoute
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new ChangesEnRoute(
            p.getString( 8 ),
            p.getTrainCategory(),
            p.getString( 4 ),
            p.getString( 4 ),
            p.skip( 1 ),
            p.getString( 8 ),
            p.getBusSec(),
            p.getPowerType(),
            p.getTimingLoad(),
            p.getString( 3 ),
            p.getOperatingCharacteristics(),
            p.getTrainClass(),
            p.getSleepers(),
            p.getReservations(),
            p.skip( 1 ),
            p.getCatering(),
            p.getServiceBranding(),
            p.skip( 4 ),
            p.getInt( 5 )
    );

    private final String location;
    private final TrainCategory trainCat;
    private final String trainIdentity;
    private final String headcode;
    // No longer used
    //private final String courseInd;
    private final String serviceCode;
    private final BusSec portionId;
    private final PowerType powerType;
    private final TimingLoad timingLoad;
    private final String speed;
    private final OperatingCharacteristics[] operatingCharacteristics;
    private final TrainClass trainClass;
    private final Sleepers sleepers;
    private final Reservations reservations;
    // No longer used
    //private final String connectionIndicator;
    private final Catering[] cateringCode;
    private final ServiceBranding[] serviceBranding;
    // No longer used
    //private final String tractionClass;
    private final int uicCode;

    public ChangesEnRoute( String location,
                           TrainCategory trainCat, String trainIdentity, String headcode,
                           Void courseInd,
                           String serviceCode,
                           BusSec portionId,
                           PowerType powerType,
                           TimingLoad timingLoad,
                           String speed,
                           OperatingCharacteristics[] operatingCharacteristics,
                           TrainClass trainClass,
                           Sleepers sleepers,
                           Reservations reservations,
                           Void connectionIndicator,
                           Catering[] cateringCode,
                           ServiceBranding[] serviceBranding,
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

    public TrainCategory getTrainCat()
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

    public BusSec getPortionId()
    {
        return portionId;
    }

    public PowerType getPowerType()
    {
        return powerType;
    }

    public TimingLoad getTimingLoad()
    {
        return timingLoad;
    }

    public String getSpeed()
    {
        return speed;
    }

    public OperatingCharacteristics[] getOperatingCharacteristics()
    {
        return operatingCharacteristics;
    }

    public TrainClass getTrainClass()
    {
        return trainClass;
    }

    public Sleepers getSleepers()
    {
        return sleepers;
    }

    public Reservations getReservations()
    {
        return reservations;
    }

    public Catering[] getCateringCode()
    {
        return cateringCode;
    }

    public ServiceBranding[] getServiceBranding()
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
