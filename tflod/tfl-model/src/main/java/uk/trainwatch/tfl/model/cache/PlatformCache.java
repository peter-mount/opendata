/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import uk.trainwatch.tfl.model.Platform;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TflPlatformCache")
public class PlatformCache
{

    private static final SQLResultSetHandler<Platform> FROM_SQL = rs -> new Platform(
            rs.getInt( "id"),
            rs.getInt( "plat" ),
            rs.getString( "name" ),
            rs.getString( "fullname" )
    );

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    private Platform get( String field, Object key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement s = SQL.prepare( con, "SELECT * FROM tfl.platform WHERE " + field + "=?", key ) ) {
                return SQL.stream( s, FROM_SQL ).
                        findAny().
                        orElse( null );
            }
        }
    }

    @CacheResult
    public Platform get( @CacheKey int lineId )
            throws SQLException
    {
        return get( "id", lineId );
    }

    @CacheResult
    public Platform getFullName( @CacheKey String name )
            throws SQLException
    {
        return get( "fullname", name );
    }

    @CacheResult
    public Platform getName( @CacheKey String name )
            throws SQLException
    {
        return get( "name", name );
    }

}
