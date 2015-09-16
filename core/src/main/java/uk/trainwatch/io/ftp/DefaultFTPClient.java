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
package uk.trainwatch.io.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import uk.trainwatch.util.WriterConsumer;

/**
 *
 * @author peter
 */
public class DefaultFTPClient
        implements uk.trainwatch.io.ftp.FTPClient
{

    private static final Logger LOG = Logger.getLogger( DefaultFTPClient.class.getName() );

    private final FTPClient ftp;
    private final boolean useEpsvWithIPv4;
    private final boolean localActive;
    private final boolean binaryTransfer;
    private final boolean debuggingEnabled;

    private boolean loggedIn;
    private WriterConsumer writer;

    DefaultFTPClient( FTPClientBuilder builder )
    {
        this.useEpsvWithIPv4 = builder.useEpsvWithIPv4;
        this.localActive = builder.localActive;
        this.binaryTransfer = builder.binaryTransfer;

        this.debuggingEnabled = builder.debuggingEnabled && builder.consumer != null;

        if( builder.proxy == null ) {
            ftp = new FTPClient();
        }
        else {
            if( builder.proxyUser == null ) {
                ftp = new FTPHTTPClient( builder.proxy, builder.proxyPort );
            }
            else {
                ftp = new FTPHTTPClient( builder.proxy, builder.proxyPort, builder.proxyUser, builder.proxyPass );
            }
        }

        if( builder.keepAliveTimeout >= 0 ) {
            ftp.setControlKeepAliveTimeout( builder.keepAliveTimeout );
        }

        if( builder.controlKeepAliveReplyTimeout >= 0 ) {
            ftp.setControlKeepAliveReplyTimeout( builder.controlKeepAliveReplyTimeout );
        }

        ftp.setListHiddenFiles( builder.listHiddenFiles );

        if( builder.consumer != null ) {
            // Don't log blank lines
            writer = new WriterConsumer( builder.consumer, true );

            if( builder.printCommands ) {
                // suppress login details
                ftp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( writer ), true ) );
            }
        }
    }

    @Override
    public void close()
            throws IOException
    {
        try {
            if( ftp.isConnected() ) {
                try {
                    if( loggedIn ) {
                        ftp.logout();
                    }
                }
                finally {
                    loggedIn = false;

                    ftp.disconnect();
                }
            }
        }
        finally {
            if( writer != null ) {
                writer.close();
                writer = null;
            }
        }
    }

    @Override
    public void log( String msg )
    {
        if( writer != null ) {
            writer.println( msg );
        }
    }

    @Override
    public void log( Supplier<String> msg )
    {
        if( writer != null ) {
            writer.println( msg.get() );
        }
    }

    private void debug( Supplier<String> msg )
    {
        if( debuggingEnabled ) {
            log( msg );
        }
    }

    @Override
    public void connect( String server, int port )
            throws IOException
    {
        if( ftp.isConnected() ) {
            throw new IllegalStateException( "Already connected" );
        }

        debug( () -> "Connecting to " + server + " on " + (port > 0 ? port : ftp.getDefaultPort()) );

        if( port > 0 ) {
            ftp.connect( server, port );
        }
        else {
            ftp.connect( server );
        }

        debug( () -> "Connected to " + server + " on " + (port > 0 ? port : ftp.getDefaultPort()) );

        // After connection attempt, you should check the reply code to verifysuccess.
        if( !FTPReply.isPositiveCompletion( ftp.getReplyCode() ) ) {
            debug( () -> "Connection refused to " + server + " on " + (port > 0 ? port : ftp.getDefaultPort()) );
            throw new ConnectException( "Failed to connect to " + server );
        }
    }

    @Override
    public boolean isConnected()
    {
        return ftp.isConnected();
    }

    @Override
    public void login( String username, String password )
            throws IOException
    {
        if( loggedIn ) {
            throw new IllegalStateException( "Already logged in" );
        }

        if( !ftp.login( username, password ) ) {
            throw new ConnectException( "Login failed" );
        }

        String systemType = ftp.getSystemType();
        debug( () -> "Remote system is " + systemType );

        if( binaryTransfer ) {
            ftp.setFileType( FTP.BINARY_FILE_TYPE );
        }
        else {
            // in theory this should not be necessary as servers should default to ASCII but they don't all do so - see NET-500
            ftp.setFileType( FTP.ASCII_FILE_TYPE );
        }

        // Use passive mode as default because most of us are
        // behind firewalls these days.
        if( localActive ) {
            ftp.enterLocalActiveMode();
        }
        else {
            ftp.enterLocalPassiveMode();
        }

        ftp.setUseEPSVwithIPv4( useEpsvWithIPv4 );

        loggedIn = true;
    }

    @Override
    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    @Override
    public boolean changeWorkingDirectory( String pathname )
            throws IOException
    {
        return ftp.changeWorkingDirectory( pathname );
    }

    @Override
    public boolean changeToParentDirectory()
            throws IOException
    {
        return ftp.changeToParentDirectory();
    }

    @Override
    public boolean appendFile( String remote, InputStream local )
            throws IOException
    {
        return ftp.appendFile( remote, local );
    }

    @Override
    public OutputStream appendFileStream( String remote )
            throws IOException
    {
        return ftp.appendFileStream( remote );
    }

    @Override
    public Writer appendWriter( String remote )
            throws IOException
    {
        return new OutputStreamWriter( appendFileStream( remote ) );
    }

    @Override
    public boolean store( String remote, InputStream local )
            throws IOException
    {
        return ftp.storeFile( remote, local );
    }

    @Override
    public OutputStream storeOutputStream( String remote )
            throws IOException
    {
        return ftp.storeFileStream( remote );
    }

    @Override
    public Writer storeWriter( String remote )
            throws IOException
    {
        return new OutputStreamWriter( storeOutputStream( remote ) );
    }

    @Override
    public boolean completePendingCommand()
            throws IOException
    {
        return ftp.completePendingCommand();
    }

    @Override
    public boolean retrieveFile( String remote, OutputStream local )
            throws IOException
    {
        return ftp.retrieveFile( remote, local );
    }

    @Override
    public InputStream retrieveFileStream( String remote )
            throws IOException
    {
        return ftp.retrieveFileStream( remote );
    }

    @Override
    public Reader retrieveReader( String remote )
            throws IOException
    {
        return new InputStreamReader( retrieveFileStream( remote ) );
    }

    @Override
    public FTPFile mlistFile( String pathname )
            throws IOException
    {
        return ftp.mlistFile( pathname );
    }

    @Override
    public Collection<FTPFile> mlistDir()
            throws IOException
    {
        return Arrays.asList( ftp.mlistDir() );
    }

    @Override
    public Collection<FTPFile> mlistDir( String pathname )
            throws IOException
    {
        return Arrays.asList( ftp.mlistDir( pathname ) );
    }

    @Override
    public Collection<FTPFile> mlistDir( String pathname, FTPFileFilter filter )
            throws IOException
    {
        return Arrays.asList( ftp.mlistDir( pathname, filter ) );
    }

    @Override
    public boolean deleteFile( String pathname )
            throws IOException
    {
        return ftp.deleteFile( pathname );
    }

    @Override
    public boolean makeDirectory( String pathname )
            throws IOException
    {
        return ftp.makeDirectory( pathname );
    }

    @Override
    public String printWorkingDirectory()
            throws IOException
    {
        return ftp.printWorkingDirectory();
    }

    @Override
    public Collection<FTPFile> listFiles( String pathname )
            throws IOException
    {
        return Arrays.asList( ftp.listFiles( pathname ) );
    }

    @Override
    public Collection<FTPFile> listFiles()
            throws IOException
    {
        return Arrays.asList( ftp.listFiles() );
    }

    @Override
    public Collection<FTPFile> listFiles( String pathname, FTPFileFilter filter )
            throws IOException
    {
        return Arrays.asList( ftp.listFiles( pathname, filter ) );
    }

    @Override
    public Collection<FTPFile> listDirectories()
            throws IOException
    {
        return Arrays.asList( ftp.listDirectories() );
    }

    @Override
    public Collection<FTPFile> listDirectories( String parent )
            throws IOException
    {
        return Arrays.asList( ftp.listDirectories( parent ) );
    }

    @Override
    public Collection<String> listNames( String pathname )
            throws IOException
    {
        return Arrays.asList( ftp.listNames( pathname ) );
    }

    @Override
    public Collection<String> listNames()
            throws IOException
    {
        return Arrays.asList( ftp.listNames() );
    }

}
