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
package uk.trainwatch.app.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.app.Utility;

/**
 *
 * @author Peter T Mount
 */
@MetaInfServices( Utility.class )
public class Memo
        implements Utility
{

    protected static final Logger LOG = Logger.getLogger( Memo.class.getName() );

    private final Options options;

    private RabbitConnection con;
    private String title;
    private String tweetAs;
    private String user;
    private String hash;
    private List<String> files;
    private LocalDateTime expires;
    private String type;

    public Memo()
    {
        this.options = new Options().
                addOption( null, "rabbituser", true, "RabbitMQ username" ).
                addOption( null, "rabbitpassword", true, "RabbitMQ password" ).
                addOption( null, "rabbithost", true, "RabbitMQ hostname" ).
                addOption( null, "title", true, "Memo title" ).
                addOption( null, "tweet", true, "tweet the memo as user" ).
                addOption( null, "user", true, "a51.li user" ).
                addOption( null, "hash", true, "a51.li hash" ).
                addOption( null, "expires", true, "Expiry in days" ).
                addOption( null, "type", true, "Memo type, defaults to text" );
    }

    @Override
    public Options getOptions()
    {
        return options;
    }

    @Override
    public boolean parseArgs( CommandLine cmd )
    {
        LOG.log( Level.INFO, "Connecting to rabbit" );
        con = new RabbitConnection( cmd.getOptionValue( "rabbituser" ),
                                    cmd.getOptionValue( "rabbitpassword" ),
                                    cmd.getOptionValue( "rabbithost", "mq.iot.onl" ) );
        title = cmd.getOptionValue( "title" );
        tweetAs = cmd.getOptionValue( "tweet" );
        user = cmd.getOptionValue( "user" );
        hash = cmd.getOptionValue( "hash" );

        type = cmd.getOptionValue( "type", "TEXT" );
        
        String exp = cmd.getOptionValue( "expires" );
        if( exp != null )
        {
            expires = LocalDateTime.now().
                    plusDays( Long.parseLong( exp ) );
        }

        files = cmd.getArgList();

        LOG.log( Level.INFO, "Files {0}", files );

        return true;
    }

    @Override
    public void runUtility()
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising" );

        Consumer<? super JsonStructure> consumer = RabbitMQ.jsonConsumer( con, "a51.li.memo" );

        for( String fileName : files )
        {
            String t = title;
            if( t == null )
            {
                t = fileName;
            }

            LOG.log( Level.INFO, "Reading {0}", fileName );

            String text;
            try( BufferedReader r = new BufferedReader( new FileReader( fileName ) ) )
            {
                text = r.lines().
                        collect( Collectors.joining( "<br/>" ) );
            }

            LOG.log( Level.INFO, "Generating json" );

            JsonObjectBuilder b = Json.createObjectBuilder().
                    add( "user", user ).
                    add( "hash", hash ).
                    add( "type", type ).
                    add( "title", t ).
                    add( "text", text );

            if( expires != null )
            {
                b.add( "expires", expires.toString() );
            }

            if( tweetAs != null )
            {
                b.add( "tweetAs", tweetAs ).
                        add( "tweet", t );
            }

            JsonObject o = b.build();

            LOG.log( Level.INFO, "Tweeting {0}", o );

            consumer.accept( o );

            LOG.log( Level.INFO, "Done" );
        }

        System.exit( 0 );
    }

}
