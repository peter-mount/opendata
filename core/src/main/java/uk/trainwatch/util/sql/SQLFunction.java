/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Function;

/**
 * A variant of a Function which allows for a SQL exception to be passed through
 * <p>
 * @param <T> Key/Source value
 * @param <R> Return value
 * <p>
 * @author Peter T Mount
 */
@FunctionalInterface
public interface SQLFunction<T, R>
{

    R apply( T t )
            throws SQLException;

    /**
     * Returns a composed function that first applies the {@code before} function to its input, and then applies this
     * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function.
     * <p>
     * @param <V>    the type of input to the {@code before} function, and to the composed function
     * @param before the function to apply before this function is applied
     * <p>
     * @return a composed function that first applies the {@code before} function and then applies this function
     * <p>
     * @throws NullPointerException if before is null
     * <p>
     * @see #andThen(Function)
     */
    default <V> SQLFunction<V, R> compose( SQLFunction<? super V, ? extends T> before )
    {
        Objects.requireNonNull( before );
        return ( V v ) -> apply( before.apply( v ) );
    }

    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function.
     * <p>
     * @param <V>   the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * <p>
     * @return a composed function that first applies this function and then applies the {@code after} function
     * <p>
     * @throws NullPointerException if after is null
     * <p>
     * @see #compose(Function)
     */
    default <V> SQLFunction<T, V> andThen( SQLFunction<? super R, ? extends V> after )
    {
        Objects.requireNonNull( after );
        return ( T t ) -> after.apply( apply( t ) );
    }

    /**
     * Guard's this SQLFunction by ensuring that any {@link SQLException} is wrapped in an {@link UncheckedSQLException}
     * wrapping it in a {@link Function}.
     * <p>
     * @param <T>
     * @param <R>
     * @param f   a
     * <p>
     * @return
     */
    static <T, R> Function<T, R> guard( SQLFunction<T, R> f )
    {
        return t -> {
            try {
                return f.apply( t );
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
     * Guard's this SQLFunction by ensuring that any {@link SQLException} is wrapped in an {@link UncheckedSQLException}
     * wrapping it in a {@link Function}.
     * <p>
     * @return function
     */
    default Function<T, R> guard()
    {
        return guard( this );
    }

    /**
     * Composes a new SQLFunction so that if an UncheckedSQLException is thrown then the original SQLException is rethrown.
     * <p>
     * @param <T>
     * @param <R>
     * @param f
     *            <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    static <T, R> SQLFunction<T, R> compose( Function<T, R> f )
            throws SQLException
    {
        return t -> {
            try {
                return f.apply( t );
            }
            catch( UncheckedSQLException ex ) {
                throw ex.getCause();
            }
        };
    }

    /**
     * Returns a SQLFunction that returns the ResultSet
     * @return 
     */
    static SQLFunction<ResultSet, ResultSet> identity()
    {
        return r -> r;
    }
}
