/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan.loader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
@MetaInfServices( Utility.class )
public class NaPTAN
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( NaPTAN.class.getName() );
    private List<Path> files;
    private final Map<Path, Importer> importers = new HashMap<>();

    @Override
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        files = Utility.getArgFileList( cmd );

        return !files.isEmpty();
    }

    @Override
    protected void initDB( Connection con )
            throws SQLException
    {
        files.stream().
                // Resolve the known filename to an Importer
                map( NaPTAN::resolve ).
                // Filter out nulls - i.e. unsupported files
                filter( Objects::nonNull ).
                // Map into importers by the path
                map( i -> importers.computeIfAbsent( i.getPath(), k -> i ) ).
                // Call it's initDB code
                forEach( SQLConsumer.guard( i -> i.initDB( con ) ) );
    }

    @Override
    public void runUtility()
            throws Exception
    {
        importFiles( files, (c, p) -> {
            try {
                Importer i = importers.get( p );
                if( i != null ) {
                    i.importFile( c );
                }
            } catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            };
        } );
    }

    private static Importer resolve( Path path )
    {
        Path p = path.getFileName();
        switch( p.toString() ) {
            case "AirReferences.csv":
                return new AirReferences( path );
            case "RailReferences.csv":
                return new RailReferences( path );
            default:
                LOG.log( Level.FINE, () -> "Ignoring unsupported file " + p );
                return null;
        }
    }
}
