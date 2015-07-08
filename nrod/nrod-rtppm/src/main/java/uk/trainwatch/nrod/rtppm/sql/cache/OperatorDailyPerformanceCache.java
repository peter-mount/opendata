/*
 * Copyright 2015 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.nrod.rtppm.sql.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import uk.trainwatch.nrod.rtppm.sql.OperatorDailyPerformance;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "OperatorDailyPerformanceCache")
public class OperatorDailyPerformanceCache
{

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @CacheResult
    public Collection<OperatorDailyPerformance> getMonthPerformance( @CacheKey OperatorLocalDate key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement( "SELECT * FROM rtppm.daily WHERE dt BETWEEN ? AND ? AND operator=? ORDER BY dt_id" ) ) {
            // The start of the month
            s.setLong( 1, TimeUtils.toDBDate.apply( key.getDate() ) );
            s.setLong( 2, TimeUtils.toDBDate.apply( key.getDate().plusMonths( 1L ) ) );
            s.setInt( 3, key.getId() );

            return SQL.stream( s, OperatorDailyPerformance.fromSQL ).
                    collect( Collectors.toList() );
        }
    }

    @CacheResult
    public Collection<OperatorDailyPerformance> getPerformance( @CacheKey OperatorLocalDate key )
            throws SQLException
    {
        return getPerformanceNoCache( key );
    }

    public Collection<OperatorDailyPerformance> getPerformanceNoCache( OperatorLocalDate key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT p.* FROM rtppm.daily p INNER JOIN rtppm.operator o ON p.operator=o.id WHERE dt=? ORDER BY o.display" ) ) {
            s.setLong( 1, TimeUtils.toDBDate.apply( key.getDate() ) );
            return SQL.stream( s, OperatorDailyPerformance.fromSQL ).
                    collect( Collectors.toList() );
        }
    }

    @CacheResult
    public Optional<OperatorDailyPerformance> getOperatorPerformance( @CacheKey OperatorLocalDate key )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = SQL.prepare( con,
                                                "SELECT p.* FROM rtppm.daily p INNER JOIN rtppm.operator o ON p.operator=o.id WHERE dt=? and o.id=?",
                                                TimeUtils.toDBDate.apply( key.getDate() ),
                                                key.getId()
             ) ) {
            return SQL.stream( s, OperatorDailyPerformance.fromSQL ).
                    findAny();
        }
    }

}
