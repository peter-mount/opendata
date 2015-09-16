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
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import uk.trainwatch.util.Consumers;

/**
 *
 * @author peter
 * @param <T>
 */
@FunctionalInterface
public interface IOConsumer<T>
{

    void accept( T v )
            throws IOException;

    /**
     * Convert this consumer into a standard {@link Consumer}.
     * <p>
     * When an {@link IOException} is thrown then an {@link UncheckedIOException} will be thrown instead.
     * <p>
     * @return consumer
     */
    default Consumer<T> guard()
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
    default IOConsumer<T> andThen( IOConsumer<? super T> after )
    {
        Objects.requireNonNull( after );
        return ( T t ) -> {
            accept( t );
            after.accept( t );
        };
    }

    static <T> IOConsumer<T> andThen( IOConsumer<T> a, IOConsumer<T> b )
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
    static <T> Consumer<T> guard( IOConsumer<T> c )
    {
        return c == null ? Consumers.sink() : v -> {
            try {
                c.accept( v );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        };
    }

    static <T> void forEach( Collection<T> col, IOConsumer<T> c )
            throws IOException
    {
        if( col != null && !col.isEmpty() ) {
            try {
                col.forEach( c.guard() );
            }
            catch( UncheckedIOException ex ) {
                throw ex.getCause();
            }
        }
    }
}
