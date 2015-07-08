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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.nrod.rtppm.sql.cache.CalendarDayPPMCache;
import uk.trainwatch.nrod.rtppm.sql.cache.MonthDailyPPMCache;
import uk.trainwatch.nrod.rtppm.sql.cache.OperatorDailyPerformanceCache;
import uk.trainwatch.nrod.rtppm.sql.cache.OperatorLocalDate;
import uk.trainwatch.util.MinMaxStatistics;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class PerformanceManager
{

    @Inject
    private OperatorDailyPerformanceCache operatorDailyPerformanceCache;

    @Inject
    private MonthDailyPPMCache monthDailyPPMCache;

    @Inject
    private CalendarDayPPMCache calendarDayPPMCache;

    /**
     * Get's the daily performance for an Operator for a month
     * <p>
     * @param operator
     * @param date
     *                 <p>
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
     * Get's the daily performance for an Operator for a month
     * <p>
     * @param operatorId
     * @param date
     *                   <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Collection<OperatorDailyPerformance> getMonthPerformance( int operatorId, LocalDate date )
            throws SQLException
    {
        return operatorDailyPerformanceCache.getMonthPerformance( new OperatorLocalDate( operatorId, date.withDayOfMonth( 1 ) ) );
    }

    public Collection<OperatorDailyPerformance> getCurrentPerformance()
            throws SQLException
    {
        // Convert now to rail date - so 01:59 will show the previous day
        LocalDate now = TimeUtils.getLocalDateTime().minusHours( 2 ).toLocalDate();

        // Currently this is not cached
        return operatorDailyPerformanceCache.getPerformanceNoCache( new OperatorLocalDate( now ) );
    }

    public Collection<OperatorDailyPerformance> getPerformance( LocalDate date )
            throws SQLException
    {
        return operatorDailyPerformanceCache.getPerformance( new OperatorLocalDate( date ) );
    }

    public Optional<OperatorDailyPerformance> getOperatorPerformance( int id, LocalDate date )
            throws SQLException
    {
        return operatorDailyPerformanceCache.getOperatorPerformance( new OperatorLocalDate( id, date ) );
    }

    /**
     * The current years
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public MinMaxStatistics getYears()
            throws SQLException
    {
        return calendarDayPPMCache.getYears( Integer.MIN_VALUE );
    }

    public MinMaxStatistics getMonths( int year )
            throws SQLException
    {
        return calendarDayPPMCache.getMonths( -year );
    }

    public MinMaxStatistics getDays( int year, int month )
            throws SQLException
    {
        return calendarDayPPMCache.getDays( month + (year * 100) );
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
        return monthDailyPPMCache.get( (year * 100) + month );
    }

}
