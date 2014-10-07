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
package uk.trainwatch.io.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Read's PSV files.
 * <p>
 * These are "|" delimited with the first line containing the field names.
 * <p>
 * From PostgreSQL you can generate these from queries in psql with:
 * <ol>
 * <li>\f |</li>
 * <li>\a</li>
 * <li>SELECT * FROM sometable;</li>
 * <li>\g outputFile.psv</li>
 * </ol>
 * <p>
 * @author Peter T Mount
 */
public class PsvReader
{

    public static <T> Collection<T> load( Path path, Function<String[], T> f )
            throws IOException
    {
        return load( new FileReader( path.toFile() ), f );
    }

    public static <T> Collection<T> load( File file, Function<String[], T> f )
            throws IOException
    {
        if( file.exists() && file.canRead() )
        {
            return load( new FileReader( file ), f );
        }
        else
        {
            return Collections.emptyList();
        }
    }

    /**
     * Load a PSV file from an InputStream
     * <p>
     * @param <T> Type of record
     * @param is  InputStream
     * @param f   mapper to map String[] to final record
     * <p>
     * @return Collection of results
     * <p>
     * @throws IOException on read failure
     */
    public static <T> Collection<T> load( InputStream is, Function<String[], T> f )
            throws IOException
    {
        return load( new InputStreamReader( is ), f );
    }

    /**
     * Load a PSV file from a Reader.
     * <p>
     * Each line is read with the first discarded, then split on "|" before being passed to the mapping function which
     * should parsed to create the specified object.
     * <p>
     * If the function returns null, then no object is entered into the final collection.
     * <p>
     * @param <T> Type of record
     * @param r   Reader
     * @param f   mapper to map String[] to final record
     * <p>
     * @return Collection of results
     * <p>
     * @throws IOException on read failure
     */
    public static <T> Collection<T> load( Reader r, Function<String[], T> f )
            throws IOException
    {
        try( BufferedReader br = new BufferedReader( r ) )
        {
            return br.lines().
                    // --------------------------
                    // No blank lines or comments
                    map( String::trim ).
                    filter( l -> !l.isEmpty() ).
                    filter( l -> !l.startsWith( "#" ) ).
                    // ----------------------------------------------
                    // Additional row returned from psql so Ignore it
                    filter( l -> !(l.startsWith( "(" ) && l.endsWith( " rows)" )) ).
                    // -----------------------------------------------------
                    // Skip the first real data line, it's the field headers
                    skip( 1 ).
                    // ----------------------------------
                    // Uncomment to dump stream to stdout
                    //peek(System.out::println).
                    // ----------------------------------
                    // Process the record
                    map( l -> l.split( "\\|" ) ).
                    map( f ).
                    //-------------------------------------
                    // Do nothing if function returned null
                    filter( Objects::nonNull ).
                    // ----------------------------
                    collect( Collectors.toList() );
        }
    }
}
