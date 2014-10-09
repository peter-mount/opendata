/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif;

import uk.trainwatch.nrod.timetable.cif.record.CIFParser;
import java.io.File;
import java.nio.file.Files;
import java.util.Objects;
import uk.trainwatch.util.Consumers;

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
            // Strict mode so we fail on an invalid record type
            final CIFParser parser = new CIFParser( true );

            // Stream from the file
            Files.lines( cifFile.toPath() ).
                    // Set the parser to the current line
                    map( parser::parse ).
                    // Filter nulls i.e. unsupported/not yet implemented records
                    filter( Objects::nonNull ).
                    // Limit rows parsed
                    //limit( 10 ).
                    // Dump to stdout
                    //forEach( System.out::println );
                    // Dump to the bit bucket
                    forEach( Consumers.sink() );

        }
        catch( Exception ex )
        {
            ex.printStackTrace( System.out );
        }

        // Dummy, causes netbeans not to collaps the output on success
        throw new RuntimeException();
    }
}
