/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link Consumer} which will pass the consumed value to a {@link Supplier} via an internal queue.
 * <p>
 * It's designed to allow a {@link java.util.stream.Stream} to run in one thread but be fed data from another.
 * <p>
 * @author Peter T Mount
 * @param <T> Type of object to queue
 * <p>
 * @see Streams#consumerStream(java.util.function.Consumer)
 * @see Streams#consumerStream(int, java.util.function.Consumer)
 */
public class BlockingSupplierConsumer<T>
        implements BlockingSupplier<T>,
                   Consumer<T>
{

    private static final Logger LOG = Logger.getLogger(BlockingSupplierConsumer.class.getName() );
    private final BlockingQueue<T> queue;
    private boolean valid = true;

    /**
     * Construct an instance with an unlimited capacity in the queue
     */
    public BlockingSupplierConsumer()
    {
        this.queue = new LinkedBlockingQueue();
    }

    /**
     * Construct an instance with a limited capacity
     * <p>
     * @param maxSize
     */
    public BlockingSupplierConsumer( int maxSize )
    {
        if( maxSize < 1 )
        {
            throw new IllegalArgumentException( "Maximum size must be >0" );
        }
        this.queue = new LinkedBlockingQueue( maxSize );
    }

    public synchronized boolean isValid()
    {
        return valid;
    }

    @Override
    public synchronized void setInvalid()
    {
        this.valid = false;
    }

    private void assertValid()
    {
        if( !isValid() )
        {
            throw new IllegalStateException( "Backing stream has shutdown" );
        }
    }

    @Override
    public void accept( final T t )
    {
        assertValid();

        try
        {
            queue.offer( t, 1, TimeUnit.SECONDS );
        }
        catch( InterruptedException ex )
        {
            LOG.log( Level.SEVERE, "Failed to submit " + t, ex );
        }
    }

    @Override
    public T get()
    {
        assertValid();

        T v = null;
        do
        {
            try
            {
                v = queue.poll( 10, TimeUnit.SECONDS );
            }
            catch( InterruptedException ex )
            {
                Thread.currentThread().
                        interrupt();
            }
        }
        while( v == null );
        return v;
    }

}
