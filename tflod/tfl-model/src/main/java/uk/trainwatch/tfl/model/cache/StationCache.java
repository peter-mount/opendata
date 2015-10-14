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
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.tfl.model.Line;
import uk.trainwatch.tfl.model.Platform;
import uk.trainwatch.tfl.model.Station;
import uk.trainwatch.util.sql.Database;
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
            rs.getString( "naptan" ),
            rs.getString( "name" )
    );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private LineCache lineCache;

    @Inject
    private PlatformCache platformCache;

    private Station get( String field, Object key, boolean like )
            throws SQLException
    {
        String op = like ? " LIKE ?" : "=?";

        try( Connection con = dataSource.getConnection() ) {
            Optional<Station> station;
            try( PreparedStatement s = SQL.prepare( con,
                                                    "SELECT * FROM tfl.station WHERE " + field + (like ? " LIKE ?" : "=?"),
                                                    like ? ("%" + key) : key
            ) ) {
                station = SQL.stream( s, FROM_SQL ).
                        findAny();
            }

            if( station.isPresent() ) {
                return populate( con, station.get() );
            }
            else {
                return null;
            }
        }
    }

    private Station populate( Connection con, Station station )
            throws SQLException
    {
        // Lines for this station
        List<Line> lines = station.getLines();
        try( PreparedStatement s = SQL.prepare( con, "SELECT lineid FROM tfl.station_line WHERE stationid=?", station.getId() ) ) {
            SQL.stream( s, SQL.INT_LOOKUP ).
                    map( SQLFunction.guard( lineCache::get ) ).
                    forEach( lines::add );
        }

        // Platforms
        List<Platform> platforms = station.getPlatforms();
        try( PreparedStatement s = SQL.prepare( con, "SELECT platid FROM tfl.station_platform WHERE stationid=?", station.getId() ) ) {
            SQL.stream( s, SQL.INT_LOOKUP ).
                    map( SQLFunction.guard( platformCache::get ) ).
                    forEach( platforms::add );
        }
        return station;
    }

    @CacheResult
    public Station get( @CacheKey int id )
            throws SQLException
    {
        return get( "id", id, false );
    }

    @CacheResult
    public Station getNaptan( @CacheKey String key )
            throws SQLException
    {
        return get( "naptan", key, false );
    }

    @CacheResult
    public Station getName( @CacheKey String name )
            throws SQLException
    {
        return get( "name", name, false );
    }

    @CacheResult
    public Station getLuCrs( @CacheKey String name )
            throws SQLException
    {
        return get( "naptan", name, true );
    }

}
