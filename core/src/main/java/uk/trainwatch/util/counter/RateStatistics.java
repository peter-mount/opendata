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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.DaemonThreadFactory;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class RateStatistics
        implements Runnable
{

    private static final Logger LOG = Logger.getLogger( RateStatistics.class.getName() );

    @Database("rail") @Inject
    private DataSource dataSource;

    private ScheduledFuture<?> scheduledFuture;

    private final Map<String, Integer> stats = new ConcurrentHashMap<>();
    private String hostname;

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
        LOG.log( Level.INFO, () -> "Hostname" + hostname );

        scheduledFuture = DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( this, 1L, 1L, TimeUnit.MINUTES );
    }

    @PreDestroy
    void stop()
    {
        scheduledFuture.cancel( true );
    }

    @Override
    public void run()
    {
        LOG.log( Level.INFO, () -> "Running persistence " + stats );
        try( Connection con = dataSource.getConnection();
             PreparedStatement ps = SQL.prepare( con, "INSERT INTO report.stats (tm,name,value,host) VALUES (now(),?,?,?)" ) ) {
            stats.forEach( SQLBiConsumer.guard( ( label, value ) -> {
                SQL.executeUpdate( ps, label, value, hostname );
                LOG.log( Level.INFO, () -> label + "=" + value );
            } ) );
        }
        catch( Throwable ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

    void submit( String label, int lastCount )
    {
        int i = label.indexOf( '[' );
        if( i > -1 ) {
            label = label.substring( 0, i );
        }
        if( !label.isEmpty() ) {
            stats.put( label, lastCount );
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

    public Integer get( String key )
    {
        return stats.get( key );
    }

    public void clear()
    {
        stats.clear();
    }

    public Set<String> keySet()
    {
        return stats.keySet();
    }

    public Set<Map.Entry<String, Integer>> entrySet()
    {
        return stats.entrySet();
    }

    public void forEach( BiConsumer<? super String, ? super Integer> action )
    {
        stats.forEach( action );
    }

}
