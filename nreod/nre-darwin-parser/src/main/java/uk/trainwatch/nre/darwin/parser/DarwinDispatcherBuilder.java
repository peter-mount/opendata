/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.function.BiConsumer;
import uk.trainwatch.nre.darwin.model.ppt.alarms.RTTIAlarm;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Association;
import uk.trainwatch.nre.darwin.model.ppt.schedules.DeactivatedSchedule;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Schedule;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.model.ppt.tddata.TrackingID;
import uk.trainwatch.nre.darwin.model.ppt.trainalerts.TrainAlert;
import uk.trainwatch.nre.darwin.model.ppt.trainorder.TrainOrder;

/**
 * A builder for {@link DarwinDispatcher}.
 *
 * Use this to create a DarwinDispatcher instance. You can add as many {@link BiConsumer}'s as you want. Adding more than one
 * consumer at a time to a message type will cause a composition so the previous and new consumers will both be executed in the
 * order they are added to the builder.
 *
 * @author peter
 */
public final class DarwinDispatcherBuilder
{

    private BiConsumer<Pport, Schedule> schedule = null;
    private BiConsumer<Pport, DeactivatedSchedule> deactivatedSchedule = null;
    private BiConsumer<Pport, Association> association = null;
    private BiConsumer<Pport, TS> ts = null;
    private BiConsumer<Pport, StationMessage> stationMessage = null;
    private BiConsumer<Pport, TrainAlert> trainAlert = null;
    private BiConsumer<Pport, TrainOrder> trainOrder = null;
    private BiConsumer<Pport, TrackingID> trackingID = null;
    private BiConsumer<Pport, RTTIAlarm> alarm = null;

    public DarwinDispatcherBuilder()
    {
    }

    public DarwinDispatcherBuilder addSchedule( BiConsumer<Pport, Schedule> schedule )
    {
        this.schedule = this.schedule == null ? schedule : schedule.andThen( schedule );
        return this;
    }

    public DarwinDispatcherBuilder addDeactivatedSchedule( BiConsumer<Pport, DeactivatedSchedule> deactivatedSchedule )
    {
        this.deactivatedSchedule = this.deactivatedSchedule == null ? deactivatedSchedule : deactivatedSchedule.andThen( deactivatedSchedule );
        return this;
    }

    public DarwinDispatcherBuilder addAssociation( BiConsumer<Pport, Association> association )
    {
        this.association = this.association == null ? association : association.andThen( association );
        return this;
    }

    public DarwinDispatcherBuilder addTs( BiConsumer<Pport, TS> ts )
    {
        this.ts = this.ts == null ? ts : ts.andThen( ts );
        return this;
    }

    public DarwinDispatcherBuilder addStationMessage( BiConsumer<Pport, StationMessage> stationMessage )
    {
        this.stationMessage = this.stationMessage == null ? stationMessage : stationMessage.andThen( stationMessage );
        return this;
    }

    public DarwinDispatcherBuilder addTrainAlert( BiConsumer<Pport, TrainAlert> trainAlert )
    {
        this.trainAlert = this.trainAlert == null ? trainAlert : trainAlert.andThen( trainAlert );
        return this;
    }

    public DarwinDispatcherBuilder addTrainOrder( BiConsumer<Pport, TrainOrder> trainOrder )
    {
        this.trainOrder = this.trainOrder == null ? trainOrder : trainOrder.andThen( trainOrder );
        return this;
    }

    public DarwinDispatcherBuilder addTrackingID( BiConsumer<Pport, TrackingID> trackingID )
    {
        this.trackingID = this.trackingID == null ? trackingID : trackingID.andThen( trackingID );
        return this;
    }

    public DarwinDispatcherBuilder addAlarm( BiConsumer<Pport, RTTIAlarm> alarm )
    {
        this.alarm = this.alarm == null ? alarm : alarm.andThen( alarm );
        return this;
    }

    public DarwinDispatcher build()
    {
        return new DarwinDispatcher( schedule, deactivatedSchedule, association, ts, stationMessage, trainAlert, trainOrder, trackingID, alarm );
    }

}
