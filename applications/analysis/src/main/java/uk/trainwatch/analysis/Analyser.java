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
package uk.trainwatch.analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import uk.trainwatch.analysis.rtppm.RtppmReporter;
import uk.trainwatch.analysis.rtppm.TrustLogger;
import uk.trainwatch.nrod.rtppm.factory.RTPPMDataMsgFactory;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.nrod.rtppm.sql.OperatorPPMSQL;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class Analyser
        implements ServletContextListener
{

    @Inject
    private Rabbit rabbit;

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private TrustLogger trustLogger;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        // TrustDispatcher is now done in the main feed

        rabbit.queueDurableConsumer( "analyser.rtppm.db", "nr.rtppm", RabbitMQ.toJsonObject.andThen( RTPPMDataMsgFactory.INSTANCE ), this::recordRtppm );

        // Don't need this now
//        rabbit.queueDurableConsumer( "analyser.trust.movement.logger", "nr.trust.mvt",
//                                     RabbitMQ.toJsonObject,
//                                     new TrainReporter( rabbit.publishJson( "report.movement" ) )
//        );
        rabbit.queueDurableConsumer( "analyser.trust.db", "nr.trust.mvt", RabbitMQ.toString, SQLConsumer.guard( trustLogger ) );
    }

    @Override

    public void contextDestroyed( ServletContextEvent sce )
    {
    }

    private void recordRtppm( RTPPMDataMsg m )
    {
        try {
            try( Connection con = dataSource.getConnection() ) {
                OperatorPPMSQL.INSERT_OPERATORPAGEPPM.accept( con, m );
            }
        }
        catch( SQLException |
               UncheckedSQLException ex ) {
            Logger.getLogger( RtppmReporter.class.getName() ).
                    log( Level.SEVERE, "Failure in rtppm", ex );
        }
    }
}
