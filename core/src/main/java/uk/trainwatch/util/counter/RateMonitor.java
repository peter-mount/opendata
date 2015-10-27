/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.counter;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import uk.trainwatch.util.CDIUtils;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 * A Consumer which counts the number of times it's called.
 * <p>
 * Behind the scenes this also logs the rate once per minute and then resets.
 * <p>
 * Normal use you need to create a single instance of RateMonitor and then from within a stream by using
 * {@code peek( rateMonitor ).}
 * <p>
 * An alternate use when being used with another consumer is: {@code something.forEach( rateMonitor.andThen( consumer ) )} where
 * consumer is another consumer or lambda expression.
 * <p>
 * Do not place the RateMonitor.log() call within a stream. Doing that will cause a new instance to be created for every time
 * you use the stream.
 * <p>
 * @param <T> <p>
 * @author Peter T Mount
 */
public class RateMonitor<T>
        implements Consumer<T>
{

    private final AtomicInteger counter = new AtomicInteger();
    private final ScheduledFuture<?> scheduledFuture;

    @SuppressWarnings("NonConstantLogger")
    private final Logger log;

    @Inject
    private RateStatistics rateStatistics;

    private final String label;
    private int lastCount;
    private long total;

    public static <T> RateMonitor<T> log( String label )
    {
        return new RateMonitor<>( null, Level.INFO, label );
    }

    public static <T> RateMonitor<T> log( Logger log, String label )
    {
        return new RateMonitor<>( log, Level.INFO, label );
    }

    public static <T> RateMonitor<T> log( Level level, String label )
    {
        return new RateMonitor<>( null, level, label );
    }

    public static <T> RateMonitor<T> log( Logger log, Level level, String label )
    {
        return new RateMonitor<>( log, level, label );
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public RateMonitor( Logger log, Level level, String label )
    {
        this.log = log == null ? Logger.getLogger( RateMonitor.class.getName() ) : log;
        this.label = label;
        scheduledFuture = DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( () -> {
            // Read & reset the count. Don't put it in the logger as we use a supplier & won't be invoked if the logger
            // isn't showing the required level
            lastCount = counter.getAndSet( 0 );
            total += lastCount;

            // Reduce logspam by only logging when we've done something
            if( lastCount > 0 ) {
                this.log.log( level, () -> label + ' ' + lastCount );
            }

            rateStatistics.getConsumer( label ).accept( lastCount );
        }, 1L, 1L, TimeUnit.MINUTES );

        CDIUtils.inject( this );
    }

    @Override
    public String toString()
    {
        return label + ' ' + lastCount + "/" + (total + counter.get());
    }

    public static <T> RateMonitor<T> log( String log, String label )
    {
        return log( log, Level.INFO, label );
    }

    public static <T> RateMonitor<T> log( String log, Level level, String label )
    {
        return log( Logger.getLogger( Objects.requireNonNull( log ) ), level, label );
    }

    public static <T> RateMonitor<T> log( Class log, String label )
    {
        return log( log, Level.INFO, label );
    }

    public static <T> RateMonitor<T> log( Class log, Level level, String label )
    {
        return log( Objects.requireNonNull( log.getName() ), level, label );
    }

    @Override
    public void accept( T t )
    {
        counter.incrementAndGet();
    }

    /**
     * The current snapshot of the counter's value.
     * <p>
     * @return
     */
    public final int get()
    {
        return counter.get();
    }

    public boolean cancel( boolean mayInterruptIfRunning )
    {
        return scheduledFuture.cancel( mayInterruptIfRunning );
    }

    public boolean isCancelled()
    {
        return scheduledFuture.isCancelled();
    }

    public boolean isDone()
    {
        return scheduledFuture.isDone();
    }

    public final void reset()
    {
        counter.set( 0 );
        lastCount = 0;
        total = 0L;
    }

}
