/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordType;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.ATSCode;
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
 * A container for a train schedule
 * <p>
 * @author Peter T Mount
 */
public class Schedule
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final BasicSchedule basicSchedule;
    private final BasicScheduleExtras basicScheduleExtras;
    private final List<Location> locations;

    Schedule( BasicSchedule basicSchedule, BasicScheduleExtras basicScheduleExtras, List<Location> locations )
    {
        this.basicSchedule = basicSchedule;
        this.basicScheduleExtras = basicScheduleExtras;
        this.locations = locations;
    }

    BasicSchedule getBasicSchedule()
    {
        return basicSchedule;
    }

    BasicScheduleExtras getBasicScheduleExtras()
    {
        return basicScheduleExtras;
    }

    /**
     * Convenience method to get the origin (start) {@link Location} from the schedule
     * <p>
     * @return Location or null if empty (should never occur)
     */
    public OriginLocation getOrigin()
    {
        if( locations == null || locations.isEmpty() ) {
            return null;
        }
        return (OriginLocation) locations.get( 0 );
    }

    public LocalTime getDeparture()
    {
        OriginLocation l = getOrigin();
        if( l == null ) {
            return null;
        }
        if( isClass5() || isFreight() ) {
            return l.getWorkDeparture();
        }
        return l.getPublicDeparture();
    }

    /**
     * Convenience method to get the Terminating (end) {@link Location} from the schedule
     * <p>
     * @return Location or null if empty (should never occur)
     */
    public TerminatingLocation getDestination()
    {
        if( locations == null || locations.isEmpty() ) {
            return null;
        }
        return (TerminatingLocation) locations.get( locations.size() - 1 );
    }

    public LocalTime getArrival()
    {
        TerminatingLocation l = getDestination();
        if( l == null ) {
            return null;
        }
        if( isClass5() || isFreight() ) {
            return l.getWorkArrival();
        }
        return l.getPublicArrival();
    }

    public Location getLocation( Tiploc tiploc )
    {
        return locations.stream().
                filter( l -> l.getLocation().
                        equals( tiploc ) ).
                filter( l -> l.getRecordType() != RecordType.CR ).
                findFirst().
                orElse( null );
    }

    public TransactionType getTransactionType()
    {
        return basicSchedule.getTransactionType();
    }

    public TrainUID getTrainUid()
    {
        return basicSchedule.getTrainUid();
    }

    public LocalDate getRunsFrom()
    {
        return basicSchedule.getRunsFrom();
    }

    public LocalDate getRunsTo()
    {
        return basicSchedule.getRunsTo();
    }

    public DaysRun getDaysRun()
    {
        return basicSchedule.getDaysRun();
    }

    public BankHolidayRunning getBankHolidayRunning()
    {
        return basicSchedule.getBankHolidayRunning();
    }

    public TrainStatus getTrainStatus()
    {
        return basicSchedule.getTrainStatus();
    }

    public TrainCategory getTrainCategory()
    {
        return basicSchedule.getTrainCategory();
    }

    public String getTrainIdentity()
    {
        return basicSchedule.getTrainIdentity();
    }

    public boolean isFreight()
    {
        return getTrainIdentity().
                equals( "    " );
    }

    public boolean isClass5()
    {
        return getTrainIdentity().
                startsWith( "5" );
    }

    public String getHeadCode()
    {
        return basicSchedule.getHeadCode();
    }

    public String getServiceCode()
    {
        return basicSchedule.getServiceCode();
    }

    public BusSec getPortionId()
    {
        return basicSchedule.getPortionId();
    }

    public PowerType getPowerType()
    {
        return basicSchedule.getPowerType();
    }

    public TimingLoad getTimingLoad()
    {
        return basicSchedule.getTimingLoad();
    }

    public String getSpeed()
    {
        return basicSchedule.getSpeed();
    }

    public OperatingCharacteristics[] getOperatingCharacteristics()
    {
        return basicSchedule.getOperatingCharacteristics();
    }

    public TrainClass getTrainClass()
    {
        return basicSchedule.getTrainClass();
    }

    public Sleepers getSleepers()
    {
        return basicSchedule.getSleepers();
    }

    public Reservations getReservations()
    {
        return basicSchedule.getReservations();
    }

    public Catering[] getCateringCode()
    {
        return basicSchedule.getCateringCode();
    }

    public ServiceBranding[] getServiceBranding()
    {
        return basicSchedule.getServiceBranding();
    }

    public STPIndicator getStpInd()
    {
        return basicSchedule.getStpInd();
    }

    public int getUicCode()
    {
        return basicScheduleExtras.getUicCode();
    }

    public ATOCCode getAtocCode()
    {
        return basicScheduleExtras.getAtocCode();
    }

    public ATSCode getApplicableTimetableCode()
    {
        return basicScheduleExtras.getApplicableTimetableCode();
    }

    public int size()
    {
        return locations.size();
    }

    public boolean isEmpty()
    {
        return locations.isEmpty();
    }

    public Stream<Location> stream()
    {
        return locations.stream();
    }

    public void forEach( Consumer<Location> action )
    {
        locations.forEach( action );
    }

    public List<Location> getLocations()
    {
        return locations;
    }

}
