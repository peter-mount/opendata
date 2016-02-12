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

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import uk.trainwatch.util.ConsumerBuilder.Then;

/**
 * A convenience builder for creating Consumer chains
 *
 * @param <U> Type of payload to consume
 *
 * @author peter
 */
public class ConsumerBuilder<U>
{

    private final Chain<U> chain = new Chain<>();

    /**
     * Add a consumer to the end of the chain
     *
     * @param c
     *
     * @return
     */
    public ConsumerBuilder<U> add( Consumer<U> c )
    {
        chain.add( c );
        return this;
    }

    /**
     * Adds a consumer to the end of the chain
     *
     * @param <T> Type consumer accepts
     * @param f   mapping function
     * @param c   consumer
     *
     * @return
     */
    public <T> ConsumerBuilder<U> add( Function<U, T> f, Consumer<T> c )
    {
        chain.add( f, c );
        return this;
    }

    /**
     * Begin building a conditional block of consumers
     *
     * @param p Predicate
     *
     * @return
     */
    public If<U> _if( Predicate<U> p )
    {
        return new If( p, this );
    }

    /**
     * Begin building a conditional block of consumers
     *
     * @param p supplier which will determine the outcome
     *
     * @return
     */
    public If<U> _if( BooleanSupplier p )
    {
        return new If( u -> p.getAsBoolean(), this );
    }

    /**
     * Build the final consumer
     *
     * @return
     */
    public Consumer<U> build()
    {
        return Consumers.ensureNotNull( chain.action );
    }

    /**
     * Build the final consumer wrapped around a test using a predicate
     *
     * @param p Predicate to determine if the consumer will do anything
     *
     * @return
     */
    public Consumer<U> build( Predicate<U> p )
    {
        Consumer<U> c = build();
        return u -> {
            if( p.test( u ) ) {
                c.accept( u );
            }
        };
    }

    /**
     * Build the final consumer wrapped around a test using a predicate
     *
     * @param p supplier which will determine the outcome
     *
     * @return
     */
    public Consumer<U> build( BooleanSupplier p )
    {
        Consumer<U> c = build();
        return u -> {
            if( p.getAsBoolean() ) {
                c.accept( u );
            }
        };
    }

    /**
     * Build the final consumer. This consumer will only do anything if the supplied object is not null
     *
     * @return
     */
    public Consumer<U> buildNonNull()
    {
        return build( Objects::nonNull );
    }

    /**
     * Builder used when building a conditional section
     *
     * @param <U>
     */
    public class If<U>
    {

        private final Predicate<U> p;
        private final ConsumerBuilder<U> b;
        private final Chain<U> t = new Chain<>();
        private final Chain<U> f = new Chain<>();

        private If( Predicate<U> p, ConsumerBuilder<U> b )
        {
            this.p = p;
            this.b = b;
        }

        /**
         * End the conditional block
         *
         * @return
         */
        private ConsumerBuilder<U> endIf()
        {
            if( f.action == null ) {
                return b.add( Consumers.ifThen( p, t.action ) );
            }
            else if( t.action == null ) {
                return b.add( Consumers.ifNotThen( p, f.action ) );
            }
            else {
                return b.add( Consumers.ifThenElse( p, t.action, f.action ) );
            }
        }

        /**
         * Consumer to invoke if true
         *
         * @param c consumer
         *
         * @return
         */
        public Then<U> _then( Consumer<U> c )
        {
            return new Then( t, f, this )._then( c );
        }

        /**
         * Consumer to invoke if true
         *
         * @param <T> Type consumer accepts
         * @param x   mapping function
         * @param c   consumer
         *
         * @return
         */
        public <T> Then<U> _then( Function<U, T> x, Consumer<T> c )
        {
            t.add( x, c );
            return new Then( t, f, this )._then( x, c );
        }

        /**
         * Consumer to invoke if false
         *
         * @param c consumer
         *
         * @return
         */
        public Else<U> _else( Consumer<U> c )
        {
            return new Else( f, this )._else( c );
        }

        /**
         * Consumer to invoke if false
         *
         * @param <T> Type consumer accepts
         * @param x   mapping function
         * @param c   consumer
         *
         * @return
         */
        public <T> Else<U> _else( Function<U, T> x, Consumer<T> c )
        {
            return new Else( f, this )._else( x, c );
        }
    }

    public class Then<U>
    {

        private final Chain<U> t, f;
        private final If<U> b;

        private Then( Chain<U> t, Chain<U> f, If<U> b )
        {
            this.t = t;
            this.f = f;
            this.b = b;
        }

        /**
         * Consumer to invoke if true
         *
         * @param c consumer
         *
         * @return
         */
        public Then<U> _then( Consumer<U> c )
        {
            t.add( c );
            return this;
        }

        /**
         * Consumer to invoke if true
         *
         * @param <T> Type consumer accepts
         * @param x   mapping function
         * @param c   consumer
         *
         * @return
         */
        public <T> Then<U> _then( Function<U, T> x, Consumer<T> c )
        {
            t.add( x, c );
            return this;
        }

        /**
         * Consumer to invoke if false
         *
         * @param c consumer
         *
         * @return
         */
        public Else<U> _else( Consumer<U> c )
        {
            return new Else( f, b )._else( c );
        }

        /**
         * Consumer to invoke if false
         *
         * @param <T> Type consumer accepts
         * @param x   mapping function
         * @param c   consumer
         *
         * @return
         */
        public <T> Else<U> _else( Function<U, T> x, Consumer<T> c )
        {
            return new Else( f, b )._else( x, c );
        }

        /**
         * End the conditional block
         *
         * @return
         */
        public ConsumerBuilder<U> endIf()
        {
            return b.endIf();
        }
    }

    public class Else<U>
    {

        private final Chain<U> f;
        private final If<U> b;

        private Else( Chain<U> f, If<U> b )
        {
            this.f = f;
            this.b = b;
        }

        /**
         * Consumer to invoke if false
         *
         * @param c consumer
         *
         * @return
         */
        public Else<U> _else( Consumer<U> c )
        {
            f.add( c );
            return this;
        }

        /**
         * Consumer to invoke if false
         *
         * @param <T> Type consumer accepts
         * @param x   mapping function
         * @param c   consumer
         *
         * @return
         */
        public <T> Else<U> _else( Function<U, T> x, Consumer<T> c )
        {
            f.add( x, c );
            return this;
        }

        /**
         * End the conditional block
         *
         * @return
         */
        public ConsumerBuilder<U> endIf()
        {
            return b.endIf();
        }
    }

    private static class Chain<U>
    {

        private Consumer<U> action;

        public void add( Consumer<U> c )
        {
            if( c != null ) {
                action = Consumers.andThen( action, c );
            }
        }

        public <T> void add( Function<U, T> f, Consumer<T> c )
        {
            add( u -> c.accept( f.apply( u ) ) );
        }

    }
}
