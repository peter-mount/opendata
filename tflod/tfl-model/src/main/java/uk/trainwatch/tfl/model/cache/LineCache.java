/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.tfl.model.Line;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TflLineCache")
public class LineCache
{

    private static final SQLResultSetHandler<Line> FROM_SQL = rs -> new Line(
            rs.getString( "code" ),
            rs.getString( "name" )
    );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    private Line get( String field, Object key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement s = SQL.prepare( con, "SELECT * FROM tfl.line WHERE " + field + "=?", key ) ) {
                return SQL.stream( s, FROM_SQL ).
                        findAny().
                        orElse( null );
            }
        }
    }

    @CacheResult
    public Line get( @CacheKey int lineId )
            throws SQLException
    {
        return get( "id", lineId );
    }

    @CacheResult
    public Line getCode( @CacheKey String code )
            throws SQLException
    {
        return get( "code", code );
    }

    @CacheResult
    public Line getName( @CacheKey String name )
            throws SQLException
    {
        return get( "name", name );
    }

}
