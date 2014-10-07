/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Peter T Mount
 */
public enum DaemonThreadFactory
        implements ThreadFactory
{

    INSTANCE;

    private final ThreadGroup group = new ThreadGroup( "ODC Group" );
    private final AtomicInteger seq = new AtomicInteger();

    @Override
    public Thread newThread( Runnable r )
    {
        Thread t = new Thread( group, r, "DaemonThread-" + seq.incrementAndGet() );
        t.setDaemon( true );
        return t;
    }

}
