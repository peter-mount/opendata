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

        Consumer<T> c = consumers[0];
        for( int i = 1; i < consumers.length; i++ )
        {
            c = c.andThen( consumers[i] );
        }
        return c;
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
        return guard( LOG, c );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T> object type
     * @param log Logger
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
     * Wraps a {@link Consumer} so that it's run within an {@link Executor}.
     * <p>
     * @param <T> Type of consumed object
     * @param e   Executor to use
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     */
    public static <T> Consumer<T> threadedConsumer( Executor e, Consumer<T> c )
    {
        return t -> e.execute( () -> c.accept( t ) );
    }

    /**
     * Wraps a {@link Consumer} so that it's run within an cached thread pool. This pool is unique to this single
     * consumer.
     * <p>
     * @param <T> Type of consumed object
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     * <p>
     * @see Executors#newCachedThreadPool()
     */
    public static <T> Consumer<T> cachedThreadPoolConsumer( Consumer<T> c )
    {
        return threadedConsumer( Executors.newCachedThreadPool( DaemonThreadFactory.INSTANCE ), c );
    }

    /**
     * Wraps a {@link Consumer} so that it's run within a a single thread.
     * <p>
     * @param <T> Type of consumed object
     * @param e   Executor to use
     * @param c   Consumer to wrap
     * <p>
     * @return Consumer
     * <p>
     * @see Executors#newSingleThreadExecutor()
     */
    public static <T> Consumer<T> singleThreadConsumer( Consumer<T> c )
    {
        return threadedConsumer( Executors.newSingleThreadExecutor( DaemonThreadFactory.INSTANCE ), c );
    }
}
