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
package uk.trainwatch.io.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import uk.trainwatch.io.format.DataReader;

/**
 *
 * @author peter
 */
public enum WireMessageRegistry
        implements Iterable<String>
{

    INSTANCE;

    private final Map<String, WireMessageFormat<?>> types = new ConcurrentHashMap<>();

    private WireMessageRegistry()
    {
        ServiceLoader.load( WireMessageFormat.class )
                .forEach( this::register );
    }

    public void register( WireMessageFormat<?> fmt )
    {
        if( types.putIfAbsent( fmt.getType(), fmt ) != null ) {
            throw new IllegalArgumentException( "Type " + fmt.getType() + " already registered" );
        }
    }

    public <V> WireMessageFormat<V> get( String type )
    {
        return (WireMessageFormat<V>) types.get( type );
    }

    public <T> WireMessage<T> readMessage( byte[] b )
            throws IOException
    {
        try( ByteArrayInputStream bais = new ByteArrayInputStream( b ) ) {
            return readMessage( bais );
        }
    }

    public <T> WireMessage<T> readMessage( InputStream is )
            throws IOException
    {
        try( DataReader r = new DataReader( is ) ) {
            return readMessage( r );
        }
    }

    public <T> WireMessage<T> readMessage( DataReader r )
            throws IOException
    {
        String type = r.readString();
        WireMessageFormat fmt = get( type );
        if( fmt == null ) {
            return null;
        }

        return new WireMessage<>( type, r, fmt.reader() );
    }

    public Stream<String> getTypes()
    {
        return types.keySet().stream();
    }

    @Override
    public Iterator<String> iterator()
    {
        // Doing it this way means we have a read-only iterator
        final Iterator<String> it = types.keySet().iterator();
        return new Iterator<String>()
        {

            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public String next()
            {
                return it.next();
            }

        };
    }

}
