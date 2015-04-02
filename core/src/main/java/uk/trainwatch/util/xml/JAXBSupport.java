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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author peter
 */
public class JAXBSupport
{

    private final int capacity;
    private final JAXBContext context;

    private final BlockingQueue<Unmarshaller> unmarshallerQueue;

    public JAXBSupport( String packages ) throws JAXBException
    {
        this( 10, packages );
    }

    public JAXBSupport( String... packages ) throws JAXBException
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
        this.capacity = capacity;
        unmarshallerQueue = new LinkedBlockingDeque<>( capacity );
        this.context = JAXBContext.newInstance( packages );
    }

    public JAXBSupport populateUnmarshaller( int capacity )
            throws JAXBException
    {
        final int max = Math.max( capacity, this.capacity );
        synchronized( context )
        {
            while( unmarshallerQueue.size() < max )
            {
                if( !unmarshallerQueue.offer( context.createUnmarshaller() ) )
                {
                    break;
                }
            }
        }
        return this;
    }

    public Unmarshaller getUnmarshaller()
            throws JAXBException
    {
        Unmarshaller u;
        try
        {
            u = unmarshallerQueue.poll( 1, TimeUnit.SECONDS );
        } catch( InterruptedException ex )
        {
            u = null;
        }
        if( u == null )
        {
            synchronized( context )
            {
                u = context.createUnmarshaller();
            }
        }
        return u;
    }

    public void returnUnmarshaller( Unmarshaller u )
    {
        unmarshallerQueue.offer( u );
    }

    public <T, S> T unmarshall( JAXBUnmarshaller f )
            throws JAXBException
    {
        try
        {
            final Unmarshaller unmarshaller = getUnmarshaller();
            try
            {
                return (T) f.apply( unmarshaller );
            } finally
            {
                returnUnmarshaller( unmarshaller );
            }
        } catch( IOException | InterruptedException ex )
        {
            throw new JAXBException( ex );
        }
    }

    public <T> T unmarshall( String s ) throws JAXBException
    {
        return unmarshall( m ->
        {
            try( Reader r = new StringReader( s ) )
            {
                return m.unmarshal( r );
            }
        } );
    }
}
