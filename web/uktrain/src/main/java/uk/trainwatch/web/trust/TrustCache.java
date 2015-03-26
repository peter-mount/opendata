/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import uk.trainwatch.nrod.rtppm.sql.Operator;
import uk.trainwatch.nrod.rtppm.sql.OperatorManager;

/**
 * Handles the storage of current data
 * <p>
 * @author peter
 */
public enum TrustCache
{

    INSTANCE;

    /**
     * The max age in hours. This must be >2 hours due to train activations occurring 2 hours prior to a service is due to start
     */
    private static final long MAX_AGE_HOURS = 3;

    private final Logger log = Logger.getLogger( TrustCache.class.getName() );

    private final Map<Integer, Map<String, Trust>> trains = new ConcurrentHashMap<>();

    /**
     * Retrieves a Trust instance of a toc/trainId.
     * <p>
     * @param toc
     * @param trainId
     * <p>
     * @return
     */
    public Trust getTrust( int toc, String trainId )
    {
        return trains.computeIfAbsent( toc, k -> new ConcurrentHashMap<>() ).
                computeIfAbsent( trainId, id -> new Trust( toc, id ) );
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
        return trains.keySet().stream().
                collect( Collectors.toMap( Function.identity(), id ->
                                {
                                    Operator o = OperatorManager.INSTANCE.getOperator( id );
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
            log.log( Level.FINE, () -> "Expiring cache for toc " + toc + " size " + initialSize );

            tocTrains.values().
                    removeIf( t -> t.isExpired( expiryTime ) );

            final long finalSize = tocTrains.size();
            log.log( initialSize != finalSize ? Level.INFO : Level.FINE,
                    () -> "Expired cache for toc " + toc + " size " + finalSize + " expired " + (initialSize - finalSize) );
        } );
    }
}
