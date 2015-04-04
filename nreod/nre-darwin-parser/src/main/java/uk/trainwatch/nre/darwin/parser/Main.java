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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
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
        Consumer<Pport> d = new DarwinDispatcherBuilder().
                addStationMessage( p -> System.out.println( p ) ).
                //addAlarm( (p,m) -> System.out.println(m)).
                //addSchedule( System.out::println ).
                //addSchedule( p->System.out.println("schedule") ).
                //addTrainAlert( System.out::println ).
                //addTs( System.out::println ).
                //addTs( p->System.out.println("ts") ).
                addTs( p -> System.out.println( DarwinJaxbContext.toXML.apply( p ) ) ).
                addTs( p -> {
                    try {
                        System.out.println( DarwinJaxbContext.INSTANCE.marshall( p ) );
                    }
                    catch( JAXBException ex ) {
                        Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                } ).
                build();

        final File dir = new File( "/home/peter/nre" );

        int fc = 0;
        for( File f: dir.listFiles( n -> n.getName().startsWith( "pPortData.log" ) ) ) {
            log.log( Level.INFO, () -> "Reading " + f.getName() );
            Files.lines( f.toPath() ).
                    map( DarwinJaxbContext.fromXML ).
                    filter( Objects::nonNull ).
                    limit( 5 ).
                    forEach( d );

            fc++;
            if( fc > 2 ) {
                throw new RuntimeException();
            }
        }
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
