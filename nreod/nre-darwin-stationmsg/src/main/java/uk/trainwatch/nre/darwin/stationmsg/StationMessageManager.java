/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.stationmsg;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.util.cache.Cache;

/**
 *
 * @author peter
 */
public enum StationMessageManager
        implements Consumer<Pport>
{

    INSTANCE;

    private final Logger log = Logger.getLogger( StationMessageManager.class.getName() );

    /**
     * Cache of messages
     */
    private final Cache<Integer, StationMessage> cache;
    /**
     * Map of locations to messages
     */
    private final Map<String, Collection<Integer>> locations = new ConcurrentHashMap<>();

    private StationMessageManager()
    {
        cache = new Cache<>( 10000, Duration.ofHours( 12 ) );
        cache.setEvicted( this::remove );
    }

    private Collection<Integer> getLocation( String tpl )
    {
        return locations.computeIfAbsent( tpl, k -> new CopyOnWriteArraySet<>() );
    }

    /**
     * Get all messages for a crs sorted by id
     * <p>
     * @param crs 3alpha code for a station
     * <p>
     * @return Stream of StationMessages, may be empty but never null
     */
    public Stream<StationMessage> getMessages( String crs )
    {
        return locations.getOrDefault( crs, Collections.emptyList() ).
                stream().
                sorted().
                map( cache::get ).
                filter( Objects::nonNull );
    }

    public StationMessage getMessage( int id )
    {
        return cache.get( id );
    }

    @Override
    public void accept( Pport t )
    {
        t.getUR().getOW().forEach( this::accept );
    }

    private void accept( StationMessage m )
    {
        int id = m.getId();

        log.log( Level.INFO, () -> "Received station message " + id );

        StationMessage oldM = cache.put( id, m );

        // TODO make this more efficient, we could have a brief moment when a rid is not visible
        if( oldM == null ) {
            log.log( Level.INFO, () -> "Creating " + id );
        }
        else {
            log.log( Level.INFO, () -> "Updating " + id );
            // Remove any xrefs in tpl if we have an update
            oldM.getStation().forEach( tl -> getLocation( tl.getCrs() ).remove( id ) );
        }

        // Add locations from t
        m.getStation().forEach( tl -> getLocation( tl.getCrs() ).add( id ) );
        log.log( Level.INFO, () -> "Complete " + cache.size() );

        // FIXME remove
        log.log( Level.INFO,
                 () -> "Affects: " + m.getStation().stream().map( StationMessage.Station::getCrs ).collect( Collectors.joining( ", " ) )
        );
    }

    /**
     * Remove a rid from the cache
     * <p>
     * @param id
     */
    public void remove( Integer id )
    {
        cache.computeIfPresent( id, this::remove );
    }

    private StationMessage remove( Integer id, StationMessage ts )
    {
        log.log( Level.INFO, () -> "Removing " + id );

        // Remove all xrefs
        ts.getStation().forEach( tl -> getLocation( tl.getCrs() ).remove( id ) );

        // Now remove from forecasts
        return null;
    }

}
