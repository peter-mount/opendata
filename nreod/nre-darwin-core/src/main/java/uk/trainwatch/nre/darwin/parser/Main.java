/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * Temporary test to parse xml files from the NRE FTP site into the parser
 *
 * @author peter
 */
public class Main
{

    private static final Logger log = Logger.getLogger( Main.class.getName() );

    public static void main( String... args )
            throws Exception
    {
        tsMoreThanOne( args );

        // Stops netbeans from collapsing the output
        throw new RuntimeException();
    }

    /**
     * Can test data & report the maximum number of TS records in a single pPort message
     * <p>
     * @param args
     *             <p>
     * @throws Exception
     */
    private static void tsMaxSize( String... args )
            throws Exception
    {
        final File dir = new File( "/home/peter/nre" );
        System.out.println(
                Stream.of( dir.listFiles( n -> n.getName().startsWith( "pPortData.log" ) ) ).
                map( File::toPath ).
                flatMap( f -> {
                    try {
                        return Files.lines( f );
                    }
                    catch( IOException ex ) {
                        throw new UncheckedIOException( ex );
                    }
                } ).
                map( DarwinJaxbContext.fromXML ).
                filter( Objects::nonNull ).
                filter( p -> p.getUR() != null ).
                mapToInt( p -> p.getUR().getTS().size() ).
                max().
                getAsInt()
        );

    }

    /**
     * Extract the first xml with more than 1 TS entry
     * <p>
     * @param args
     *             <p>
     * @throws Exception
     */
    private static void tsMoreThanOne( String... args )
            throws Exception
    {
        final File dir = new File( "/home/peter/nre" );
        Stream.of( dir.listFiles( n -> n.getName().startsWith( "pPortData.log" ) ) ).
                map( File::toPath ).
                flatMap( f -> {
                    try {
                        return Files.lines( f );
                    }
                    catch( IOException ex ) {
                        throw new UncheckedIOException( ex );
                    }
                } ).
                map( DarwinJaxbContext.fromXML ).
                filter( Objects::nonNull ).
                filter( p -> p.getUR() != null ).
                filter( p -> p.getUR().getTS().size() > 1 ).
                findAny().
                ifPresent( p -> {
                    System.out.println( DarwinJaxbContext.toXML.apply( p ) );
                    p.getUR().getTS().stream().
                    map( TS::getRid ).
                    forEach( System.out::println );
                } );
//                map( DarwinJaxbContext.toXML ).
//                findAny().
//                ifPresent( System.out::println );
    }


    public static void main1( String... args )
            throws Exception
    {
        Consumer<Pport> d = new DarwinDispatcherBuilder().
                //addStationMessage( ( p, m ) -> System.out.println( p.getUR().getUpdateOrigin() + " " + m ) ).
                build();

        for( String arg: args ) {
            log.log( Level.INFO, () -> "Reading " + arg );
            Path p = Paths.get( arg );
            Files.lines( p ).
                    map( DarwinJaxbContext.fromXML ).
                    filter( Objects::nonNull ).
                    forEach( d );
        }
    }
}
