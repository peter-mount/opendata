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
package uk.trainwatch.analysis.rtppm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import uk.trainwatch.nrod.rtppm.model.OperatorPagePPM;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.nrod.rtppm.sql.Operator;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.app.Application;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author Peter T Mount
 */
public class RtppmTweet
        implements Consumer<RTPPMDataMsg>
{

    private static final Logger LOG = Logger.getLogger( RtppmTweet.class.getName() );

    private final Properties jdbcProps;
    private final Consumer<? super JsonStructure> twitterBot;

    private Connection getConnection()
            throws SQLException
    {
        return DriverManager.getConnection( jdbcProps.getProperty( "url" ),
                                            jdbcProps.getProperty( "username" ),
                                            jdbcProps.getProperty( "password" )
        );
    }

    private final Map<String, OperatorPagePPM> current = new ConcurrentHashMap<>();

    public RtppmTweet( RabbitConnection con, Properties jdbcProps )
    {
        this.jdbcProps = jdbcProps;
        twitterBot = RabbitMQ.jsonConsumer( con, "twitter.tweet" );
    }

    @Override
    public void accept( RTPPMDataMsg m )
    {
        Streams.stream( m.getOperatorPages() ).
                forEach( o ->
                        {
                            // Replace with the new one
                            OperatorPagePPM last = current.replace( o.getName(), o );

                            // When total drops it's usually the begining of the day
                            if( last != null
                                && last.getTotal() > o.getTotal()
                                && last.getTotal() > 0 )
                            {
                                tweet( last );
                            }
                } );
    }

    private Operator getOperator( final String name )
    {
        try( Connection con = getConnection() )
        {
            try( PreparedStatement s = con.prepareStatement( "SELECT * from rtppm.operator WHERE operator=?" ) )
            {
                s.setString( 1, name );
                return SQL.stream( s, Operator.fromSQL ).
                        findFirst().
                        orElse( null );
            }
        }
        catch( SQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to retrieve Operator " + name );
            return null;
        }
    }

    /**
     * Tweet the PPM
     * <p>
     * @param ppm
     */
    private void tweet( final OperatorPagePPM ppm )
    {
        Operator o = getOperator( ppm.getName() );
        if( o == null )
        {
            return;
        }

        // Get yesterday's date (as the day starts at 0200
        LocalDate date = LocalDate.now().
                minusDays( 1 );

        // The tweet text
        String tweet = String.format(
                "%s PPM for %04d %s %d: %d%% (%d%%). %d ran, %d ontime, %d late %d c/vlate %s",
                o.getDisplay(),
                date.getYear(),
                date.getMonth().
                getDisplayName( TextStyle.SHORT, Locale.ENGLISH ),
                date.getDayOfMonth(),
                ppm.getPpm().
                getValue(),
                ppm.getRollingPPM().
                getValue(),
                ppm.getTotal(),
                ppm.getOnTime(),
                ppm.getLate(),
                ppm.getCancelVeryLate(),
                o.getHashtag()
        );

        if( tweet.length() > 135 )
        {
            tweet = tweet.substring( 0, 135 ) + " …";
        }

        // This is low volume so reading it here is fine & allows us to change
        // on the fly
        Properties props;
        try
        {
            props = Application.loadProperties( "rtppm-twitter.properties" );
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, "No rtppm-twitter.properties?", ex );
            return;
        }

        twitterBot.accept( Json.createObjectBuilder().
                add( "user", props.getProperty( "user" ) ).
                add( "hash", props.getProperty( "hash" ) ).
                add( "tweetAs", props.getProperty( "tweetAs" ) ).
                add( "tweet", tweet ).
                build()
        );
    }

}
