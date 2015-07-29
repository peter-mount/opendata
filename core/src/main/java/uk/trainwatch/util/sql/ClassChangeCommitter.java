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
package uk.trainwatch.util.sql;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Invokes a commit if the payload class changes
 * <p>
 * @author peter
 * @param <T>
 */
public class ClassChangeCommitter<T>
        implements Consumer<T>
{

    private static final Logger LOG = Logger.getLogger( ClassChangeCommitter.class.getName() );
    private final Connection con;
    private Class lastClass;

    public ClassChangeCommitter( Connection con )
    {
        this.con = con;
    }

    @Override
    public void accept( T t )
    {
        if( t != null ) {
            Class<?> clazz = t.getClass();
            if( lastClass != null && !lastClass.equals( clazz ) ) {
                LOG.log( Level.INFO, () -> "Committing work for " + lastClass.getSimpleName() + " found " + clazz.getSimpleName() );
                try {
                    con.commit();
                }
                catch( BatchUpdateException ex ) {
                    throw new UncheckedSQLException( ex.getNextException() );
                }
                catch( SQLException ex ) {
                    throw new UncheckedSQLException( ex );
                }
            }
            lastClass = clazz;
        }
    }

}
