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

import java.util.Collection;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class StateEngineTest
{

    private StateEngine engine;

    @Before
    public void setUp()
    {
        engine = StateEngine.builder()
                .add( "INIT" )
                .initial()
                .next( "NEXT" )
                .fail( "FAIL" )
                .build()
                //
                .add( "NEXT" )
                .terminal()
                .build()
                //
                .add( "FAIL" )
                .next( "NEXT" )
                .build()
                //
                .build();
    }

    @Test
    public void initial()
    {
        Collection<StateEngine.State> initial = engine.getInitialStates();

        Iterator<StateEngine.State> it = initial.iterator();
        assertTrue( it.hasNext() );

        StateEngine.State state = it.next();
        assertEquals( "INIT", state.getName() );

        assertFalse( it.hasNext() );
    }

    @Test
    public void next()
    {
        StateEngine.State state = engine.lookup( "INIT" );

        state = state.next();
        assertEquals( "NEXT", state.getName() );
        assertTrue( state.isTerminal() );

        StateEngine.State state2 = state.next();
        assertSame( state, state2 );
    }

    @Test
    public void fail()
    {
        StateEngine.State state = engine.lookup( "INIT" );

        state = state.fail();
        assertEquals( "FAIL", state.getName() );
        assertFalse( state.isTerminal() );

        state = state.next();
        assertEquals( "NEXT", state.getName() );
        assertTrue( state.isTerminal() );
    }

}
