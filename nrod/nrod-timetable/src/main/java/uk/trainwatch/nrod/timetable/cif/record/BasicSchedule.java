/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalDate;
import java.util.function.Function;
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
    //static final Function<CIFParser, Record> factory = factory1.compose( CIFParser::debug );

    // FIXME many of these can be objects
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
    // No longer used so no need to store
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
    //private final String connectionInd;
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
                          // No longer used, here so we can skip
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
                          // No longer used, here so we can skip
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

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
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

    @Override
    public String toString()
    {
        return "BasicSchedule{" + "transactionType=" + transactionType + ", trainUid=" + trainUid + ", runsFrom=" + runsFrom + ", runsTo=" + runsTo + ", daysRun=" + daysRun + ", bankHolRun=" + bankHolRun + ", trainStatus=" + trainStatus + ", trainCat=" + trainCat + ", trainIdentity=" + trainIdentity + ", headCode=" + headCode + ", serviceCode=" + serviceCode + ", portionId=" + portionId + ", powerType=" + powerType + ", timingLoad=" + timingLoad + ", speed=" + speed + ", operatingCharacteristics=" + operatingCharacteristics + ", trainClass=" + trainClass + ", sleepers=" + sleepers + ", reservations=" + reservations + ", cateringCode=" + cateringCode + ", serviceBranding=" + serviceBranding + ", stpInd=" + stpInd + '}';
    }

}
