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
package uk.trainwatch.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A {@link SQLSupplier} implementation that gets it's values from a {@link ResultSet}.
 * <p>
 * When the ResultSet has been depleted this supplier will return null.
 * <p>
 * @author peter
 * @param <T>
 */
public class SQLResultSetSupplier<T>
        implements SQLSupplier<T>
{

    private final ResultSet rs;
    private final SQLFunction<ResultSet, T> factory;

    public SQLResultSetSupplier( ResultSet rs, SQLFunction<ResultSet, T> factory )
    {
        this.rs = rs;
        this.factory = factory;
    }

    public SQLResultSetSupplier( PreparedStatement s, SQLFunction<ResultSet, T> factory )
            throws SQLException
    {
        this( s.executeQuery(), factory );
    }

    @Override
    public T get()
            throws SQLException
    {
        if( rs.next() )
        {
            return factory.apply( rs );
        }
        else
        {
            return null;
        }
    }

}
