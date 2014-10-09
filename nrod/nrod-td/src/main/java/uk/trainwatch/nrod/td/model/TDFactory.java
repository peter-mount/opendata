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
package uk.trainwatch.nrod.td.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 * Factories for creating TDMessage's
 * <p>
 * @author Peter T Mount
 */
enum TDFactory
        implements Function<JsonObject, TDMessage>
{

    BERTH_STEP
            {

                @Override
                public BerthStep apply( JsonObject t )
                {
                    BerthStep s = new BerthStep();
                    init( t, s );
                    s.setFrom( JsonUtils.getString( t, "from" ) );
                    s.setTo( JsonUtils.getString( t, "to" ) );
                    s.setDescr( JsonUtils.getString( t, "descr" ) );
                    return s;
                }
            },
    BERTH_CANCEL
            {

                @Override
                public BerthCancel apply( JsonObject t )
                {
                    BerthCancel s = new BerthCancel();
                    init( t, s );
                    s.setFrom( JsonUtils.getString( t, "from" ) );
                    s.setDescr( JsonUtils.getString( t, "descr" ) );
                    return s;
                }
            },
    BERTH_INTERPOSE
            {

                @Override
                public BerthInterpose apply( JsonObject t )
                {
                    BerthInterpose s = new BerthInterpose();
                    init( t, s );
                    s.setTo( JsonUtils.getString( t, "to" ) );
                    s.setDescr( JsonUtils.getString( t, "descr" ) );
                    return s;
                }
            },
    HEARTBEAT
            {

                @Override
                public Heartbeat apply( JsonObject t )
                {
                    Heartbeat s = new Heartbeat();
                    init( t, s );
                    s.setReport_time( JsonUtils.getString( t, "report_time" ) );
                    return s;
                }
            },
    // S class messages
    SIGNAL_UPDATE
            {

                @Override
                public SignalUpdate apply( JsonObject t )
                {
                    SignalUpdate s = new SignalUpdate();
                    initSignal( t, s );
                    return s;
                }
            },
    SIGNAL_REFRESH
            {

                @Override
                public SignalRefresh apply( JsonObject t )
                {
                    SignalRefresh s = new SignalRefresh();
                    initSignal( t, s );
                    return s;
                }
            },
    SIGNAL_REFRESH_END
            {

                @Override
                public SignalEndRefresh apply( JsonObject t )
                {
                    SignalEndRefresh s = new SignalEndRefresh();
                    initSignal( t, s );
                    return s;
                }
            };

    @Override
    public abstract TDMessage apply( JsonObject t );

    protected final void init( JsonObject t, TDMessage m )
    {
        m.setAreaId( JsonUtils.getString( t, "area_id" ) );
        m.setTime( JsonUtils.getLong( t, "time" ) );
    }

    protected final void initSignal( JsonObject t, SignalMessage m )
    {
        init( t, m );
        m.setAddress( JsonUtils.getString( t, "address" ) );
        m.setData( JsonUtils.getString( t, "data" ) );
        m.setReport_time( JsonUtils.getLocalTime( t, "report_time" ) );
    }

}
