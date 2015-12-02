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

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the new WireMessage class
 * <p>
 * @author peter
 */
public class WireMessageTest
{

    private static final String HEADER1 = "Header1";
    private static final String VALUE1 = "Value1";

    private static final String HEADER2 = "Header2";
    private static final String VALUE2 = "Value2";

    private static final String HEADER3 = "Header3";
    private static final String VALUE3 = "Value3";

    private static final String TEST_STRING = "A test string";

    private static final String TEST_LINES[] = {
        TEST_STRING,
        "Line 2",
        "Line 3",
        "Line 4"
    };

    private WireMessageBuilder build( String type )
    {
        return new WireMessageBuilder( type )
                .add( HEADER1, VALUE1 )
                .add( HEADER2, VALUE2 )
                .add( HEADER3, VALUE3 );
    }

    private WireMessage<?> test( WireMessage<?> m )
    {
        assertNotNull( m );

        assertEquals( VALUE1, m.get( HEADER1 ) );
        assertEquals( VALUE2, m.get( HEADER2 ) );
        assertEquals( VALUE3, m.get( HEADER3 ) );

        return m;
    }

    @Test
    public void empty()
            throws Exception
    {
        WireMessageRegistry.INSTANCE.register( new ByteArrayFormat()
        {

            @Override
            public String getType()
            {
                return "TestEmpty";
            }
        } );

        byte b[] = build( "TestEmpty" ).build();

        WireMessage<?> m = WireMessageRegistry.INSTANCE.readMessage( b );
        test( m );
        assertTrue( m.isContentEmpty() );
        assertNull( m.getContent() );
    }

    @Test
    public void text()
            throws Exception
    {
        WireMessageRegistry.INSTANCE.register( new StringFormat()
        {

            @Override
            public String getType()
            {
                return "TestText";
            }
        } );

        byte b[] = build( "TestText" )
                .content( TEST_STRING )
                .build();

        WireMessage<String> m = WireMessageRegistry.INSTANCE.readMessage( b );
        test( m );
        assertFalse( m.isContentEmpty() );
        assertEquals( TEST_STRING, m.getContent() );
    }

    @Test
    public void binary()
            throws Exception
    {
        WireMessageRegistry.INSTANCE.register( new ByteArrayFormat()
        {

            @Override
            public String getType()
            {
                return "TestByteArray";
            }
        } );

        final byte ary[] = new byte[5];
        ary[0] = 42;
        ary[1] = 7;
        ary[4] = 21;

        byte b[] = build( "TestByteArray" )
                .content( () -> ary )
                .build();

        WireMessage<byte[]> m = WireMessageRegistry.INSTANCE.readMessage( b );
        test( m );
        assertFalse( m.isContentEmpty() );
        assertTrue( Arrays.equals( ary, m.getContent() ) );
    }
}
