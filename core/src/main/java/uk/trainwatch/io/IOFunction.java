/*
 * Copyright 2015 peter.
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
package uk.trainwatch.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author peter
 * @param <U>
 * @param <T>
 */
@FunctionalInterface
public interface IOFunction<T, R>
{

    R apply( T v )
            throws IOException;

    /**
     * Convert this consumer into a standard {@link Consumer}.
     * <p>
     * When an {@link IOException} is thrown then an {@link UncheckedIOException} will be thrown instead.
     * <p>
     * @return consumer
     */
    default Function<T, R> guard()
    {
        return guard( this );
    }

    /**
     * Convert an {@link IOConsumer} into a standard {@link Consumer}.
     * <p>
     * When an {@link IOException} is thrown then an {@link UncheckedIOException} will be thrown instead.
     * <p>
     * @param c IOConsumer to guard
     * <p>
     * @return consumer
     */
    static <V, T> Function<V, T> guard( IOFunction<V, T> c )
    {
        return v -> {
            try {
                return c.apply( v );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        };
    }

    default <V> IOFunction<V, R> compose( IOFunction<? super V, ? extends T> before )
    {
        Objects.requireNonNull( before );
        return ( V v ) -> apply( before.apply( v ) );
    }

    default <V> IOFunction<T, V> andThen( IOFunction<? super R, ? extends V> after )
    {
        Objects.requireNonNull( after );
        return ( T t ) -> after.apply( apply( t ) );
    }

}
