/*
 * Copyright 2014 peter.
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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A suite of utilities to compliment {@link Collectors}
 * <p>
 * @author peter
 */
public class CollectorUtils
{
    /**
     * {@link Collector.Characteristics} for an identity, i.e. a Map or Set
     */
    public static final Set<Collector.Characteristics> IDENTITY_CHARACTERISTICS
                                                       = Collections.unmodifiableSet( EnumSet.of( Collector.Characteristics.IDENTITY_FINISH ) );

    /**
     * Returns a Collector who's elements are unique.
     * <p>
     * The combiner determines what to do with existing entries. Usually one of:
     * <ol>
     * <li>{@link Functions#writeOnceBinaryOperator()} to keep any existing entry</li>
     * <li>{@link Functions#overwritingBinaryOperator()} to replace any existing entry with the new one</li>
     * <li>{@link Functions#throwingBinaryOperator()} to throw an exception</li>
     * </ol>
     * <p>
     * @param <T>         Input Type
     * @param <A>         Accumulation type
     * @param supplier    Supplier for the accumulator type
     * @param accumulator Accumulation function
     * @param combiner    Combination operation
     * <p>
     * @return Collector
     */
    public static <T, A> Collector<T, A, T> identityCollector( Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner )
    {
        return collector( supplier, accumulator, combiner, IDENTITY_CHARACTERISTICS );
    }

    /**
     * Returns a Collector
     * <p>
     * @param <T>             Input Type
     * @param <A>             Accumulation type
     * @param <R>
     * @param supplier        Supplier for the accumulator type
     * @param accumulator     Accumulation function
     * @param combiner        combining function
     * @param characteristics Characteristics
     * <p>
     * @return Collector
     */
    public static <T, A, R> Collector<T, A, R> collector( Supplier<A> supplier,
                                                          BiConsumer<A, T> accumulator,
                                                          BinaryOperator<A> combiner,
                                                          Set<Collector.Characteristics> characteristics )
    {
        return new CollectorImpl( supplier, accumulator, combiner, Functions.castingIdentity(), characteristics );
    }

    /**
     * Returns a Collector
     * <p>
     * @param <T>             Input Type
     * @param <A>             Accumulation type
     * @param <R>             Reduction type
     * @param supplier        Supplier for the accumulator type
     * @param accumulator     Accumulation function
     * @param combiner        combining function
     * @param finisher        finishing function
     * @param characteristics Characteristics
     * <p>
     * @return Collector
     */
    public static <T, A, R> Collector<T, A, R> collector( Supplier<A> supplier,
                                                          BiConsumer<A, T> accumulator,
                                                          BinaryOperator<A> combiner,
                                                          Function<A, R> finisher,
                                                          Set<Collector.Characteristics> characteristics )
    {
        return new CollectorImpl( supplier, accumulator, combiner, finisher, characteristics );
    }

    static class CollectorImpl<T, A, R>
            implements Collector<T, A, R>
    {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Collector.Characteristics> characteristics;

        CollectorImpl( Supplier<A> supplier,
                       BiConsumer<A, T> accumulator,
                       BinaryOperator<A> combiner,
                       Function<A, R> finisher,
                       Set<Collector.Characteristics> characteristics )
        {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public BiConsumer<A, T> accumulator()
        {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier()
        {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner()
        {
            return combiner;
        }

        @Override
        public Function<A, R> finisher()
        {
            return finisher;
        }

        @Override
        public Set<Collector.Characteristics> characteristics()
        {
            return characteristics;
        }
    }
}
