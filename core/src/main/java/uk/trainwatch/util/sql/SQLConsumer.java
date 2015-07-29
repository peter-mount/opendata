/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A variant of a consumer which allows for a SQL exception to be passed through
 * <p>
 * @param <T> Return value
 * <p>
 * @author Peter T Mount
 */
@FunctionalInterface
public interface SQLConsumer<T>
{

    void accept( T t )
            throws SQLException;

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * <p>
     * @param after the operation to perform after this operation
     * <p>
     * @return a composed {@code Consumer} that performs in sequence this operation followed by the {@code after}
     *         operation
     * <p>
     * @throws NullPointerException if {@code after} is null
     */
    default SQLConsumer<T> andThen( SQLConsumer<? super T> after )
    {
        Objects.requireNonNull( after );
        return ( T t ) -> {
            accept( t );
            after.accept( t );
        };
    }

    /**
     * Composes this SQLConsumer into a {@link SQLBiConsumer} where the first parameter is ignored.
     * <p>
     * This is useful for {@link java.util.Map#forEach(java.util.function.BiConsumer)}
     * <p>
     * @param <T>
     * @param <U>
     * @param c   <p>
     * @return
     */
    static <T, U> SQLBiConsumer<U, T> composeBiConsumer( SQLConsumer<T> c )
    {
        return ( u, t ) -> c.accept( t );
    }

    /**
     * Wraps a SQLConsumer with a Consumer which allows for any SQLException to be converted into an
     * UncheckedSQLException
     * <p>
     * @param <T>
     * @param c   <p>
     * @return
     */
    static <T> Consumer<T> guard( SQLConsumer<T> c )
    {
        return t -> {
            try {
                c.accept( t );
            }
            catch( BatchUpdateException ex ) {
                throw new UncheckedSQLException( ex.getNextException() );
            }
            catch( SQLException ex ) {
                throw new UncheckedSQLException( ex );
            }
        };
    }

    /**
     * Wraps a SQLConsumer with a Consumer which allows for any SQLException to be converted into an
     * UncheckedSQLException
     * <p>
     * @return consumer
     */
    default Consumer<T> guard()
    {
        return guard( this );
    }

    /**
     * Similar to {@link #guard(uk.trainwatch.util.sql.SQLConsumer)} but this will consumer any duplicate key
     * violations. This is useful when importing data from archives but allows us to continue as the database already
     * contains some (not necessarily all) data.
     * <p>
     * @param <T>
     * @param c
     *            <p>
     * @return
     */
    static <T> Consumer<T> guardIgnoreDuplicates( SQLConsumer<T> c )
    {
        return t -> {
            try {
                c.accept( t );
            }
            catch( SQLException ex ) {
                String m = ex.getMessage();
                if( m == null || !m.contains( "duplicate key value violates unique constraint" ) ) {
                    throw new UncheckedSQLException( ex );
                }
            }
        };
    }

    /**
     * Composes a new SQLConsumer so that if an UncheckedSQLException is thrown then the original SQLException is
     * rethrown.
     * <p>
     * @param <T>
     * @param c   <p>
     * @return <p>
     * @throws SQLException
     */
    static <T> SQLConsumer<T> compose( Consumer<T> c )
            throws SQLException
    {
        return t -> {
            try {
                c.accept( t );
            }
            catch( UncheckedSQLException ex ) {
                throw ex.getCause();
            }
        };
    }
}
