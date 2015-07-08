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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import uk.trainwatch.nrod.rtppm.sql.DailyPPM;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "MonthDailyPPMCache")
public class MonthDailyPPMCache
{

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @CacheResult
    public Map<Integer, List<DailyPPM>> get( @CacheKey Integer monthYear )
            throws SQLException
    {
        int year = monthYear / 100;
        int month = monthYear % 100;

        try( Connection con = dataSource.getConnection();
             PreparedStatement s = con.prepareStatement(
                     "SELECT d.dt, p.operator,p.ppm FROM rtppm.daily p INNER JOIN datetime.dim_date d ON d.dt_id=p.dt WHERE d.year=? AND d.month=? ORDER BY p.dt" ) ) {
            s.setInt( 1, year );
            s.setInt( 2, month );
            return SQL.stream( s, rs -> new DailyPPM( rs.getDate( 1 ), rs.getInt( 2 ), rs.getInt( 3 ) ) ).
                    collect( Collectors.groupingBy( DailyPPM::getId ) );
        }
    }
}
