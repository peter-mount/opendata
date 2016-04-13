/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.reference.tools;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
//@MetaInfServices(Utility.class)
public class DarwinImport
//        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( DarwinReference.class.getName() );
    private static final String SCHEMA = "darwin";
    private List<Path> cifFiles;
    private Consumer<String> monitor;
/*
    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        // The first one will be the CIF name
        cifFiles = Utility.getArgFileList( cmd );

        return !cifFiles.isEmpty();
    }

    @Override
    public void runUtility()
            throws Exception
    {
        monitor = RateMonitor.log( "lines" );

        importFiles( cifFiles, this::parse );
    }

    private void parse( Connection con, Path path )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.darwinimport(?::xml)" ) ) {
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
                con.commit();
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        }
    }
*/
}
