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
package uk.trainwatch.apps.signalanalyser;

import java.io.IOException;
import java.util.Properties;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.util.app.Application;
import static uk.trainwatch.util.app.BaseApplication.loadProperties;

/**
 *
 * @author peter
 */
public abstract class AbstractApp
        extends Application
{

    private RabbitConnection rabbitmq;

    protected final RabbitConnection getRabbitmq()
    {
        return rabbitmq;
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

}
