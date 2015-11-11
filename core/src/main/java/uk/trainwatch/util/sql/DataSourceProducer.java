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
package uk.trainwatch.util.sql;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class DataSourceProducer
{

    private static volatile DataSourceProducer instance;

    private static boolean useJndi;
    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    private static Factory factory;

    public DataSourceProducer()
    {
        this( true );
    }

    private DataSourceProducer( boolean useJndi )
    {
        this.useJndi = useJndi;
    }

    public static DataSourceProducer getInstance()
    {
        if( instance == null ) {
            synchronized( DataSourceProducer.class ) {
                if( instance == null ) {
                    instance = new DataSourceProducer( false );
                }
                instance.start();
            }
        }
        return instance;
    }

    public static void setUseJndi( boolean useJndi )
    {
        DataSourceProducer.useJndi = useJndi;
    }

    public static void setFactory( Properties p )
    {
        if( factory != null ) {
            throw new IllegalStateException( "DataSource Factory already defined" );
        }
        factory = new Factory( p );
    }

    @PostConstruct
    void start()
    {
        instance = this;
    }

    @Produces
    @Database("")
    DataSource getDataSource( InjectionPoint injectionPoint )
    {
        for( Annotation a: injectionPoint.getQualifiers() ) {
            if( a instanceof Database ) {
                return getDataSource( ((Database) a).value() );
            }
        }
        throw new IllegalArgumentException( "Unable to inject unnamed DataSource" );
    }

    public DataSource getDataSource( String name )
    {
        return dataSources.computeIfAbsent( name, this::lookup );
    }

    private DataSource lookup( String name )
    {
        DataSource dataSource = null;

        if( useJndi ) {
            try {
                dataSource = InitialContext.doLookup( "java:/comp/env/jdbc/" + name );
            }
            catch( NamingException ex ) {
                // Do nothing
            }
        }

        if( dataSource == null && factory != null ) {
            dataSource = factory.getDataSource( name );
        }

        if( dataSource == null ) {
            throw new IllegalArgumentException( "Cannot find DataSource " + name );
        }

        return dataSource;
    }

    private static class Factory
    {

        private final Properties properties;
        private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

        public Factory( Properties properties )
        {
            this.properties = properties;
        }

        public DataSource getDataSource( String name )
        {
            return dataSources.computeIfAbsent( name, this::newDataSource );
        }

        private DataSource newDataSource( String name )
        {
            PGPoolingDataSource ds = new PGPoolingDataSource();
            ds.setApplicationName( "OpenData" );
            ds.setDataSourceName( name );
            ds.setMaxConnections( 10 );
            ds.setInitialConnections( 0 );

            ds.setUser( Objects.requireNonNull( properties.getProperty( name + ".username" ), "username required for " + name ) );
            ds.setPassword( Objects.requireNonNull( properties.getProperty( name + ".password" ), "password required for " + name ) );
            ds.setServerName( Objects.requireNonNull( properties.getProperty( name + ".hostname" ), "hostname required for " + name ) );
            ds.setDatabaseName( properties.getProperty( name + ".database", name ) );
            ds.setPortNumber( Integer.parseInt( properties.getProperty( name + ".port", "0" ) ) );

            return ds;
        }
    }
}
