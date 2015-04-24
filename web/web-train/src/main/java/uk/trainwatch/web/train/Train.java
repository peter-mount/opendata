/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Schedule;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.TplLocation;

/**
 * A normalised version of a train
 * <p>
 * @author peter
 */
public final class Train
{

    private final String rid;
    private final Pport pport;
    private final List<TrainMovement> movement;
    private final Schedule schedule;
    private final TS ts;
    private final LocalTime lastReport;
    private final TrainMovement nextReport;
    private final Duration untilNextReport;

    public static Train create( Pport pport )
    {
        if( pport == null || !pport.isSetUR() ) {
            return null;
        }

        Pport.UR ur = pport.getUR();
        Schedule schedule = ur.getSchedule().stream().findAny().orElse( null );
        TS ts = ur.getTS().stream().findAny().orElse( null );
        if( schedule == null && ts == null ) {
            return null;
        }

        return new Train( pport, schedule, ts );
    }

    private Train( Pport pport, Schedule schedule, TS ts )
    {
        this.pport = pport;
        this.schedule = schedule;
        this.ts = ts;
        rid = isSchedulePresent() ? schedule.getRid() : ts.getRid();

        Map<String, TrainMovement> map = new HashMap<>();

        if( isSchedulePresent() ) {
            schedule.getOROrOPOROrIP().
                    stream().
                    map( TplLocation::castTplLocation ).
                    filter( Objects::nonNull ).
                    forEach( l -> map.computeIfAbsent( l.getTpl(), TrainMovement::new ).setSchedule( l ) );
        }

        if( isTsPresent() ) {
            ts.getLocation().
                    stream().
                    forEach( l -> map.computeIfAbsent( l.getTpl(), TrainMovement::new ).setTs( l ) );
        }

        // Now sort into the correct order
        movement = new ArrayList<>( map.values() );
        movement.sort( TrainMovement::compare );

        // Flag origin & destination entries
        if( !movement.isEmpty() ) {
            movement.get( 0 ).setOrigin( true );
            movement.get( movement.size() - 1 ).setDestination( true );
        }

        // Get last report time
        lastReport = movement.stream().
                sorted( TrainMovement::compareReverse ).
                filter( TrainMovement::isTsPresent ).
                map( TrainMovement::getAt ).
                filter( Objects::nonNull ).
                findAny().
                orElse( null );

        // Where we are expected next
        if( isRunning() ) {
            // Running so find the next movement
            nextReport = movement.stream().
                    // Filter out all who's expected time is before the last report
                    filter( m -> {
                        LocalTime t = m.getScheduledTime();
                        return t == null ? false : t.isAfter( lastReport );
                    } ).
                    // Filter out suppressed and entries with no arrival time or a recorded time
                    filter( TrainMovement.isNotSuppressed.and( TrainMovement::isSetArrival ).and( m -> !m.isSetAt() ) ).
                    // Get the first one
                    findAny().
                    orElse( null );

            untilNextReport = nextReport == null ? null : Duration.between( lastReport, nextReport.getExpectedTime() );
        }
        else {
            // Not running so find the first departure
            nextReport = movement.stream().
                    filter( TrainMovement.isNotSuppressed.and( TrainMovement::isSetDeparture ).and( m -> !m.isSetAt() ) ).
                    findAny().
                    orElse( null );

            untilNextReport = nextReport == null ? null : Duration.between( LocalTime.now(), nextReport.getExpectedTime() );
        }
    }

    public String getRid()
    {
        return rid;
    }

    public boolean isSchedulePresent()
    {
        return schedule != null;
    }

    public boolean isDeactivated()
    {
        return pport.getUR().isSetDeactivated();
    }

    public boolean isTsPresent()
    {
        return ts != null;
    }

    public Collection<TrainMovement> getMovement()
    {
        return movement;
    }

    /**
     * The last reported TSLocation
     * <p>
     * @return TrainMovement or null
     */
    public TrainMovement getLastReportedMovement()
    {
        return movement.stream().
                sorted( TrainMovement::compareReverse ).
                filter( m -> m.isTsPresent() && m.isSetAt() ).
                findAny().
                orElse( null );
    }

    /**
     * Returns the TrainMovement for a specified tiploc
     * <p>
     * @param tpl tiploc
     * <p>
     * @return TrainMovement or null if not found
     */
    public TrainMovement getMovement( String tpl )
    {
        return movement.stream().
                filter( m -> m.getTpl().equals( tpl ) ).
                findAny().
                orElse( null );
    }

    public Pport getPport()
    {
        return pport;
    }

    public Schedule getSchedule()
    {
        return schedule;
    }

    public TS getTs()
    {
        return ts;
    }

    public TplLocation getOrigin()
    {
        return schedule == null ? null : schedule.getOrigin();
    }

    public TplLocation getDestination()
    {
        return schedule == null ? null : schedule.getDestination();
    }

    /**
     * The time of the last report.
     * <p>
     * @return LocalTime or midnight/{@link LocalTime#MIN} if none
     */
    public LocalTime getLastReport()
    {
        return lastReport == null ? LocalTime.MIN : lastReport;
    }

    /**
     * Returns the next expected report location.
     * <p>
     * Note: this will be limited to a movement with a GBTT or WTT arrival time
     * <p>
     * @return next TrainMovement or null.
     */
    public TrainMovement getNextReport()
    {
        return nextReport;
    }

    /**
     * The duration until the next report
     * <p>
     * @return Duration or null
     */
    public Duration getUntilNextReport()
    {
        return untilNextReport;
    }

    /**
     * Is this train running or has run?
     * <p>
     * This is defined by having a lastReport time
     * <p>
     * @return true if we have had a report, false if none
     */
    public boolean isRunning()
    {
        return lastReport != null;
    }

}
