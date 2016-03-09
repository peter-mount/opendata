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
package uk.trainwatch.util.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import uk.trainwatch.util.CollectionBuilder;
import uk.trainwatch.util.MapBuilder;

/**
 *
 * @author peter
 */
public class MapConfiguration
        implements Configuration
{

    protected final Map<String, Object> config;

    public MapConfiguration()
    {
        this( new ConcurrentHashMap<>() );
    }

    public MapConfiguration( Map<String, Object> config )
    {
        this.config = config;
    }

    @Override
    public Configuration getConfiguration( String key, Supplier<Configuration> defaultValue )
    {
        Object o = config.computeIfPresent( key,
                                            ( k, v ) -> {
                                                if( v instanceof JsonObject ) {
                                                    return new MapConfiguration( MapBuilder.fromJsonObject( (JsonObject) v ).build() );
                                                }
                                                if( v instanceof Map ) {
                                                    return new MapConfiguration( ((Map<String, Object>) v) );
                                                }
                                                return v;
                                            } );
        return o instanceof Configuration ? (Configuration) o : defaultValue.get();
    }

    @Override
    public Stream<Object> collection( String key )
    {
        Object o = config.get( key );

        if( o == null ) {
            return Stream.empty();
        }

        if( o instanceof JsonArray ) {
            return ((JsonArray) o).stream().map( this::map );
        }

        if( o instanceof JsonObject ) {
            return Stream.of( MapBuilder.fromJsonObject( (JsonObject) o ) );
        }

        if( o instanceof Collection ) {
            return ((Collection) o).stream();
        }

        return Stream.of( o );
    }

    private Object map( JsonValue v )
    {
        switch( v.getValueType() ) {
            case STRING:
                return ((JsonString) v).getString();

            case NUMBER:
                return ((JsonNumber) v).bigDecimalValue();

            case TRUE:
                return true;

            case FALSE:
                return false;

            case OBJECT:
                return MapBuilder.fromJsonObject( (JsonObject) v );

            case ARRAY:
                return CollectionBuilder.fromJsonArray( (JsonArray) v );

            case NULL:
            default:
                return null;
        }
    }

    @Override
    public Collection<String> getKeys()
    {
        return config.keySet();
    }

    @Override
    public Stream<String> keys()
    {
        return config.keySet().stream();
    }

    @Override
    public String getString( String key )
    {
        return Objects.toString( config.get( key ), null );
    }

    @Override
    public Object get( Object key )
    {
        return config.get( key );
    }

    @Override
    public String getString( String key, String defaultValue )
    {
        return Objects.toString( config.get( key ), defaultValue );
    }

    @Override
    public int size()
    {
        return config.size();
    }

    @Override
    public boolean isEmpty()
    {
        return config.isEmpty();
    }

    @Override
    public boolean containsKey( Object key )
    {
        return config.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value )
    {
        return false;
    }

    @Override
    public Object put( String key, Object value )
    {
        return null;
    }

    @Override
    public Object remove( Object key )
    {
        return null;
    }

    @Override
    public void putAll( Map<? extends String, ? extends Object> m )
    {
    }

    @Override
    public void clear()
    {
    }

    @Override
    public Set<String> keySet()
    {
        return config.keySet();
    }

    @Override
    public Collection<Object> values()
    {
        return Collections.emptyList();
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return Collections.emptySet();
    }

    @Override
    public void forEach( BiConsumer<? super String, ? super Object> action )
    {
        keySet().forEach( k -> action.accept( k, getString( k ) ) );
    }

    @Override
    public void replaceAll( BiFunction<? super String, ? super Object, ? extends Object> function )
    {
    }

    @Override
    public Object putIfAbsent( String key, Object value )
    {
        return null;
    }

    @Override
    public boolean remove( Object key, Object value )
    {
        return false;
    }

    @Override
    public boolean replace( String key, Object oldValue, Object newValue )
    {
        return false;
    }

    @Override
    public Object replace( String key, Object value )
    {
        return false;
    }

    @Override
    public Object computeIfAbsent( String key, Function<? super String, ? extends Object> mappingFunction )
    {
        return null;
    }

    @Override
    public Object computeIfPresent( String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction )
    {
        return null;
    }

    @Override
    public Object compute( String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction )
    {
        return null;
    }

    @Override
    public Object merge( String key, Object value, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction )
    {
        return null;
    }

}
