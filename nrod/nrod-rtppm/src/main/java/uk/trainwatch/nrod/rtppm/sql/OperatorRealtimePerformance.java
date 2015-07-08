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
 *
 * @author peter
 */
@XmlRootElement(name = "operatorRealtimePerformance")
public class OperatorRealtimePerformance
        extends AbstractOperatorPerformance
        implements Comparable<OperatorRealtimePerformance>
{

    private static final long serialVersionUID  = 1L;

    public static final SQLFunction<ResultSet, OperatorRealtimePerformance> fromSQL = rs -> new OperatorRealtimePerformance(
            rs.getLong( 1 ),
            rs.getLong( 2 ),
            rs.getInt( 3 ),
            rs.getInt( 4 ),
            rs.getInt( 5 ),
            rs.getInt( 6 ),
            rs.getInt( 7 ),
            rs.getInt( 8 ),
            rs.getInt( 9 ),
            rs.getInt( 10 )
    );

    private int time;

    public OperatorRealtimePerformance()
    {
    }

    public OperatorRealtimePerformance( long id,
                                        long date,
                                        int time,
                                        int operatorId, int run, int ontime,
                                        int late, int canceled, int ppm,
                                        int rollingPpm )
    {
        super( id, date, operatorId, run, ontime, late, canceled, ppm,
               rollingPpm );
        this.time = time;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime( int time )
    {
        this.time = time;
    }

    @Override
    public int compareTo( OperatorRealtimePerformance o )
    {
        int r = Long.compare( getDate(), o.getDate() );
        if( r == 0 ) {
            r = Integer.compare( time, o.getTime() );
        }
        return r;
    }

}
