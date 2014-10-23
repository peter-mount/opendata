/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.cache;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.DaemonThreadFactory;
import uk.trainwatch.util.sql.ConcurrentSQLHashMap;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLBiFunction;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.SQLSupplier;

/**
 * A Cache backed by a {@link ConcurrentSQLHashMap} which with expire entries based on age and maximum cache size.
 * <p>
 * @param <K>
 * @param <V> <p>
 * @author Peter T Mount
 */
public class Cache<K, V>

{

    private static final Logger LOG = Logger.getLogger( Cache.class.getName() );

    /**
     * The default max age for elements, 10 minutes
     */
    private static final Duration DEFAULT_MAX_AGE = Duration.ofMinutes( 10 );
    /**
     * The default max size of 1000 entries
     */
    private static final long DEFAULT_MAX_SIZE = 1000;

    private final ConcurrentSQLHashMap<K, CacheEntry<K, V>> map = new ConcurrentSQLHashMap<>();

    private final long maxSize;
    private final Duration maxAge;

    public Cache()
    {
        this( DEFAULT_MAX_SIZE, DEFAULT_MAX_AGE );
    }

    public Cache( long maxSize )
    {
        this( maxSize, DEFAULT_MAX_AGE );
    }

    public Cache( Duration maxAge )
    {
        this( DEFAULT_MAX_SIZE, maxAge );
    }

    public Cache( long maxSize, Duration maxAge )
    {
        this.maxSize = maxSize;
        this.maxAge = maxAge;

        // Expire by age in the background once every minute
        DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( this::expireByAge, 1L, 1L, TimeUnit.MINUTES );
    }

    /**
     * Run by the timer, expires all entries if they are old enough.
     */
    private void expireByAge()
    {
        // While we are at it, just make certain we are not too big
        expire();

        // Now expire old entries
        final long size = size();

        LocalDateTime expiry = LocalDateTime.now().
                minus( maxAge );
        LOG.log( Level.FINE, () -> "Expiring entries older than " + expiry );

        expire( expiry );

        final long end = size();
        LOG.log( Level.INFO, () -> "Expired " + (size - end) + " entries. Now have " + end );
    }

    /**
     * Expires all entries that were inserted before expires
     * <p>
     * @param expires LocalDateTime marking point entries are removed
     */
    private void expire( LocalDateTime expires )
    {
        LOG.log( Level.FINE, () -> "Size before: " + size() );
        map.values().
                removeIf( e -> e.isBefore( expires ) );
        LOG.log( Level.INFO, () -> "Size after: " + size() );
    }

    /**
     * Expires the oldest entries if we are too big.
     * <p>
     * Here we find the oldest entry, then expire everything within 2 minutes of that entry
     */
    private void expire()
    {
        if( size() > maxSize )
        {
            LOG.log( Level.INFO, () -> "expiring as above " + maxSize + " currently " + size() );
            synchronized( this )
            {
                while( size() > maxSize )
                {
                    map.values().
                            stream().
                            map( CacheEntry::getEntered ).
                            min( LocalDateTime::compareTo ).
                            ifPresent( dt -> expire( dt.plusMinutes( 2l ) ) );
                }
            }
        }
    }

    private V getV( CacheEntry<K, V> e )
    {
        return e == null ? null : e.getValue();
    }

    private CacheEntry<K, V> getC( V v )
    {
        return v == null ? null : new CacheEntry<>( v );
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public void clear()
    {
        map.clear();
    }

    public V get( K key )
    {
        return getV( map.get( key ) );
    }

    public V getOrDefault( K key, V defaultValue )
    {
        V v;
        return (v = get( key )) == null ? defaultValue : v;
    }

    public boolean containsKey( K key )
    {
        return map.containsKey( key );
    }

    private V putImpl( K key, V value )
    {
        return getV( map.put( key, new CacheEntry<>( value ) ) );
    }

    public V put( K key, V value )
    {
        V v = putImpl( key, value );
        expire();
        return v;
    }

    public void putAll( Map<? extends K, ? extends V> m )
    {
        m.entrySet().
                stream().
                forEach( e -> put( e.getKey(), e.getValue() ) );
        expire();
    }

    public V remove( K key )
    {
        return getV( map.remove( key ) );
    }

    public Set<K> keySet()
    {
        return map.keySet();
    }

    public V putIfAbsent( K key, V value )
    {
        V v = getV( map.putIfAbsent( key, new CacheEntry<>( value ) ) );
        expire();
        return v;
    }

    public boolean remove( Object key, Object value )
    {
        return map.remove( key, value );
    }

    public void forEach( BiConsumer<? super K, ? super V> action )
    {
        map.forEach( (k, v) -> action.accept( k, getV( v ) ) );
    }

    public void forEachSQL( SQLBiConsumer<? super K, ? super V> action )
            throws SQLException
    {
        map.forEachSQL( (k, v) -> action.accept( k, getV( v ) ) );
    }

    public V computeIfAbsent( K key, Function<? super K, ? extends V> mappingFunction )
    {
        return getV( map.computeIfAbsent( key, k -> getC( mappingFunction.apply( k ) ) ) );
    }

    public V computeIfPresent( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        expire();
        return getV( map.computeIfPresent( key, (k, v) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
    }

    public V compute( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        expire();
        return getV( map.compute( key, (k, v) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
    }

    public V merge( K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction )
    {
        expire();
        return getV(
                map.merge( key, getC( value ),
                           (o, v) -> getC( remappingFunction.apply( getV( o ), getV( v ) ) ) )
        );
    }

    public void forEachSQL( SQLConsumer<? super V> action )
            throws SQLException
    {
        map.forEachSQL( v -> action.accept( getV( v ) ) );
    }

    public V computeSQLIfAbsent( K key, SQLSupplier<? extends V> mappingFunction )
            throws SQLException
    {
        return getV( map.computeSQLIfAbsent( key, () -> getC( mappingFunction.get() ) ) );
    }

    public V computeSQLIfPresent( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        return getV( map.computeSQLIfPresent( key, (k, v) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
    }

    public V computeSQL( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        return getV( map.computeSQL( key, (k, v) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
    }

    public V mergeSQL( K key, V value, SQLBiFunction<? super V, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        return getV( map.
                mergeSQL( key, getC( value ), (o, v) -> getC( remappingFunction.apply( getV( o ), getV( v ) ) ) ) );
    }

    private static class CacheEntry<K, V>
    {

        private final V value;
        private final LocalDateTime entered;

        public CacheEntry( V value )
        {
            this.value = value;
            entered = LocalDateTime.now();
        }

        public CacheEntry( K key, V value )
        {
            this( value );
        }

        public LocalDateTime getEntered()
        {
            return entered;
        }

        public V getValue()
        {
            return value;
        }

        public boolean isBefore( ChronoLocalDateTime<?> other )
        {
            return entered.isBefore( other );
        }

    }

}
