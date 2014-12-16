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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author Peter T Mount
 */
public class UncheckedSQLException
        extends RuntimeException
{

    private static final long serialVersionUID = -8134305061645241065L;

    /**
     * Constructs an instance of this class.
     * <p>
     * @param message the detail message, can be null
     * @param cause   the {@code IOException}
     * <p>
     * @throws NullPointerException if the cause is {@code null}
     */
    public UncheckedSQLException( String message, IOException cause )
    {
        super( message, Objects.requireNonNull( cause ) );
    }

    /**
     * Constructs an instance of this class.
     * <p>
     * @param cause the {@code IOException}
     * <p>
     * @throws NullPointerException if the cause is {@code null}
     */
    public UncheckedSQLException( SQLException cause )
    {
        super( Objects.requireNonNull( cause ) );
    }

    /**
     * Returns the cause of this exception.
     * <p>
     * @return the {@code IOException} which is the cause of this exception.
     */
    @Override
    public SQLException getCause()
    {
        return (SQLException) super.getCause();
    }

    /**
     * Called to read the object from a stream.
     * <p>
     * @throws InvalidObjectException if the object is invalid or has a cause that is not an {@code IOException}
     */
    @SuppressWarnings("ThrowableResultIgnored")
    private void readObject( ObjectInputStream s )
            throws IOException,
                   ClassNotFoundException
    {
        s.defaultReadObject();
        Throwable cause = super.getCause();
        if( !(cause instanceof UncheckedSQLException) )
        {
            throw new InvalidObjectException( "Cause must be an UncheckedSQLException" );
        }
    }
}
