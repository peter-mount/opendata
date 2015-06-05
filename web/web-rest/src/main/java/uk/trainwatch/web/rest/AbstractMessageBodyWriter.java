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
package uk.trainwatch.web.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import uk.trainwatch.util.TimeUtils;

/**
 * Base implementation of {@link MessageBodyWriter} which will process our custom annotations before passing on to the writer
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractMessageBodyWriter
        implements MessageBodyWriter
{

    @Override
    public long getSize( Object obj, Class type, Type genericType,
                         Annotation[] annotations, MediaType mediaType )
    {
        return -1;
    }

    @Override
    public boolean isWriteable( Class type, Type genericType,
                                Annotation annotations[], MediaType mediaType )
    {
        return true;
    }

    private void handleCache( Cache c, MultivaluedMap httpHeaders )
    {
        Duration d = Duration.of( c.maxAge(), c.unit() );

        long age = d.getSeconds();
        httpHeaders.add( "Cache-Control", "public, max-age=" + age + ", s-maxage=" + age + ", no-transform" );

        long expires = c.expires();
        if( expires > 0 ) {
            LocalDateTime dt = LocalDateTime.now( TimeUtils.UTC ).plus( Duration.of( expires, c.unit() ) );
            httpHeaders.add( "Expires", TimeUtils.toDateHeader( dt ) );
        }
    }

    protected final void handleAnnotations( Object target, Class type, Type genericType,
                                            Annotation[] annotations, MediaType mediaType,
                                            MultivaluedMap httpHeaders, OutputStream outputStream )
            throws IOException
    {
        for( Annotation a: annotations ) {
            if( a.annotationType().equals( Cache.class ) ) {
                handleCache( (Cache) a, httpHeaders );
            }
        }
    }

    @Override
    public final void writeTo( Object t, Class type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap mm, OutputStream out )
            throws IOException,
                   WebApplicationException
    {
        handleAnnotations( t, type, type1, antns, mt, mm, out );
        writeBodyTo( t, type, type1, antns, mt, mm, out );
    }

    protected abstract void writeBodyTo( Object t, Class type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap mm, OutputStream out )
            throws IOException;

}
