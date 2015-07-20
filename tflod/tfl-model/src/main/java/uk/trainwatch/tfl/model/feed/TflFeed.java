/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.feed;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author peter
 */
public class TflFeed
{

    private final Supplier<String> supplier;
    private final Consumer<String> consumer;
    private final long interval;
    private final TimeUnit timeUnit;

    private Timer timer;

    public TflFeed( Supplier<String> supplier, Consumer<String> consumer, long interval, TimeUnit timeUnit )
    {
        this.supplier = supplier;
        this.consumer = consumer;
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    public void start()
    {
        if( timer == null ) {
            timer = new Timer( true );
            timer.scheduleAtFixedRate( new Task(), 5000L, timeUnit.MILLISECONDS.convert( interval, timeUnit ) );
        }
    }

    public void stop()
    {
        if( timer != null ) {
            timer.cancel();
            timer = null;
        }
    }

    private class Task
            extends TimerTask
    {

        @Override
        public void run()
        {
            String s = supplier.get();
            if( s != null && !s.isEmpty() ) {
                consumer.accept( s );
            }
        }

    }
}
