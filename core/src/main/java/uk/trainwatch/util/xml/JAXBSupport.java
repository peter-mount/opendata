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
package uk.trainwatch.util.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import uk.trainwatch.util.lock.Lock;
import uk.trainwatch.util.lock.WriteLock;

/**
 *
 * @author peter
 */
public class JAXBSupport
{

    private static final Logger LOG = Logger.getLogger( JAXBSupport.class.getName() );
    private final int capacity;
    private final JAXBContext context;
    private final Queue<Unmarshaller> unmarshallerQueue;
    private final Queue<Marshaller> marshallerQueue;
    private final Lock lock = new WriteLock();

    public JAXBSupport( String packages )
            throws JAXBException
    {
        this( 10, packages );
    }

    public JAXBSupport( String... packages )
            throws JAXBException
    {
        this( 10, packages );
    }

    public JAXBSupport( int capacity, String... packages )
            throws JAXBException
    {
        this( capacity, Stream.of( packages ).collect( Collectors.joining( ":" ) ) );
    }

    public JAXBSupport( int capacity, String packages )
            throws JAXBException
    {
        this( capacity, JAXBContext.newInstance( packages ) );
    }

    public JAXBSupport( Class... classes )
            throws JAXBException
    {
        this( 10, classes );
    }

    public JAXBSupport( int capacity, Class... classes )
            throws JAXBException
    {
        this( capacity, JAXBContext.newInstance( classes ) );
    }

    private JAXBSupport( int capacity, JAXBContext context )
            throws JAXBException
    {
        this.capacity = capacity;
        unmarshallerQueue = new ArrayDeque<>( capacity );
        marshallerQueue = new ArrayDeque<>( capacity );
        this.context = context;
    }

    public JAXBSupport populateUnmarshaller( int capacity )
            throws JAXBException
    {
        try( Lock l = lock.lock() ) {
            final int max = Math.max( capacity, this.capacity );
            while( unmarshallerQueue.size() < max ) {
                unmarshallerQueue.offer( context.createUnmarshaller() );
            }
        }
        return this;
    }

    public JAXBSupport populateMarshaller( int capacity )
            throws JAXBException
    {
        try( Lock l = lock.lock() ) {
            final int max = Math.max( capacity, this.capacity );
            while( marshallerQueue.size() < max ) {
                marshallerQueue.offer( context.createMarshaller() );
            }
        }
        return this;
    }

    public Unmarshaller getUnmarshaller()
            throws JAXBException
    {
        try( Lock l = lock.lock() ) {
            Unmarshaller u = unmarshallerQueue.poll();
            if( u == null ) {
                LOG.log( Level.INFO, "Creating unmarshaller" );
                u = context.createUnmarshaller();
            }
            return u;
        }
    }

    public void returnUnmarshaller( Unmarshaller u )
    {
        try( Lock l = lock.lock() ) {
            if( unmarshallerQueue.size() < capacity ) {
                unmarshallerQueue.offer( u );
            }
        }
    }

    public Marshaller getMarshaller()
            throws JAXBException
    {
        try( Lock l = lock.lock() ) {
            Marshaller u = marshallerQueue.poll();
            if( u == null ) {
                LOG.log( Level.INFO, "Creating marshaller" );
                u = context.createMarshaller();
            }
            return u;
        }
    }

    public void returnMarshaller( Marshaller u )
    {
        try( Lock l = lock.lock() ) {
            if( unmarshallerQueue.size() < capacity ) {
                marshallerQueue.offer( u );
            }
        }
    }

    public <T> T unmarshall( JAXBUnmarshaller f )
            throws JAXBException
    {
        try {
            final Unmarshaller unmarshaller = getUnmarshaller();
            try {
                return (T) f.apply( unmarshaller );
            }
            finally {
                returnUnmarshaller( unmarshaller );
            }
        }
        catch( IOException |
               InterruptedException ex ) {
            throw new JAXBException( ex );
        }
    }

    public <T> T unmarshall( String s )
            throws JAXBException
    {
        return unmarshall( m -> {
            try( Reader r = new StringReader( s ) ) {
                return m.unmarshal( r );
            }
        } );
    }

    public <T> T marshall( JAXBMarshaller f )
            throws JAXBException
    {
        try {
            final Marshaller m = getMarshaller();
            try {
                return (T) f.apply( m );
            }
            finally {
                returnMarshaller( m );
            }
        }
        catch( IOException |
               InterruptedException ex ) {
            throw new JAXBException( ex );
        }
    }

    public <T> String marshallToString( T v )
            throws JAXBException
    {
        return marshall( m -> {
            try( StringWriter s = new StringWriter() ) {
                m.marshal( v, s );
                return s.toString();
            }
        } );
    }

}
