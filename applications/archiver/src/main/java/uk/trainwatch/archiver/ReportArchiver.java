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
package uk.trainwatch.archiver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.json.JsonObject;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;

/**
 * Archives any generated reports made by the system
 * <p>
 * @author Peter T Mount
 */
public class ReportArchiver
{

    private static final File REPORT_DIR = new File( "/usr/local/networkrail/reports" );

    protected static final Logger LOG = Logger.getLogger( ReportArchiver.class.getName() );

    public static void setup( RabbitConnection rabbitmq )
    {
        Consumer<JsonObject> reportMonitor = RateMonitor.log( LOG, "report archives" );

        // Our recorder, this one does not append but replace the file
        Consumer<JsonObject> recorder = new FileRecorder<>( ReportArchiver::getPath,
                                                            JsonUtils.jsonObjectToString,
                                                            StandardOpenOption.CREATE, StandardOpenOption.WRITE );

        RabbitMQ.queueDurableStream( rabbitmq, "archiver.report", "report.#",
                                     s -> s.map( RabbitMQ.toString.andThen( JsonUtils.parseJsonObject ) ).
                                     filter( Objects::nonNull ).
                                     peek( reportMonitor ).
                                     forEach( Consumers.guard( recorder ) )
        );
    }

    private static String getRecordPath( JsonObject t )
    {
        String recordPath = JsonUtils.getString( t, "recordPath" );
        if( recordPath == null )
        {
            return null;
        }

        recordPath = recordPath.trim();

        return recordPath.isEmpty() || recordPath.contains( ".." ) || recordPath.contains( "/." ) ? null : recordPath;
    }

    private static Path getPath( JsonObject t )
    {
        final String recordPath = getRecordPath( t );
        if( recordPath == null )
        {
            return null;
        }
        File file = new File( REPORT_DIR, recordPath + ".json" );
        file.getParentFile().
                mkdirs();

        return file.toPath();
    }
}
