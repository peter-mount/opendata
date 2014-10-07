/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
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

    @Override
    public Thread newThread( Runnable r )
    {
        Thread t = new Thread( group, r, "Open-Data-DaemonThread-" + seq.incrementAndGet() );
        t.setDaemon( true );
        return t;
    }

}
