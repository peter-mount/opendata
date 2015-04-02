/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import uk.trainwatch.nre.darwin.model.ppt.schema.DataResponse;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * Takes a {@link Pport} and dispatches it to a relevant {@link BiConsumer} based on the messages content
 *
 * @author peter
 */
public final class DarwinDispatcher
        implements Consumer<Pport>
{

    private final Consumer<Pport> schedule;
    private final Consumer<Pport> deactivatedSchedule;
    private final Consumer<Pport> association;
    private final Consumer<Pport> ts;
    private final Consumer<Pport> stationMessage;
    private final Consumer<Pport> trainAlert;
    private final Consumer<Pport> trainOrder;
    private final Consumer<Pport> trackingID;
    private final Consumer<Pport> alarm;

    DarwinDispatcher( Consumer<Pport> schedule, Consumer<Pport> deactivatedSchedule, Consumer<Pport> association, Consumer<Pport> ts, Consumer<Pport> stationMessage, Consumer<Pport> trainAlert, Consumer<Pport> trainOrder, Consumer<Pport> trackingID, Consumer<Pport> alarm )
    {
        this.schedule = schedule;
        this.deactivatedSchedule = deactivatedSchedule;
        this.association = association;
        this.ts = ts;
        this.stationMessage = stationMessage;
        this.trainAlert = trainAlert;
        this.trainOrder = trainOrder;
        this.trackingID = trackingID;
        this.alarm = alarm;
    }

    @Override
    public void accept( Pport t )
    {
        if( t == null )
        {
            return;
        }

        DataResponse r = t.getUR();
        if( r == null )
        {
            return;
        }

        dispatch( t, schedule, r::getSchedule );
        dispatch( t, deactivatedSchedule, r::getDeactivated );
        dispatch( t, association, r::getAssociation );
        dispatch( t, ts, r::getTS );
        dispatch( t, stationMessage, r::getOW );
        dispatch( t, trainAlert, r::getTrainAlert );
        dispatch( t, trainOrder, r::getTrainOrder );
        dispatch( t, trackingID, r::getTrackingID );
        dispatch( t, alarm, r::getAlarm );
    }

    private <T> void dispatch( Pport p, Consumer<Pport> c, Supplier<List<T>> s )
    {
        if( c != null )
        {
            final List<T> l = s.get();
            if( l != null && !l.isEmpty() )
            {
                c.accept( p );
            }
        }
    }

}
