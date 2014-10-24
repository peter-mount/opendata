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
package uk.trainwatch.nrod.timetable.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.CIFParser;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.counter.CounterConsumer;

/**
 * Utility which checks the CIF file for data types that we currently don't support in various enums
 * <p>
 * @author Peter T Mount
 */
@MetaInfServices( Utility.class )
public class TimeTableChecker
        implements Utility
{

    private static final Logger LOG = Logger.getLogger( TimeTableChecker.class.getName() );
    private final Options options;

    private Consumer<Path> consumer;
    private List<Path> cifFiles;

    public TimeTableChecker()
    {
        this.options = new Options().
                addOption( null, "timingload", false, "Check for unsupported TimingLoad/Train class in CIF file" ).
                addOption( null, "atoccode", false, "Check for unsupported/new ATOC code for TOC's" ).
                addOption( null, "operatingcharacteristics", false, "Check for unsupported Operating Characteristics" );
    }

    @Override
    public final Options getOptions()
    {
        return options;
    }

    @Override
    public boolean parseArgs( CommandLine cmd )
    {

        // Form a consumer chain of the operations required
        Consumer<Path> c = null;

        if( cmd.hasOption( "timingload" ) )
        {
            c = Consumers.andThen( c, this::findUnsupportedTimingLoads );
        }

        if( cmd.hasOption( "atoccode" ) )
        {
            c = Consumers.andThen( c, this::findUnsupportedATOCCode );
        }

        if( cmd.hasOption( "operatingcharacteristics" ) )
        {
            c = Consumers.andThen( c, this::findUnsupportedOperatingCharacteristics );
        }

        if( c == null )
        {
            LOG.severe( "No options provided" );
            return false;
        }

        // Start by logging the filename then the actions
        consumer = Consumers.andThen( p -> LOG.log( Level.INFO, () -> "Parsing " + p ), c );

        // Now all remaining arguments are CIF files
        List<String> args = cmd.getArgList();
        cifFiles = args.stream().
                map( File::new ).
                map( File::toPath ).
                collect( Collectors.toList() );

        return !cifFiles.isEmpty();
    }

    @Override
    public void runUtility()
            throws Exception
    {
        cifFiles.forEach( consumer );
    }

    private Stream<String> lines( Path p )
    {
        try
        {
            return Files.lines( p );
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to parse " + p );
            return Stream.empty();
        }
    }

    /**
     * TimingLoad maps EMU types but also train class types.
     * <p>
     * Under normal use, TimingLoad will return {@link TimingLoad#UNKNOWN} for anything it doesn't know about.
     * <p>
     * So, to support new types, this shows how to scan for unsupported classes and generates the source so you can past
     * it into TimingLoad.
     * <p>
     * @throws IOException
     */
    private void findUnsupportedTimingLoads( final Path cifFile )
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        CounterConsumer<String> found = new CounterConsumer<>();

        LOG.log( Level.INFO, "Scanning for unsupported TimingLoad's..." );

        lines( cifFile ).
                map( parser::parse ).
                map( Functions.castTo( BasicSchedule.class ) ).
                filter( Objects::nonNull ).
                // Filter for the unkown value
                filter( s -> s.getTimingLoad() == TimingLoad.UNKNOWN ).
                // Swap back the original line from the file
                map( parser::currentLine ).
                // Manually extract the field
                map( l -> l.substring( 53, 57 ) ).
                // Filter so we only have valid classes
                filter( s -> s.matches( "\\d\\d\\d " ) ).
                // Trim
                map( String::trim ).
                // Count it for the result
                peek( found ).
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // This is formatted so can be pasted into TimingLoad source
                forEach( c -> System.out.printf( "/**\n/* Class %1$s\n*/\nC%1$s(\"%1$s\",\"Class %1$s\"),\n", c ) );

        LOG.log( Level.INFO, () -> "Found " + found.get() + " unknown entries" );
    }

    private void findUnsupportedATOCCode( final Path cifFile )
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        CounterConsumer<String> found = new CounterConsumer<>();

        LOG.log( Level.INFO, "Scanning for unsupported ATOCCode's..." );

        lines( cifFile ).
                map( parser::parse ).
                map( Functions.castTo( BasicScheduleExtras.class ) ).
                filter( Objects::nonNull ).
                // Filter for the unkown value
                filter( s -> s.getAtocCode() == ATOCCode.UNKNOWN ).
                // Swap back the original line from the file
                map( parser::currentLine ).
                // Manually extract the field
                map( l -> l.substring( 11, 13 ) ).
                // Count it for the result
                peek( found ).
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // This is formatted so can be pasted into ATOCCode source
                forEach( c -> System.out.printf( "/**\n/* %1$s\n*/\n%1$s(\"%1$s\",\"%1$s\"),\n", c ) );

        LOG.log( Level.INFO, () -> "Found " + found.get() + " unknown entries" );
    }

    private void findUnsupportedOperatingCharacteristics( final Path cifFile )
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        CounterConsumer<String> found = new CounterConsumer<>();

        LOG.log( Level.INFO, "Scanning for unsupported OperatingCharacteristics's..." );

        lines( cifFile ).
                map( parser::parse ).
                map( Functions.castTo( BasicSchedule.class ) ).
                filter( Objects::nonNull ).
                // Filter for the unkown value
                filter( s -> s.getOperatingCharacteristics().length != 6 ).
                // Swap back the original line from the file
                map( parser::currentLine ).
                // Manually extract the field
                map( l -> l.substring( 60, 66 ) ).
                // Split stream into individual characters
                flatMap( l -> Stream.of( l.split( "." ) ) ).
                // Filter out " "
                filter( c -> !c.equals( " " ) ).
                // Filter those which map to unknown
                filter( c -> OperatingCharacteristics.lookup( c ) == OperatingCharacteristics.UNKNOWN ).
                // Count it for the result
                peek( found ).
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // Write to stdout
                forEach( System.out::println );

        LOG.log( Level.INFO, () -> "Found " + found.get() + " unknown entries" );
    }

}
