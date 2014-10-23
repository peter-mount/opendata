/*
 * Copyright 2014 Peter T Mount.
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
package uk.trainwatch.util.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@link ConcurrentHashMap} with {@link SQLMap} attached.
 * <p>
 * @param <K>
 * @param <V> <p>
 * @author Peter T Mount
 */
public class ConcurrentSQLHashMap<K, V>
        extends ConcurrentHashMap<K, V>
        implements SQLMap<K, V>
{

    public ConcurrentSQLHashMap()
    {
    }

    public ConcurrentSQLHashMap( Map<? extends K, ? extends V> m )
    {
        super( m );
    }

    public ConcurrentSQLHashMap( int initialCapacity )
    {
        super( initialCapacity );
    }

    public ConcurrentSQLHashMap( int initialCapacity, float loadFactor )
    {
        super( initialCapacity, loadFactor );
    }

    public ConcurrentSQLHashMap( int initialCapacity, float loadFactor, int concurrencyLevel )
    {
        super( initialCapacity, loadFactor, concurrencyLevel );
    }

}
