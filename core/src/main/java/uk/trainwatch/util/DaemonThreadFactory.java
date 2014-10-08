/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link ThreadFactory} that creates daemon threads. It also maintains our two thread Executors.
 * <p>
 * @author Peter T Mount
 */
public enum DaemonThreadFactory
        implements ThreadFactory
{

    INSTANCE;

    private final ThreadGroup group = new ThreadGroup( "Open Data ThreadGroup" );
    /**
     * Our Thread ID counter
     */
    private final AtomicInteger seq = new AtomicInteger();
    /**
     * Old school cached Thread Pool, use for normal tasks
     */
    private final Executor cachedExecutor = Executors.newCachedThreadPool( this );
    /**
     * Work stealing pool which will use all available processors
     */
    private final Executor workExecutor = Executors.newWorkStealingPool();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 4, this );

    /**
     * Returns the main Executor.
     * <p>
     * @return
     */
    public Executor getCachedExecutor()
    {
        return cachedExecutor;
    }

    /**
     * Returns the Work Executor. Unlike {@link #getCachedExecutor()} this one maintains enough threads for the number
     * of available processors on the underlying system.
     * <p>
     * @return
     */
    public Executor getWorkExecutor()
    {
        return workExecutor;
    }

    public ScheduledFuture<?> schedule( Runnable command, long delay, TimeUnit unit )
    {
        return scheduler.schedule( command, delay, unit );
    }

    public <V> ScheduledFuture<V> schedule( Callable<V> callable, long delay, TimeUnit unit )
    {
        return scheduler.schedule( callable, delay, unit );
    }

    public ScheduledFuture<?> scheduleAtFixedRate( Runnable command, long initialDelay, long period, TimeUnit unit )
    {
        return scheduler.scheduleAtFixedRate( command, initialDelay, period, unit );
    }

    public ScheduledFuture<?> scheduleWithFixedDelay( Runnable command, long initialDelay, long delay, TimeUnit unit )
    {
        return scheduler.scheduleWithFixedDelay( command, initialDelay, delay, unit );
    }

    @Override
    public Thread newThread( Runnable r )
    {
        Thread t = new Thread( group, r, "Open-Data-DaemonThread-" + seq.incrementAndGet() );
        t.setDaemon( true );
        return t;
    }

}
