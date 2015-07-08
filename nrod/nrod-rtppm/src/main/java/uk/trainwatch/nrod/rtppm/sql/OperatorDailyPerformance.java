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

import java.sql.ResultSet;
import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.util.sql.SQLFunction;

/**
 * The Operator performance as stored in the database
 *
 * @author peter
 */
@XmlRootElement(name = "operatorPerformance")
public class OperatorDailyPerformance
        extends AbstractOperatorPerformance
        implements Comparable<OperatorDailyPerformance>
{

    private static final long serialVersionUID  = 1L;

    public static final SQLFunction<ResultSet, OperatorDailyPerformance> fromSQL = rs -> new OperatorDailyPerformance(
            rs.getLong( 1 ),
            rs.getLong( 2 ),
            rs.getInt( 3 ),
            rs.getInt( 4 ),
            rs.getInt( 5 ),
            rs.getInt( 6 ),
            rs.getInt( 7 ),
            rs.getInt( 8 ),
            rs.getInt( 9 )
    );

    public OperatorDailyPerformance()
    {
    }

    public OperatorDailyPerformance( long id, long date, int operatorId, int run,
                                     int ontime, int late, int canceled, int ppm,
                                     int rollingPpm )
    {
        super( id, date, operatorId, run, ontime, late, canceled, ppm,
               rollingPpm );
    }

    @Override
    public int compareTo( OperatorDailyPerformance o )
    {
        return Long.compare( getDate(), o.getDate() );
    }

}
