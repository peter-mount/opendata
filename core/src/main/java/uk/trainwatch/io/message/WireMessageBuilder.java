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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import uk.trainwatch.io.IOBiConsumer;
import uk.trainwatch.io.format.DataWriter;

/**
 *
 * @author peter
 */
public class WireMessageBuilder<T>
{

    private final String type;
    private final IOBiConsumer<DataWriter, T> writer;
    private Map<String, String> headers;
    private Supplier<T> content;

    public WireMessageBuilder( String type )
    {
        this.type = type;
        WireMessageFormat fmt = WireMessageRegistry.INSTANCE.get( type );
        if( fmt == null ) {
            throw new IllegalArgumentException( "Message type " + type + " is unsupported" );
        }
        writer = fmt.writer();
    }

    /**
     * Build the binary version of the message.
     * <p>
     * At this point will the content (if any) be consumed via the suppliers used.
     * <p>
     * @return
     */
    public byte[] build()
    {
        try {
            return DataWriter.write( ( oos ) -> {
                oos.writeString( type );
                oos.writeMap( headers, DataWriter::writeString, DataWriter::writeString );

                T body = content == null ? null : content.get();
                oos.writeBoolean( body != null );
                if( body != null ) {
                    writer.accept( oos, body );
                }
            } );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    /**
     * Add a key/value to the header. If the key already exists then this will be replaced
     * <p>
     * @param k Key
     * @param v Value
     * <p>
     * @return builder
     */
    public WireMessageBuilder add( String k, String v )
    {
        if( headers == null ) {
            headers = new LinkedHashMap<>();
        }
        headers.put( k, v );
        return this;
    }

    public WireMessageBuilder content( Supplier<T> content )
    {
        this.content = content;
        return this;
    }

    /**
     * Set the raw body content
     * <p>
     * @param content content or null
     * <p>
     * @return builder
     */
    public WireMessageBuilder content( T content )
    {
        return content( () -> content );
    }

}
