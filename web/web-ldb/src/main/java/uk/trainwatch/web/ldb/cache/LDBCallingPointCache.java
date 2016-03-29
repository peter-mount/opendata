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
import java.util.stream.Collectors;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.web.ldb.CallingPoint;

/**
 * Perform a lookup of a trains calling points from a trains id.
 * <p>
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "LDBCallingPointCache")
public class LDBCallingPointCache
{

    private static final String SELECT_SQL = "SELECT t.tpl,"
                                             + " COALESCE(e.dep, e.etdep, e.arr, e.etarr) AS time,"
                                             + " (e.dep IS NOT NULL OR e.arr IS NOT NULL) AS report,"
                                             + " s.can"
                                             + " FROM darwin.schedule_entry s"
                                             + " INNER JOIN darwin.forecast f ON s.schedule=f.schedule"
                                             + " INNER JOIN darwin.forecast_entry e ON f.id = e.fid AND s.tpl=e.tpl AND e.ldb=true"
                                             + " INNER JOIN darwin.tiploc t on e.tpl=t.id"
                                             + " WHERE f.id=?"
                                             + " ORDER BY s.id";

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private DarwinReferenceManager darwinReferenceManager;

    @CacheResult
    public Collection<CallingPoint> getCallingPoints( @CacheKey long id )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, SELECT_SQL, id ) ) {
                return SQL.stream( ps, CallingPoint.fromSQL( darwinReferenceManager ) ).
                        collect( Collectors.toList() );
            }
        }
    }
}
