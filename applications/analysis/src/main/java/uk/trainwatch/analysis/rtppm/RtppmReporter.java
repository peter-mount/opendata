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
package uk.trainwatch.analysis.rtppm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.nrod.rtppm.factory.RTPPMDataMsgFactory;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.nrod.rtppm.sql.OperatorPPMSQL;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.app.Application;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
public class RtppmReporter
{

    private static Properties jdbcProps;

    private static Connection getConnection()
            throws SQLException
    {
        return DriverManager.getConnection( jdbcProps.getProperty( "url" ),
                                            jdbcProps.getProperty( "username" ),
                                            jdbcProps.getProperty( "password" )
        );
    }

    public static void setup( RabbitConnection rabbitmq )
            throws IOException
    {
        jdbcProps = Application.loadProperties( "rtppm.properties" );

        Consumer<RTPPMDataMsg> recorder = m ->
        {
            try
            {
                try( Connection con = getConnection() )
                {
                    OperatorPPMSQL.INSERT_OPERATORPAGEPPM.accept( con, m );
                }
            }
            catch( SQLException |
                   UncheckedSQLException ex )
            {
                Logger.getLogger( RtppmReporter.class.getName() ).
                        log( Level.SEVERE, "Failure in rtppm", ex );
            }
        };

        Consumer<RTPPMDataMsg> rtppmMonitor = RateMonitor.log( RtppmReporter.class,
                                                               "rtppm messages recorded" );

        // The daily tweet of performance
        //Consumer<RTPPMDataMsg> dailyTweet = new RtppmTweet( rabbitmq, jdbcProps );

        Consumer<RTPPMDataMsg> consumer = Consumers.andThen( recorder, rtppmMonitor /*, dailyTweet*/ );

        RabbitMQ.queueDurableStream( rabbitmq,
                              "analyser.rtppm.db",
                              "nr.rtppm",
                              s -> s.map( RabbitMQ.toString.andThen( JsonUtils.parseJsonObject ) ).
                              map( RTPPMDataMsgFactory.INSTANCE ).
                              forEach( consumer )
        );
    }

}
