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
package uk.trainwatch.nrod.td.berth;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Map of berths within an area
 * <p>
 * @author peter
 */
public interface BerthMap
{

    /**
     * Put a head code into a berth
     * <p>
     * @param berth Berth
     * @param code  head code
     * @return 
     */
    BerthMap put( String berth, String code );

    /**
     * Clear a berth of a train
     * <p>
     * @param berth Berth
     */
    void clear( String berth );

    /**
     * Cancels a berth only if it contains the given head code
     * <p>
     * @param berth Berth
     * @param code  Head code to cancel
     * <p>
     * @return
     */
    BerthMap cancel( String berth, String code );

    /**
     * Get the head code for a berth
     * <p>
     * @param berth Berth
     * <p>
     * @return head code or null if empty
     */
    String get( String berth );

    /**
     * The number of active berths
     * <p>
     * @return active berth count
     */
    int size();

    /**
     * Are there no berth's occupied?
     * <p>
     * @return
     */
    boolean isEmpty();

    /**
     * Is a berth present and occupied
     * <p>
     * @param berth Berth
     * <p>
     * @return true if occupied
     */
    boolean containsBerth( String berth );

    /**
     * Clears all berths
     */
    void clear();

    /**
     * Returns a Set of occupied berths.
     * <p>
     * @return
     */
    Set<String> berthSet();

    /**
     * Run a {@link BiConsumer} against each occupied Berth.
     * <p>
     * For example: {@code berths.forEach( (berth,headcode) -&gt; System.out.printf("Berth %s HeadCode %s\n", berth, headcode) );}
     * <p>
     * @param action BiConsumer
     */
    void forEach( BiConsumer<? super String, ? super String> action );

    /**
     * Apply the mappingFunction against a berth if it's not present/occupied
     * <p>
     * @param berth           Berth
     * @param mappingFunction Mapping function
     * <p>
     * @return Berths occupier or null if not occupied
     */
    String computeIfAbsent( String berth, Function<? super String, ? extends String> mappingFunction );

    /**
     * Apply the remapping function if the berth is occupied
     * <p>
     * @param berth             Berth
     * @param remappingFunction Mapping function
     * <p>
     * @return Berths occupier or null if not occupied
     */
    String computeIfPresent( String berth, BiFunction<? super String, ? super String, ? extends String> remappingFunction );

    /**
     * If a berth is occupied, pass the head code to a {@link Consumer}
     * <p>
     * @param berth
     * @param c
     */
    default void consumeIfPresent( String berth, Consumer<String> c )
    {
        String code = get( berth );
        if( code != null )
        {
            c.accept( code );
        }
    }

    /**
     * If a berth is occupied, pass the head code to a {@link BiConsumer}
     * <p>
     * @param berth
     * @param c
     */
    default void consumeIfPresent( String berth, BiConsumer<String, String> c )
    {
        String code = get( berth );
        if( code != null )
        {
            c.accept( berth, code );
        }
    }
}
