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
import uk.trainwatch.nre.darwin.model.ppt.alarms.RTTIAlarm;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Association;
import uk.trainwatch.nre.darwin.model.ppt.schedules.DeactivatedSchedule;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Schedule;
import uk.trainwatch.nre.darwin.model.ppt.schema.DataResponse;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.model.ppt.tddata.TrackingID;
import uk.trainwatch.nre.darwin.model.ppt.trainalerts.TrainAlert;
import uk.trainwatch.nre.darwin.model.ppt.trainorder.TrainOrder;

/**
 * Takes a {@link Pport} and dispatches it to a relevant {@link BiConsumer} based on the messages content
 *
 * @author peter
 */
public final class DarwinDispatcher
        implements Consumer<Pport>
{

    private final BiConsumer<Pport, Schedule> schedule;
    private final BiConsumer<Pport, DeactivatedSchedule> deactivatedSchedule;
    private final BiConsumer<Pport, Association> association;
    private final BiConsumer<Pport, TS> ts;
    private final BiConsumer<Pport, StationMessage> stationMessage;
    private final BiConsumer<Pport, TrainAlert> trainAlert;
    private final BiConsumer<Pport, TrainOrder> trainOrder;
    private final BiConsumer<Pport, TrackingID> trackingID;
    private final BiConsumer<Pport, RTTIAlarm> alarm;

    DarwinDispatcher( BiConsumer<Pport, Schedule> schedule, BiConsumer<Pport, DeactivatedSchedule> deactivatedSchedule, BiConsumer<Pport, Association> association, BiConsumer<Pport, TS> ts, BiConsumer<Pport, StationMessage> stationMessage, BiConsumer<Pport, TrainAlert> trainAlert, BiConsumer<Pport, TrainOrder> trainOrder, BiConsumer<Pport, TrackingID> trackingID, BiConsumer<Pport, RTTIAlarm> alarm )
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

    private <T> void dispatch( Pport p, BiConsumer<Pport, T> c, Supplier<List<T>> s )
    {
        if( c != null )
        {
            final List<T> l = s.get();
            if( !l.isEmpty() )
            {
                l.forEach( v -> c.accept( p, v ) );
            }
        }
    }

}
