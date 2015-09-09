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
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import uk.trainwatch.web.ldb.model.TrainFactory;

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

    /**
     * Searches made before 0500 will do an extra check with the previous SSD.
     *
     * We use 0500 not 0200 for the rare cases of long running trains, specifically sleepers.
     *
     * This should be a rare case so the extra overhead should be minimal.
     */
    private static final LocalTime MIDNIGHT_TIME = LocalTime.of( 5, 0 );

    private static final String TODAY_SQL = "SELECT f.rid"
                                            + " FROM darwin.forecast f"
                                            + " INNER JOIN darwin.forecast_entry e ON f.id=e.fid"
                                            + " INNER JOIN darwin.location l ON e.tpl=l.tpl"
                                            + " INNER JOIN darwin.crs c on l.crs=c.id"
                                            + " WHERE c.crs=?"
                                            + " AND f.ssd = ?"
                                            + " AND COALESCE(e.wtd,e.wta,e.wtp) BETWEEN ? AND ?";

    private static final String ARCHIVE_SQL = "SELECT f.rid"
                                              + " FROM darwin.forecastarc f"
                                              + " INNER JOIN darwin.forecast_entryarc e ON f.id=e.fid"
                                              + " INNER JOIN darwin.location l ON e.tpl=l.tpl"
                                              + " INNER JOIN darwin.crs c on l.crs=c.id"
                                              + " WHERE c.crs=?"
                                              + " AND f.ssd =?"
                                              + " AND COALESCE(e.wtd,e.wta,e.wtp) BETWEEN ? AND ?";

    private static final String SEARCH_SQL = TODAY_SQL + " UNION " + ARCHIVE_SQL;

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @Inject
    private CrsTiplocCache crsTiplocCache;

    @Inject
    private TrainCache trainCache;

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

        // to is exclusive so add 1 hour then -1s so we search :00:00 to :59:59
        java.sql.Time to = java.sql.Time.valueOf( fromTime.plusHours( 1 ).minusSeconds( 1 ) );

        // If searching in the early hours also check previous SSD to handle trains starting before midnight.
        boolean midnight = fromTime.isBefore( MIDNIGHT_TIME );

        // Searching in the past?
        boolean archiveOnly = startDate.isBefore( LocalDate.now() );

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

            // Now the search which crosses both live and archive tables
            // FIXME: Optimise this, only scan live for today
            try( PreparedStatement ps = SQL.prepare( con, SEARCH_SQL,
                                                     crs, ssd, from, to,
                                                     crs, ssd, from, to
            ) ) {
                System.out.println( ps );
                Stream<String> stream = SQL.stream( ps, SQL.STRING_LOOKUP );

                // Check across previous midnight?
                if( midnight ) {
                    ssd = java.sql.Date.valueOf( startDate.minusDays( 1 ) );
                    SQL.setParameters( ps,
                                       crs, ssd, from, to,
                                       crs, ssd, from, to );
                    System.out.println( ps );
                    stream = Stream.concat( SQL.stream( ps, SQL.STRING_LOOKUP ), stream );
                }

                // Now translate into Train objects
                return stream.
                        sorted().
                        distinct().
                        // If searching in the past then force archived so the search doesn't scan the live tables
                        map( TrainFactory::getTrainProxy ).
                        // Flag as archived?
                        map( t -> t.setArchived( archiveOnly ) ).
                        // Perform the search
                        map( t -> new SearchResult( t, filter ) ).
                        // Filter out invalid entries
                        filter( SearchResult::isValid ).
                        // Sort into date/time format
                        sorted().
                        collect( Collectors.toList() );
            }

        }
    }

}
