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

import uk.trainwatch.util.app.Utility;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.UnrecognizedOptionException;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.app.BaseApplication;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import uk.trainwatch.util.CDIUtils;
import uk.trainwatch.util.LoggingUtils;

/**
 *
 * @author Peter T Mount
 */
public class Main
        extends BaseApplication
{

    private static final Logger LOG = Logger.getLogger( "main" );

    public static void main( String... args )
            throws Exception
    {
        System.setProperty( "org.jboss.logging.provider", "jdk" );
        System.setProperty( "hazelcast.logging.type", "jdk" );
        LoggingUtils.setLevel( Level.INFO );
        LOG.info( "Initializing utilities..." );

        LoggingUtils.setLevel( Level.SEVERE );
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();

        LoggingUtils.setLevel( Level.INFO );
        int rc = 1;
        try {
            rc = run( args );
        }
        finally {
            LOG.log( Level.INFO, "Return code: {0}", rc );
            LoggingUtils.setLevel( Level.SEVERE );
            weld.shutdown();
        }

        LoggingUtils.setLevel( Level.INFO );
        LOG.log( Level.INFO, "Exiting" );

        System.exit( rc );
    }

    private static int run( String... args )
            throws Exception
    {

        // Load all of the utility implementations
        ServiceLoader<Utility> loader = ServiceLoader.load( Utility.class );
        Map<String, Utility> tools = StreamSupport.stream( loader.spliterator(), false ).
                collect( Collectors.toMap( Utility::getName, Function.identity() ) );

        Supplier<String> toolNames = () -> tools.keySet().
                stream().
                sorted().
                collect( Collectors.joining( ", ", "Available tools: ", "" ) );

        Consumer<Utility> showHelp = u -> new HelpFormatter().printHelp( u.getName(), u.getOptions() );

        if( args.length == 0 ) {
            LOG.log( Level.INFO, toolNames );
            return 1;
        }

        String toolName = args[0];

        if( "-?".equals( toolName ) || "--help".equals( toolName ) ) {
            HelpFormatter hf = new HelpFormatter();
            tools.keySet().
                    stream().
                    sorted().
                    map( tools::get ).
                    filter( Objects::nonNull ).
                    forEach( showHelp );
        }
        else {
            Utility util = getUtility( tools, toolNames, toolName );
            if( util != null ) {

                String toolArgs[] = Arrays.copyOfRange( args, 1, args.length );

                if( toolArgs.length == 0 || "-?".equals( toolArgs[0] ) || "--help".equals( toolArgs[0] ) ) {
                    new HelpFormatter().printHelp( util.getName(), util.getOptions() );
                }
                else {
                    try {
                        CommandLineParser parser = new BasicParser();
                        CommandLine cmd = parser.parse( util.getOptions(), toolArgs );
                        if( util.parseArgs( cmd ) ) {
                            // Simple banner for identifying whats being run
                            LoggingUtils.logBanner( "Utility: " + util.getName() );
                            try {
                                CDIUtils.inject( util );
                                util.call();
                                return 0;
                            }
                            finally {
                                LoggingUtils.logBanner( "End run of " + util.getName() );
                            }
                        }
                        else {
                            LOG.log( Level.WARNING, () -> "Failed to parse args " + cmd );
                            showHelp.accept( util );
                        }
                    }
                    catch( UnrecognizedOptionException ex ) {
                        LOG.log( Level.WARNING, ex, ex::getMessage );
                        showHelp.accept( util );
                    }
                    catch( UncheckedSQLException ex ) {
                        LOG.log( Level.SEVERE, null, ex.getCause() );
                    }
                    catch( Exception ex ) {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
            }
        }
        return 1;
    }

    private static Utility getUtility( Map<String, Utility> tools, Supplier<String> toolNames, String toolName )
    {
        Utility util = tools.get( toolName );
        if( util == null ) {
            LOG.log( Level.SEVERE, () -> "No utility called " + toolName );
            LOG.log( Level.INFO, toolNames );
            return null;
        }

        return util;
    }
}
