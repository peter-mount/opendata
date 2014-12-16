/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A variant of a BiFunction which allows for a SQL exception to be passed through
 * <p>
 * @param <T> Key/Source value
 * @param <U>
 * @param <R>
 *            <p>
 * @author Peter T Mount
 */
@FunctionalInterface
public interface SQLBiFunction<T, U, R>
{

    R apply( T key, U val )
            throws SQLException;

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
     */
    default <V> SQLBiFunction<T, U, V> andThen( SQLFunction<? super R, ? extends V> after )
    {
        Objects.requireNonNull( after );
        return ( T t, U u ) -> after.apply( apply( t, u ) );
    }

    /**
     * Guard's this SQLFunction by ensuring that any {@link SQLException} is wrapped in an {@link UncheckedSQLException}
     * wrapping it in a {@link Function}.
     * <p>
     * @param <T>
     * @param <U>
     * @param <R>
     * @param f   a
     * <p>
     * @return
     */
    static <T, U, R> BiFunction<T, U, R> guard( SQLBiFunction<T, U, R> f )
    {
        return ( t, u ) ->
        {
            try
            {
                return f.apply( t, u );
            }
            catch( SQLException ex )
            {
                throw new UncheckedSQLException( ex );
            }
        };
    }

    /**
     * Guard's this SQLFunction by ensuring that any {@link SQLException} is wrapped in an {@link UncheckedSQLException}
     * wrapping it in a {@link Function}.
     * <p>
     * @return
     */
    default BiFunction<T, U, R> guard()
    {
        return guard( this );
    }

    /**
     * Composes a new SQLBiFunction so that if an UncheckedSQLException is thrown then the original SQLException is
     * rethrown.
     * <p>
     * @param <T>
     * @param <U>
     * @param <R>
     * @param f
     *            <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    static <T, U, R> SQLBiFunction<T, U, R> compose( BiFunction<T, U, R> f )
            throws SQLException
    {
        return ( t, u ) ->
        {
            try
            {
                return f.apply( t, u );
            }
            catch( UncheckedSQLException ex )
            {
                throw ex.getCause();
            }
        };
    }
}
