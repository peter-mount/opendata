/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.svg;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 *
 * @author peter
 */
public class TimeRemainingMonitor<T>
        implements Consumer<T>
{

    private final AtomicInteger counter = new AtomicInteger();
    private final ScheduledFuture<?> scheduledFuture;

    @SuppressWarnings("NonConstantLogger")

    private final AtomicLong count = new AtomicLong();
    private LocalDateTime start;
    private LocalDateTime latest;

    public TimeRemainingMonitor( Logger log, String label, long total )
    {
        scheduledFuture = DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( () -> {
            Duration d = getDuration();
            long millis = d.toMillis();
            if( millis > 0L ) {
                log.log( Level.INFO, () -> label + ' ' + d.plusMillis( millis * total / count.get() ) );
            }
        }, 1L, 1L, TimeUnit.MINUTES );
    }

    public synchronized Duration getDuration()
    {
        return start == null ? Duration.ZERO : Duration.between( start, latest );
    }

    @Override
    public synchronized void accept( T t )
    {
        counter.incrementAndGet();
        LocalDateTime now = LocalDateTime.now();
        if( start == null ) {
            start = now;
        }
        latest = now;
    }

}
