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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import uk.trainwatch.io.IOConsumer;
import uk.trainwatch.util.CollectionUtils;

/**
 * A simple FTP Client.
 * <p>
 * To use, create an instance by configuring a {@link FTPClientBuilder}.
 * <p>
 * For example: {@code try( FTPClient ftp = new FTPClientBuilder().build() ) { use ftp } }
 * <p>
 * @author peter
 */
public interface FTPClient
        extends Closeable
{

    /**
     * Connect
     * <p>
     * @param server host name
     * <p>
     * @throws IOException
     */
    default void connect( String server )
            throws IOException
    {
        connect( server, 0 );
    }

    /**
     * Connect
     * <p>
     * @param server host name
     * @param port   remote port
     * <p>
     * @throws IOException
     */
    void connect( String server, int port )
            throws IOException;

    boolean isConnected();

    /**
     * Log in to the remote server
     * <p>
     * @param username username
     * @param password password
     * <p>
     * @throws IOException
     */
    void login( String username, String password )
            throws IOException;

    /**
     * Log a message to the logger if there is one
     * <p>
     * @param msg
     */
    void log( Supplier<String> msg );

    /**
     * Log a message to the logger if there is one
     * <p>
     * @param msg
     */
    void log( String msg );

    boolean isLoggedIn();

    /**
     * Append an InputStream to a remote file
     * <p>
     * @param remote file name
     * @param local  stream
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean appendFile( String remote, InputStream local )
            throws IOException;

    default boolean appendFile( FTPFile remote, InputStream local )
            throws IOException
    {
        return appendFile( remote.getName(), local );
    }

    /**
     * Return an OutputStream to append to a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return stream
     * <p>
     * @throws IOException
     */
    OutputStream appendFileStream( String remote )
            throws IOException;

    /**
     * Return a Writer to append to a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return Writer
     * <p>
     * @throws IOException
     */
    Writer appendWriter( String remote )
            throws IOException;

    boolean completePendingCommand()
            throws IOException;

    /**
     * Retrieve a remote file and write to an output stream
     * <p>
     * @param remote file name
     * @param local  Output stream
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean retrieveFile( String remote, OutputStream local )
            throws IOException;

    default boolean retrieveFile( FTPFile remote, OutputStream local )
            throws IOException
    {
        return retrieveFile( remote.getName(), local );
    }

    /**
     * Retrieve a remote file
     * <p>
     * @param file    file to create. Name will also be the remote name in the current directory
     * @param options CopyOptions to use
     * <p>
     * @throws IOException
     */
    default void retrieve( File file, CopyOption... options )
            throws IOException
    {
        retrieve( file.getName(), file, options );
    }

    /**
     * Retrieve a remote file
     * <p>
     * @param remote  file name
     * @param file    file to create
     * @param options CopyOptions to use
     * <p>
     * @throws IOException
     */
    default void retrieve( FTPFile remote, File file, CopyOption... options )
            throws IOException
    {
        retrieve( remote, file.toPath(), options );
    }

    /**
     * Retrieve a remote file
     * <p>
     * @param remote  file name
     * @param file    file to create
     * @param options CopyOptions to use
     * <p>
     * @throws IOException
     */
    default void retrieve( String remote, File file, CopyOption... options )
            throws IOException
    {
        retrieve( remote, file.toPath(), options );
    }

    /**
     * Retrieve a remote file
     * <p>
     * @param target  file to create. Name will also be the remote name in the current directory
     * @param options CopyOptions to use
     * <p>
     * @throws IOException
     */
    default void retrieve( Path target, CopyOption... options )
            throws IOException
    {
        retrieve( target.getFileName().toString(), target, options );
    }

    /**
     * Retrieve a file
     * <p>
     * @param f       FTPFile to retrieve
     * @param target  Target directory, the file's name will be used
     * @param options CopyOption's
     * <p>
     * @return Path of file retrieved
     * <p>
     * @throws IOException
     */
    default Path retrieve( FTPFile f, Path target, CopyOption... options )
            throws IOException
    {
        Path path = target.resolve( f.getName() );
        retrieve( f.getName(), path, options );
        return path;
    }

    /**
     * Retrieve a remote file
     * <p>
     * @param remote  file name
     * @param target  file to create. Name will also be the remote name in the current directory
     * @param options CopyOptions to use
     * <p>
     * @throws IOException
     */
    default void retrieve( String remote, Path target, CopyOption... options )
            throws IOException
    {
        try( InputStream is = retrieveFileStream( remote ) ) {
            if( is == null ) {
                throw new FileNotFoundException( target.toString() );
            }
            Files.copy( is, target, options );
        }
        finally {
            completePendingCommand();
        }
    }

    /**
     * Return an InputStream containing the contents of a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return stream
     * <p>
     * @throws IOException
     */
    InputStream retrieveFileStream( String remote )
            throws IOException;

    default InputStream retrieveFileStream( FTPFile remote )
            throws IOException
    {
        return retrieveFileStream( remote.getName() );
    }

    /**
     * Return a Reader containing the contents of a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return Reader
     * <p>
     * @throws IOException
     */
    Reader retrieveReader( String remote )
            throws IOException;

    default Reader retrieveReader( FTPFile remote )
            throws IOException
    {
        return retrieveReader( remote.getName() );
    }

    /**
     * Store the contents of an InputStream on the remote server
     * <p>
     * @param remote file name
     * @param local  stream to store
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean store( String remote, InputStream local )
            throws IOException;

    /**
     * Store a File to the remote server
     * <p>
     * @param file
     *             <p>
     * @throws IOException
     */
    default void store( File file )
            throws IOException
    {
        store( file.getName(), file );
    }

    /**
     * Store a File to the remote server
     * <p>
     * @param remote
     * @param file
     *               <p>
     * @throws IOException
     */
    default void store( String remote, File file )
            throws IOException
    {
        store( remote, file.toPath() );
    }

    /**
     * Store a Path to the remote server
     * <p>
     * @param target
     *               <p>
     * @throws IOException
     */
    default void store( Path target )
            throws IOException
    {
        store( target.getFileName().toString(), target );
    }

    /**
     * Store a Path to the remote server
     * <p>
     * @param remote
     * @param target
     *               <p>
     * @throws IOException
     */
    default void store( String remote, Path target )
            throws IOException
    {
        try( OutputStream os = storeOutputStream( remote ) ) {
            Files.copy( target, os );
        }
        finally {
            completePendingCommand();
        }
    }

    /**
     * Return an OutputStream which will write to a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return stream
     * <p>
     * @throws IOException
     */
    OutputStream storeOutputStream( String remote )
            throws IOException;

    /**
     * Return a Writer which will write to a remote file
     * <p>
     * @param remote file name
     * <p>
     * @return writer
     * <p>
     * @throws IOException
     */
    Writer storeWriter( String remote )
            throws IOException;

    /**
     * Delete a remote file
     * <p>
     * @param pathname file name
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean deleteFile( String pathname )
            throws IOException;

    /**
     * Change to the parent directory
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean changeToParentDirectory()
            throws IOException;

    Collection<String> listNames( String pathname )
            throws IOException;

    default Stream<String> names( String pathname )
            throws IOException
    {
        return listNames( pathname ).stream();
    }

    Collection<String> listNames()
            throws IOException;

    default Stream<String> names()
            throws IOException
    {
        return listNames().stream();
    }

    default void forEachName( Consumer<String> c )
            throws IOException
    {
        CollectionUtils.forEach( listNames(), c );
    }

    default void forEachName( String pathName, Consumer<String> c )
            throws IOException
    {
        CollectionUtils.forEach( listNames( pathName ), c );
    }

    /**
     * List all directories in the current directory
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    Collection<FTPFile> listDirectories()
            throws IOException;

    default Stream<FTPFile> directories()
            throws IOException
    {
        return listDirectories().stream();
    }

    /**
     * List all directories in a directory
     * <p>
     * @param parent directory name
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    Collection<FTPFile> listDirectories( String parent )
            throws IOException;

    default Stream<FTPFile> directories( String parent )
            throws IOException
    {
        return listDirectories( parent ).stream();
    }

    /**
     * List all files in a directory
     * <p>
     * @param pathname directory name
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    Collection<FTPFile> listFiles( String pathname )
            throws IOException;

    default Stream<FTPFile> files( String pathname )
            throws IOException
    {
        return listFiles( pathname ).stream();
    }

    /**
     * List all files in the current directory
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    Collection<FTPFile> listFiles()
            throws IOException;

    default Stream<FTPFile> files()
            throws IOException
    {
        return listFiles().stream();
    }

    /**
     * List all files in a directory
     * <p>
     * @param pathname directory name
     * @param filter   Filter to use
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    Collection<FTPFile> listFiles( String pathname, FTPFileFilter filter )
            throws IOException;

    default Stream<FTPFile> files( String pathname, FTPFileFilter filter )
            throws IOException
    {
        return listFiles( pathname, filter ).stream();
    }

    /**
     * List all files in a directory
     * <p>
     * @param filter Filter to use
     * <p>
     * @return collection of entries
     * <p>
     * @throws IOException
     */
    default Collection<FTPFile> listFiles( FTPFileFilter filter )
            throws IOException
    {
        return listFiles( null, filter );
    }

    default Stream<FTPFile> files( FTPFileFilter filter )
            throws IOException
    {
        return listFiles( filter ).stream();
    }

    /**
     * List files in the current directory and pass each one to a consumer
     * <p>
     * @param c Consumer
     * <p>
     * @throws IOException
     */
    default void forEachFile( Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( listFiles(), c );
    }

    /**
     * List files in the current directory and pass each one to a consumer
     * <p>
     * @param c Consumer
     * <p>
     * @throws IOException
     */
    default void forEach( IOConsumer<FTPFile> c )
            throws IOException
    {
        forEachFile( c.guard() );
    }

    /**
     * List files in the specified directory and pass each one to a consumer
     * <p>
     * @param pathname directory name
     * @param c        Consumer
     * <p>
     * @throws IOException
     */
    default void forEachFile( String pathname, Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( listFiles( pathname ), c );
    }

    /**
     * List files in the specified directory and pass each one to a consumer
     * <p>
     * @param pathname directory name
     * @param c        Consumer
     * <p>
     * @throws IOException
     */
    default void forEach( String pathname, IOConsumer<FTPFile> c )
            throws IOException
    {
        try {
            forEachFile( pathname, c.guard() );
        }
        catch( UncheckedIOException ex ) {
            throw ex.getCause();
        }
    }

    /**
     * List files in the specified directory, apply a filter then pass each one to a consumer
     * <p>
     * @param pathname directory name
     * @param filter   filter
     * @param c        Consumer
     * <p>
     * @throws IOException
     */
    default void forEachFile( String pathname, FTPFileFilter filter, Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( listFiles( pathname, filter ), c );
    }

    /**
     * List files in the specified directory, apply a filter then pass each one to a consumer
     * <p>
     * @param filter filter
     * @param c      Consumer
     * <p>
     * @throws IOException
     */
    default void forEach( FTPFileFilter filter, IOConsumer<FTPFile> c )
            throws IOException
    {
        forEach( null, filter, c );
    }

    /**
     * List files in the specified directory, apply a filter then pass each one to a consumer
     * <p>
     * @param pathname directory name
     * @param filter   filter
     * @param c        Consumer
     * <p>
     * @throws IOException
     */
    default void forEach( String pathname, FTPFileFilter filter, IOConsumer<FTPFile> c )
            throws IOException
    {
        try {
            forEachFile( pathname, filter, c.guard() );
        }
        catch( UncheckedIOException ex ) {
            throw ex.getCause();
        }
    }

    /**
     * Create a directory
     * <p>
     * @param pathname directory name
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean makeDirectory( String pathname )
            throws IOException;

    Collection<FTPFile> mlistDir()
            throws IOException;

    Collection<FTPFile> mlistDir( String pathname )
            throws IOException;

    Collection<FTPFile> mlistDir( String pathname, FTPFileFilter filter )
            throws IOException;

    default void forEachMlistDir( Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( mlistDir(), c );
    }

    default void forEachMlist( IOConsumer<FTPFile> c )
            throws IOException
    {
        try {
            forEachMlistDir( c.guard() );
        }
        catch( UncheckedIOException ex ) {
            throw ex.getCause();
        }
    }

    default void forEachMlistDir( String pathname, Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( mlistDir( pathname ), c );
    }

    default void forEachMlist( String pathname, IOConsumer<FTPFile> c )
            throws IOException
    {
        try {
            forEachMlistDir( pathname, c.guard() );
        }
        catch( UncheckedIOException ex ) {
            throw ex.getCause();
        }
    }

    default void forEachMlistDir( String pathname, FTPFileFilter filter, Consumer<FTPFile> c )
            throws IOException
    {
        CollectionUtils.forEach( mlistDir( pathname, filter ), c );
    }

    default void forEachMlist( String pathname, FTPFileFilter filter, IOConsumer<FTPFile> c )
            throws IOException
    {
        try {
            forEachMlistDir( pathname, filter, c.guard() );
        }
        catch( UncheckedIOException ex ) {
            throw ex.getCause();
        }
    }

    FTPFile mlistFile( String pathname )
            throws IOException;

    default void mlistFile( String pathname, IOConsumer<FTPFile> c )
            throws IOException
    {
        FTPFile file = mlistFile( pathname );
        if( file != null ) {
            try {
                c.accept( file );
            }
            catch( UncheckedIOException ex ) {
                throw ex.getCause();
            }
        }

    }

    /**
     * The current directory
     * <p>
     * @return
     *         <p>
     * @throws IOException
     */
    String printWorkingDirectory()
            throws IOException;

    /**
     * Change to the specified directory
     * <p>
     * @param pathname
     *                 <p>
     * @return
     *         <p>
     * @throws IOException
     */
    boolean changeWorkingDirectory( String pathname )
            throws IOException;

    /**
     * Simple test to see if a remote file should be retrieved.
     * <p>
     * This returns true if file does not exist, if the lengths don't match or the remote file's date is newer than the local file
     * <p>
     * @param file
     * @param ftp
     *             <p>
     * @return
     */
    default boolean isFileRetrievable( File file, FTPFile ftp )
    {
        return !file.exists()
               || file.length() != ftp.getSize()
               || file.lastModified() < ftp.getTimestamp().getTimeInMillis();
    }

}
