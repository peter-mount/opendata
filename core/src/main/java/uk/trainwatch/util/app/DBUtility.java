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
package uk.trainwatch.util.app;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import static uk.trainwatch.util.app.BaseApplication.LOG;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
public abstract class DBUtility
        implements Utility
{

    private final Options options;
    private String uri, user, pass;

    public DBUtility()
    {
        this.options = new Options();
        options.addOption( "U", "username", true, "DB Username" );
        options.addOption( "P", "password", true, "DB Password" );
        options.addOption( "D", "database", true, "Database name" );
        options.addOption( "H", "hostname", true, "DB Host, defaults to localhost" );
    }

    @Override
    public final Options getOptions()
    {
        return options;
    }

    @Override
    public boolean parseArgs( CommandLine cmd )
    {
        user = Objects.requireNonNull( cmd.getOptionValue( "username" ), "username required" );
        pass = Objects.requireNonNull( cmd.getOptionValue( "password" ), "password required" );

        StringBuilder b = new StringBuilder( "jdbc:postgresql:" );
        String host = cmd.getOptionValue( "hostname" );
        if( host != null && !host.isEmpty() )
        {
            b.append( "//" ).
                    append( host ).
                    append( '/' );
        }
        b.append( cmd.getOptionValue( "database", "rail" ) );
        uri = b.toString();

        return true;
    }

    protected final Connection getConnection()
            throws SQLException
    {
        return DriverManager.getConnection( uri, user, pass );
    }

    /**
     * Called by {@link #importFiles(java.util.Collection, uk.trainwatch.util.sql.SQLBiConsumer) } at the start of the import.
     * <p>
     * The default is to do nothing.
     * <p>
     * @param con
     *            <p>
     * @throws SQLException
     */
    protected void initDB( Connection con )
            throws SQLException
    {
    }

    /**
     * Import each file in sequence
     * <p>
     * @param files    Collection of {@link Path}'s to import
     * @param importer {@link SQLBiConsumer} that will import each file
     * <p>
     * @throws SQLException
     */
    protected final void importFiles( Collection<Path> files, SQLBiConsumer<Connection, Path> importer )
            throws SQLException
    {
        BiConsumer<Connection, Path> consumer = importer.guard();

        try( Connection con = getConnection() )
        {
            con.setAutoCommit( false );
            try
            {
                LOG.log( Level.INFO, "Initialising database" );
                initDB( con );

                files.forEach( f -> consumer.accept( con, f ) );

                // Now this may take a while ;-)
                LOG.log( Level.INFO, () -> "Committing to database" );
                con.commit();
                LOG.log( Level.INFO, () -> "Commit complete." );
            }
            catch( UncheckedIOException |
                   SQLException |
                   UncheckedSQLException ex )
            {
                LOG.log( Level.SEVERE, ex, () -> "Commit failed: " + ex.getMessage() );

                LOG.log( Level.INFO, () -> "Rolling back transaction" );
                con.rollback();
            }
        }
    }
}
