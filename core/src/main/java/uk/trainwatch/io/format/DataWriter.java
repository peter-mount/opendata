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
package uk.trainwatch.io.format;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import uk.trainwatch.io.IOBiConsumer;
import uk.trainwatch.io.IOConsumer;

/**
 *
 * @author peter
 */
public class DataWriter
        implements Closeable
{

    private final DataOutputStream os;

    public static byte[] write( IOConsumer<DataWriter> c )
            throws IOException
    {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            try( DataWriter w = new DataWriter( baos ) ) {
                c.accept( w );
                w.flush();
            }
            return baos.toByteArray();
        }
    }

    public DataWriter( OutputStream os )
    {
        if( os instanceof DataOutputStream ) {
            this.os = (DataOutputStream) os;
        }
        else {
            this.os = new DataOutputStream( os );
        }
    }

    /**
     * Write a string in raw char format.
     * <p>
     * Unlikect {@link DataOutputStream#writeChars(java.lang.String)} this writes the string length first as an integer.
     * If the string is null (which writeChars does not support) this writes -1 as the length.
     * <p>
     * The number of bytes written will be 4 + (2*s.length) unless null then 4 bytes will be written.
     * <p>
     * @param s
     *          <p>
     * @throws IOException
     */
    public void writeString( String s )
            throws IOException
    {
        os.writeInt( s == null ? -1 : s.length() );
        if( s != null ) {
            os.writeChars( s );
        }
    }

    public <V> void writeCollection( Collection<V> c, IOConsumer<V> entryWriter )
            throws IOException
    {
        writeCollection( c, ( w, v ) -> entryWriter.accept( v ) );
    }

    public <V> void writeCollection( Collection<V> c, IOBiConsumer<DataWriter, V> entryWriter )
            throws IOException
    {
        os.writeInt( c == null ? -1 : c.size() );
        if( c != null ) {
            c.forEach( v -> {
                try {
                    entryWriter.accept( this, v );
                }
                catch( IOException ex ) {
                    throw new UncheckedIOException( ex );
                }
            } );
        }
    }

    public <K, V> void writeMap( Map<K, V> m, IOConsumer<K> keyWriter, IOConsumer<V> valueWriter )
            throws IOException
    {
        writeMap( m, ( w, k ) -> keyWriter.accept( k ), ( w, v ) -> valueWriter.accept( v ) );
    }

    public <K, V> void writeMap( Map<K, V> m, IOBiConsumer<DataWriter, K> keyWriter, IOBiConsumer<DataWriter, V> valueWriter )
            throws IOException
    {
        os.writeInt( m == null ? -1 : m.size() );
        if( m != null ) {
            m.forEach( ( k, v ) -> {
                try {
                    keyWriter.accept( this, k );
                    valueWriter.accept( this, v );
                }
                catch( IOException ex ) {
                    throw new UncheckedIOException( ex );
                }
            } );
        }
    }

    public void writeBytes( Supplier<byte[]> s )
            throws IOException
    {
        writeBytes( s.get() );
    }

    public void writeBytes( byte[] b )
            throws IOException
    {
        os.writeInt( b == null ? -1 : b.length );
        if( b != null ) {
            os.write( b, 0, b.length );
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Delegate to DataOutputStream">
    public synchronized void write( int b )
            throws IOException
    {
        os.write( b );
    }

    public synchronized void write( byte[] b, int off, int len )
            throws IOException
    {
        os.write( b, off, len );
    }

    public void flush()
            throws IOException
    {
        os.flush();
    }

    public final void writeBoolean( boolean v )
            throws IOException
    {
        os.writeBoolean( v );
    }

    public final void writeByte( int v )
            throws IOException
    {
        os.writeByte( v );
    }

    public final void writeShort( int v )
            throws IOException
    {
        os.writeShort( v );
    }

    public final void writeChar( int v )
            throws IOException
    {
        os.writeChar( v );
    }

    public final void writeInt( int v )
            throws IOException
    {
        os.writeInt( v );
    }

    public final void writeLong( long v )
            throws IOException
    {
        os.writeLong( v );
    }

    public final void writeFloat( float v )
            throws IOException
    {
        os.writeFloat( v );
    }

    public final void writeDouble( double v )
            throws IOException
    {
        os.writeDouble( v );
    }

    public final void writeBytes( String s )
            throws IOException
    {
        os.writeBytes( s );
    }

    public final void writeChars( String s )
            throws IOException
    {
        os.writeChars( s );
    }

    public final void writeUTF( String str )
            throws IOException
    {
        os.writeUTF( str );
    }

    public final int size()
    {
        return os.size();
    }

    public void write( byte[] b )
            throws IOException
    {
        os.write( b );
    }

    public void close()
            throws IOException
    {
        os.close();
    }
    //</editor-fold>

}
