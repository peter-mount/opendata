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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

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
        if( host != null && !host.isEmpty() ) {
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
}
