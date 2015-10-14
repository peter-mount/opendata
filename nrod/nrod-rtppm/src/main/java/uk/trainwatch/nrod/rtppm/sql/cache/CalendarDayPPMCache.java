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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.MinMaxStatistics;
import uk.trainwatch.util.sql.Database;

/**
 * Cache requests for the calendar.
 * <p>
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "CalendarDayPPMCache")
public class CalendarDayPPMCache
{

    @Database("rail")
    @Inject
    private DataSource dataSource;

    /**
     * The current years
     * <p>
     * @param year Cache key, usually {@link Integer#MIN_VALUE}
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    @CacheResult
    public MinMaxStatistics getYears( @CacheKey Integer year )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             Statement s = con.createStatement() ) {
            MinMaxStatistics stat = new MinMaxStatistics();
            ResultSet rs = s.executeQuery( "SELECT min(d.year),max(d.year) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt" );
            if( rs.next() ) {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }

    /**
     * The months for this year
     * <p>
     * @param year the year, but negative, so 2015 needs to be -2015 for the cache
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    @CacheResult
    public MinMaxStatistics getMonths( @CacheKey Integer year )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT min(d.month),max(d.month) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt WHERE d.year=?" ) ) {
            s.setInt( 1, -year );
            MinMaxStatistics stat = new MinMaxStatistics();
            ResultSet rs = s.executeQuery();
            if( rs.next() ) {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }

    /**
     * Days in the month
     * <p>
     * @param monthYear month + (year*100)
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    @CacheResult
    public MinMaxStatistics getDays( @CacheKey Integer monthYear )
            throws SQLException
    {
        int year = monthYear / 100;
        int month = monthYear % 100;

        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT min(d.day),max(d.day) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt WHERE d.year=? AND d.month=?" ) ) {
            s.setInt( 1, year );
            s.setInt( 2, month );
            MinMaxStatistics stat = new MinMaxStatistics();
            ResultSet rs = s.executeQuery();
            if( rs.next() ) {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }
}
