/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.counter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple consumer which will keep a count of the number of times it's called.
 * <p>
 * It's used for monitoring the amount of traffic within a consumer chain
 * <p>
 * @author Peter T Mount
 * @param <T>
 */
public final class CounterConsumer<T>
        implements Consumer<T>
{

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void accept( T t )
    {
        counter.incrementAndGet();
    }

    public final int get()
    {
        return counter.get();
    }

    public final void set( int newValue )
    {
        counter.set( newValue );
    }

    public void reset()
    {
        counter.set( 0 );
    }
    
    public Consumer<T> logEvery(int range,Logger log)
    {
        return o ->
            {
                if( (counter.get() % range) == 0 )
                {
                    log.log( Level.INFO, () -> "Imported " + counter.get() );
                }
            };
    }
}
