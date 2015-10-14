/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DatabaseConfiguration;
import uk.trainwatch.util.sql.DataSourceProducer;
import uk.trainwatch.util.sql.Database;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class ConfigurationService
{

    private static volatile ConfigurationService instance;

    private final Map<String, Configuration> publicConfig = new ConcurrentHashMap<>();
    private final Map<String, Configuration> privateConfig = new ConcurrentHashMap<>();

    public static ConfigurationService getInstance()
    {
        if( instance == null ) {
            synchronized( ConfigurationService.class ) {
                if( instance == null ) {
                    instance = new ConfigurationService();
                    instance.dataSource = DataSourceProducer.getInstance().getDataSource( "config" );
                }
            }
        }
        return instance;
    }

    @Database("config")
    @Inject
    private DataSource dataSource;

    public Configuration getConfiguration( String name )
    {
        return publicConfig.computeIfAbsent( name, this::newConfiguration );
    }

    public Configuration getPrivateConfiguration( String name )
    {
        return privateConfig.computeIfAbsent( name, this::newPrivateConfiguration );
    }

    private Configuration newConfiguration( String name )
    {
        return new DatabaseConfiguration( dataSource, "config.config", "name", "key", "value", name, true );
    }

    private Configuration newPrivateConfiguration( String name )
    {
        return new DatabaseConfiguration( dataSource, "config.prconfig", "name", "key", "value", name, true );
    }

    /**
     *
     * @param injectionPoint
     *                       <p>
     * @return
     */
    @Produces
    Configuration getConfiguration( InjectionPoint injectionPoint )
    {
        for( Annotation a: injectionPoint.getQualifiers() ) {
            if( a instanceof Named ) {
                return getConfiguration( ((Named) a).value() );
            }
            if( a instanceof PrivateConfiguration ) {
                return getPrivateConfiguration( ((PrivateConfiguration) a).value() );
            }
        }
        return getConfiguration( "default" );
    }
}
