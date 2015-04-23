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
package uk.trainwatch.analysis;

import uk.trainwatch.analysis.rtppm.RtppmReporter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import uk.trainwatch.analysis.rtppm.TrustLogger;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.util.app.Application;
import static uk.trainwatch.util.app.Application.loadProperties;

/**
 *
 * @author Peter T Mount
 */
public class Main
        extends Application
{

    private RabbitConnection rabbitmq;

    public static void main( String... args )
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising Rail Analyser" );

        new Main().run();
    }

    @Override
    protected void setupBrokers()
            throws IOException
    {
        Properties p = loadProperties( "rabbit.properties" );

        rabbitmq = new RabbitConnection( p.getProperty( "username" ),
                                         p.getProperty( "password" ),
                                         p.getProperty( "host" ) );
    }

    @Override
    protected void stop()
    {
        super.stop();
        rabbitmq.close();
    }

    @Override
    protected void setupApplication()
            throws IOException
    {
        RtppmReporter.setup( rabbitmq );
        //TocPerformance.setup( rabbitmq );
        TrustDispatcher.setup( rabbitmq );
        TrainMovementLogger.setup( rabbitmq );
        TrustLogger.setup( rabbitmq );
    }

}
