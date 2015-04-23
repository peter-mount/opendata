/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Pport pport;
    private final List<TrainMovement> movement;
    private final Schedule schedule;
    private final TS ts;

    Train( Pport pport )
    {
        this.pport = pport;

        Pport.UR ur = pport.getUR();
        schedule = ur.getSchedule().stream().findAny().orElse( null );
        ts = ur.getTS().stream().findAny().orElse( null );

        Map<String, TrainMovement> map = new HashMap<>();

        if( isSchedulePresent() ) {
            schedule.getOROrOPOROrIP().
                    stream().
                    map( TplLocation::castTplLocation ).
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
    }

    public boolean isSchedulePresent()
    {
        return schedule != null;
    }

    public boolean isTsPresent()
    {
        return ts != null;
    }

    public Collection<TrainMovement> getMovement()
    {
        return movement;
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

}
