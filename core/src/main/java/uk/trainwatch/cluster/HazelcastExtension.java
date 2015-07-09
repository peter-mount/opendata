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
package uk.trainwatch.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import uk.trainwatch.util.ResourceFinder;
import uk.trainwatch.util.Streams;

/**
 * Extension to manage our dynamic Hazelcast config
 *
 * @author peter
 */
public class HazelcastExtension
        implements Extension
{

    private static final Logger log = Logger.getLogger( HazelcastExtension.class.getName() );

    public static final String HAZELCAST_CONFIG_LOCATION = "hazelcast.config.location";

    private static final String HAZELCAST_START = "<hazelcast>";
    private static final String HAZELCAST_END = "</hazelcast>";

    private File configFile;

    private CachingProvider provider;

    public void beforeBeanDiscovery( @Observes BeforeBeanDiscovery type )
    {
        // Scan for all cluster.xml files & merge into a single hazelcast.xml file
        try {
            configFile = File.createTempFile( "hazelcast", ".xml" );
            configFile.deleteOnExit();
            System.setProperty( HAZELCAST_CONFIG_LOCATION, configFile.getCanonicalPath() );
            System.setProperty( "hazelcast.config", configFile.getCanonicalPath() );

            System.setProperty( "hazelcast.jcache.provider.type", "server" );

            log.log( Level.INFO, () -> "Generating " + configFile );

            URL baseURL = new URL( System.getProperty( "uk.trainwatch.cluster", "file:/usr/local/etc/cluster.xml" ) );

            try( FileWriter w = new FileWriter( configFile ) ) {
                PrintWriter pw = new PrintWriter( w );
                pw.println( HAZELCAST_START );

                ResourceFinder finder = new ResourceFinder( "META-INF/" );
                Streams.concat( finder.findAll( "cluster.xml" ),
                                Collections.singletonList( baseURL ) ).
                        forEach( url -> {
                            log.log( Level.INFO, () -> "Found " + url );

                            try( BufferedReader r = new BufferedReader( new InputStreamReader( url.openStream() ) ) ) {
                                r.lines().
                                filter( l -> !l.contains( HAZELCAST_START ) && !l.contains( HAZELCAST_END ) ).
                                peek( l -> log.log( Level.INFO, l ) ).
                                forEach( pw::println );
                            }
                            catch( IOException ex ) {
                                throw new UncheckedIOException( ex );
                            }
                        } );
                pw.println( HAZELCAST_END );
                w.flush();
            }

        }
        catch( IOException |
               UncheckedIOException ex ) {
            log.log( Level.SEVERE, null, ex );
            throw new IllegalArgumentException( ex );
        }
    }

    public void afterBeanDiscovery( @Observes AfterBeanDiscovery type )
    {
        log.log( Level.INFO, "Getting CachingProvider" );
        provider = Caching.getCachingProvider();

        // Obtain the CacheManager - this will force Hazelcast to start up.
        // We'll report the available cache names anyhow
        log.log( Level.INFO, "Getting CacheManager" );
        provider.getCacheManager().
                getCacheNames().
                forEach( cacheName -> log.log( Level.INFO, () -> "Found cache: " + cacheName ) );

        log.log( Level.INFO, "Hazelcast & JCache started" );
    }

    public void beforeShutdown( @Observes BeforeShutdown type )
    {
        if( provider != null ) {
            provider.close();
        }
    }
}
