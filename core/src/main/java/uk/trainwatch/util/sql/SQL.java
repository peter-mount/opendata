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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A suite of JDBC utilities
 * <p>
 * @author Peter T Mount
 */
public class SQL
{

    private static final Logger LOG = Logger.getLogger( SQL.class.getName() );

    /**
     * Return a {@link Stream} of objects produced from a {@link ResultSet}
     * <p>
     * @param <T>     Type of object
     * @param rs      ResultSet
     * @param factory Function to generate the object from the ResultSet
     * <p>
     * @return stream
     */
    public static <T> Stream<T> stream( ResultSet rs, SQLFunction<ResultSet, T> factory )
    {
        if( rs == null )
        {
            return Stream.empty();
        }
        return StreamSupport.stream( new ResultSetSpliterator<>( rs, factory ), false );
    }

    /**
     * Return a {@link Stream} of objects produced from the result of a query against a {@link PreparedStatement}
     * <p>
     * @param <T>     Type of object
     * @param s       PreparedStatement
     * @param factory Function to generate the object from the ResultSet
     * <p>
     * @return stream
     * <p>
     * @throws SQLException if the query fails
     */
    public static <T> Stream<T> stream( PreparedStatement s, SQLFunction<ResultSet, T> factory )
            throws SQLException
    {
        return stream( s.executeQuery(), factory );
    }

    /**
     * Utility to prepare a {@link PreparedStatement} from a SQL and arguments
     * <p>
     * @param c    Connection connection
     * @param sql  SQL to use
     * @param args Arguments for sql
     * <p>
     * @return PreparedStatement
     * <p>
     * @throws SQLException
     */
    public static PreparedStatement prepare( Connection c, String sql, Object... args )
            throws SQLException
    {
        PreparedStatement s = c.prepareStatement( sql );
        int i = 1;
        for( Object arg: args )
        {
            s.setObject( i++, arg );
        }
        return s;
    }

    /**
     * Delete the contents of a table and it's id sequence
     * <p>
     * @param con    Connection
     * @param schema Database schema
     * @param table  Table name
     */
    public static void deleteIdTable( Connection con, String schema, String table )
    {
        deleteTable( con, schema, table );
        resetSequence( con, schema, table + "_id_seq" );
    }

    /**
     * Delete the contents of a table
     * <p>
     * @param con    Connection
     * @param schema Database schema
     * @param table  Table name
     */
    public static void deleteTable( Connection con, String schema, String table )
    {
        LOG.log( Level.INFO, () -> "Deleting " + schema + "." + table );
        try( Statement s = con.createStatement() )
        {
            s.execute( "DELETE FROM " + schema + "." + table );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    /**
     * Resets a PostgreSQL sequence
     * <p>
     * @param con      Connection
     * @param schema   Schema
     * @param sequence Sequence name
     */
    public static void resetSequence( Connection con, String schema, String sequence )
    {
        LOG.log( Level.INFO, () -> "Resetting sequence " + schema + "." + sequence );
        try( Statement s = con.createStatement() )
        {
            s.execute( "ALTER SEQUENCE " + schema + "." + sequence + " RESTART WITH 1" );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    /**
     * Ensures that a normalizing table representing an enum.
     * <p>
     * The table consists of two columns, id which maps to ordinal() and code which maps to toString().
     * <p>
     * @param con       Connection
     * @param schema    Database schema
     * @param enumClass Enum class
     */
    public static void updateEnumTable( Connection con, String schema, Class<? extends Enum<?>> enumClass )
    {
        String table = enumClass.getSimpleName().
                toLowerCase();

        deleteTable( con, schema, table );

        LOG.log( Level.INFO, () -> "Reinitialising enum mapping table " + schema + "." + table );
        try( PreparedStatement s = con.prepareStatement(
                "INSERT INTO " + schema + "." + table + " (id,code) VALUES (?,?)" ) )
        {
            for( Enum<?> enumValue: enumClass.getEnumConstants() )
            {
                s.setInt( 1, enumValue.ordinal() );
                s.setString( 2, enumValue.toString() );
                s.executeUpdate();
            }
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    /**
     * Get the current value of a sequence
     * <p>
     * @param con      Connection
     * @param sequence Sequence name
     * <p>
     * @return current value
     * <p>
     * @throws SQLException if the sequence has not been updated in this connection
     */
    public static long currval( Connection con, String sequence )
            throws SQLException
    {
        try( Statement s = con.createStatement() )
        {
            try( ResultSet rs = s.executeQuery( "SELECT currval('timetable.schedule_id_seq')" ) )
            {
                if( rs != null && rs.next() )
                {
                    return rs.getLong( 1 );
                }
            }
        }
        throw new SQLException( "No currval for " + sequence );
    }

    /**
     * SQLFunction to retrieve the first column of a ResultSet as an Integer
     */
    public static final SQLFunction<ResultSet, Integer> INT_LOOKUP = rs -> rs.getInt( 1 );
    /**
     * SQLFunction to retrieve the first column of a ResultSet as a Long
     */
    public static final SQLFunction<ResultSet, Long> LONG_LOOKUP = rs -> rs.getLong( 1 );
    /**
     * SQLFunction to retrieve the first column of a ResultSet as a String
     */
    public static final SQLFunction<ResultSet, String> STRING_LOOKUP = rs -> rs.getString( 1 );

    public static <T> T wasNull( ResultSet rs, T v )
            throws SQLException
    {
        return rs.wasNull() ? null : v;
    }

    public static final Integer getInt( ResultSet rs, int c )
            throws SQLException
    {
        return wasNull( rs, rs.getInt( c ) );
    }

    public static final Integer getInt( ResultSet rs, String c )
            throws SQLException
    {
        return wasNull( rs, rs.getInt( c ) );
    }

    public static final void setInt( PreparedStatement ps, int c, Integer i )
            throws SQLException
    {
        if( i == null )
        {
            ps.setNull( c, Types.INTEGER );
        }
        else
        {
            ps.setInt( c, i );
        }
    }

}
