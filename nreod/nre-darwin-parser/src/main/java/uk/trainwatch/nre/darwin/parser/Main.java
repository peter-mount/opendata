/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.io.File;
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
                //addStationMessage( ( p, m ) -> System.out.println( p.getUR().getUpdateOrigin() + " " + m ) ).
                //addAlarm( (p,m) -> System.out.println(m)).
                //addTs( ( p, m ) -> System.out.println( p ) ).
                build();

        final File dir = new File( "/home/peter/nre" );

        int fc = 0;
        for( File f : dir.listFiles( n -> n.getName().startsWith( "pPortData.log" ) ) )
        {
            log.log( Level.INFO, () -> "Reading " + f.getName() );
            Files.lines( f.toPath() ).
                    map( DarwinJaxbContext.fromXML ).
                    filter( Objects::nonNull ).
                    limit( 5 ).
                    forEach( d );

            fc++;
            if( fc > 2 )
            {
                throw new RuntimeException();
            }
        }
    }

    public static void main1( String... args )
            throws Exception
    {
        DarwinDispatcher d = new DarwinDispatcherBuilder().
                //addStationMessage( ( p, m ) -> System.out.println( p.getUR().getUpdateOrigin() + " " + m ) ).
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
