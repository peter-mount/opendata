/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.tfl.model.Line;
import uk.trainwatch.tfl.model.Platform;
import uk.trainwatch.tfl.model.Station;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLFunction;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TflStationCache")
public class StationCache
{

    private static final SQLResultSetHandler<Station> FROM_SQL = rs -> new Station(
            rs.getInt( "id" ),
            rs.getString( "code" ),
            rs.getString( "name" )
    );

    @Resource(name = "jdbc/Rail")
    private DataSource dataSource;

    @Inject
    private LineCache lineCache;

    @Inject
    private PlatformCache platformCache;

    private Station get( String field, Object key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            Optional<Station> station;
            try( PreparedStatement s = SQL.prepare( con, "SELECT * FROM tfl.station WHERE " + field + "=?", key ) ) {
                station = SQL.stream( s, FROM_SQL ).
                        findAny();
            }

            if( station.isPresent() ) {
                // Lines for this station
                List<Line> lines = station.get().getLines();
                try( PreparedStatement s = SQL.prepare( con, "SELECT lineid FROM tfl.station_line WHERE stationid=?", key ) ) {
                    SQL.stream( s, SQL.INT_LOOKUP ).
                            map( SQLFunction.guard( lineCache::get ) ).
                            forEach( lines::add );
                }

                // Platforms
                List<Platform> platforms = station.get().getPlatforms();
                try( PreparedStatement s = SQL.prepare( con, "SELECT platid FROM tfl.station_platform WHERE stationid=?", key ) ) {
                    SQL.stream( s, SQL.INT_LOOKUP ).
                            map( SQLFunction.guard( platformCache::get ) ).
                            forEach( platforms::add );
                }
            }

            return station.orElse( null );
        }
    }

    @CacheResult
    public Station get( @CacheKey int id )
            throws SQLException
    {
        return get( "id", id );
    }

    @CacheResult
    public Station getNaptan( @CacheKey String key )
            throws SQLException
    {
        return get( "naptan", key );
    }

    @CacheResult
    public Station getName( @CacheKey String name )
            throws SQLException
    {
        return get( "name", name );
    }

}
