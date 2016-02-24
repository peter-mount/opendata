/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Peter T Mount
 */
public class Streams
{

    /**
     * Ensures we have a Stream
     * <p>
     * @param <T> Type
     * @param s   Stream
     * <p>
     * @return s or {@link Stream#empty()} if s is null
     */
    public static <T> Stream<T> stream( Stream<T> s )
    {
        if( s == null ) {
            return Stream.empty();
        }
        return s;
    }

    /**
     * Returns a Stream for a collection
     * <p>
     * @param <T> Type
     * @param col collection
     * <p>
     * @return Stream or {@link Stream#empty()} if col is null or empty
     */
    public static <T> Stream<T> stream( Collection<T> col )
    {
        if( col == null || col.isEmpty() ) {
            return Stream.empty();
        }
        return col.stream();
    }

    /**
     * Returns a stream containing e or an empty stream if that value is null.
     * <p>
     * Note: This is equivalent to Stream.ofNullable() present in Java 9.
     * <p>
     * @param <T>
     * @param e
     *            <p>
     * @return
     */
    public static <T> Stream<T> ofNullable( T e )
    {
        return e == null ? Stream.empty() : Stream.of( e );
    }

    /**
     * Concatenate two streams.
     * <p>
     * Unlike {@link Stream#concat(java.util.stream.Stream, java.util.stream.Stream)} if s1 is null then we just return
     * s2. Useful when concatenating multiple streams.
     * <p>
     * @param <T> Type
     * @param s1  Stream 1, may be null
     * @param s2  Stream 2, must not be null
     * <p>
     * @return new Stream or s2 if s1 was null
     */
    public static <T> Stream<T> concat( Stream<T> s1, Stream<? extends T> s2 )
    {
        Objects.requireNonNull( s2 );
        return s1 == null ? (Stream<T>) s2 : Stream.concat( s1, s2 );
    }

    /**
     * Concatenate multiple streams. Unlike {@link Stream#concat(java.util.stream.Stream, java.util.stream.Stream)}
     * which only accepts two streams, this can concatenate as many streams as required.
     * <p>
     * @param <T>
     * @param streams <p>
     * @return
     */
    public static <T> Stream<T> concat( Stream<? extends T>... streams )
    {
        Objects.requireNonNull( streams );
        Stream<T> s = null;
        for( Stream<? extends T> s2: streams ) {
            s = concat( s, s2 );
        }
        return stream( s );
    }

    /**
     * Create a Stream across multiple collections.
     * <p>
     * If a collection specified is null or empty then it is ignored.
     * <p>
     * @param <T>  Type of Stream
     * @param cols One or more Collection's who's streams to concat
     * <p>
     * @return Stream
     */
    public static <T> Stream<T> concat( Collection<? extends T>... cols )
    {
        Objects.requireNonNull( cols );

        Stream<T> s = null;
        for( Collection<? extends T> col: cols ) {
            if( col != null && !col.isEmpty() ) {
                s = concat( s, col.stream() );
            }
        }
        return stream( s );
    }

    /**
     * Creates a {@link Consumer} which will pass an object on to a {@link Stream}.
     * <p>
     * The factory consumer will handle the configuration and operation of the stream. This is called once from a
     * background thread, the passed stream being fed by the final consumer.
     * <p>
     * The returned Consumer will then offer the object to the stream by means of a backing queue.
     * <p>
     * Note: This stream is infinite so you cannot do most operations. Ones that are safe are:
     * <p>
     * {@link Stream#filter(java.util.function.Predicate)},
     * {@link Stream#forEach(java.util.function.Consumer)},
     * {@link Stream#map(java.util.function.Function)},
     * {@link Stream#mapToDouble(java.util.function.ToDoubleFunction)},
     * {@link Stream#mapToInt(java.util.function.ToIntFunction)},
     * {@link Stream#mapToLong(java.util.function.ToLongFunction)},
     * {@link Stream#peek(java.util.function.Consumer)}.
     * <p>
     * If you want a proper stream, then in the {@link Stream#forEach(java.util.function.Consumer)} consumer create a
     * new stream based on the data within that individual "message" and then use
     * {@link Stream#flatMap(java.util.function.Function)}.
     * <p>
     * Once you have done that, this stream then returns to get the next element and the stream you are working on is
     * that of the flattened stream so will be safe to use any method.
     * <p>
     * @param <T>     Type of the stream
     * @param factory Consumer that will configure and run the stream
     * <p>
     * @return Consumer
     */
    public static <T> Consumer<T> consumerStream( Consumer<Stream<T>> factory )
    {
        return consumerStream( new BlockingSupplierConsumer<>(), factory );
    }

    /**
     * Creates a {@link Consumer} which will pass an object on to a {@link Stream}.
     * <p>
     * The factory consumer will handle the configuration and operation of the stream. This is called once from a
     * background thread, the passed stream being fed by the final consumer.
     * <p>
     * The returned Consumer will then offer the object to the stream by means of a backing queue.
     * <p>
     * Note: This stream is infinite so you cannot do most operations. Ones that are safe are:
     * <p>
     * {@link Stream#filter(java.util.function.Predicate)},
     * {@link Stream#forEach(java.util.function.Consumer)},
     * {@link Stream#map(java.util.function.Function)},
     * {@link Stream#mapToDouble(java.util.function.ToDoubleFunction)},
     * {@link Stream#mapToInt(java.util.function.ToIntFunction)},
     * {@link Stream#mapToLong(java.util.function.ToLongFunction)},
     * {@link Stream#peek(java.util.function.Consumer)}.
     * <p>
     * If you want a proper stream, then in the {@link Stream#forEach(java.util.function.Consumer)} consumer create a
     * new stream based on the data within that individual "message" and then use
     * {@link Stream#flatMap(java.util.function.Function)}.
     * <p>
     * Once you have done that, this stream then returns to get the next element and the stream you are working on is
     * that of the flattened stream so will be safe to use any method.
     * <p>
     * @param <T>     Type of the stream
     * @param maxSize The maximum size of the backing queue
     * @param factory Consumer that will configure and run the stream
     * <p>
     * @return Consumer
     */
    public static <T> Consumer<T> consumerStream( int maxSize, Consumer<Stream<T>> factory )
    {
        return consumerStream( new BlockingSupplierConsumer<>( maxSize ), factory );
    }

    private static <T> Consumer<T> consumerStream( final BlockingSupplierConsumer<T> supplier, Consumer<Stream<T>> factory )
    {
        supplierStream( supplier, factory );

        // wrap the supplier so no one can actually get at the underlying BlockingSupplier
        return t -> supplier.accept( t );
    }

    /**
     * Creates a stream in a background thread which will be passed entries from the given {@link Supplier}.
     * <p>
     * The factory consumer will handle the configuration and operation of the stream. This is called once from a
     * background thread, the passed stream being fed by the final consumer.
     * <p>
     * The returned Consumer will then offer the object to the stream by means of a backing queue.
     * <p>
     * Note: This stream is infinite so you cannot do most operations. Ones that are safe are:
     * <p>
     * {@link Stream#filter(java.util.function.Predicate)},
     * {@link Stream#forEach(java.util.function.Consumer)},
     * {@link Stream#map(java.util.function.Function)},
     * {@link Stream#mapToDouble(java.util.function.ToDoubleFunction)},
     * {@link Stream#mapToInt(java.util.function.ToIntFunction)},
     * {@link Stream#mapToLong(java.util.function.ToLongFunction)},
     * {@link Stream#peek(java.util.function.Consumer)}.
     * <p>
     * If you want a proper stream, then in the {@link Stream#forEach(java.util.function.Consumer)} consumer create a
     * new stream based on the data within that individual "message" and then use
     * {@link Stream#flatMap(java.util.function.Function)}.
     * <p>
     * Once you have done that, this stream then returns to get the next element and the stream you are working on is
     * that of the flattened stream so will be safe to use any method.
     * <p>
     * @param <T>      Type of the stream
     * @param supplier Supplier that will provide
     * @param factory  Consumer that will configure and run the stream
     */
    public static <T> void supplierStream( BlockingSupplier<T> supplier, Consumer<Stream<T>> factory )
    {
        DaemonThreadFactory.INSTANCE.newThread(
                () -> {
                    try( Stream<T> s = Stream.generate( supplier ) ) {
                        factory.accept( s );
                    }
                    finally {
                        supplier.setInvalid();
                    }
                } ).
                start();
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T> Type
     * @param c   Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( Consumer<T> c )
    {
        return fork( 1, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T>     Type
     * @param timeout Time out
     * @param unit    TimeUnit of timeout
     * @param c       Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( long timeout, TimeUnit unit, Consumer<T> c )
    {
        return fork( 1, false, timeout, unit, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T>  Type
     * @param size Queue size
     * @param c    Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( int size, Consumer<T> c )
    {
        return fork( size, false, 10, TimeUnit.SECONDS, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T>     Type
     * @param size    Queue size
     * @param timeout Time out
     * @param unit    TimeUnit of timeout
     * @param c       Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( int size, long timeout, TimeUnit unit, Consumer<T> c )
    {
        return fork( size, false, timeout, unit, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T>      Type
     * @param size     Queue size
     * @param parallel true to process in parallel, false for sequential
     * @param c        Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( int size, boolean parallel, Consumer<T> c )
    {
        return fork( size, parallel, 10, TimeUnit.SECONDS, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread
     * <p>
     * @param <T>      Type
     * @param parallel true to process in parallel, false for sequential
     * @param c        Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( boolean parallel, Consumer<T> c )
    {
        return fork( 1, parallel, 10, TimeUnit.SECONDS, c );
    }

    /**
     * Creates a consumer which will hand off work to another consumer on a background thread.
     * <p>
     * The resulting consumer will block for timeout/unit amount of time if the underlying queue is full - i.e. when the wrapped consumer is busy.
     * <p>
     * If a timeout occurs then an {@link IllegalStateException} is thrown to the caller.
     * <p>
     * @param <T>      Type
     * @param size     Queue size. When this queue is full then the feeding consumer will block
     * @param parallel true to process in parallel, false for sequential
     * @param timeout  Time out
     * @param unit     TimeUnit of timeout
     * @param c        Consumer
     * <p>
     * @return new consumer
     */
    public static <T> Consumer<T> fork( int size, boolean parallel, long timeout, TimeUnit unit, Consumer<T> c )
    {
        // Guard otherwise the stream fails and the origin will block until queue fills & times out
        Consumer<T> consumer = Consumers.guard( c );

        BlockingQueue<T> queue = size > 1 ? new ArrayBlockingQueue<>( size ) : new SynchronousQueue<>();

        supplierStream( queue::poll, parallel ? s -> s.parallel().forEach( consumer ) : s -> s.forEach( consumer ) );

        return t -> {
            try {
                if( !queue.offer( t, timeout, unit ) ) {
                    throw new IllegalStateException( "Unable to offer entry after " + timeout + " " + unit );
                }
            }
            catch( InterruptedException ex ) {
                throw new IllegalStateException( "Offer interrupted", ex );
            }
        };
    }

    public static <E> Stream<E> of( Enumeration<E> en )
    {
        return StreamSupport.stream( Spliterators.spliteratorUnknownSize( new Iterator<E>()
        {
            @Override
            public boolean hasNext()
            {
                return en.hasMoreElements();
            }

            @Override
            public E next()
            {
                return en.nextElement();
            }
        }, 0 ),
                                     false );
    }
}
