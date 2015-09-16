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
package uk.trainwatch.util;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class WriterConsumerTest
{

    private static final String LINE1 = "Line 1";
    private static final String LINE2 = "Line 2";
    private AtomicBoolean a;
    private AtomicBoolean b;
    private AtomicInteger l;
    private Consumer<String> c;

    @Before
    public void before()
    {
        a = new AtomicBoolean( false );
        b = new AtomicBoolean( false );
        l = new AtomicInteger();
        c = s -> {
            System.out.printf( "Line: %2d \"%s\"\n", l.incrementAndGet(), s );

            if( LINE1.equals( s ) ) {
                assertFalse( "Already received first line", a.get() );
                a.set( true );
            }

            if( LINE2.equals( s ) ) {
                assertFalse( "Already received second line", b.get() );
                b.set( true );
            }
        };

    }

    @After
    public void after()
    {
        b = null;
        c = null;
    }

    /**
     * Raw write
     * <p>
     * @throws IOException
     */
    @Test
    public void write_String()
            throws IOException
    {
        try( Writer w = new WriterConsumer( c ) ) {
            w.write( LINE1 );
            w.write( '\n' );
            w.write( LINE2 );
        }

        assertTrue( "Missing line 1", a.get() );
        assertTrue( "Missing line 2", b.get() );
    }

    /**
     * Test println() splits correctly
     * <p>
     * @throws IOException
     */
    @Test
    public void println()
            throws IOException
    {
        try( WriterConsumer w = new WriterConsumer( c ) ) {
            w.println( LINE1 );
            w.println( LINE2 );
        }

        assertTrue( "Missing line 1", a.get() );
        assertTrue( "Missing line 2", b.get() );
    }

    /**
     * Test println with embedded \n works
     * <p>
     * @throws IOException
     */
    @Test
    public void println2()
            throws IOException
    {
        try( WriterConsumer w = new WriterConsumer( c ) ) {
            w.println( LINE1 + "\n" + LINE2 + "\n" );
        }

        assertTrue( "Missing line 1", a.get() );
        assertTrue( "Missing line 2", b.get() );
    }

}
