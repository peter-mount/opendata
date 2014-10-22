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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A suite of JDBC utilities
 * <p>
 * @author Peter T Mount
 */
public class SQL
{

    private static final Logger LOG = Logger.getLogger( SQL.class.getName() );

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
            for( Enum<?> enumValue : enumClass.getEnumConstants() )
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

}
