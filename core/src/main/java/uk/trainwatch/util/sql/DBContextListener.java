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
package uk.trainwatch.util.sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 *
 * @author peter
 */
public abstract class DBContextListener
        implements ServletContextListener
{

    protected final Logger log;

    private DataSource railDataSource;

    public DBContextListener()
    {
        this.log = Logger.getLogger( getClass().
                getName() );
    }

    /**
     * The DataSource for the rail database
     * @return 
     */
    protected final DataSource getRailDataSource()
    {
        return railDataSource;
    }

    @Override
    public final void contextInitialized( ServletContextEvent sce )
    {
        try {
            railDataSource = InitialContext.
                    doLookup( "java:/comp/env/jdbc/rail" );

            init( sce );
        }
        catch( NamingException |
               SQLException ex ) {
            log.log( Level.SEVERE, "Init failed", ex );
            throw new RuntimeException( ex );
        }
    }

    @Override
    public final void contextDestroyed( ServletContextEvent sce )
    {
        shutdown( sce );
    }

    /**
     * Called when the context starts
     * 
     * @param sce
     * @throws SQLException 
     */
    protected abstract void init( ServletContextEvent sce )
            throws SQLException;

    /**
     * Called when the context shuts down
     * @param sce 
     */
    protected void shutdown( ServletContextEvent sce )
    {
    }
}
