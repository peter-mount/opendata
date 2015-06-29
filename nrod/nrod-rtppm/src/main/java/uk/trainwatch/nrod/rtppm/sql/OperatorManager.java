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
package uk.trainwatch.nrod.rtppm.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 * A cache of current RTPPM Operator's
 * <p>
 * @author peter
 */
@ApplicationScoped
public class OperatorManager
{

    @Resource( name = "jdbc/rail" )
    private DataSource dataSource;

    private final Map<Integer, Operator> operators = new ConcurrentHashMap<>();

    public Operator getOperator( int id )
    {
        return operators.get( id );
    }

    public Map<Integer, Operator> getOperatorMap()
    {
        return operators;
    }

    public Collection<Operator> getOperators()
    {
        return new TreeSet<>( operators.values() );
    }

    @PostConstruct
    public void reload()
    {
        try( Connection con = dataSource.getConnection() )
        {
            try( Statement s = con.createStatement() )
            {
                Map<Integer, Operator> ops = SQL.stream( s.executeQuery( "SELECT * FROM rtppm.operator" ),
                                                         Operator.fromSQL
                ).
                        collect( Collectors.toMap( Operator::getId, Function.identity() ) );
                // Put all into the map
                operators.putAll( ops );
                // Remove any value thats not in the map
                operators.values().
                        removeIf( o -> !ops.containsValue( o ) );
            }
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }
}
