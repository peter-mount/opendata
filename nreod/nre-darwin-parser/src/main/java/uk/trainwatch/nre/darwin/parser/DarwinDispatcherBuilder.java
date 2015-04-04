/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import uk.trainwatch.nre.darwin.model.ppt.schema.DataResponse;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.util.BiConsumers;
import uk.trainwatch.util.Consumers;

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
        this.schedule = Consumers.andThen( this.schedule, schedule );
        return this;
    }

    public DarwinDispatcherBuilder addDeactivatedSchedule( Consumer<Pport> deactivatedSchedule )
    {
        this.deactivatedSchedule = Consumers.andThen( this.deactivatedSchedule, deactivatedSchedule );
        return this;
    }

    public DarwinDispatcherBuilder addAssociation( Consumer<Pport> association )
    {
        this.association = Consumers.andThen( this.association, association );
        return this;
    }

    public DarwinDispatcherBuilder addTs( Consumer<Pport> ts )
    {
        this.ts = Consumers.andThen( this.ts, ts );
        return this;
    }

    public DarwinDispatcherBuilder addStationMessage( Consumer<Pport> stationMessage )
    {
        this.stationMessage = Consumers.andThen( this.stationMessage, stationMessage );
        return this;
    }

    public DarwinDispatcherBuilder addTrainAlert( Consumer<Pport> trainAlert )
    {
        this.trainAlert = Consumers.andThen( this.trainAlert, trainAlert );
        return this;
    }

    public DarwinDispatcherBuilder addTrainOrder( Consumer<Pport> trainOrder )
    {
        this.trainOrder = Consumers.andThen( this.trainOrder, trainOrder );
        return this;
    }

    public DarwinDispatcherBuilder addTrackingID( Consumer<Pport> trackingID )
    {
        this.trackingID = Consumers.andThen( this.trackingID, trackingID );
        return this;
    }

    public DarwinDispatcherBuilder addAlarm( Consumer<Pport> alarm )
    {
        this.alarm = Consumers.andThen( this.alarm, alarm );
        return this;
    }

    /**
     * Used by build(), if the consumer has been defined then return a BiConsumer that will test a Pport message and invoke the consumer
     * <p>
     * @param c
     * @param s
     * <p>
     * @return
     */
    private BiConsumer<Pport, DataResponse> build( Consumer<Pport> c, Function<DataResponse, List<?>> s )
    {
        // No consumer defined then do nothing
        if( c == null ) {
            return null;
        }

        // BiConsumer which will test for the required elements & only if they exist forward to the underlying consumer
        return ( p, r ) -> {
            final List<?> l = s.apply( r );
            if( l != null && !l.isEmpty() ) {
                c.accept( p );
            }
        };
    }

    public Consumer<Pport> build()
    {
        // Build a BiConsumer which will generate just those required to be tested
        // based on the fields being checked
        BiConsumer<Pport, DataResponse> c = BiConsumers.andThen(
                build( schedule, DataResponse::getSchedule ),
                build( deactivatedSchedule, DataResponse::getDeactivated ),
                build( association, DataResponse::getAssociation ),
                build( ts, DataResponse::getTS ),
                build( stationMessage, DataResponse::getOW ),
                build( trainAlert, DataResponse::getTrainAlert ),
                build( trainOrder, DataResponse::getTrainOrder ),
                build( trackingID, DataResponse::getTrackingID ),
                build( alarm, DataResponse::getAlarm )
        );

        // Return the actual consumer. This will test to ensure Pport & Pport.UR and not null before passing to our test above
        return t -> {
            if( t != null ) {
                DataResponse r = t.getUR();
                if( r != null ) {
                    c.accept( t, r );
                }
            }
        };
    }

}
