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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A simple state engine
 *
 * @author peter
 */
public interface StateEngine
{

    /**
     * Lookup a state by its name.
     *
     * @param name State name
     *
     * @return State
     *
     * @throws NullPointerException if name is not a valid state
     */
    State lookup( String name );

    /**
     * An unmodifiable collection of state names
     *
     * @return
     */
    Collection<String> getNames();

    /**
     * An unmodifiable collection of all States
     *
     * @return
     */
    Collection<State> getStates();

    /**
     * An unmodifiable collection of all initial states
     *
     * @return
     */
    Collection<State> getInitialStates();

    static interface State
    {

        /**
         * The name of this state
         *
         * @return
         */
        String getName();

        /**
         * Switch to the next state. If we cannot change then it will return this one.
         *
         * @return
         */
        State next();

        /**
         * Switch state on some failure. If there is no failure state then this returns {@link #next() }
         *
         * @return
         */
        State fail();

        /**
         * This state is executable. True if next() will return a different state.
         *
         * @return
         */
        default boolean isExecutable()
        {
            return !isTerminal();
        }

        /**
         * Is this state terminal. True if next() will return this same state,
         *
         * @return
         */
        boolean isTerminal();

        /**
         * Is this an initial state
         *
         * @return
         */
        boolean isInitial();

        StateEngine getStateEngine();
    }

    static interface StateBuilder
    {

        /**
         * Optional description returned by {@link State#toString()}. If not defined this will return the state name
         *
         * @param desc
         *
         * @return
         */
        StateBuilder desc( String desc );

        /**
         * The next state. If not defined then this will cause the state to return itself.
         *
         * @param name
         *
         * @return
         */
        StateBuilder next( String name );

        /**
         * The failure state. If not defined then this will use the next state.
         *
         * @param name
         *
         * @return
         */
        StateBuilder fail( String name );

        /**
         * Mark this state as an initial state
         *
         * @return
         */
        StateBuilder initial();

        /**
         * Mark this state as a terminal state
         *
         * @return
         */
        StateBuilder terminal();

        /**
         * Complete this state
         *
         * @return The builder
         */
        Builder build();
    }

    static interface Builder
    {

        /**
         * Add a new State
         *
         * @param name
         *
         * @return
         */
        StateBuilder add( String name );

        /**
         * Build the StateEngine
         *
         * @return
         */
        StateEngine build();
    }

    static Builder builder()
    {
        class StateImpl
                implements State
        {

            private final String name;
            private final String desc;
            private final boolean initial;
            private final boolean terminal;
            private State next, fail;
            private StateEngine engine;

            @SuppressWarnings("LeakingThisInConstructor")
            public StateImpl( String name, String desc, boolean initial, boolean terminal )
            {
                this.name = name;
                this.desc = desc;
                this.initial = initial;
                this.terminal = terminal;
            }

            @Override
            public String toString()
            {
                return desc;
            }

            @Override
            public String getName()
            {
                return name;
            }

            @Override
            public boolean isInitial()
            {
                return initial;
            }

            @Override
            public boolean isTerminal()
            {
                return terminal;
            }

            @Override
            public State next()
            {
                return next;
            }

            @Override
            public State fail()
            {
                return fail;
            }

            @Override
            public StateEngine getStateEngine()
            {
                return engine;
            }

            @Override
            public int hashCode()
            {
                return name.hashCode();
            }

            @Override
            @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
            public boolean equals( Object obj )
            {
                return obj == this;
            }
        }

        class Entry
        {

            final String name;
            String desc;
            boolean initial, terminal;
            String next;
            String fail;
            Entry nextEntry;
            Entry failEntry;
            private StateImpl state;

            public Entry( String name )
            {
                this.name = name;
            }

            public StateImpl getState()
            {
                if( state == null ) {
                    state = new StateImpl( name, desc, initial, terminal );
                }
                return state;
            }

            @Override
            public int hashCode()
            {
                return name.hashCode();
            }

            @Override
            public boolean equals( Object obj )
            {
                return obj instanceof Entry && ((Entry) obj).name.equals( name );
            }
        }

        Map<String, Entry> entries = new HashMap<>();

        return new Builder()
        {

            @Override
            public StateEngine build()
            {

                // Link and validate the states
                entries.values()
                        .stream()
                        .forEach( e -> {
                            if( e.terminal && e.nextEntry != null ) {
                                throw new IllegalStateException( e.name + " is terminal but has next " + e.next + " configured" );
                            }

                            // Map next to this entry if not defined
                            e.nextEntry = Objects.requireNonNull( e.next == null ? e : entries.get( e.next ),
                                                                  "next " + e.next + " for " + e.name + " not defined" );

                            // Map fail to nextEntry if not defined
                            e.failEntry = Objects.requireNonNull( e.fail == null ? e.nextEntry : entries.get( e.fail ),
                                                                  "fail " + e.fail + " for " + e.name + " not defined" );
                        } );

                // Pass 1 create states & the StateEngine
                Map<String, State> states = entries.values()
                        .stream()
                        .map( Entry::getState )
                        .collect( Collectors.toConcurrentMap( State::getName, Function.identity() ) );

                Collection<String> names = Collections.unmodifiableCollection( states.keySet() );
                Collection<State> allStates = Collections.unmodifiableCollection( states.values() );
                Collection<State> initialStates = Collections.unmodifiableCollection( allStates.stream()
                        .filter( State::isInitial )
                        .collect( Collectors.toList() ) );

                StateEngine engine = new StateEngine()
                {
                    @Override
                    public State lookup( String name )
                    {
                        return Objects.requireNonNull( states.get( name ) );
                    }

                    @Override
                    public Collection<String> getNames()
                    {
                        return names;
                    }

                    @Override
                    public Collection<State> getStates()
                    {
                        return allStates;
                    }

                    @Override
                    public Collection<State> getInitialStates()
                    {
                        return initialStates;
                    }

                };

                // Pass 2 link the states together
                entries.values()
                        .forEach( e -> {
                            StateImpl s = e.getState();
                            s.engine = engine;
                            s.next = e.nextEntry.getState();
                            s.fail = e.failEntry.getState();
                        } );

                return engine;
            }

            @Override
            public StateBuilder add( String name )
            {
                if( entries.containsKey( name ) ) {
                    throw new IllegalArgumentException( "State " + name + " already defined" );
                }
                Builder builder = this;
                Entry entry = entries.computeIfAbsent( name, Entry::new );
                return new StateBuilder()
                {

                    @Override
                    public StateBuilder desc( String desc )
                    {
                        entry.desc = desc;
                        return this;
                    }

                    @Override
                    public StateBuilder next( String name )
                    {
                        entry.next = name;
                        return this;
                    }

                    @Override
                    public StateBuilder fail( String name )
                    {
                        entry.fail = name;
                        return this;
                    }

                    @Override
                    public StateBuilder initial()
                    {
                        entry.initial = true;
                        return this;
                    }

                    @Override
                    public StateBuilder terminal()
                    {
                        entry.terminal = true;
                        return this;
                    }

                    @Override
                    public Builder build()
                    {
                        return builder;
                    }
                };
            }

        };
    }
}
