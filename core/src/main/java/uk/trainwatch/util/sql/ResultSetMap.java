/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import uk.trainwatch.util.UnmodifiableMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * A {@link Map} implementation backed by a {@link ResultSet}.
 * 
 * The map's keys are defined on instantiation from the {@link ResultSetMetaData} however all retrieve operations delegate to the ResultSet.
 * 
 * This map is unmodifiable.
 * <p>
 * <p>
 * @author peter
 */
public class ResultSetMap
        extends UnmodifiableMap<String, Object>
{

    private final ResultSet rs;
    private final int cc;
    private final Map<String, Integer> keys;

    public ResultSetMap( ResultSet rs )
            throws SQLException
    {
        this.rs = rs;
        ResultSetMetaData rsmd = rs.getMetaData();
        cc = rsmd.getColumnCount();
        Map<String, Integer> map = new HashMap<>();
        for( int i = 1; i <= cc; i++ ) {
            map.put( rsmd.getColumnName( i ), i );
        }
        keys = Collections.unmodifiableMap( map );
    }

    @Override
    public Object get( Object key )
    {
        try {
            return rs.getObject( Objects.toString( key ) );
        }
        catch( SQLException ex ) {
            throw new UncheckedSQLException( ex );
        }
    }

    @Override
    public boolean containsKey( Object key )
    {
        return keys.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value )
    {
        return false;
    }

    @Override
    public void forEach( BiConsumer<? super String, ? super Object> action )
    {
        keys.forEach( SQLBiConsumer.guard( ( k, i ) -> action.accept( k, rs.getObject( i ) ) ) );
    }

    @Override
    public Set<String> keySet()
    {
        return keys.keySet();
    }

    @Override
    public Collection<Object> values()
    {
        try {
            List<Object> s = new ArrayList<>( cc );
            for( int i = 1; i <= cc; i++ ) {
                s.add( rs.getObject( i ) );
            }
            return Collections.unmodifiableCollection( s );
        }
        catch( SQLException ex ) {
            throw new UncheckedSQLException( ex );
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        try {
            Set<Entry<String, Object>> s = new HashSet<>();
            for( String key: keys.keySet() ) {
                s.add( new MapEntry( key, rs.getObject( key ) ) );
            }
            return Collections.unmodifiableSet( s );
        }
        catch( SQLException ex ) {
            throw new UncheckedSQLException( ex );
        }
    }

    @Override
    public int size()
    {
        return cc;
    }

    @Override
    public boolean isEmpty()
    {
        return cc == 0;
    }

    private static class MapEntry
            implements Entry<String, Object>
    {

        private final String key;
        private final Object value;

        public MapEntry( String key, Object value )
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey()
        {
            return key;
        }

        @Override
        public Object getValue()
        {
            return value;
        }

        @Override
        public Object setValue( Object value )
        {
            throw new UnsupportedOperationException();
        }

    }
}
