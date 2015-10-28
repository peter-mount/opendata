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
package uk.trainwatch.util.counter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
@WebListener
public class RateStatistics
        implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger( RateStatistics.class.getName() );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    private ScheduledFuture<?> scheduledFuture;

    private final Map<String, Stat> stats = new ConcurrentHashMap<>();
    private String hostname;
    private String title;

    private String getHostName()
    {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch( UnknownHostException ex ) {
            return "localHost";
        }
    }

    @PostConstruct
    void start()
    {
        hostname = getHostName();
    }

    @Override
    public synchronized void contextInitialized( ServletContextEvent sce )
    {
        // On webapps set it to a default title based on the name
        if( title == null ) {
            ServletContext ctx = sce.getServletContext();
            setTitle( ctx.getContextPath() + " on " + hostname );
        }
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

    @PreDestroy
    void stop()
    {
        scheduledFuture.cancel( true );
    }

    private void persist( Stat stat, int value )
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement ps = SQL.prepare( con, "INSERT INTO report.stats (tm,name,value,host) VALUES (now(),?,?,?)" ) ) {
            SQL.executeUpdate( ps, stat.getName(), value, hostname );
            LOG.log( Level.INFO, () -> stat.getName() + "=" + value );
        }
        catch( Throwable ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

    public int size()
    {
        return stats.size();
    }

    public boolean isEmpty()
    {
        return stats.isEmpty();
    }

    public void clear()
    {
        stats.clear();
    }

    public Consumer<Integer> getConsumer( String label )
    {
        String l = label;
        int i = label.indexOf( '[' );
        if( i > 0 ) {
            l = label.substring( 0, i );
        }
        return stats.computeIfAbsent( l, Stat::new );
    }

    public Stream<Stat> stream()
    {
        return stats.values().stream();
    }

    public synchronized String getTitle()
    {
        if( title == null ) {
            return "Rate statistics on " + hostname;
        }
        return title;
    }

    public synchronized void setTitle( String title )
    {
        this.title = title;
    }

    public static final class BoundedDeque
            implements Consumer<Integer>
    {

        private final int maxSize;
        private final Deque<Integer> deque;
        private LocalDateTime lastTime;
        private int lowAlarm = 0;
        private int highAlarm = Integer.MAX_VALUE;
        private int lastValue;
        private int total;

        public BoundedDeque( int maxSize )
        {
            this.maxSize = maxSize;
            this.deque = new ConcurrentLinkedDeque<>();
            // Ensures we have some data
            accept( 0 );
        }

        @Override
        public synchronized void accept( Integer t )
        {
            lastTime = TimeUtils.getLocalDateTime();
            lastValue = t;
            total += t;
            deque.offerLast( t );
            while( deque.size() > maxSize ) {
                deque.pollFirst();
            }
        }

        public JsonArrayBuilder toArray()
        {
            JsonArrayBuilder a = Json.createArrayBuilder();
            deque.forEach( a::add );
            return a;
        }

        public synchronized LocalDateTime getLastTime()
        {
            return lastTime;
        }

        public synchronized int getTotal()
        {
            return total;
        }

        public synchronized void reset()
        {
            total = 0;
        }

        public synchronized void reset( Consumer<Integer> c )
        {
            c.accept( total );
            total = 0;
        }

        public synchronized int getHighAlarm()
        {
            return highAlarm;
        }

        public synchronized void setHighAlarm( int highAlarm )
        {
            this.highAlarm = highAlarm;
        }

        public synchronized int getLowAlarm()
        {
            return lowAlarm;
        }

        public synchronized void setLowAlarm( int lowAlarm )
        {
            this.lowAlarm = lowAlarm;
        }

        public synchronized boolean isLow()
        {
            return lastValue < lowAlarm;
        }

        public synchronized boolean isHigh()
        {
            return lastValue > highAlarm;
        }

        public synchronized int getLastValue()
        {
            return lastValue;
        }

        public int get( BinaryOperator<Integer> f )
        {
            return deque.stream().reduce( f ).orElse( 0 );
        }

        public synchronized JsonObjectBuilder toJsonObjectBuilder()
        {
            return Json.createObjectBuilder()
                    .add( "time", lastTime.toString() )
                    .add( "millis", lastTime.toInstant( ZoneOffset.UTC ).toEpochMilli() )
                    .add( "low", isLow() )
                    .add( "high", isHigh() )
                    .add( "lowValue", getLowAlarm() )
                    .add( "highvalue", getHighAlarm() )
                    .add( "values", toArray() )
                    .add( "current", getLastValue() )
                    .add( "min", get( Math::min ) )
                    .add( "max", get( Math::max ) );
        }
    }

    public final class Stat
            implements Consumer<Integer>,
                       Comparable<Stat>
    {

        private final String name;
        /**
         * The last hour, so up to 60 entries
         */
        private final BoundedDeque lastHour = new BoundedDeque( 60 );
        /**
         * The last day, one for every 15 minutes, max 96 elements
         */
        private final BoundedDeque lastDay = new BoundedDeque( 96 );

        Stat( String name )
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public BoundedDeque getLastHour()
        {
            return lastHour;
        }

        public BoundedDeque getLastDay()
        {
            return lastDay;
        }

        @Override
        public void accept( Integer t )
        {
            if( t != null ) {
                // Update within the lock
                update( t );
                // persist outside - otherwise we could deadlock if the db is busy
                persist( this, t );
            }
        }

        private synchronized void update( int t )
        {
            lastHour.accept( t );

            if( Duration.between( lastDay.getLastTime(), lastHour.getLastTime() ).toMinutes() >= 15 ) {
                lastHour.reset( lastDay );
            }
        }

        @Override
        public int compareTo( Stat o )
        {
            return String.CASE_INSENSITIVE_ORDER.compare( name, o.getName() );
        }
    }
}
