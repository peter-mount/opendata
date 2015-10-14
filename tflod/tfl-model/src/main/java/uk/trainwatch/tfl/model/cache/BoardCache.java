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
import java.util.stream.Collectors;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.tfl.model.BoardEntry;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TflBoardCache")
public class BoardCache
{

    private static final SQLResultSetHandler<BoardEntry> FROM_SQL = rs -> new BoardEntry();

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private StationCache stationCache;

    /**
     * Return the display boards for a specific platformid
     * <p>
     * @param platid
     *               <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    @CacheResult
    public List<BoardEntry> get( @CacheKey int platid )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement s = SQL.prepare( con,
                                                    "SELECT * FROM tfl.board"
                                                    + " WHERE platid=?"
                                                    + " ORDER BY expt",
                                                    platid ) ) {
                return SQL.stream( s, FROM_SQL ).
                        collect( Collectors.toList() );
            }
        }
    }

    /**
     * Return all departures for an entire station
     * <p>
     * @param naptan
     *               <p>
     * @return
     * @throws java.sql.SQLException
     */
    @CacheResult
    public List<BoardEntry> get( @CacheKey String naptan )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement s = SQL.prepare( con,
                                                    "SELECT b.* FROM tfl.board b"
                                                    + " INNER JOIN tfl.station_platform sp ON b.platid=sb.id"
                                                    + " INNER JOIN tfl.station s ON sb.stationid=s.id"
                                                    + " WHERE s.naptan=?"
                                                    + " ORDER BY expt",
                                                    naptan ) ) {
                return SQL.stream( s, FROM_SQL ).
                        collect( Collectors.toList() );
            }
        }
    }
}
