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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class DarwinImport
        implements Consumer<String>
{

    private static final Logger LOG = Logger.getLogger( DarwinImport.class.getName() );

    private static final String QUEUE = "db.darwin";
    private static final String ROUTING_KEY = "nre.push";

    private static final String IMPORT_SQL = "SELECT darwin.darwinimport(?::xml)";

    @Inject
    private Rabbit rabbit;

    @Database("rail")
    @Inject
    private DataSource dataSource;

    private Consumer<String> monitor;

    @PostConstruct
    public void start()
    {
        monitor = RateMonitor.log( LOG, "darwin.db" );
        rabbit.queueDurableConsumer( QUEUE, ROUTING_KEY, RabbitMQ.toString, this );
    }

    @Override
    public void accept( String xml )
    {
        if( xml != null ) {
            try {
                try( Connection con = dataSource.getConnection();
                     PreparedStatement ps = SQL.prepare( con, IMPORT_SQL ) ) {
                    {
                        ps.setString( 1, xml );
                        try( ResultSet rs = ps.executeQuery() ) {

                        }
                    }
                }

                monitor.accept( xml );
            }
            catch( Throwable t ) {
                // Catch everything that fails, log it so we can verify again later
                LOG.log( Level.SEVERE, t, () -> "Failed to import:\n" + xml );
            }
        }
    }

}
