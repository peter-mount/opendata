/*
 * Copyright 2014 peter.
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
package uk.trainwatch.apps.signalanalyser.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.json.JsonObject;

/**
 *
 * @author peter
 */
public class NetworkMapper
        implements Consumer<JsonObject>
{

    /**
     * Max time in minutes between writes if an update has been made
     */
    private static final int MAX_DURATION_BETWEEN_WRITES = 10;
    /**
     * Max number of adds between writes, overrides time limit
     */
    private static final int MAX_ADD_BETWEEN_WRITES = 100;

    /**
     * Location of file to write
     */
    private static final String OUTPUT = "/usr/local/networkrail/tdmap";

    private static final Logger LOG = Logger.getLogger( NetworkMapper.class.getName() );

    private final Map<String, Map<String, Set<String>>> networkMap = new ConcurrentHashMap<>();
    private LocalDateTime lastWrite;
    private int addCount;

    public NetworkMapper()
            throws IOException
    {
        // Load in the current definition
        File dir = new File( OUTPUT );
        for( File areaDir : dir.listFiles( (d, n) -> !n.startsWith( "." ) && n.length() == 2 ) ) {
            File file = new File( areaDir, "td.txt" );
            if( file.exists() ) {
                LOG.log( Level.INFO, () -> "Reading " + file );
                try( BufferedReader r = new BufferedReader( new FileReader( file ) ) ) {
                    r.lines().
                            map( l -> l.split( " " ) ).
                            forEach( s -> {
                                Set<String> dest = getBerth( s[0], s[1] );
                                for( int i = 2; i < s.length; i++ ) {
                                    dest.add( s[i] );
                                }
                            } );
                }
            }
        }

        lastWrite = LocalDateTime.now();
    }

    private Set<String> getBerth( String area, String berth )
    {
        return networkMap.computeIfAbsent( area, a -> new ConcurrentHashMap<>() ).
                computeIfAbsent( berth, f -> new ConcurrentSkipListSet<>() );
    }

    private boolean add( String area, String from, String to )
    {
        return getBerth( area, from ).
                add( to );
    }

    @Override
    public void accept( JsonObject t )
    {
        // Map only CA as they record a movement between berths
        if( "CA".equals( t.getString( "msg_type" ) ) ) {
            String area = t.getString( "area_id" );
            String from = t.getString( "from" );
            String to = t.getString( "to" );
            if( add( area, from, to ) ) {
                addCount++;
                LOG.log( Level.INFO, () -> "Linking " + area + " " + t.getString( "from" ) + " " + t.getString( "to" ) );
                write();
            };
        }
    }

    private void backup( File f1, File f2 )
    {
        if( f2.exists() ) {
            LOG.log( Level.FINE, () -> "Deleting " + f2 );
            f2.delete();
        }
        if( f1.exists() ) {
            LOG.log( Level.FINE, () -> "Renaming " + f1.getName() + " to " + f2.getName() );
            f1.renameTo( f2 );
        }
    }

    private File backup( File dir )
    {
        // Backup last 9 versions
        File file = new File( dir, "td-9.txt" );
        File f2;
        for( int i = 8; i > 0; i-- ) {
            f2 = file;
            file = new File( dir, "td-" + i + ".txt" );
            backup( file, f2 );
        }

        f2 = file;
        file = new File( dir, "td.txt" );
        backup( file, f2 );
        return file;
    }

    private void write()
    {
        LocalDateTime now = LocalDateTime.now();
        if( addCount >= MAX_ADD_BETWEEN_WRITES
                || Duration.between( lastWrite, now ).
                toMinutes() >= MAX_DURATION_BETWEEN_WRITES ) {
            LOG.log( Level.INFO, "Writing current map" );
            networkMap.keySet().
                    stream().
                    sorted().
                    forEach( area -> {
                        File dir = new File( OUTPUT );
                        dir = new File( dir, area );
                        if( dir.mkdirs() ) {
                            LOG.log( Level.INFO, "Created {0}", dir );
                        }
                        File file = backup( dir );
                        LOG.log( Level.FINE, "Writing {0}", file );
                        try( PrintWriter w = new PrintWriter( new FileWriter( file ) ) ) {
                            networkMap.get( area ).
                            entrySet().
                            stream().
                            sorted( (a, b) -> a.getKey().
                                    compareTo( b.getKey() ) ).
                            forEach( e -> w.printf( "%s %s %s\n",
                                                    area,
                                                    e.getKey(),
                                                    e.getValue().
                                                    stream().
                                                    collect( Collectors.joining( " " ) ) )
                            );
                        } catch( IOException ex ) {
                            LOG.log( Level.SEVERE, "Failed to write " + file, ex );
                        }
                    }
                    );

            lastWrite = now;
            addCount = 0;
        }
    }
}
