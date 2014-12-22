/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author peter
 */
public interface Importer<T>
{

    static final String SCHEMA = "reference";

    Logger getLog();

    Path getPath();

    default void initDB( Connection con )
            throws SQLException
    {
    }

    default void importFile( Connection con )
            throws IOException,
                   SQLException
    {
        Path path = getPath();
        getLog().
                log( Level.INFO, () -> "Importing " + path );
        try( Reader r = new BufferedReader( new FileReader( path.toFile() ) );
             CSVParser parser = new CSVParser( r, CSVFormat.RFC4180.withHeader() ) ) {
            try( PreparedStatement ps = prepare( con ) ) {
                parser.getRecords().
                        stream().
                        map( getMapper() ).
                        forEach( insert( ps ) );
            }
        }
    }

    Function<CSVRecord, T> getMapper();

    PreparedStatement prepare( Connection con )
            throws SQLException;

    Consumer<T> insert( PreparedStatement ps );
}
