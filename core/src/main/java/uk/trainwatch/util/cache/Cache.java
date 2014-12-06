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
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import uk.trainwatch.util.CollectorUtils;
import uk.trainwatch.util.DaemonThreadFactory;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.LocalDateTimeRange;
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
        map.values().
                removeIf( e -> !e.isAfter( expires ) );
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
            synchronized( this )
            {
                LocalDateTimeRange range = new LocalDateTimeRange();
                while( size() > maxSize )
                {
                    range.reset();
                    map.values().
                            stream().
                            map( CacheEntry::getEntered ).
                            forEach( range );

                    range.ifPresent( r -> expire( r.getMin() ) );
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
        map.forEach( ( k, v ) -> action.accept( k, getV( v ) ) );
    }

    public void forEachSQL( SQLBiConsumer<? super K, ? super V> action )
            throws SQLException
    {
        map.forEachSQL( ( k, v ) -> action.accept( k, getV( v ) ) );
    }

    public V computeIfAbsent( K key, Function<? super K, ? extends V> mappingFunction )
    {
        try
        {
            return getV( map.computeIfAbsent( key, k -> getC( mappingFunction.apply( k ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V computeIfPresent( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        try
        {
            return getV( map.computeIfPresent( key, ( k, v ) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V compute( K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction )
    {
        try
        {
            return getV( map.compute( key, ( k, v ) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V merge( K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction )
    {
        try
        {
            return getV(
                    map.merge( key, getC( value ),
                               ( o, v ) -> getC( remappingFunction.apply( getV( o ), getV( v ) ) ) )
            );
        }
        finally
        {
            expire();
        }
    }

    public void forEachSQL( SQLConsumer<? super V> action )
            throws SQLException
    {
        map.forEachSQL( v -> action.accept( getV( v ) ) );
    }

    public V computeSQLIfAbsent( K key, SQLSupplier<? extends V> mappingFunction )
            throws SQLException
    {
        try
        {
            return getV( map.computeSQLIfAbsent( key, () -> getC( mappingFunction.get() ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V computeSQLIfPresent( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return getV( map.computeSQLIfPresent( key, ( k, v ) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V computeSQL( K key, SQLBiFunction<? super K, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return getV( map.computeSQL( key, ( k, v ) -> getC( remappingFunction.apply( k, getV( v ) ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    public V mergeSQL( K key, V value, SQLBiFunction<? super V, ? super V, ? extends V> remappingFunction )
            throws SQLException
    {
        try
        {
            return getV( map.
                    mergeSQL( key, getC( value ), ( o, v ) -> getC( remappingFunction.apply( getV( o ), getV( v ) ) ) ) );
        }
        finally
        {
            expire();
        }
    }

    /**
     * Call a Consumer with a value in the cache only if it's present.
     * <p>
     * @param k Key
     * @param c Consumer
     */
    public void ifPresent( K k, Consumer<V> c )
    {
        CacheEntry<K, V> e = map.get( k );
        if( e != null )
        {
            c.accept( getV( e ) );
        }
    }

    /**
     * Call a BiConsumer with a value in the cache only if it's present.
     * <p>
     * @param k Key
     * @param c BiConsumer
     */
    public void ifPresent( K k, BiConsumer<K, V> c )
    {
        CacheEntry<K, V> e = map.get( k );
        if( e != null )
        {
            c.accept( k, getV( e ) );
        }
    }

    /**
     * Call a Consumer with they key if it's not present in the cache.
     * <p>
     * @param k Key
     * @param c Consumer
     */
    public void ifAbsent( K k, Consumer<K> c )
    {
        if( !map.containsKey( k ) )
        {
            c.accept( k );
        }
    }

    /**
     * Call a BiConsumer with the cache and key if the key is not present in the cache.
     * <p>
     * @param k Key
     * @param c Consumer
     */
    public void ifAbsent( K k, BiConsumer<Cache<K, V>, K> c )
    {
        if( !map.containsKey( k ) )
        {
            c.accept( this, k );
        }
    }

    /**
     * Computes a value if it's not present in the cache and then if the cache entry is present passes the final value to a Consumer.
     * <p>
     * If the cache does not contain an entry the mapping function is used to create a new one. If the function returns a non-null entry then the cache is
     * updated.
     * <p>
     * The consumer is only called if the entry is not null.
     * <p>
     * @param k Key
     * @param m Mapping function to create the new value.
     * @param c Consumer
     */
    public void compute( K k, Function<? super K, ? extends V> m, Consumer<V> c )
    {
        V v = computeIfAbsent( k, m );
        if( v != null )
        {
            c.accept( v );
        }
    }

    /**
     * Computes a value if it's not present in the cache and then if the cache entry is present passes the final value to a BiConsumer.
     * <p>
     * If the cache does not contain an entry the mapping function is used to create a new one. If the function returns a non-null entry then the cache is
     * updated.
     * <p>
     * The consumer is only called if the entry is not null.
     * <p>
     * @param k Key
     * @param m Mapping function to create the new value.
     * @param c Consumer
     */
    public void compute( K k, Function<? super K, ? extends V> m, BiConsumer<K, V> c )
    {
        V v = computeIfAbsent( k, m );
        if( v != null )
        {
            c.accept( k, v );
        }
    }

    /**
     * Pass a cache value to a Consumer with the value in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       Consumer if the value exists in the cache
     * <p>
     * @return Consumer
     */
    public <T> Consumer<T> consume( Function<T, K> kMapper, Consumer<V> c )
    {
        return t -> ifPresent( kMapper.apply( t ), c );
    }

    /**
     * Pass a cache value to a BiConsumer with the value in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       BiConsumer if the value exists in the cache
     * <p>
     * @return BiConsumer
     */
    public <T> Consumer<T> consume( Function<T, K> kMapper, BiConsumer<K, V> c )
    {
        return t -> ifPresent( kMapper.apply( t ), c );
    }

    /**
     * Pass a cache value to a Consumer if it's present in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       Consumer if the value exists in the cache
     * <p>
     * @return Consumer
     */
    public <T> Consumer<T> consumeIfPresent( Function<T, K> kMapper, Consumer<V> c )
    {
        return t -> ifPresent( kMapper.apply( t ), c );
    }

    /**
     * Pass a cache value to a BiConsumer if it's present in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       BiConsumer if the value exists in the cache
     * <p>
     * @return BiConsumer
     */
    public <T> Consumer<T> consumeIfPresent( Function<T, K> kMapper, BiConsumer<K, V> c )
    {
        return t -> ifPresent( kMapper.apply( t ), c );
    }

    /**
     * Pass the key to a Consumer if it's absent in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       Consumer if the key is absent
     * <p>
     * @return Consumer
     */
    public <T> Consumer<T> consumeIfAbsent( Function<T, K> kMapper, Consumer<K> c )
    {
        return t -> ifAbsent( kMapper.apply( t ), c );
    }

    /**
     * Pass the key to a BiConsumer if it's absent in the cache
     * <p>
     * @param <T>     Type to consume
     * @param kMapper Mapping function to convert T into the cache key
     * @param c       BiConsumer if the key is absent
     * <p>
     * @return BiConsumer
     */
    public <T> Consumer<T> consumeIfAbsent( Function<T, K> kMapper, BiConsumer<Cache<K, V>, K> c )
    {
        return t -> ifAbsent( kMapper.apply( t ), c );
    }

    /**
     * Returns a {@link Collector} that will populate this cache
     * <p>
     * @param <T>         Type of input into the collector
     * @param keyMapper   Mapping function to generate the key from T
     * @param valueMapper Mapping function to generate the value from T
     * <p>
     * @return Collector
     */
    public <T> Collector<T, ?, V> collect( Function<T, K> keyMapper, Function<T, V> valueMapper )
    {
        return collect( keyMapper, valueMapper, Functions.writeOnceBinaryOperator() );
    }

    /**
     * Returns a {@link Collector} that will populate this cache
     * <p>
     * @param <T>         Type of input into the collector
     * @param keyMapper   Mapping function to generate the key from T
     * @param valueMapper Mapping function to generate the value from T
     * <p>
     * @return Collector
     */
    public <T> Collector<T, K, V> collect( Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<K> combiner )
    {
        return CollectorUtils.collector( () -> null,
                                         ( n, t ) -> computeIfAbsent( keyMapper.apply( t ), k -> valueMapper.apply( t ) ),
                                         combiner,
                                         CollectorUtils.IDENTITY_CHARACTERISTICS );
    }

    /**
     * Returns a {@link Collector} that will populate this cache
     * <p>
     * @param keyMapper Mapping function to generate the key from the value
     * <p>
     * @return Collector
     */
    public Collector<V, ?, V> collect( Function<V, K> keyMapper )
    {
        return CollectorUtils.
                identityCollector( () -> null,
                                   ( n, t ) -> computeIfAbsent( keyMapper.apply( t ), k -> t ),
                                   Functions.writeOnceBinaryOperator() );
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

        public boolean isAfter( ChronoLocalDateTime<?> other )
        {
            return entered.isAfter( other );
        }
    }

}
