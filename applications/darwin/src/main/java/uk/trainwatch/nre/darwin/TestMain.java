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
package uk.trainwatch.nre.darwin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 * Simple standalone application that reads the full messages from Darwin and processes them into the database.
 * <p>
 * This function was part of tomcat but it's been pulled out to make it more efficient.
 * <p>
 * @author Peter T Mount
 */
public class TestMain
        extends AbtractMain
{

    @Override
    protected void mainLoop()
            throws Exception
    {
        Consumer<String> monitor = RateMonitor.log( "lines" );

        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.darwinimport(?::xml)" ) ) {
                //File dir = new File( darwinProperties.getProperty( "directory" ) );
                File dir = new File( "/home/peter/nre150513" );
                //File dir = new File( "/home/peter/temp/trainorder" );
                Stream.of( dir.listFiles( f -> f.isFile() && f.getName().startsWith( "pPortData.log.2015-05-13-1" ) ) ).
                        //parallel().
                        sorted( File::compareTo ).
                        //limit( 5 ).
                        map( File::toPath ).
                        forEach( path -> {
                            LOG.log( Level.INFO, () -> "Parsing " + path );
                            try( Stream<String> lines = Files.lines( path ) ) {
                                lines.filter( l -> l != null && !l.isEmpty() ).
                                peek( monitor ).
                                forEach( xml -> {
                                    try {
                                        ps.setString( 1, xml );
                                        try( ResultSet rs = ps.executeQuery() ) {

                                        }
                                    }
                                    catch( SQLException ex ) {
                                        System.out.println( xml );
                                        throw new UncheckedSQLException( ex );
                                    }
                                }
                                );
//                        map( DarwinJaxbContext.fromXML ).
//                        filter( Objects::nonNull ).
//                        forEach( dispatcher );
                            }
                            catch( IOException ex ) {
                                throw new UncheckedIOException( ex );
                            }
                        } );
            }
        }
        catch( SQLException ex ) {
            throw new UncheckedSQLException( ex );
        }
    }

    public static void main( String... args )
            throws Exception
    {
        LOG.log( Level.INFO, "Initialising Darwin Database" );

        new TestMain().run();
    }

}
