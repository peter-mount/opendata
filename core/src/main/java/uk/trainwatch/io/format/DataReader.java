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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import uk.trainwatch.io.IOConsumer;
import uk.trainwatch.io.IOFunction;
import uk.trainwatch.io.IOSupplier;

/**
 *
 * @author peter
 */
public class DataReader
        implements Closeable
{

    private final DataInputStream is;

    public static void read( byte[] b, IOConsumer<DataReader> c )
            throws IOException
    {
        try( ByteArrayInputStream is = new ByteArrayInputStream( b ) ) {
            read( is, c );
        }
    }

    public static void read( InputStream is, IOConsumer<DataReader> c )
            throws IOException
    {
        try( DataReader r = new DataReader( is ) ) {
            c.accept( r );
        }
    }

    public DataReader( byte[] b )
    {
        this( new ByteArrayInputStream( b ) );
    }

    public DataReader( InputStream is )
    {
        this.is = is instanceof DataInputStream ? (DataInputStream) is : new DataInputStream( is );
    }

    /**
     * Reads a string written with {@link #writeString(java.io.DataOutputStream, java.lang.String)}
     * <p>
     * @return
     * @throws IOException
     */
    public String readString()
            throws IOException
    {
        int s = is.readInt();
        if( s < 0 ) {
            return null;
        }
        char c[] = new char[s];
        for( int i = 0; i < s; i++ ) {
            c[i] = is.readChar();
        }
        return new String( c );
    }

    public <K, V> Map<K, V> readMap( IOSupplier<K> keyReader, IOSupplier<V> valueReader )
            throws IOException
    {
        return readMap( r -> keyReader.get(), r -> valueReader.get() );
    }

    public <K, V> Map<K, V> readMap( Supplier<Map<K, V>> supplier, IOSupplier<K> keyReader, IOSupplier<V> valueReader )
            throws IOException
    {
        return readMap( supplier, r -> keyReader.get(), r -> valueReader.get() );
    }

    public <K, V> Map<K, V> readMap( IOFunction<DataReader, K> keyReader, IOFunction<DataReader, V> valueReader )
            throws IOException
    {
        return readMap( HashMap::new, keyReader, valueReader );
    }

    public <K, V> Map<K, V> readMap( Supplier<Map<K, V>> supplier, IOFunction<DataReader, K> keyReader, IOFunction<DataReader, V> valueReader )
            throws IOException
    {
        int s = is.readInt();
        if( s == -1 ) {
            return null;
        }

        final Map<K, V> m = supplier.get();
        while( s > 0 ) {
            s--;
            m.put( keyReader.apply( this ), valueReader.apply( this ) );
        }
        return m;
    }

    public <V> List<V> readList( IOSupplier<V> entryReader )
            throws IOException
    {
        return readList( r -> entryReader.get() );
    }

    public <V> List<V> readList( IOFunction<DataReader, V> entryReader )
            throws IOException
    {
        return readCollection( ArrayList::new, entryReader );
    }

    public <V> Set<V> readSet( IOSupplier<V> entryReader )
            throws IOException
    {
        return readSet( r -> entryReader.get() );
    }

    public <V> Set<V> readSet( IOFunction<DataReader, V> entryReader )
            throws IOException
    {
        return readCollection( HashSet::new, entryReader );
    }

    public <V, C extends Collection<V>> C readCollection( Supplier<C> supplier, IOSupplier<V> entryReader )
            throws IOException
    {
        return readCollection( supplier, r -> entryReader.get() );
    }

    public <V, C extends Collection<V>> C readCollection( Supplier<C> supplier, IOFunction<DataReader, V> entryReader )
            throws IOException
    {
        int s = is.readInt();
        if( s == -1 ) {
            return null;
        }

        final C c = supplier.get();
        while( s > 0 ) {
            s--;
            c.add( entryReader.apply( this ) );
        }
        return c;
    }

    public byte[] readBytes()
            throws IOException
    {
        int s = is.readInt();
        if( s == -1 ) {
            return null;
        }

        byte b[] = new byte[s];
        is.read( b, 0, s );
        return b;
    }

    //<editor-fold defaultstate="collapsed" desc="Delegate to DataInputStream">
    public final int read( byte[] b )
            throws IOException
    {
        return is.read( b );
    }

    public final int read( byte[] b, int off, int len )
            throws IOException
    {
        return is.read( b, off, len );
    }

    public final void readFully( byte[] b )
            throws IOException
    {
        is.readFully( b );
    }

    public final void readFully( byte[] b, int off, int len )
            throws IOException
    {
        is.readFully( b, off, len );
    }

    public final int skipBytes( int n )
            throws IOException
    {
        return is.skipBytes( n );
    }

    public final boolean readBoolean()
            throws IOException
    {
        return is.readBoolean();
    }

    public final byte readByte()
            throws IOException
    {
        return is.readByte();
    }

    public final int readUnsignedByte()
            throws IOException
    {
        return is.readUnsignedByte();
    }

    public final short readShort()
            throws IOException
    {
        return is.readShort();
    }

    public final int readUnsignedShort()
            throws IOException
    {
        return is.readUnsignedShort();
    }

    public final char readChar()
            throws IOException
    {
        return is.readChar();
    }

    public final int readInt()
            throws IOException
    {
        return is.readInt();
    }

    public final long readLong()
            throws IOException
    {
        return is.readLong();
    }

    public final float readFloat()
            throws IOException
    {
        return is.readFloat();
    }

    public final double readDouble()
            throws IOException
    {
        return is.readDouble();
    }

    public final String readUTF()
            throws IOException
    {
        return is.readUTF();
    }

    public static final String readUTF( DataInput in )
            throws IOException
    {
        return DataInputStream.readUTF( in );
    }

    public int read()
            throws IOException
    {
        return is.read();
    }

    public long skip( long n )
            throws IOException
    {
        return is.skip( n );
    }

    public int available()
            throws IOException
    {
        return is.available();
    }

    public void close()
            throws IOException
    {
        is.close();
    }

    public synchronized void mark( int readlimit )
    {
        is.mark( readlimit );
    }

    public synchronized void reset()
            throws IOException
    {
        is.reset();
    }

    public boolean markSupported()
    {
        return is.markSupported();
    }
    //</editor-fold>

}
