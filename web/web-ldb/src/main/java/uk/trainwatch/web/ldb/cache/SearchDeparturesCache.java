/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.Predicates;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.web.ldb.model.SearchResult;

/**
 * Cache that holds Crs to Tiplocs... used often in ldb lookups
 * <p>
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "SearchDeparturesCache")
public class SearchDeparturesCache
{

    private static final Logger LOG = Logger.getLogger( SearchDeparturesCache.class.getName() );

    private static final String SEARCH_SQL = "SELECT * FROM darwin.searchDepartures(?,?,?);";

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @Inject
    private CrsTiplocCache crsTiplocCache;

    @CacheResult
    public Collection<SearchResult> search( @CacheKey SearchDepartureKey key )
            throws SQLException
    {
        String crs = key.getCrs();
        LocalDateTime start = key.getTime();

        LOG.log( Level.INFO, () -> "Searching departures from " + crs + " on " + start );

        LocalDate startDate = start.toLocalDate();
        LocalTime fromTime = start.toLocalTime();

        java.sql.Date ssd = java.sql.Date.valueOf( startDate );

        java.sql.Time from = java.sql.Time.valueOf( fromTime );

        try( Connection con = dataSource.getConnection() ) {

            // Form a filter from all tiplocs for the crs
            Predicate<Integer> filter = crsTiplocCache.get( crs ).
                    stream().
                    peek( tpl -> LOG.log( Level.INFO, () -> "crs " + crs + " tpl " + tpl + ":" ) ).
                    map( tpl -> (Predicate<Integer>) id -> tpl.equals( id ) ).
                    reduce( null, Predicates::or, Predicates::or );

            // Special case, no filter means no tiplocs for a crs
            if( filter == null ) {
                return Collections.emptyList();
            }

            try( PreparedStatement ps = SQL.prepare( con, SEARCH_SQL, crs, ssd, from ) ) {
                System.out.println( ps );
                return SQL.executeQuery( ps, SearchResult.fromSQL );
            }

        }
    }

}
