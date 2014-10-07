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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter T Mount
 * @param <T>
 */
public class PsvWriter<T extends PsvWritable>
        implements Consumer<T>
{

    private static final Logger LOG = Logger.getLogger( PsvWriter.class.toString() );

    private String headers;
    private final List<String> comments = new ArrayList<>();
    private final List<String> lines = new ArrayList<>();

    public PsvWriter()
    {
    }

    @Override
    public void accept( T t )
    {
        if( headers == null || headers.isEmpty() )
        {
            t.addPsvHeaders( this );
            if( headers == null || headers.isEmpty() )
            {
                headers = "No headers provided";
            }
        }
        t.toPsvWriter( this );
    }

    public PsvWriter<T> setHeaders( String... headers )
    {
        this.headers = toRecordString( headers );
        return this;
    }

    public PsvWriter<T> forEach( Collection<T> col )
    {
        col.forEach( this );
        return this;
    }

    public <V> V returnValue( V val )
    {
        return val;
    }

    private static String toRecordString( Object[] values )
    {
        StringJoiner j = new StringJoiner( "|" );
        for( Object value : values )
        {
            if( value == null )
            {
                j.add( "" );
            }
            else
            {
                j.add( value.toString() );
            }
        }
        return j.toString();
    }

    public PsvWriter<T> add( Object... values )
    {
        if( values != null && values.length > 0 )
        {
            lines.add( toRecordString( values ) );
        }
        return this;
    }

    public PsvWriter<T> comment( String comment )
    {
        comments.add( "# " + comment );
        return this;
    }

    public PsvWriter<T> comment( String fmt, Object... args )
    {
        if( args == null || args.length == 0 )
        {
            return comment( fmt );
        }

        comments.add( String.format( "# " + fmt, args ) );
        return this;
    }

    public PsvWriter<T> write( Supplier<Path> s )
    {
        if( !lines.isEmpty() )
        {
            write( s.get() );
        }
        return this;
    }

    public PsvWriter<T> write( Path p )
    {
        LOG.info( () -> "Recording " + p );

        try( FileWriter w = new FileWriter( p.toFile() ) )
        {
            write( w );
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to record " + p );
        }
        return this;
    }

    public PsvWriter<T> write( Writer w )
    {
        try( PrintWriter pw = new PrintWriter( w ) )
        {
            comments.forEach( pw::println );
            pw.println( headers );
            lines.forEach( pw::println );
        }
        return this;
    }
}
