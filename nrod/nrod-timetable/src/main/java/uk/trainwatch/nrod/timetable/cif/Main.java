/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif;

import uk.trainwatch.nrod.timetable.cif.record.CIFParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAction;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.model.ScheduleBuilderVisitor;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.counter.CounterConsumer;

/**
 * A test application which just parses a local CIF weekly file.
 * <p>
 * It's left here as it's simpler to use &amp; it's not a unit test as you need the full daily 567Mb uncompressed CIF
 * file to run it against.
 * <p>
 * When running it expects the file {@code CIF_ALL_FULL_DAILY-toc-full.CIF} to be in your home directory.
 * <p>
 * @author Peter T Mount
 */
public class Main
{

    private static final File homeDir = new File( System.getProperty( "user.home" ) );
    private static final File cifFile = new File( homeDir, "CIF_ALL_FULL_DAILY-toc-full.CIF" );

    public static void main( String... args )
            throws Exception
    {
        try
        {
            parseFile();

            // Comment out the one above & one of these for specific tests
            //parseFileBitBucket();
            //findUnsupportedTimingLoads();
            //findUnsupportedATOCCode();
        }
        catch( Exception ex )
        {
            ex.printStackTrace( System.out );
        }

        // Dummy, causes netbeans not to collaps the output on success
        throw new RuntimeException();
    }

    /**
     * Shows parsing the CIF file into a ScheduleBuilder
     * <p>
     * @throws IOException
     */
    private static void parseFile()
            throws IOException
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        // In this example we ditch the Schedule's and just count them
        CounterConsumer<Schedule> scheduleCounter = new CounterConsumer();
        CounterConsumer<TIPLOCAction> tiplocCounter = new CounterConsumer<>();

        // Our builder which builds Schedules and sends to the counter
        final ScheduleBuilderVisitor builder = new ScheduleBuilderVisitor( scheduleCounter, tiplocCounter );

        // Stream from the file
        System.out.println( "Parsing file to ScheduleBuilderVisitor..." );
        Files.lines( cifFile.toPath() ).
                map( parser::parse ).
                filter( Objects::nonNull ).
                forEach( r -> r.accept( builder ) );

        System.out.println( "Processed "
                            + scheduleCounter.get() + " schedules, "
                            + tiplocCounter.get() + " tiploc entries." );
    }

    /**
     * An example parser which simply sinks the values, useful for testing the parser against the entire set
     */
    private static void parseFileBitBucket()
            throws IOException
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        // Stream from the file
        System.out.println( "Parsing file to bit bucket..." );
        Files.lines( cifFile.toPath() ).
                // parse the next record
                map( parser::parse ).
                // Filter nulls i.e. unsupported/not yet implemented records
                filter( Objects::nonNull ).
                // Dump to the bit bucket
                forEach( Consumers.sink() );
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
    private static void findUnsupportedTimingLoads()
            throws IOException
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        System.out.println( "Scanning for unsupported TimingLoad's..." );
        Files.lines( cifFile.toPath() ).
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
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // This is formatted so can be pasted into TimingLoad source
                forEach( c -> System.out.printf( "/**\n/* Class %1$s\n*/\nC%1$s(\"%1$s\",\"Class %1$s\"),\n", c ) );
    }

    private static void findUnsupportedATOCCode()
            throws IOException
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        System.out.println( "Scanning for unsupported ATOCCode's..." );
        Files.lines( cifFile.toPath() ).
                map( parser::parse ).
                map( Functions.castTo( BasicScheduleExtras.class ) ).
                filter( Objects::nonNull ).
                // Filter for the unkown value
                filter( s -> s.getAtocCode() == ATOCCode.UNKNOWN ).
                // Swap back the original line from the file
                map( parser::currentLine ).
                // Manually extract the field
                map( l -> l.substring( 11, 13 ) ).
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // This is formatted so can be pasted into TimingLoad source
                forEach( c -> System.out.printf( "/**\n/* %1$s\n*/\n%1$s(\"%1$s\",\"%1$s\"),\n", c ) );
    }

    private static void findUnsupportedOperatingCharacteristics()
            throws IOException
    {
        // Strict mode so we fail on an invalid record type
        final CIFParser parser = new CIFParser( true );

        System.out.println( "Scanning for unsupported OperatingCharacteristics's..." );
        Files.lines( cifFile.toPath() ).
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
                // collect into a set then report findings
                collect( Collectors.toCollection( () -> new TreeSet() ) ).
                // This is formatted so can be pasted into TimingLoad source
                forEach( System.out::println );
        //forEach( c -> System.out.printf( "/**\n/* Class %1$s\n*/\nC%1$s(\"%1$s\",\"Class %1$s\"),\n", c ) );
    }

}
