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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import uk.trainwatch.util.sql.KeyValue;

/**
 * A {@link BerthMap} holding the signalling berth's within a signalling area
 * <p>
 * @author peter
 */
public class BerthHashMap
        implements BerthMap

{

    private final Map<String, String> map = new ConcurrentHashMap<>();
    private long lastUpdate;

    private void tick()
    {
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public long getLastUpdate()
    {
        return lastUpdate;
    }

    
    @Override
    public BerthMap put( String berth, String code )
    {
        tick();
        map.put( berth, code );
        return this;
    }

    @Override
    public void clear( String berth )
    {
        tick();
        map.remove( berth );
    }

    @Override
    public String get( String berth )
    {
        return map.get( berth );
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public boolean containsBerth( String berth )
    {
        return map.containsKey( berth );
    }

    @Override
    public void clear()
    {
        tick();
        map.clear();
    }

    @Override
    public BerthMap cancel( String berth, String code )
    {
        tick();
        map.remove( berth, code );
        return this;
    }

    @Override
    public Set<String> berthSet()
    {
        return map.keySet();
    }

    @Override
    public void forEach( BiConsumer<? super String, ? super String> action )
    {
        map.forEach( action );
    }

    @Override
    public Stream<KeyValue<String, String>> stream()
    {
        return map.entrySet().
                stream().
                map( KeyValue::new );
    }

    @Override
    public String computeIfAbsent( String berth, Function<? super String, ? extends String> mappingFunction )
    {
        return map.computeIfAbsent( berth, k -> {
            tick();
            return mappingFunction.apply( k );
        } );
    }

    @Override
    public String computeIfPresent( String berth, BiFunction<? super String, ? super String, ? extends String> remappingFunction )
    {
        return map.computeIfPresent( berth, remappingFunction );
    }

}
