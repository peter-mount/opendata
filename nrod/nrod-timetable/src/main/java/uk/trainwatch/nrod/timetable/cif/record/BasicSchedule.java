/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalDate;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.cif.TransactionType;

/**
 *
 * @author Peter T Mount
 */
public class BasicSchedule
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new BasicSchedule(
            p.getTransactionType(),
            p.getString( 6 ),
            p.getDate_yymmdd(),
            // FIXME 999999 is valid
            p.getDate_yymmdd(),
            p.getString( 7 ),
            p.getString( 1 ),
            p.getString( 1 ),
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
            // Spare 1
            p.getString( 1 ),
            p.getString( 1 )
    );

    // FIXME many of these can be objects
    private final TransactionType transactionType;
    private final String trainUid;
    private final LocalDate runsFrom;
    private final LocalDate runsTo;
    private final String daysRun;
    private final String bankHolRun;
    private final String trainStatus;
    private final String trainCat;
    private final String trainIdentity;
    private final String headCode;
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
    private final String connectionInd;
    private final String cateringCode;
    private final String serviceBranding;
    private final String stpInd;

    public BasicSchedule( TransactionType transactionType, String trainUid, LocalDate runsFrom, LocalDate runsTo,
                          String daysRun, String bankHolRun, String trainStatus, String trainCat, String trainIdentity,
                          String headCode, String courseInd, String serviceCode, String portionId, String powerType,
                          String timingLoad, String speed, String operatingCharacteristics, String trainClass,
                          String sleepers, String reservations, String connectionInd, String cateringCode,
                          String serviceBranding,
                          String spare,
                          String stpInd )
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
        this.connectionInd = connectionInd;
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

    public String getTrainUid()
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

    public String getDaysRun()
    {
        return daysRun;
    }

    public String getBankHolRun()
    {
        return bankHolRun;
    }

    public String getTrainStatus()
    {
        return trainStatus;
    }

    public String getTrainCat()
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

    public String getConnectionInd()
    {
        return connectionInd;
    }

    public String getCateringCode()
    {
        return cateringCode;
    }

    public String getServiceBranding()
    {
        return serviceBranding;
    }

    public String getStpInd()
    {
        return stpInd;
    }

    @Override
    public String toString()
    {
        return "BasicSchedule{" + "transactionType=" + transactionType + ", trainUid=" + trainUid + ", runsFrom=" + runsFrom + ", runsTo=" + runsTo + ", daysRun=" + daysRun + ", bankHolRun=" + bankHolRun + ", trainStatus=" + trainStatus + ", trainCat=" + trainCat + ", trainIdentity=" + trainIdentity + ", headCode=" + headCode + ", courseInd=" + courseInd + ", serviceCode=" + serviceCode + ", portionId=" + portionId + ", powerType=" + powerType + ", timingLoad=" + timingLoad + ", speed=" + speed + ", operatingCharacteristics=" + operatingCharacteristics + ", trainClass=" + trainClass + ", sleepers=" + sleepers + ", reservations=" + reservations + ", connectionInd=" + connectionInd + ", cateringCode=" + cateringCode + ", serviceBranding=" + serviceBranding + ", stpInd=" + stpInd + '}';
    }

}
