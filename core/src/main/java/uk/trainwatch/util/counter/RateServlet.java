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
package uk.trainwatch.util.counter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.BinaryOperator;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;

/**
 * Simple servlet used to expose the rate statistics.
 * <p>
 * It will return an outer json object:
 * <table>
 * <tr><th>Key</th><th>type</th><th>Content</th></tr>
 * <tr><td>title</td><td>String</td><td>Name of this instance</td></tr>
 * <tr><td>time</td><td>LocalDateTime</td><td>The time of this snapshot</td></tr>
 * <tr><td>uptime</td><td>Duration</td><td>Duration since this instance was started</td></tr>
 * <tr><td>start</td><td>LocalDateTime</td><td>Time of when this instance was started</td></tr>
 * <tr><td>stats</td><td>Array</td><td>The statistics</td></tr>
 * </table>
 * <p>
 * The stats object consists of 0 or more objects:
 * <table>
 * <tr><th>Key</th><th>type</th><th>Content</th></tr>
 * <tr><td>name</td><td>String</td><td>Name of statistic</td></tr>
 * <tr><td>hour</td><td>Object</td><td>last hour, 1 minute per sample</td></tr>
 * <tr><td>day</td><td>Object</td><td>last day, 15 minutes per sample</td></tr>
 * </table>
 * <p>
 * The hour and day objects take this form:
 * <table>
 * <tr><th>Key</th><th>type</th><th>Content</th></tr>
 * <tr><td>time</td><td>LocalDateTime</td><td>The time of last entry</td></tr>
 * <tr><td>low</td><td>boolean</td><td>true if currently below the low limit</td></tr>
 * <tr><td>lowValue</td><td>int</td><td>Value of the low limit</td></tr>
 * <tr><td>high</td><td>boolean</td><td>true if currently above the high limit</td></tr>
 * <tr><td>highValue</td><td>int</td><td>Value of the high limit</td></tr>
 * <tr><td>values</td><td>Array</td><td>The values, current value last</td></tr>
 * <tr><td>current</td><td>int</td><td>The current value</td></tr>
 * <tr><td>min</td><td>int</td><td>Max value</td></tr>
 * <tr><td>max</td><td>int</td><td>Min value</td></tr>
 * </table>
 * <p>
 * @author peter
 */
@WebServlet(name = "RateServlet", urlPatterns = "/admin/api/rate", loadOnStartup = 1)
public class RateServlet
        extends HttpServlet
{

    private static final BinaryOperator<JsonArrayBuilder> COMBINER = ( a, b ) -> a;

    private LocalDateTime startTime;

    @Inject
    private RateStatistics rateStatistics;

    @Override
    public void init( ServletConfig config )
            throws ServletException
    {
        super.init( config );

        startTime = TimeUtils.getLocalDateTime();
    }

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException,
                   IOException
    {
        LocalDateTime now = TimeUtils.getLocalDateTime();
        Duration uptime = Duration.between( startTime, now );

        JsonObject result = Json.createObjectBuilder()
                .add( "title", rateStatistics.getTitle() )
                .add( "time", now.toString() )
                .add( "uptime", uptime.toString() )
                .add( "start", startTime.toString() )
                .add( "stats", rateStatistics.stream().reduce( Json.createArrayBuilder(),
                                                               ( a, v ) -> a.add(
                                                                       Json.createObjectBuilder()
                                                                       .add( "name", v.getName() )
                                                                       .add( "hour", v.getLastHour().toJsonObjectBuilder() )
                                                                       .add( "day", v.getLastDay().toJsonObjectBuilder() )
                                                               ),
                                                               COMBINER ) )
                .build();

        byte b[] = JsonUtils.toString.apply( result ).getBytes( "UTF-8" );

        resp.setContentType( MediaType.APPLICATION_JSON );
        resp.setContentLength( b.length );
        resp.addHeader( "Access-Control-Allow-Origin", "*" );

        resp.getOutputStream().write( b );
    }

}
