/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.commons.configuration.AbstractConfiguration;
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

    @Database("rail")
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
        return new CachedConfiguration( new DatabaseConfiguration( dataSource, "config.config", "name", "key", "value", name, true ) );
    }

    private Configuration newPrivateConfiguration( String name )
    {
        return new CachedConfiguration( new DatabaseConfiguration( dataSource, "config.prconfig", "name", "key", "value", name, true ) );
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
    Configuration getConfiguration( InjectionPoint injectionPoint )
    {
        for( Annotation a: injectionPoint.getQualifiers() ) {
            if( a instanceof GlobalConfiguration ) {
                return getConfiguration( ((GlobalConfiguration) a).value() );
            }
        }
        return getConfiguration( "default" );
    }

    /**
     *
     * @param injectionPoint
     *                       <p>
     * @return
     */
    @Produces
    @PrivateConfiguration("")
    @Dependent
    Configuration getPrivateConfiguration( InjectionPoint injectionPoint )
    {
        for( Annotation a: injectionPoint.getQualifiers() ) {
            if( a instanceof PrivateConfiguration ) {
                return getPrivateConfiguration( ((PrivateConfiguration) a).value() );
            }
        }
        return getConfiguration( "default" );
    }

    private static class CachedConfiguration
            extends AbstractConfiguration
    {

        private final Configuration delegate;

        private final Map<String, Object> map = new ConcurrentHashMap<>();

        public CachedConfiguration( Configuration delegate )
        {
            this.delegate = delegate;
        }

        @Override
        public boolean isEmpty()
        {
            return map.isEmpty();
        }

        @Override
        public boolean containsKey( String key )
        {
            return map.containsKey( key ) || delegate.containsKey( key );
        }

        @Override
        public void clearProperty( String key )
        {
            map.remove( key );
        }

        @Override
        public void clear()
        {
            map.clear();
        }

        @Override
        public Object getProperty( String key )
        {
            return map.computeIfAbsent( key, delegate::getProperty );
        }

        @Override
        public void addProperty( String key, Object value )
        {
            // Do not allow the database to be modified
        }

        @Override
        protected void addPropertyDirect( String key, Object value )
        {
            // Do not allow the database to be modified
        }

        @Override
        public void setProperty( String key, Object value )
        {
            // Do not allow the database to be modified
        }

        @Override
        public Iterator<String> getKeys()
        {
            // Only return those in memory
            return map.keySet().iterator();
        }

    }
}
