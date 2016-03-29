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
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.web.ldb.LDB;
import uk.trainwatch.web.ldb.LDBTFL;
import uk.trainwatch.web.ldb.LDBTrain;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "LDBDepartureCache")
public class LDBDepartureCache
{

    private static final String DARWIN_SELECT = "SELECT * FROM darwin.departureboard(?)";

    private static final String TFL_SELECT = "SELECT "
                                             + "   'TFL' as type,"
                                             + "   b.expt as tm,"
                                             + "   b.ts as ts,"
                                             // FIXME fix db so tfl line is the toc
                                             + "   l.name as toc,"
                                             + "   '' as origin,"
                                             + "   b.towards as destination,"
                                             // Not supported for TfL
                                             + "   0 as via, 0 as cancreason, 0 as latereason,"
                                             + "   b.id as id,"
                                             + "   '' as rid,"
                                             + "   '' as uid,"
                                             + "   0 as schedule,"
                                             + "   null as arr,"
                                             + "   null as dep,"
                                             + "   b.expt as etarr,"
                                             + "   null as etdep,"
                                             + "   b.expt as pta,"
                                             + "   null as ptd,"
                                             // either name or fullname
                                             + "   p.name as plat,"
                                             + "   false as supp,"
                                             + "   false as platsup,"
                                             + "   false as cisplatsup,"
                                             + "   '00:00' as delay,"
                                             // FIXME terminate if dest is this station
                                             + "   false as term,"
                                             + "   true as ldbdel,"
                                             + "   0 as length,"
                                             + "   b.curloc AS curloc,"
                                             + "   sd.name AS altdest"
                                             + " FROM tfl.boards b"
                                             + " INNER JOIN tfl.line l ON b.lineid=l.id"
                                             + " INNER JOIN tfl.station_platform sp ON b.platid=sp.id"
                                             + " INNER JOIN tfl.platform p ON sp.platid=p.id"
                                             + " INNER JOIN tfl.station s ON sp.stationid=s.id"
                                             + " LEFT OUTER JOIN tfl.station sd ON b.dest = sd.id"
                                             + " WHERE s.naptan LIKE ?";
    @Database("rail")
    @Inject
    private DataSource dataSource;

    @CacheResult
    public Collection<LDBTrain> getDarwinDepartures( @CacheKey String crs )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, DARWIN_SELECT, crs ) ) {
                return SQL.executeQuery( ps, LDBTrain.fromSQL );
            }
        }
    }

    @CacheResult
    public Collection<LDB> getTfLDepartures( @CacheKey String crs )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, TFL_SELECT, "%" + crs ) ) {
                return SQL.stream( ps, LDBTFL.fromSQL ).
                        // Sort to Darwin rules, accounts for midnight
                        sorted( ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( a.getTime(), b.getTime() ) ).
                        collect( Collectors.toList() );
            }
        }
    }

}
