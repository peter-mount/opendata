/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench.trust;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.sql.Operator;
import uk.trainwatch.nrod.rtppm.sql.OperatorManager;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementFactory;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitConsumer;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.config.JNDIConfig;
import uk.trainwatch.util.counter.RateMonitor;

/**
 * Handles the storage of current data
 * <p>
 * @author peter
 */
@ApplicationScoped
public class TrustCache
        implements Consumer<TrustMovement>
{

    /**
     * The max age in hours. This must be >2 hours due to train activations occurring 2 hours prior to a service is due to start
     */
    private static final long MAX_AGE_HOURS = 3;

    @Inject
    private OperatorManager operatorManager;

    private static final Logger LOG = Logger.getLogger( TrustCache.class.getName() );

    private Timer timer;

    private RabbitConnection rabbitConnection;

    private final Map<Integer, Map<String, Trust>> trains = new ConcurrentHashMap<>();

    /**
     * Retrieves a Trust instance of a toc/trainId.
     * <p>
     * @param toc
     * @param trainId <p>
     * @return
     */
    public Trust getTrust( int toc, String trainId )
    {
        return trains.computeIfAbsent( toc, k -> new ConcurrentHashMap<>() ).
                computeIfAbsent( trainId, id -> new Trust( toc, id ) );
    }

    public Trust getTrustIfPresent( int toc, String trainId )
    {
        return trains.getOrDefault( toc, Collections.emptyMap() ).
                getOrDefault( trainId, null );
    }

    /**
     * Retrieve a collection of toc id's currently in the cache.
     * <p>
     * This is a snapshot of the entries at the moment the cache is viewed
     * <p>
     * @return
     */
    public Map<Integer, String> getTocs()
    {
        return trains.keySet().
                stream().
                collect( Collectors.toMap( Function.identity(), id ->
                                           {
                                               Operator o = operatorManager.getOperator( id );
                                               return o == null ? String.valueOf( id ) : o.getDisplay();
                } ) );
    }

    /**
     * Retrieve a snapshot of active trains in the cache for a specific toc.
     * <p>
     * @param toc toc id
     * <p>
     * @return Collection of Trust instances, never null
     */
    public Collection<Trust> getTrains( int toc )
    {
        return new TreeSet<>( trains.getOrDefault( toc, Collections.emptyMap() ).
                values() );
    }

    public Stream<Trust> getStream( int toc )
    {
        return getTrains( toc ).
                stream();
    }

    /**
     * Expire all entries not touched for 2 hours
     */
    public void expireCache()
    {
        final LocalDateTime expiryTime = LocalDateTime.now().
                minusHours( MAX_AGE_HOURS );

        trains.forEach( ( toc, tocTrains ) ->
        {
            final long initialSize = tocTrains.size();
            LOG.log( Level.FINE, () -> "Expiring cache for toc " + toc + " size " + initialSize );

            tocTrains.values().
                    removeIf( t -> t.isExpired( expiryTime ) );

            final long finalSize = tocTrains.size();
            LOG.log( initialSize != finalSize ? Level.INFO : Level.FINE,
                     () -> "Expired cache for toc " + toc + " size " + finalSize + " expired " + (initialSize - finalSize) );
        } );
    }

    @Override
    public void accept( TrustMovement m )
    {
        getTrust( m.getToc_id(), m.getTrain_id() ).accept( m );
    }

    @PostConstruct
    void start()
    {
        if( JNDIConfig.INSTANCE.getBoolean( "workbench.disabled" ) )
        {
            return;
        }

        LOG.log( Level.INFO, "Initialising Trust" );

        // Start the timer
        timer = new Timer( "TrustCache", true );
        timer.scheduleAtFixedRate( new TimerTask()
        {

            @Override
            public void run()
            {
                expireCache();
            }
        }, 60000L, 60000L );

        LOG.log( Level.INFO, () -> "Connecting to MQ" );

        // Connect to rabbitmq
        rabbitConnection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );

        // Activate any timetables
        String activationRoutingKey = "trust.activaton." + RabbitMQ.getHostname();
        Consumer<byte[]> ttResolverPublisher = new RabbitConsumer( rabbitConnection, activationRoutingKey );
        RabbitMQ.queueDurableStream( rabbitConnection, "trust.activation", activationRoutingKey, s -> s.
                                     map( RabbitMQ.toString ).
                                     map( JsonUtils.parseJsonObject ).
                                     map( Functions.castTo( JsonObject.class ) ).
                                     filter( Objects::nonNull ).
                                     forEach( new TimetableResolver() ) );

        // Consume raw trust mvt feed
        RabbitMQ.queueDurableStream( rabbitConnection, "trust.status", "nr.trust.mvt", s -> s.
                                     map( RabbitMQ.toString ).
                                     map( JsonUtils.parseJsonObject ).
                                     map( TrustMovementFactory.INSTANCE ).
                                     filter( Objects::nonNull ).
                                     forEach( this.
                                             andThen( new TimetableActivator( ttResolverPublisher ) ).
                                             andThen( RateMonitor.log( LOG, "Receive Trust" ) ) )
        );

        LOG.log( Level.INFO, "Trust initialised" );
    }

    @PreDestroy
    void stop()
    {
        LOG.log( Level.INFO, "Shutting down Trust" );

        try
        {
            if( rabbitConnection != null )
            {
                rabbitConnection.close();
                rabbitConnection = null;
            }
        }
        finally
        {
            if( timer != null )
            {
                timer.cancel();
                timer = null;
            }
        }
    }

}
