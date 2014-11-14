/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nrod.rtppm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public enum PerformanceManager
{

    INSTANCE;

    private DataSource dataSource;

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    /**
     * Get's the daily performance for an Operator for a month
     * <p>
     * @param date
     *             <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Collection<OperatorDailyPerformance> getMonthPerformance( Operator operator, LocalDate date )
            throws SQLException
    {
        return getMonthPerformance( operator.getId(), date );
    }

    /**
     * The current years
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public IntSummaryStatistics getYears()
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             Statement s = con.createStatement() )
        {
            IntSummaryStatistics stat = new IntSummaryStatistics();
            ResultSet rs = s.executeQuery( "SELECT min(d.year),max(d.year) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt" );
            if( rs.next() )
            {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }

    public IntSummaryStatistics getMonths( int year )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT min(d.month),max(d.month) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt WHERE d.year=?" ) )
        {
            s.setInt( 1, year );
            IntSummaryStatistics stat = new IntSummaryStatistics();
            ResultSet rs = s.executeQuery();
            if( rs.next() )
            {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }

    public IntSummaryStatistics getDays( int year, int month )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT min(d.day),max(d.day) FROM datetime.dim_date d INNER JOIN rtppm.daily r ON d.dt_id=r.dt WHERE d.year=? AND d.month=?" ) )
        {
            s.setInt( 1, year );
            s.setInt( 2, month );
            IntSummaryStatistics stat = new IntSummaryStatistics();
            ResultSet rs = s.executeQuery();
            if( rs.next() )
            {
                stat.accept( rs.getInt( 1 ) );
                stat.accept( rs.getInt( 2 ) );
            }
            return stat;
        }
    }

    /**
     * Returns a map keyed by operator id of DailyPPM's for a month
     * <p>
     * @param year
     * @param month
     *              <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Map<Integer, List<DailyPPM>> getMonthsDailyPPM( int year, int month )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT d.dt, p.operator,p.ppm FROM rtppm.daily p INNER JOIN datetime.dim_date d ON d.dt_id=p.dt WHERE d.year=? AND d.month=? ORDER BY p.dt" ) )
        {
            s.setInt( 1, year );
            s.setInt( 2, month );
            return SQL.stream( s, rs -> new DailyPPM( rs.getDate( 1 ), rs.getInt( 2 ), rs.getInt( 3 ) ) ).
                    collect( Collectors.groupingBy( DailyPPM::getId ) );
        }
    }

    public Collection<OperatorDailyPerformance> getCurrentPerformance()
            throws SQLException
    {
        return getPerformance( LocalDate.now() );
    }

    public Collection<OperatorDailyPerformance> getPerformance( LocalDate date )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT p.* FROM rtppm.daily p INNER JOIN rtppm.operator o ON p.operator=o.id WHERE dt=? ORDER BY o.display" ) )
        {
            s.setLong( 1, TimeUtils.toDBDate.apply( date ) );
            return SQL.stream( s, OperatorDailyPerformance.fromSQL ).
                    collect( Collectors.toList() );
        }
    }

    /**
     * Get's the daily performance for an Operator for a month
     * <p>
     * @param date
     *             <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Collection<OperatorDailyPerformance> getMonthPerformance( int operatorId, LocalDate date )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement( "SELECT * FROM rtppm.daily WHERE dt BETWEEN ? AND ? AND operator=? ORDER BY dt_id" ) )
        {
            // The start of the month
            LocalDate startDate = TimeUtils.getLocalDate( Instant.from( date ).
                    truncatedTo( ChronoUnit.MONTHS ) );
            s.setLong( 1, TimeUtils.toDBDate.apply( startDate ) );
            s.setLong( 2, TimeUtils.toDBDate.apply( startDate.plusMonths( 1L ) ) );
            s.setInt( 3, operatorId );

            return SQL.stream( s, OperatorDailyPerformance.fromSQL ).
                    collect( Collectors.toList() );
        }
    }

}
