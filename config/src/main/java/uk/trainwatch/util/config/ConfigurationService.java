/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.json.Json;
import javax.json.JsonReader;
import uk.trainwatch.util.MapBuilder;
import uk.trainwatch.util.URIBuilder;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class ConfigurationService
{

    private static final Logger LOG = Logger.getLogger( "Configuration" );

    private static volatile ConfigurationService instance;

    private final Map<String, Configuration> config = new ConcurrentHashMap<>();

    @Deprecated
    public static synchronized ConfigurationService getInstance()
    {
        if( instance == null ) {
            instance = new ConfigurationService();
        }
        return instance;
    }

    /**
     * The configuration URI, either CONFIGURATION_URI environment or -Darea51.configuration= to application config server.
     * If none then we will default to the older database configuration
     */
    private final String remoteURI = System.getenv().get( "CONFIGURATION_URI" );
    private final String localConfig = System.getenv().getOrDefault( "CONFIGURATION_DIR", System.getProperty( "area51.configuration" ) );

    public Configuration getConfiguration( String name )
    {
        return config.computeIfAbsent( name, this::newConfiguration );
    }

    private Configuration newConfiguration( String name )
    {
        if( localConfig != null ) {
            return newLocalConfiguration( name );
        }
        else if( remoteURI != null ) {
            return newRemoteConfiguration( name );
        }
        return EmptyConfiguration.INSTANCE;
    }

    private Configuration newRemoteConfiguration( String name )
    {
        try {
            URI uri = new URI( remoteURI );
            if( uri.getQuery() == null ) {
                // Append /table/name.json to path
                return new HttpConfiguration( URIBuilder.create( uri )
                        .path( String.join( "/", uri.getPath(), name + ".json" ) )
                        .build() );
            }
            else {
                // Append table=table&name=name to uri
                return new HttpConfiguration( URIBuilder.create( uri )
                        .query( uri.getQuery() )
                        .query()
                        .add( "name", name )
                        .endQuery()
                        .build() );
            }
        }
        catch( URISyntaxException ex ) {
            throw new IllegalArgumentException( ex );
        }
    }

    private Configuration newLocalConfiguration( String name )
    {
        LOG.log( Level.INFO, () -> "Reading local config " + name );
        Path p = Paths.get( localConfig, name + ".json" );
        if( Files.isRegularFile( p, LinkOption.NOFOLLOW_LINKS ) ) {
            try( JsonReader r = Json.createReader( Files.newBufferedReader( p, StandardCharsets.UTF_8 ) ) ) {
                return new MapConfiguration( MapBuilder.fromJsonObject( r.readObject() ).build() );
            }
            catch( IOException ex ) {
                throw new IllegalArgumentException( ex );
            }
        }
        return EmptyConfiguration.INSTANCE;
    }

    /**
     *
     * @param injectionPoint
     *                       <p>
     * @return
     */
    @Produces
    @GlobalConfiguration("")
    @Dependent
    Configuration getConfiguration( InjectionPoint injectionPoint
    )
    {
        for( Annotation a: injectionPoint.getQualifiers() ) {
            if( a instanceof GlobalConfiguration ) {
                return getConfiguration( ((GlobalConfiguration) a).value() );
            }
        }
        return getConfiguration( "default" );
    }
}
