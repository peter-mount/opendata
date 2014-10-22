/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalDate;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonObject;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.util.BankHolidayRunning;
import uk.trainwatch.nrod.timetable.util.BusSec;
import uk.trainwatch.nrod.timetable.util.Catering;
import uk.trainwatch.nrod.timetable.util.DaysRun;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.PowerType;
import uk.trainwatch.nrod.timetable.util.Reservations;
import uk.trainwatch.nrod.timetable.util.STPIndicator;
import uk.trainwatch.nrod.timetable.util.ServiceBranding;
import uk.trainwatch.nrod.timetable.util.Sleepers;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainClass;
import uk.trainwatch.nrod.timetable.util.TrainStatus;
import uk.trainwatch.nrod.timetable.util.TrainUID;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author Peter T Mount
 */
public class BasicSchedule
        extends Record
{

    // Page 17
    static final Function<CIFParser, Record> factory = p -> new BasicSchedule(
            p.getTransactionType(),
            p.getTrainUID(),
            p.getDate_yymmdd(),
            p.getDate_yymmdd(),
            p.getDaysRun(),
            p.getBankHolidayRunning(),
            p.getTrainStatus(),
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
            // Spare 1
            p.skip( 1 ),
            p.getSTPIndicator()
    );

    public static final Function<JsonObject, BasicSchedule> fromJson = o -> new BasicSchedule(
            // Dummy
            TransactionType.NEW,
            new TrainUID( JsonUtils.getString( o, "trainUid" ) ),
            JsonUtils.getLocalDate( o, "runsFrom" ),
            JsonUtils.getLocalDate( o, "runsTo" ),
            new DaysRun( o.getInt( "daysRun" ) ),
            JsonUtils.getEnum( BankHolidayRunning.class, o, "bankHolidayRunning" ),
            JsonUtils.getEnum( TrainStatus.class, o, "trainStatus" ),
            JsonUtils.getEnum( TrainCategory.class, o, "trainCategory" ),
            JsonUtils.getString( o, "trainIdentity" ),
            JsonUtils.getString( o, "headCode" ),
            null,
            JsonUtils.getString( o, "serviceCode" ),
            JsonUtils.getEnum( BusSec.class, o, "portionId" ),
            JsonUtils.getEnum( PowerType.class, o, "powerType" ),
            JsonUtils.getEnum( TimingLoad.class, o, "timingLoad" ),
            JsonUtils.getString( o, "speed" ),
            JsonUtils.getEnumArray( OperatingCharacteristics.class, o, "operChars" ),
            JsonUtils.getEnum( TrainClass.class, o, "trainClass" ),
            JsonUtils.getEnum( Sleepers.class, o, "sleepers" ),
            JsonUtils.getEnum( Reservations.class, o, "reservations" ),
            null,
            JsonUtils.getEnumArray( Catering.class, o, "catering" ),
            JsonUtils.getEnumArray( ServiceBranding.class, o, "branding" ),
            null,
            JsonUtils.getEnum( STPIndicator.class, o, "stpIndicator" )
    );

    public static final Function<BasicSchedule, JsonObject> toJson = s -> Json.createObjectBuilder().
            add( "trainUid", s.getTrainUid().
                 toString() ).
            add( "runsFrom", TimeUtils.toJson( s.getRunsFrom() ) ).
            add( "runsTo", TimeUtils.toJson( s.getRunsTo() ) ).
            add( "daysRun", s.getDaysRun().
                 getDaysRunning() ).
            add( "bankHolidayRunning", s.getBankHolidayRunning().
                 toString() ).
            add( "trainStatus", s.getTrainStatus().
                 toString() ).
            add( "trainCategory", s.getTrainCategory().
                 toString() ).
            add( "trainIdentity", s.getTrainIdentity() ).
            add( "headCode", s.getHeadCode() ).
            add( "serviceCode", s.getServiceCode() ).
            add( "portionId", s.getPortionId().
                 toString() ).
            add( "powerType", s.getPowerType().
                 toString() ).
            add( "timingLoad", s.getTimingLoad().
                 toString() ).
            add( "speed", s.getSpeed() ).
            add( "operChars", JsonUtils.getArray( s.getOperatingCharacteristics() ) ).
            add( "trainClass", s.getTrainClass().
                 toString() ).
            add( "sleepers", s.getSleepers().
                 toString() ).
            add( "reservations", s.getReservations().
                 toString() ).
            add( "catering", JsonUtils.getArray( s.getCateringCode() ) ).
            add( "branding", JsonUtils.getArray( s.getServiceBranding() ) ).
            add( "stpIndicator", s.getStpInd().
                 toString() ).
            build();

    private final TransactionType transactionType;
    private final TrainUID trainUid;
    private final LocalDate runsFrom;
    private final LocalDate runsTo;
    private final DaysRun daysRun;
    private final BankHolidayRunning bankHolRun;
    private final TrainStatus trainStatus;
    private final TrainCategory trainCat;
    private final String trainIdentity;
    private final String headCode;
    private final String serviceCode;
    private final BusSec portionId;
    private final PowerType powerType;
    private final TimingLoad timingLoad;
    private final String speed;
    private final OperatingCharacteristics[] operatingCharacteristics;
    private final TrainClass trainClass;
    private final Sleepers sleepers;
    private final Reservations reservations;
    private final Catering[] cateringCode;
    private final ServiceBranding[] serviceBranding;
    private final STPIndicator stpInd;

    public BasicSchedule( TransactionType transactionType,
                          TrainUID trainUid,
                          LocalDate runsFrom,
                          LocalDate runsTo,
                          DaysRun daysRun,
                          BankHolidayRunning bankHolRun,
                          TrainStatus trainStatus,
                          TrainCategory trainCat,
                          String trainIdentity,
                          String headCode,
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
                          Void connectionInd,
                          Catering[] cateringCode,
                          ServiceBranding[] serviceBranding,
                          Void spare,
                          STPIndicator stpInd )
    {
        super( RecordType.BS );
        this.transactionType = transactionType;
        this.trainUid = trainUid;
        this.runsFrom = runsFrom;
        this.runsTo = runsTo;
        this.daysRun = daysRun;
        this.bankHolRun = bankHolRun;
        this.trainStatus = trainStatus;
        this.trainCat = trainCat;
        this.trainIdentity = trainIdentity;
        this.headCode = headCode;
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
        this.stpInd = stpInd;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

    public TransactionType getTransactionType()
    {
        return transactionType;
    }

    public TrainUID getTrainUid()
    {
        return trainUid;
    }

    public LocalDate getRunsFrom()
    {
        return runsFrom;
    }

    public LocalDate getRunsTo()
    {
        return runsTo;
    }

    public DaysRun getDaysRun()
    {
        return daysRun;
    }

    public BankHolidayRunning getBankHolidayRunning()
    {
        return bankHolRun;
    }

    public TrainStatus getTrainStatus()
    {
        return trainStatus;
    }

    public TrainCategory getTrainCategory()
    {
        return trainCat;
    }

    public String getTrainIdentity()
    {
        return trainIdentity;
    }

    public String getHeadCode()
    {
        return headCode;
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

    public STPIndicator getStpInd()
    {
        return stpInd;
    }

}
