/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        DarwinDispatcher d = new DarwinDispatcherBuilder().
                addStationMessage( ( p, m ) -> System.out.println( m ) ).
                build();

        for( String arg : args )
        {
            log.log( Level.INFO, () -> "Reading " + arg );
            Path p = Paths.get( arg );
            Files.lines( p ).
                    map( DarwinJaxbContext.fromXML ).
                    filter( Objects::nonNull ).
                    forEach( d );
        }
    }
}
