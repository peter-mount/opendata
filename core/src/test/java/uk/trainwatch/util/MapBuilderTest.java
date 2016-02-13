/*
 * Copyright 2016 peter.
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

import java.util.Map;
import java.util.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class MapBuilderTest
{

    @Test
    public void add()
    {
        Map<String, Boolean> m = MapBuilder.<String, Boolean>builder()
                .add( "true", true )
                .add( "false", false )
                .build();
        assertNotNull( m );
        assertFalse( m.isEmpty() );
        assertTrue( m.get( "true" ) );
        assertFalse( m.get( "false" ) );
    }

    @Test
    public void addAll()
    {
        Map<String, Boolean> m = MapBuilder.<String, Boolean>builder()
                .addAll( String::valueOf, true, false )
                .build();
        assertNotNull( m );
        assertFalse( m.isEmpty() );
        assertTrue( m.get( "true" ) );
        assertFalse( m.get( "false" ) );
    }

    @Test
    public void enumLookupMap()
    {
        Map<String, T> m = MapBuilder.<T>enumLookupMap( T.class );
        assertNotNull( m );
        assertFalse( m.isEmpty() );
        assertEquals( T.TRUE, m.get( "TRUE" ) );
        assertEquals( T.FALSE, m.get( "FALSE" ) );
        assertNull( m.get( "true" ) );
        assertNull( m.get( "false" ) );
    }

    @Test
    public void enumLookupMap2()
    {
        Map<String, T> m = MapBuilder.<T>enumLookupMap( T::name, T.class );
        assertNotNull( m );
        assertFalse( m.isEmpty() );
        assertEquals( T.TRUE, m.get( "TRUE" ) );
        assertEquals( T.FALSE, m.get( "FALSE" ) );
        assertNull( m.get( "true" ) );
        assertNull( m.get( "false" ) );
    }

    @Test
    public void enumLookupMap3()
    {
        Map<String, T> m = MapBuilder.<T>enumLookupMap( Enum::name, String::toUpperCase, T.class );
        assertNotNull( m );
        assertFalse( m.isEmpty() );
        assertEquals( T.TRUE, m.get( "TRUE" ) );
        assertEquals( T.FALSE, m.get( "FALSE" ) );
        assertEquals( T.TRUE, m.get( "true" ) );
        assertEquals( T.FALSE, m.get( "false" ) );
    }

    static enum T
    {
        TRUE,
        FALSE
    };
}
