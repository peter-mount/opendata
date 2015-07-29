/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * A consumer which will route an object to a consumer based on a routing function
 *
 * @param <K> Routing key
 * @param <T> Payload type
 * <p>
 * @author peter
 */
public class Router<K, T>
        implements Consumer<T>,
                   Function<T, Consumer<T>>
{

    private final Map<K, Consumer<T>> consumers = new HashMap<>();
    private final Function<T, K> router;
    private Consumer<T> sink = Consumers.sink();
    private K lastKey;
    private BiConsumer<K, K> keyChangeConsumer;

    /**
     * Create a Router which will route buy the payload's class
     *
     * @param <T> Payload type
     * <p>
     * @return Router
     */
    public static <T> Router<Class<?>, T> createClassRouter()
    {
        return new Router<>( t -> t.getClass() );
    }

    /**
     * Constructor
     *
     * @param router Mapping function to convert the payload into the routing key
     */
    public Router( Function<T, K> router )
    {
        this.router = router;
    }

    @Override
    public void accept( T t )
    {
        if( t != null ) {
            K key = router.apply( t );

            if( keyChangeConsumer != null ) {
                if( lastKey != null && !lastKey.equals( key ) ) {
                    keyChangeConsumer.accept( lastKey, key );
                }
                lastKey = key;
            }

            consumers.getOrDefault( key, sink )
                    .accept( t );
        }
    }

    @Override
    public Consumer<T> apply( T t )
    {
        return consumers.getOrDefault( router.apply( t ), sink );
    }

    public void sink( K key )
    {
        consumers.put( key, sink );
    }

    public boolean isSinked( K key )
    {
        Consumer<T> c = consumers.get( key );
        return c != null && c.equals( sink );
    }

    public boolean isRouted( K key )
    {
        return consumers.containsKey( key );
    }

    /**
     * Add a route to this instance. If a route already exists then the consumer is composed with the existing route.
     *
     * @param key
     * @param consumer
     *                 <p>
     * @return
     */
    public Router<K, T> add( K key, Consumer<T> consumer )
    {
        consumers.merge( key, consumer, ( a, b ) -> a.andThen( b ) );
        return this;
    }

    /**
     * Add a route to this instance. If a route already exists then the consumer is composed with the existing route.
     *
     * @param key
     * @param consumer
     *                 <p>
     * @return
     */
    public Router<K, T> addSQL( K key, SQLConsumer<T> consumer )
    {
        return add( key, SQLConsumer.guard( consumer ) );
    }

    public Router setKeyChangeConsumer( BiConsumer<K, K> keyChangeConsumer )
    {
        this.keyChangeConsumer = keyChangeConsumer;
        return this;
    }

    public Router setSink( Consumer<T> sink )
    {
        this.sink = sink;
        return this;
    }

}
