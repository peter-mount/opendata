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
package uk.trainwatch.archiver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import static uk.trainwatch.archiver.TrustMvtAllArchiver.LOG;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.sql.Database;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class Archiver
        implements ServletContextListener
{

    private static final File REPORT_DIR = new File( "/usr/local/networkrail/reports" );

    @Inject
    private Rabbit rabbit;

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        // Trust movements
        rabbit.queueDurableConsumer( "archiver.nrod.trust", "nr.trust.mvtall",
                                     RabbitMQ.toString.andThen( l -> l + "\n" ),
                                     Consumers.guard( LOG, FileRecorder.recordTo(
                                                              new DatePathMapper( "/usr/local/networkrail", "trust/raw", true )
                                                      ) )
        );

        // RTPPM
        rabbit.queueDurableConsumer( "archiver.nrod.rtppm", "nr.rtppm",
                                     RabbitMQ.toString,
                                     Consumers.guard( LOG, FileRecorder.recordTo(
                                                              new DatePathMapper( "/usr/local/networkrail", "rtppm/raw" )
                                                      ) )
        );

        // TD
        rabbit.queueDurableConsumer( "archiver.nrod.td", "nr.td.all",
                                     RabbitMQ.toString,
                                     Consumers.guard( LOG, FileRecorder.recordTo(
                                                              new DatePathMapper( "/usr/local/networkrail", "td", true )
                                                      ) )
        );

        // Generated Reports
        rabbit.queueDurableConsumer( "archiver.report", "report.#",
                                     RabbitMQ.toJsonObject,
                                     Consumers.guard( LOG, new FileRecorder<>( Archiver::getPath,
                                                                               JsonUtils.jsonObjectToString,
                                                                               StandardOpenOption.CREATE, StandardOpenOption.WRITE )
                                     )
        );

    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

    private static String getRecordPath( JsonObject t )
    {
        String recordPath = JsonUtils.getString( t, "recordPath" );
        if( recordPath == null ) {
            return null;
        }

        recordPath = recordPath.trim();

        return recordPath.isEmpty() || recordPath.contains( ".." ) || recordPath.contains( "/." ) ? null : recordPath;
    }

    private static Path getPath( JsonObject t )
    {
        final String recordPath = getRecordPath( t );
        if( recordPath == null ) {
            return null;
        }
        File file = new File( REPORT_DIR, recordPath + ".json" );
        file.getParentFile().
                mkdirs();

        return file.toPath();
    }
}
