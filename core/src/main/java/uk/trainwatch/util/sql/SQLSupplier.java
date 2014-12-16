/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * A variant of a supplier which allows for a SQL exception to be passed through
 * <p>
 * @author Peter T Mount
 * @param <T>
 */
@FunctionalInterface
public interface SQLSupplier<T>
{

    T get()
            throws SQLException;

    /**
     * Guards a SQLSupplier so that SQLExceptions are handled correctly
     * <p>
     * @param <T>
     * @param s   SQLSupplier
     * <p>
     * @return Supplier
     */
    static <T> Supplier<T> guard( SQLSupplier<T> s )
    {
        return () ->
        {
            try
            {
                return s.get();
            }
            catch( SQLException ex )
            {
                throw new UncheckedSQLException( ex );
            }
        };
    }

    default Supplier<T> guard()
    {
        return guard( this );
    }

    /**
     * Composes a new SQLSupplier so that if any UncheckedSQLExceptions thrown then the original SQLException is rethrown.
     * <p>
     * @param <T>
     * @param c
     * <p>
     * @return
     * <p>
     * @throws SQLException
     */
    static <T> SQLSupplier<T> compose( Supplier<T> c )
            throws SQLException
    {
        return () ->
        {
            try
            {
                return c.get();
            }
            catch( UncheckedSQLException ex )
            {
                throw ex.getCause();
            }
        };
    }
}
