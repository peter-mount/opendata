/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

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

    private Consumer<Pport> schedule = null;
    private Consumer<Pport> deactivatedSchedule = null;
    private Consumer<Pport> association = null;
    private Consumer<Pport> ts = null;
    private Consumer<Pport> stationMessage = null;
    private Consumer<Pport> trainAlert = null;
    private Consumer<Pport> trainOrder = null;
    private Consumer<Pport> trackingID = null;
    private Consumer<Pport> alarm = null;

    public DarwinDispatcherBuilder()
    {
    }

    public DarwinDispatcherBuilder addSchedule( Consumer<Pport> schedule )
    {
        this.schedule = this.schedule == null ? schedule : schedule.andThen( schedule );
        return this;
    }

    public DarwinDispatcherBuilder addDeactivatedSchedule( Consumer<Pport> deactivatedSchedule )
    {
        this.deactivatedSchedule = this.deactivatedSchedule == null ? deactivatedSchedule : deactivatedSchedule.andThen( deactivatedSchedule );
        return this;
    }

    public DarwinDispatcherBuilder addAssociation( Consumer<Pport> association )
    {
        this.association = this.association == null ? association : association.andThen( association );
        return this;
    }

    public DarwinDispatcherBuilder addTs( Consumer<Pport> ts )
    {
        this.ts = this.ts == null ? ts : ts.andThen( ts );
        return this;
    }

    public DarwinDispatcherBuilder addStationMessage( Consumer<Pport> stationMessage )
    {
        this.stationMessage = this.stationMessage == null ? stationMessage : stationMessage.andThen( stationMessage );
        return this;
    }

    public DarwinDispatcherBuilder addTrainAlert( Consumer<Pport> trainAlert )
    {
        this.trainAlert = this.trainAlert == null ? trainAlert : trainAlert.andThen( trainAlert );
        return this;
    }

    public DarwinDispatcherBuilder addTrainOrder( Consumer<Pport> trainOrder )
    {
        this.trainOrder = this.trainOrder == null ? trainOrder : trainOrder.andThen( trainOrder );
        return this;
    }

    public DarwinDispatcherBuilder addTrackingID( Consumer<Pport> trackingID )
    {
        this.trackingID = this.trackingID == null ? trackingID : trackingID.andThen( trackingID );
        return this;
    }

    public DarwinDispatcherBuilder addAlarm( Consumer<Pport> alarm )
    {
        this.alarm = this.alarm == null ? alarm : alarm.andThen( alarm );
        return this;
    }

    public DarwinDispatcher build()
    {
        return new DarwinDispatcher( schedule, deactivatedSchedule, association, ts, stationMessage, trainAlert, trainOrder, trackingID, alarm );
    }

}
