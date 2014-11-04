/*
 * Copyright 2014 Peter T Mount.
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
package uk.trainwatch.nrod.timetable.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.model.ScheduleJsonDecoder;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.sql.SQLFunction;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 * Function that will generate a {@link Schedule} from a {@link ResultSet}
 * <p>
 * @author Peter T Mount
 */
public enum ScheduleResultSetFactory
        implements SQLFunction<ResultSet, Schedule>
{

    INSTANCE;

    /**
     * The SQL that can be used with this {@link Function}
     */
    public static final String SELECT_SQL
                               = "SELECT s.schedule AS schedule FROM timetable.schedule s ";

    @Override
    public Schedule apply( ResultSet t )
    {
        try
        {
            JsonObject o = (JsonObject) JsonUtils.decode( t.getString( "schedule" ) );

            return ScheduleJsonDecoder.INSTANCE.apply( o );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

}
