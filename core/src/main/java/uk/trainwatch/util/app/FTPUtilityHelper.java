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
package uk.trainwatch.util.app;

import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import uk.trainwatch.io.ftp.FTPClient;

/**
 * A utility class to add support for ftp to be added to some Utility.
 * <p>
 * @author peter
 */
public class FTPUtilityHelper
{

    private String host;
    private int port;
    private String user;
    private String pass;

    /**
     *
     * @param options Options to add our options to
     */
    public FTPUtilityHelper( Options options )
    {
        options.addOption( null, "ftphost", true, "FTP host" ).
                addOption( null, "ftpport", true, "FTP port" ).
                addOption( null, "ftpuser", true, "FTP user" ).
                addOption( null, "ftppass", true, "FTP password" );
    }

    /**
     * Parse the options for our config
     * <p>
     * @param cmd CommandLine
     * <p>
     * @return true if parsed successfully
     */
    public boolean parseArgs( CommandLine cmd )
    {
        host = cmd.getOptionValue( "ftphost" );
        if( cmd.hasOption( "ftpport" ) ) {
            port = Integer.parseInt( cmd.getOptionValue( "ftpport" ) );
        }
        else {
            port = 0;
        }

        user = cmd.getOptionValue( "ftpuser" );
        pass = cmd.getOptionValue( "ftppass" );
        return host != null && !host.isEmpty()
               && user != null && !user.isEmpty()
               && pass != null && !pass.isEmpty();
    }

    /**
     * Connect to the ftp server using our config
     * <p>
     * @param ftp FTPClient to connect
     * <p>
     * @throws IOException
     */
    public void connect( FTPClient ftp )
            throws IOException
    {
        if( port > 0 ) {
            ftp.connect( host, port );
        }
        else {
            ftp.connect( host );
        }
    }

    /**
     * Login to the ftp server using our config
     * <p>
     * @param ftp FTPClient to login
     * <p>
     * @throws IOException
     */
    public void login( FTPClient ftp )
            throws IOException
    {
        ftp.login( user, pass );
    }
}
