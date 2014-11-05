/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.util;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A suite of utility consumers
 * <p>
 * @author Peter T Mount
 */
public final class Consumers
{

    private static final Logger LOG = Logger.getLogger( Consumers.class.getName() );

    /**
     * Create a consumer if some condition is true, null if false
     * <p>
     * @param <C>
     * @param <T>       type
     * @param condition condition
     * @param s         supplier
     * <p>
     * @return Consumer or null
     */
    public static <C extends Consumer<T>, T> C createIf( boolean condition, Supplier<C> s )
    {
        return condition ? s.get() : null;
    }

    /**
     * Wrap a consumer with one that uses a mapping function to translate from one type to the type accepted by that
     * consumer
     * <p>
     * @param <F> Source type
     * @param <T> Type accepted by the consumer
     * @param c   Consumer
     * @param f   Mapping function
     * <p>
     * @return Consumer that accepts the source type
     */
    public static <F, T> Consumer<F> transform( Consumer<T> c, Function<F, T> f )
    {
        Objects.requireNonNull( c );
        Objects.requireNonNull( f );
        return v -> c.accept( f.apply( v ) );
    }

    /**
     * Wrap a {@link Consumer} so that it only consumes if the supplied {@link Predicate} passes
     * <p>
     * @param <T> Type
     * @param p   Predicate
     * @param t   Consumer when true
     * <p>
     * @return
     */
    public static <T> Consumer<T> ifThen( Predicate<T> p, Consumer<T> t )
    {
        Objects.requireNonNull( p );
        Objects.requireNonNull( t );
        return v ->
        {
            if( p.test( v ) )
            {
                t.accept( v );
            }
        };
    }

    /**
     * Wrap a {@link Consumer} so that it only consumes if the supplied {@link Predicate} fails
     * <p>
     * @param <T> Type
     * @param p   Predicate
     * @param f   Consumer when false
     * <p>
     * @return
     */
    public static <T> Consumer<T> ifNotThen( Predicate<T> p, Consumer<T> f )
    {
        Objects.requireNonNull( p );
        Objects.requireNonNull( f );
        return ifThen( p.negate(), f );
    }

    /**
     * Wrap a pair of {@link Consumer}'s so that only one consumes depending on the result of the supplied
     * {@link Predicate}.
     * <p>
     * @param <T> Type
     * @param p   Predicate
     * @param t   Consumer when true
     * @param f   Consumer when false
     * <p>
     * @return
     */
    public static <T> Consumer<T> ifThenElse( Predicate<T> p, Consumer<T> t, Consumer<T> f )
    {
        Objects.requireNonNull( p );
        Objects.requireNonNull( t );
        Objects.requireNonNull( f );

        return v ->
        {
            if( p.test( v ) )
            {
                t.accept( v );
            }
            else
            {
                f.accept( v );
            }
        };
    }

    public static <T, U> Consumer<T> consume( Function<T, U> f, Consumer<U> c )
    {
        return t -> c.accept( f.apply( t ) );
    }

    /**
     * A consumer that does nothing
     * <p>
     * @param <T> <p>
     * @return
     */
    public static <T> Consumer<T> sink()
    {
        return v ->
        {
        };
    }

    /**
     * Ensures that you don't have a null consumer.
     * <p>
     * @param <T> data type
     * @param c   Consumer
     * <p>
     * @return c or a sink if c is null
     */
    public static <T> Consumer<T> ensureNotNull( Consumer<T> c )
    {
        return c == null ? sink() : c;
    }

    /**
     * Consumer which will log the String passed to it
     * <p>
     * @param logger Logger
     * @param level  Level to log at
     * <p>
     * @return consumer
     */
    public static Consumer<String> log( Logger logger, Level level )
    {
        Objects.requireNonNull( logger );
        Objects.requireNonNull( level );
        return s -> logger.log( level, s );
    }

    /**
     * Consumer which will log the String passed to it
     * <p>
     * @param name  Logger name
     * @param level Level to log at
     * <p>
     * @return consumer
     */
    public static Consumer<String> log( String name, Level level )
    {
        Objects.requireNonNull( name );
        Objects.requireNonNull( level );
        return log( Logger.getLogger( name ), level );
    }

    /**
     * Consumer which will log the String passed to it
     * <p>
     * @param clazz Class who's logger to use
     * @param level Level to log at
     * <p>
     * @return consumer
     */
    public static Consumer<String> log( Class clazz, Level level )
    {
        Objects.requireNonNull( clazz );
        Objects.requireNonNull( level );
        return log( clazz.getName(), level );
    }

    /**
     * Compose two Consumer's.
     * <p>
     * Unlike {@link Consumer#andThen(java.util.function.Consumer)} this will handle nulls, so:
     * <ol>
     * <li>if a is null then this returns b which must not be null</li>
     * <li>if b is null then this returns a which must not be null</li>
     * <li>otherwise as a and b are not null returns {@code a.andThen(b)}</li>
     * </ol>
     * <p>
     * @param <T>
     * @param a   first Consumer
     * @param b   second Consumer
     * <p>
     * @return composed consumer
     * <p>
     * @throws NullPointerException if both consumers are null
     */
    public static <T> Consumer<T> andThen( Consumer<T> a, Consumer<T> b )
    {
        if( a == null )
        {
            return Objects.requireNonNull( b );
        }
        else if( b == null )
        {
            return Objects.requireNonNull( a );
        }
        else
        {
            return a.andThen( b );
        }
    }

    /**
     * Returns a composed {@link Consumer} that performs in sequence each operation passed in {@code consumers}.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param consumers one or more Consumers
     * <p>
     * @return
     */
    public static <T> Consumer<T> andThen( Consumer<T>... consumers )
    {
        if( consumers == null || consumers.length == 0 )
        {
            return sink();
        }

        Consumer<T> c = null;
        for( Consumer<T> consumer : consumers )
        {
            if( consumer != null )
            {
                c = andThen( c, consumer );
            }
        }
        return ensureNotNull( c );
    }

    public static <T> Consumer<T> andThenGuarded( Consumer<T> a, Consumer<T> b )
    {
        return andThen( a, guard( b ) );
    }

    /**
     * Similar to {@link #andThen(java.util.function.Consumer...)} this will return a composed {@link Consumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param consumers one or more Consumers
     * <p>
     * @return
     */
    public static <T> Consumer<T> andThenGuarded( Consumer<T>... consumers )
    {
        return andThenGuarded( LOG, consumers );
    }

    /**
     * Similar to {@link #andThen(java.util.function.Consumer...)} this will return a composed {@link Consumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param log       Logger to log any Throwable's
     * @param consumers one or more Consumers
     * <p>
     * @return
     */
    public static <T> Consumer<T> andThenGuarded( Logger log, Consumer<T>... consumers )
    {
        Objects.requireNonNull( log );
        if( consumers == null || consumers.length == 0 )
        {
            return sink();
        }

        Consumer<T> c = null;
        for( Consumer<T> consumer : consumers )
        {
            if( consumer != null )

            {
                c = andThen( c, guard( log, consumer ) );
            }
        }
        return ensureNotNull( c );
    }

    /**
     * Similar to {@link #andThen(java.util.function.Consumer...)} this will return a composed {@link Consumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param name      Logger to log any Throwable's
     * @param consumers one or more Consumers
     * <p>
     * @return
     */
    public static <T> Consumer<T> andThenGuarded( String name, Consumer<T>... consumers )
    {
        Objects.requireNonNull( name );
        return andThenGuarded( Logger.getLogger( name ), consumers );
    }

    /**
     * Similar to {@link #andThen(java.util.function.Consumer...)} this will return a composed {@link Consumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param clazz     Logger to log any Throwable's
     * @param consumers one or more Consumers
     * <p>
     * @return
     */
    public static <T> Consumer<T> andThenGuarded( Class clazz, Consumer<T>... consumers )
    {
        Objects.requireNonNull( clazz );
        return andThenGuarded( clazz.getName(), consumers );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T> object type
     * @param c   Consumer to guard
     * <p>
     * @return new Consumer
     */
    public static <T> Consumer<T> guard( Consumer<T> c )
    {
        return guard( LOG, Objects.requireNonNull( c ) );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T> object type
     * @param log Logger to log any Throwable's
     * @param c   Consumer to guard
     * <p>
     * @return new Consumer
     */
    public static <T> Consumer<T> guard( Logger log, Consumer<T> c )
    {
        Objects.requireNonNull( log );
        Objects.requireNonNull( c );
        return v ->
        {
            try
            {
                c.accept( v );
            }
            catch( Throwable t )
            {
                log.log( Level.SEVERE, "Exception thrown by consumer " + c, t );
            }
        };
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T>  object type
     * @param name Logger to log any Throwable's
     * @param c    Consumer to guard
     * <p>
     * @return new Consumer
     */
    public static <T> Consumer<T> guard( String name, Consumer<T> c )
    {
        Objects.requireNonNull( name );
        return guard( Logger.getLogger( name ), c );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T>   object type
     * @param clazz Logger to log any Throwable's
     * @param c     Consumer to guard
     * <p>
     * @return new Consumer
     */
    public static <T> Consumer<T> guard( Class clazz, Consumer<T> c )
    {
        Objects.requireNonNull( clazz );
        return guard( clazz.getName(), c );
    }

    /**
     * Wraps a {@link Consumer} so that it's run within an {@link Executor}.
     * <p>
     * @param <T> Type of consumed object
     * @param e   Executor to use
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     */
    public static <T> Consumer<T> fork( Executor e, Consumer<T> c )
    {
        return t -> e.execute( () -> c.accept( t ) );
    }

    /**
     * Wraps a {@link Consumer} so that it's run within the main cached thread pool.
     * <p>
     * Use this for normal use. For intensive work use {@link #forkWorker(java.util.function.Consumer)} instead.
     * <p>
     * @param <T> Type of consumed object
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     * <p>
     * @see Executors#newCachedThreadPool()
     */
    public static <T> Consumer<T> fork( Consumer<T> c )
    {
        return Consumers.fork( DaemonThreadFactory.INSTANCE.getCachedExecutor(), c );
    }

    /**
     * Wraps a {@link Consumer} so that it's run within the main work-stealing thread pool which is limited to the
     * number of processors on the system.
     * <p>
     * Use this for intensive work as it will keep CPU load to reasonable amounts as it bases it's thread pool size to
     * the number of processors on the system.
     * <p>
     * @param <T> Type of consumed object
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     * <p>
     * @see Executors#newCachedThreadPool()
     */
    public static <T> Consumer<T> forkWorker( Consumer<T> c )
    {
        return Consumers.fork( DaemonThreadFactory.INSTANCE.getWorkExecutor(), c );
    }

}
