/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementFactory;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.DBContextListener;

/**
 *
 * @author peter
 */
@WebListener
public class TrustContextListener
        extends DBContextListener
{

    private static final Logger LOG = Logger.getLogger( TrustContextListener.class.getName() );
    private Timer timer;

    private RabbitConnection rabbitConnection;

    @Override
    protected void init( ServletContextEvent sce )
            throws SQLException
    {
        LOG.log( Level.INFO, "Initialising Trust" );

        // Start the timer
        timer = new Timer( "TrustCache", true );
        timer.scheduleAtFixedRate( new TimerTask()
        {

            @Override
            public void run()
            {
                TrustCache.INSTANCE.expireCache();
            }
        }, 60000L, 60000L );

        LOG.log( Level.INFO, () -> "Connecting to MQ" );

        // Connect to rabbitmq
        try
        {
            rabbitConnection = new RabbitConnection(
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/user" ),
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/password" ),
                    InitialContext.doLookup( "java:/comp/env/rabbit/uktrain/host" )
            );

            Consumer<TrustMovement> consumer = m -> TrustCache.INSTANCE.getTrust( m.getToc_id(), m.getTrain_id() ).
                    accept( m );

            RabbitMQ.queueDurableStream( rabbitConnection, "trust.status", "nr.trust.mvt", s -> s.
                    map( RabbitMQ.toString ).
                    map( JsonUtils.parseJsonObject ).
                    map( TrustMovementFactory.INSTANCE ).
                    filter( Objects::nonNull ).
                    forEach( consumer.andThen( RateMonitor.log( LOG, "Receive Trust" ) ) ) );
        } catch( NamingException ex )
        {
            Logger.getLogger( TrustContextListener.class.getName() ).
                    log( Level.SEVERE, null, ex );
            throw new SQLException( ex );
        }

        LOG.log( Level.INFO, "Trust initialised" );
    }

    @Override
    protected void shutdown( ServletContextEvent sce )
    {
        LOG.log( Level.INFO, "Shutting down Trust" );

        try
        {
            if( rabbitConnection != null )
            {
                rabbitConnection.close();
                rabbitConnection = null;
            }
        } finally
        {
            if( timer != null )
            {
                timer.cancel();
                timer = null;
            }
        }
    }

}
