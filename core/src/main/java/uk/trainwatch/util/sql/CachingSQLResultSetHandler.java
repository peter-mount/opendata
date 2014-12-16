/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Implementation of {@link SQLResultSetHandler} which caches the {@link ResultSetMetaData} object
 * <p>
 * @author peter
 * @param <T>
 */
public abstract class CachingSQLResultSetHandler<T>
        implements SQLResultSetHandler<T>
{

    private ResultSetMetaData metaData = null;

    @Override
    public final ResultSetMetaData getResultSetMetaData( ResultSet rs )
            throws SQLException
    {
        if( metaData == null )
        {
            metaData = rs.getMetaData();
        }
        return metaData;
    }

}
