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
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A simple state engine
 *
 * @author peter
 * @param <A> Type used in actions
 */
public interface StateEngine<A>
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
    State<A> lookup( String name );

    /**
     * An unmodifiable collection of state names.
     * <p>
     * The order is not guaranteed to be that of the states when they were created.
     *
     * @return
     */
    Collection<String> getNames();

    /**
     * An unmodifiable collection of all States.
     * <p>
     * The order is not guaranteed to be that of the states when they were created.
     *
     * @return
     */
    Collection<State<A>> getStates();

    /**
     * An unmodifiable collection of all initial states.
     * <p>
     * The order is not guaranteed to be that of the states when they were created.
     *
     * @return
     */
    Collection<State<A>> getInitialStates();

    /**
     * The initial state.
     * <p>
     * When there are more than one initial state this will return the first returned by {@link #getInitialStates()} but this may not necessarily be the first
     * initial state defined.
     *
     * @return initial state
     *
     * @throws IllegalStateException if there are no initial states
     */
    default State<A> initial()
    {
        Collection<State<A>> s = getInitialStates();
        if( s != null && !s.isEmpty() ) {
            Iterator<State<A>> it = s.iterator();
            if( it.hasNext() ) {
                return it.next();
            }
        }
        throw new IllegalStateException( "No initial states" );
    }

    /**
     * An individual state within the state engine
     *
     * @param <A> Type used in actions
     */
    static interface State<A>
            extends Function<A, State>
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
        State<A> next();

        /**
         * Switch state on some failure. If there is no failure state then this returns {@link #next() }
         *
         * @return
         */
        State<A> fail();

        /**
         * This state is executable. True if next() will return a different state.
         *
         * @return
         */
        boolean isExecutable();

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

        /**
         * The state engine this state belongs to
         *
         * @return
         */
        StateEngine<A> getStateEngine();
    }

    /**
     * A builder of each state
     *
     * @param <A> Type used in actions
     */
    static interface StateBuilder<A>
    {

        /**
         * Optional description returned by {@link State#toString()}. If not defined this will return the state name
         *
         * @param desc
         *
         * @return
         */
        StateBuilder<A> desc( String desc );

        /**
         * The next state. If not defined then this will cause the state to return itself.
         *
         * @param name
         *
         * @return
         */
        StateBuilder<A> next( String name );

        /**
         * The failure state. If not defined then this will use the next state.
         *
         * @param name
         *
         * @return
         */
        StateBuilder<A> fail( String name );

        /**
         * Mark this state as an initial state
         *
         * @return
         */
        StateBuilder<A> initial();

        /**
         * Mark this state as a terminal state
         *
         * @return
         */
        StateBuilder<A> terminal();

        /**
         * Mark this state as executable.
         *
         * @return
         */
        StateBuilder<A> executable();

        /**
         * Adds an action to this state
         *
         * @param action function that accepts an argument and the current state. Returns the new state or null to remain unchanged.
         *
         * @return
         */
        StateBuilder<A> action( BiFunction<A, State<A>, State<A>> action );

        /**
         * Complete this state
         *
         * @return The builder
         */
        Builder<A> build();
    }

    /**
     * A builder of a state engine
     *
     * @param <A> Type used in actions
     */
    static interface Builder<A>
    {

        /**
         * Add a new State
         *
         * @param name
         *
         * @return
         */
        StateBuilder<A> add( String name );

        /**
         * Build the StateEngine
         *
         * @return
         */
        StateEngine<A> build();
    }

    /**
     * Return a new StateEngine Builder instance
     *
     * @param <A> Type used in actions
     *
     * @return builder instance
     */
    static <A> Builder<A> builder()
    {
        class StateImpl
                implements State<A>
        {

            private final String name;
            private final String desc;
            private final boolean initial;
            private final boolean terminal;
            private final boolean executable;
            private final BiFunction<A, State<A>, State<A>> action;
            private State next, fail;
            private StateEngine engine;

            public StateImpl( String name, String desc, boolean initial, boolean terminal, boolean executable, BiFunction<A, State<A>, State<A>> action )
            {
                this.name = name;
                this.desc = desc;
                this.initial = initial;
                this.terminal = terminal;
                this.executable = executable;
                this.action = action;
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
            public boolean isExecutable()
            {
                return executable;
            }

            @Override
            public State<A> next()
            {
                return next;
            }

            @Override
            public State<A> fail()
            {
                return fail;
            }

            @Override
            public StateEngine<A> getStateEngine()
            {
                return engine;
            }

            @Override
            public State<A> apply( A t )
            {
                if( action == null ) {
                    return this;
                }
                State s = action.apply( t, this );
                return s == null ? this : s;
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
            boolean initial, terminal, executable;
            String next;
            String fail;
            Entry nextEntry;
            Entry failEntry;
            private StateImpl state;
            BiFunction<A, State<A>, State<A>> action;

            public Entry( String name )
            {
                this.name = name;
            }

            public StateImpl getState()
            {
                if( state == null ) {
                    state = new StateImpl( name, desc, initial, terminal, executable, action );
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
                Map<String, State<A>> states = entries.values()
                        .stream()
                        .map( Entry::getState )
                        .collect( Collectors.toConcurrentMap( State::getName, Function.identity() ) );

                Collection<String> names = Collections.unmodifiableCollection( states.keySet() );
                Collection<State<A>> allStates = Collections.unmodifiableCollection( states.values() );
                Collection<State<A>> initialStates = Collections.unmodifiableCollection( allStates.stream()
                        .filter( State::isInitial )
                        .collect( Collectors.toList() ) );

                StateEngine<A> engine = new StateEngine<A>()
                {
                    @Override
                    public State<A> lookup( String name )
                    {
                        return Objects.requireNonNull( states.get( name ) );
                    }

                    @Override
                    public Collection<String> getNames()
                    {
                        return names;
                    }

                    @Override
                    public Collection<State<A>> getStates()
                    {
                        return allStates;
                    }

                    @Override
                    public Collection<State<A>> getInitialStates()
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
            public StateBuilder<A> add( String name )
            {
                if( entries.containsKey( name ) ) {
                    throw new IllegalArgumentException( "State " + name + " already defined" );
                }
                Builder builder = this;
                Entry entry = entries.computeIfAbsent( name, Entry::new );
                return new StateBuilder<A>()
                {

                    @Override
                    public StateBuilder<A> desc( String desc )
                    {
                        entry.desc = desc;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> next( String name )
                    {
                        entry.next = name;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> fail( String name )
                    {
                        entry.fail = name;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> initial()
                    {
                        entry.initial = true;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> terminal()
                    {
                        entry.terminal = true;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> executable()
                    {
                        entry.executable = true;
                        return this;
                    }

                    @Override
                    public StateBuilder<A> action( BiFunction<A, State<A>, State<A>> action )
                    {
                        entry.action = action;
                        return this;
                    }

                    @Override
                    public Builder<A> build()
                    {
                        return builder;
                    }
                };
            }

        };
    }
}
