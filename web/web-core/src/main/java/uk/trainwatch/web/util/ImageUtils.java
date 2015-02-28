/*
 * Copyright 2014 Peter T Mount.
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
package uk.trainwatch.web.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public class ImageUtils
{

    private static final Map<String, String> MIME_TYPES = new ConcurrentHashMap<>();

    static {
        Path path = FileSystems.getDefault().
                getPath( "/etc/mime.types" );
        try {
            Files.lines( path ).
                    map( l -> l.replaceAll( "\\p{Space}", " " ) ).
                    map( l -> l.split( " " ) ).
                    forEach( s -> {
                        for( int i = 1; i < s.length; i++ ) {
                            MIME_TYPES.putIfAbsent( s[i], s[0] );
                        }
                    } );
        }
        catch( IOException ex ) {
            Logger.getLogger( ImageUtils.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
    }

    private ImageUtils()
    {
    }

    /**
     * Returns the content-type mime type for a path
     * <p>
     * @param path Path
     * <p>
     * @return mime type or null if not supported
     */
    public static String getContentType( Path path )
    {
        String p = path.toString();

        int i = p.lastIndexOf( '.' );
        if( i < 0 && (i + 1) < p.length() ) {
            return null;
        }

        return MIME_TYPES.get( p.substring( i + 1 ) );
    }

    /**
     * Add contentType to the request based on the Path
     * <p>
     * @param path     Path
     * @param response Response to add to
     * <p>
     * @throws IllegalArgumentException if path is of an unsupported mime type
     */
    public static void addContentType( Path path, HttpServletResponse response )
    {
        String mime = getContentType( path );
        if( mime != null ) {
            response.setContentType( mime );
        }
    }

    /**
     * Adds the content length & last modified fields for a path
     * <p>
     * @param path     Path
     * @param response Response to add to
     */
    public static void addContentLenth( Path path, HttpServletResponse response )
    {
        addContentLenth( path.toFile(), response );
    }

    /**
     * Adds the content length & last modified fields for a File
     * <p>
     * @param file     File
     * @param response Response to add to
     */
    public static void addContentLenth( File file, HttpServletResponse response )
    {
        addLastModified( file, response );
        response.addIntHeader( "Content-Length", (int) file.length() );
    }

    public static void addLastModified( Path path, HttpServletResponse response )
    {
        addLastModified( path.toFile(), response );
    }

    public static void addLastModified( File file, HttpServletResponse response )
    {
        response.addDateHeader( "last-modified", file.lastModified() );
    }

    /**
     * Adds access control origin for the world
     * <p>
     * @param response Response to add to
     */
    public static void addAccessControlAllowOrigin( HttpServletResponse response )
    {
        addAccessControlAllowOrigin( "*", response );
    }

    /**
     * Adds access control origin for a specific domain
     * <p>
     * @param domain   The domain to allow access to
     * @param response Response to add to
     */
    public static void addAccessControlAllowOrigin( String domain, HttpServletResponse response )
    {
        response.addHeader( "Access-Control-Allow-Origin", domain );
    }

    /**
     * Sends an image file to the response
     * <p>
     * @param path     Path to the file in the file system
     * @param cache    CacheControl
     * @param response Response to send to
     * <p>
     * @throws IOException              on failure
     * @throws IllegalArgumentException if the image is not a supported image type
     */
    public static void sendFile( Path path, CacheControl cache, HttpServletResponse response )
            throws IOException
    {
        addContentType( path, response );
        addAccessControlAllowOrigin( response );
        cache.addHeaders( response );
        addContentLenth( path, response );
        Files.copy( path, response.getOutputStream() );
    }

}
