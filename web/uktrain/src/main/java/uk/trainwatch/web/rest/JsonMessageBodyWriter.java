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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Custom {@link MessageBodyWriter} which generates proper json output, specifically lists are not wrapped in another
 * object.
 * <p>
 * Src: <a
 * href="http://stackoverflow.com/questions/2199453/how-can-i-customize-serialization-of-a-list-of-jaxb-objects-to-json#3143214">StackOverflow</a>
 * <p>
 * @author Peter T Mount
 */
@Provider
@Produces( "application/json" )
public class JsonMessageBodyWriter
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

    @Override
    public void writeTo( Object target, Class type, Type genericType,
                         Annotation[] annotations, MediaType mediaType,
                         MultivaluedMap httpHeaders, OutputStream outputStream )
            throws IOException
    {
        new ObjectMapper().
                // Comment INDENT_OUTPUT on public site, this is used during development only
                configure( SerializationFeature.INDENT_OUTPUT, true ).
                writeValue( outputStream, target );
    }
}
