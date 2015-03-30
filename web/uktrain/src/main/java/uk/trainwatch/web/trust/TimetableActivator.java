/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.util.function.Consumer;
import javax.json.Json;
import uk.trainwatch.nrod.trust.model.TrainActivation;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.rabbitmq.RabbitMQ;

/**
 *
 * @author peter
 */
public class TimetableActivator
        implements Consumer<TrustMovement>
{

    private final Consumer<byte[]> ttResolverPublisher;

    public TimetableActivator( Consumer<byte[]> ttResolverPublisher )
    {
        this.ttResolverPublisher = ttResolverPublisher;
    }

    @Override
    public void accept( TrustMovement m )
    {
        if( m instanceof TrainActivation ) {
            activate( (TrainActivation) m );
        }
        else if( m instanceof TrainMovement ) {
            activateMovement( (TrainMovement) m );
        }
    }

    private void activate( TrainActivation t )
    {
        ttResolverPublisher.accept( RabbitMQ.jsonToBytes.apply(
                Json.createObjectBuilder().
                add( "train_uid", t.getTrain_uid() ).
                add( "time", t.getOrigin_dep_timestamp() ).
                add( "toc_id", t.getToc_id() ).
                add( "train_id", t.getTrain_id() ).
                build() ) );
    }

    private void activateMovement( TrainMovement m )
    {
        Trust trust = TrustCache.INSTANCE.getTrustIfPresent( m.getToc_id(), m.getTrain_id() );
        if( trust != null && !Trust.isScheduled( trust ) && !trust.isScheduleFail() ) {
            ttResolverPublisher.accept( RabbitMQ.jsonToBytes.apply(
                    Json.createObjectBuilder().
                    add( "train_uid", m.getTrain_id().substring( 2, 7 ) ).
                    add( "time", m.getTimestamp() ).
                    add( "toc_id", trust.getToc() ).
                    add( "train_id", trust.getId() ).
                    build() ) );
        }
    }
}
