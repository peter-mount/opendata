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
package uk.trainwatch.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.function.Consumer;

/**
 * A {@link Writer} which will pass on complete lines of text to a consumer
 * <p>
 * @author peter
 */
public final class WriterConsumer
        extends Writer
{

    private StringBuilder sb = new StringBuilder();
    private final Consumer<String> consumer;
    private final boolean filterBlankLines;

    /**
     * Constructor, send every line to the consumer
     * <p>
     * @param consumer Consumer
     */
    public WriterConsumer( Consumer<String> consumer )
    {
        this( consumer, false );
    }

    /**
     * Constructor, send every line to the consumer unless filterBlankLines is true and the string would be empty
     * <p>
     * @param consumer         Consumer
     * @param filterBlankLines true then ignore empty blank lines
     */
    public WriterConsumer( Consumer<String> consumer, boolean filterBlankLines )
    {
        this.consumer = consumer;
        this.filterBlankLines = filterBlankLines;
    }

    /**
     * Convenience to print a string to writer as a single line
     * <p>
     * @param s
     */
    public synchronized void println( String s )
    {
        sb.append( s ).append( '\n' );
        try {
            flush();
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    @Override
    public synchronized void write( String str )
            throws IOException
    {
        sb.append( str );
        flush();
    }

    @Override
    public synchronized void write( char[] cbuf )
            throws IOException
    {
        sb.append( cbuf );
        flush();
    }

    @Override
    public synchronized void write( int c )
            throws IOException
    {
        sb.append( (char) c );
        flush();
    }

    @Override
    public synchronized void write( String str, int off, int len )
            throws IOException
    {
        sb.append( str, off, len );
        flush();
    }

    @Override
    public synchronized void write( char[] cbuf, int off, int len )
            throws IOException
    {
        sb.append( cbuf, off, len );
        flush();
    }

    /**
     * Flush the writer by sending everything in the buffer to the consumer
     * <p>
     * @throws IOException
     */
    @Override
    public synchronized void flush()
            throws IOException
    {
        while( sb.length() > 0 ) {
            int i = sb.indexOf( "\n" );
            if( filterBlankLines && i == 0 ) {
                // Filter out blank lines
                truncate( 1 );
            }
            else if( i > -1 ) {
                truncate( consume( i ) );
            }
            else {
                truncate( removeWhiteSpace( sb.length() ) );
                return;
            }
        }
    }

    private int consume( int e )
    {
        int p = removeWhiteSpace( e );
        if( p < e ) {
            consumer.accept( sb.substring( p, e ) );
        }
        return e + 1;
    }

    private void truncate( int p )
    {
        if( p > 0 && p < sb.length() ) {
            // Truncate up to the specified point.
            sb.delete( 0, p );
        }
        else if( p == sb.length() ) {
            // Truncate everything
            sb.setLength( 0 );
        }
    }

    private int removeWhiteSpace( int i )
    {
        int p = 0;
        while( p < i && Character.isSpaceChar( sb.charAt( p ) ) ) {
            p++;
        }
        return p;
    }

    @Override
    public synchronized void close()
            throws IOException
    {
        // Flush any content
        flush();

        // Any remaining text is the last line just not \n terminated
        consume( sb.length() );

        // Now kill the writer by removing the StringBuilder
        sb = null;
    }

}
