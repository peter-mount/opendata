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
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A suite of utility consumers
 * <p>
 * @author Peter T Mount
 */
public final class BiConsumers
{

    private static final Logger LOG = Logger.getLogger( BiConsumers.class.getName() );

    /**
     * A consumer that does nothing
     * <p>
     * @param <T> <p>
     * @param <U> object type
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> sink()
    {
        return ( a, b ) -> {
        };
    }

    /**
     * Ensures that you don't have a null consumer.
     * <p>
     * @param <T> data type
     * @param <U> object type
     * @param c   Consumer
     * <p>
     * @return c or a sink if c is null
     */
    public static <T, U> BiConsumer<T, U> ensureNotNull( BiConsumer<T, U> c )
    {
        return c == null ? sink() : c;
    }

    /**
     * Compose two BiConsumer's.
     * <p>
     * Unlike {@link BiConsumer#andThen(java.util.function.BiConsumer)} this will handle nulls, so:
     * <ol>
     * <li>if a is null then this returns b which must not be null</li>
     * <li>if b is null then this returns a which must not be null</li>
     * <li>otherwise as a and b are not null returns {@code a.andThen(b)}</li>
     * </ol>
     * <p>
     * @param <T>
     * @param <U> object type
     * @param a   first BiConsumer
     * @param b   second BiConsumer
     * <p>
     * @return composed consumer
     * <p>
     * @throws NullPointerException if both consumers are null
     */
    public static <T, U> BiConsumer<T, U> andThen( BiConsumer<T, U> a, BiConsumer<T, U> b )
    {
        return a == null ? Objects.requireNonNull( b ) : b == null ? a : a.andThen( b );
    }

    /**
     * Returns a composed {@link BiConsumer} that performs in sequence each operation passed in {@code consumers}.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param <U>       object type
     * @param consumers one or more BiConsumers
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> andThen( BiConsumer<T, U>... consumers )
    {
        if( consumers == null || consumers.length == 0 ) {
            return sink();
        }

        BiConsumer<T, U> c = null;
        for( BiConsumer<T, U> consumer: consumers ) {
            if( consumer != null ) {
                c = andThen( c, consumer );
            }
        }
        return ensureNotNull( c );
    }

    public static <T, U> BiConsumer<T, U> andThenGuarded( BiConsumer<T, U> a, BiConsumer<T, U> b )
    {
        return andThen( a, guard( b ) );
    }

    /**
     * Similar to {@link #andThen(java.util.function.BiConsumer...)} this will return a composed {@link BiConsumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param <U>       object type
     * @param consumers one or more BiConsumers
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> andThenGuarded( BiConsumer<T, U>... consumers )
    {
        return andThenGuarded( LOG, consumers );
    }

    /**
     * Similar to {@link #andThen(java.util.function.BiConsumer...)} this will return a composed {@link BiConsumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param <U>       object type
     * @param log       Logger to log any Throwable's
     * @param consumers one or more BiConsumers
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> andThenGuarded( Logger log, BiConsumer<T, U>... consumers )
    {
        Objects.requireNonNull( log );
        if( consumers == null || consumers.length == 0 ) {
            return sink();
        }

        BiConsumer<T, U> c = null;
        for( BiConsumer<T, U> consumer: consumers ) {
            if( consumer != null ) {
                c = andThen( c, guard( log, consumer ) );
            }
        }
        return ensureNotNull( c );
    }

    /**
     * Similar to {@link #andThen(java.util.function.BiConsumer...)} this will return a composed {@link BiConsumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param <U>       object type
     * @param name      Logger to log any Throwable's
     * @param consumers one or more BiConsumers
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> andThenGuarded( String name, BiConsumer<T, U>... consumers )
    {
        Objects.requireNonNull( name );
        return andThenGuarded( Logger.getLogger( name ), consumers );
    }

    /**
     * Similar to {@link #andThen(java.util.function.BiConsumer...)} this will return a composed {@link BiConsumer} that
     * performs in sequence each operation passed in {@code consumers}. Unlike the other method each consumer will be
     * guarded, so that if one fails it does not cause processing to stop and subsequent consumers will still be
     * executed.
     * <p>
     * Note: If no consumers are passed then this will return a sink consumer. If just 1 consumer is passed then we will
     * return just that consumer.
     * <p>
     * @param <T>       Type
     * @param <U>       object type
     * @param clazz     Logger to log any Throwable's
     * @param consumers one or more BiConsumers
     * <p>
     * @return
     */
    public static <T, U> BiConsumer<T, U> andThenGuarded( Class clazz, BiConsumer<T, U>... consumers )
    {
        Objects.requireNonNull( clazz );
        return andThenGuarded( clazz.getName(), consumers );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T> object type
     * @param <U> object type
     * @param c   BiConsumer to guard
     * <p>
     * @return new BiConsumer
     */
    public static <T, U> BiConsumer<T, U> guard( BiConsumer<T, U> c )
    {
        return guard( LOG, Objects.requireNonNull( c ) );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T> object type
     * @param <U> object type
     * @param log Logger to log any Throwable's
     * @param c   BiConsumer to guard
     * <p>
     * @return new BiConsumer
     */
    public static <T, U> BiConsumer<T, U> guard( Logger log, BiConsumer<T, U> c )
    {
        Objects.requireNonNull( log );
        Objects.requireNonNull( c );
        return ( a, b ) -> {
            try {
                c.accept( a, b );
            }
            catch( Throwable t ) {
                log.log( Level.SEVERE, "Exception thrown by consumer " + c, t );
            }
        };
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T>  object type
     * @param <U>  object type
     * @param name Logger to log any Throwable's
     * @param c    BiConsumer to guard
     * <p>
     * @return new BiConsumer
     */
    public static <T, U> BiConsumer<T, U> guard( String name, BiConsumer<T, U> c )
    {
        Objects.requireNonNull( name );
        return guard( Logger.getLogger( name ), c );
    }

    /**
     * Wraps a consumer with a guard so that any {@link Throwable} thrown is caught and logged but not passed up to the
     * caller.
     * <p>
     * @param <T>   object type
     * @param <U>   object type
     * @param clazz Logger to log any Throwable's
     * @param c     BiConsumer to guard
     * <p>
     * @return new BiConsumer
     */
    public static <T, U> BiConsumer<T, U> guard( Class clazz, BiConsumer<T, U> c )
    {
        Objects.requireNonNull( clazz );
        return guard( clazz.getName(), c );
    }

}
