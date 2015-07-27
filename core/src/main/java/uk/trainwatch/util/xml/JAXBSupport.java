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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Node;

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
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

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

        for( int i = 0; i < capacity; i++ )
        {
            unmarshallerQueue.offer( context.createUnmarshaller() );
            marshallerQueue.offer( context.createMarshaller() );
        }
    }

    private <T> T poll( Queue<T> queue )
    {
        lock.lock();
        try
        {
            T u = queue.poll();
            for( int i = 0; u == null && i < 10; i++ )
            {
                condition.await( 1, TimeUnit.SECONDS );
                u = queue.poll();
            }
            return u;
        } catch( InterruptedException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
        finally
        {
            lock.unlock();
        }
    }

    private <T> void offer( Queue<T> queue, T u )
    {
        lock.lock();
        try
        {
            if( queue.size() < capacity )
            {
                queue.offer( u );
                condition.signalAll();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    @FunctionalInterface
    private static interface JAXBFunction<T>
    {

        T apply( JAXBContext ctx )
                throws JAXBException;
    }

    private <T> T create( JAXBFunction< T> f )
            throws JAXBException
    {
        lock.lock();
        try
        {
            LOG.log( Level.INFO, "Creating marshaller" );
            return f.apply( context );
        }
        finally
        {
            lock.unlock();
        }
    }

    public Unmarshaller getUnmarshaller()
            throws JAXBException
    {
        Unmarshaller u = poll( unmarshallerQueue );
        if( u == null )
        {
            u = create( JAXBContext::createUnmarshaller );
        }
        return u;
    }

    public void returnUnmarshaller( Unmarshaller u )
    {
        offer( unmarshallerQueue, u );
    }

    public Marshaller getMarshaller()
            throws JAXBException
    {
        Marshaller u = poll( marshallerQueue );
        if( u == null )
        {
            u = create( JAXBContext::createMarshaller );
        }
        return u;
    }

    public void returnMarshaller( Marshaller u )
    {
        offer( marshallerQueue, u );
    }

    public <T> T unmarshall( JAXBUnmarshaller f )
            throws JAXBException
    {
        try
        {
            final Unmarshaller unmarshaller = getUnmarshaller();
            try
            {
                return (T) f.apply( unmarshaller );
            }
            finally
            {
                returnUnmarshaller( unmarshaller );
            }
        } catch( IOException |
                 InterruptedException ex )
        {
            throw new JAXBException( ex );
        }
    }

    public <T> T unmarshall( Node n )
            throws JAXBException
    {
        return unmarshall( m -> m.unmarshal( n ) );
    }

    public <T> T unmarshall( String s )
            throws JAXBException
    {
        return unmarshall( m ->
        {
            try( Reader r = new StringReader( s ) )
            {
                return m.unmarshal( r );
            }
        } );
    }

    public <T> T unmarshall( File f )
            throws JAXBException
    {
        return unmarshall( m -> m.unmarshal( f ) );
    }

    public <T> T unmarshall( Reader r )
            throws JAXBException
    {
        return unmarshall( m -> m.unmarshal( r ) );
    }

    public <T> T unmarshall( InputStream is )
            throws JAXBException
    {
        return unmarshall( m -> m.unmarshal( is ) );
    }

    public void marshall( JAXBMarshaller f )
            throws JAXBException
    {
        try
        {
            final Marshaller m = getMarshaller();
            try
            {
                f.accept( m );
            }
            finally
            {
                returnMarshaller( m );
            }
        } catch( IOException |
                 InterruptedException ex )
        {
            throw new JAXBException( ex );
        }
    }

    public <T> String marshallToString( T v )
            throws JAXBException
    {
        try( StringWriter s = new StringWriter() )
        {
            marshall( v, s );
            return s.toString();
        } catch( IOException ex )
        {
            throw new JAXBException( ex );
        }
    }

    public void marshall( Object v, Node n )
            throws JAXBException
    {
        marshall( m -> m.marshal( v, n ) );
    }

    public void marshall( Object v, File f )
            throws JAXBException
    {
        marshall( m -> m.marshal( v, f ) );
    }

    public void marshall( Object v, Writer w )
            throws JAXBException
    {
        marshall( m -> m.marshal( v, w ) );
    }

    public void marshall( Object v, OutputStream os )
            throws JAXBException
    {
        marshall( m -> m.marshal( v, os ) );
    }

}
