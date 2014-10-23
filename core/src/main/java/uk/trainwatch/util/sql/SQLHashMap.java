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

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@link HashMap} with {@link SQLMap}
 * <p>
 * @author Peter T Mount
 */
public class SQLHashMap<K, V>
        extends HashMap<K, V>
        implements SQLMap<K, V>
{

    public SQLHashMap()
    {
    }

    public SQLHashMap( Map<? extends K, ? extends V> m )
    {
        super( m );
    }

    public SQLHashMap( int initialCapacity )
    {
        super( initialCapacity );
    }

    public SQLHashMap( int initialCapacity, float loadFactor )
    {
        super( initialCapacity, loadFactor );
    }

}
