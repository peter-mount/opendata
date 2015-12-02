/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.io.message;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import uk.trainwatch.io.format.DataReader;
import uk.trainwatch.io.format.DataWriter;
import uk.trainwatch.io.IOConsumer;
import uk.trainwatch.io.IOFunction;

/**
 * A simple message suitable for sending over RabbitMQ.
 * <p>
 * <table>
 * <tr><th>Byte</th><th>Length</th><th>Content</th></tr>
 * <tr><td>0</td><td></td><td>Message type (UTF8)</td></tr>
 * <tr><td>type.length</td><td>4</td><td>Number of entries in the header</td></tr>
 * <tr><td>type.length+4</td><td>8</td><td>Size of the body</td></tr>
 * <tr><td>type.length+12</td><td></td><td>Start of header</td></tr>
 * <tr><td>x</td><td>length @ 4</td><td>Body</td></tr>
 * </table>
 * <p>
 * Each header consists of two UTF8 strings, the key followed by the value.
 * <p>
 * @param <T>
 *            <p>
 * @paeram T type of content
 * @author peter
 */
public class WireMessage<T>
{

    private final String type;
    private final Map<String, String> headers;
    private final T content;

    WireMessage( String type, DataReader r, IOFunction<DataReader, T> f )
            throws IOException
    {
        this.type = type;
        headers = r.readMap( r::readString, r::readString );
        content = r.readBoolean() ? f.apply( r ) : null;
    }

    public String getType()
    {
        return type;
    }

    public T getContent()
    {
        return content;
    }

    public boolean isContentEmpty()
    {
        return content == null;
    }

    public int headerSize()
    {
        return headers == null ? 0 : headers.size();
    }

    public boolean isHeadersEmpty()
    {
        return headers == null || headers.isEmpty();
    }

    public String get( String key )
    {
        return headers == null ? null : headers.get( key );
    }

    public Set<String> keySet()
    {
        return headers == null ? Collections.emptySet() : headers.keySet();
    }

    public Set<Map.Entry<String, String>> entrySet()
    {
        return headers == null ? Collections.emptySet() : headers.entrySet();
    }

    public String getOrDefault( Object key, String defaultValue )
    {
        return headers == null ? defaultValue : headers.getOrDefault( key, defaultValue );
    }

    public void forEach( BiConsumer<? super String, ? super String> action )
    {
        if( headers != null ) {
            headers.forEach( action );
        }
    }


}
