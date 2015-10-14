/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;

/**
 * Cache that holds Crs to Tiplocs... used often in ldb lookups
 * <p>
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TiplocCache")
public class TiplocCache
{

    private static final Logger LOG = Logger.getLogger( TiplocCache.class.getName() );

    private static final String SELECT_SQL = "SELECT id FROM darwin.tiploc WHERE tpl=?";

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @CacheResult
    public Collection<Integer> get( @CacheKey String tiploc )
            throws SQLException
    {
        LOG.log( Level.INFO, () -> "Looking up Tiplocs for " + tiploc );

        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, SELECT_SQL, tiploc ) ) {
                return SQL.stream( ps, SQL.INT_LOOKUP ).
                        filter( Objects::nonNull ).
                        collect( Collectors.toList() );
            }
        }
    }
}
