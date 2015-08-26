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
package uk.trainwatch.nre.darwin;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class DarwinDBImport
        implements ServletContextListener
{

    private static final String QUEUE = "darwin.db";
    private static final String ROUTING_KEY = "nre.push";

    @Inject
    private Rabbit rabbit;

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        rabbit.queueDurableConsumer( QUEUE, ROUTING_KEY, RabbitMQ.toString,
                                     SQLConsumer.guard( new DarwinImport( dataSource ) ).
                                     andThen( Consumers.guard( FileRecorder.recordTo(
                                                             new DatePathMapper( "/usr/local/networkrail/darwin", "pport", true )
                                                     ) ) )
        );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

}