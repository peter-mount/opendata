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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import uk.trainwatch.util.BiConsumers;

/**
 *
 * @author peter
 * @param <U>
 * @param <V>
 */
@FunctionalInterface
public interface IOBiConsumer<U, V>
{

    void accept( U u, V v )
            throws IOException;

    /**
     * Convert this consumer into a standard {@link Consumer}.
     * <p>
     * When an {@link IOException} is thrown then an {@link UncheckedIOException} will be thrown instead.
     * <p>
     * @return consumer
     */
    default BiConsumer<U, V> guard()
    {
        return guard( this );
    }

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * <p>
     * @return a composed {@code Consumer} that performs in sequence this
     *         operation followed by the {@code after} operation
     * <p>
     * @throws NullPointerException if {@code after} is null
     */
    default IOBiConsumer<U, V> andThen( IOBiConsumer<? super U, ? super V> after )
    {
        Objects.requireNonNull( after );
        return ( s, t ) -> {
            accept( s, t );
            after.accept( s, t );
        };
    }

    static <U, V> IOBiConsumer<U, V> andThen( IOBiConsumer<U, V> a, IOBiConsumer<U, V> b )
    {
        return a == null ? Objects.requireNonNull( b ) : b == null ? a : a.andThen( b );
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
    static <U, V> BiConsumer<U, V> guard( IOBiConsumer<U, V> c )
    {
        return c == null ? BiConsumers.sink() : ( u, v ) -> {
            try {
                c.accept( u, v );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        };
    }
}
