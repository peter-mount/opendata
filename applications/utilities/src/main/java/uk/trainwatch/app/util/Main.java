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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.UnrecognizedOptionException;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.app.BaseApplication;

/**
 *
 * @author Peter T Mount
 */
public class Main
        extends BaseApplication
{

    public static void main( String... args )
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

        if( args.length == 0 )
        {
            LOG.log( Level.INFO, toolNames );
            return;
        }

        String toolName = args[0];

        if( "-?".equals( toolName ) || "--help".equals( toolName ) )
        {
            HelpFormatter hf = new HelpFormatter();
            tools.keySet().
                    stream().
                    sorted().
                    map( tools::get ).
                    filter( Objects::nonNull ).
                    forEach( showHelp );
        }
        else
        {

            Utility util = tools.get( toolName );
            if( util == null )
            {
                LOG.log( Level.SEVERE, () -> "No utility called " + toolName );
                LOG.log( Level.INFO, toolNames );
                return;
            }

            String toolArgs[] = Arrays.copyOfRange( args, 1, args.length );

            if( toolArgs.length == 0 || "-?".equals( toolArgs[0] ) || "--help".equals( toolArgs[0] ) )
            {
                new HelpFormatter().printHelp( util.getName(), util.getOptions() );
            }
            else
            {
                try
                {
                    CommandLineParser parser = new BasicParser();
                    CommandLine cmd = parser.parse( util.getOptions(), toolArgs );
                    if( util.parseArgs( cmd ) )
                    {
                        util.call();
                    }
                    else
                    {
                        LOG.log( Level.WARNING, () -> "Failed to parse args " + cmd );
                        showHelp.accept( util );
                    }
                }
                catch( UnrecognizedOptionException ex )
                {
                    LOG.log( Level.WARNING, ex, ex::getMessage );
                    showHelp.accept( util );
                }
                catch( UncheckedSQLException ex )
                {
                    LOG.log( Level.SEVERE, null, ex.getCause() );
                }
                catch( Exception ex )
                {
                    LOG.log( Level.SEVERE, null, ex );
                }
            }
        }
    }

}
