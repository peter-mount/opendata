/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nrod.tpnm.model.Directed;
import uk.trainwatch.nrod.tpnm.model.Node;
import uk.trainwatch.nrod.tpnm.model.Point;
import uk.trainwatch.nrod.tpnm.model.Signal;
import uk.trainwatch.nrod.tpnm.model.Station;
import uk.trainwatch.nrod.tpnm.model.Stationcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Stationtypedesc;
import uk.trainwatch.nrod.tpnm.model.Track;
import uk.trainwatch.nrod.tpnm.model.Trackcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Uiccodedesc;
import uk.trainwatch.nrod.tpnm.model.Way;
import uk.trainwatch.util.ParserUtils;
import uk.trainwatch.util.Router;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.xml.XMLStreamParserBuilder;

/**
 *
 * @author peter
 */
@MetaInfServices(Utility.class)
public class TPNMImport
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( TPNMImport.class.getName() );
    private static final String SCHEMA = "tpnm";
    private List<Path> cifFiles;
    private boolean fullImport;

    public TPNMImport()
    {
        super();
        getOptions().
                addOption( null, "full", false, "Full rather than Incremental import" );
    }

    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        fullImport = cmd.hasOption( "full" );

        // The first one will be the CIF name
        cifFiles = Utility.getArgFileList( cmd );

        return !cifFiles.isEmpty();
    }

    @Override
    protected void initDB( Connection con )
            throws SQLException
    {
        Stream.of(
                "security_way",
                "securitysection",
                "signal",
                //
                "waypoint",
                "node",
                "way",
                //
                "track",
                "station",
                "stationtypedesc",
                "stationcategorydesc",
                "trackcategorydesc",
                "uiccodedesc"
        ).forEach( k -> SQL.deleteTable( con, SCHEMA, k ) );

    }

    @Override
    public void runUtility()
            throws Exception
    {
        importFiles( cifFiles, ( con, path ) -> {
            try {
                importFile( con, path );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
            catch( ParserConfigurationException ex ) {
                throw new IllegalStateException( ex );
            }
        } );
    }

    private Consumer<Object> lastRoute;

    private void importFile( Connection con, Path path )
            throws SQLException,
                   IOException,
                   ParserConfigurationException
    {
        RateMonitor<org.w3c.dom.Node> monitor = RateMonitor.log( LOG, "Records" );

        Router<Class<?>, Object> router = Router.createClassRouter().
                addSQL( Uiccodedesc.class, v -> importUicCodeDesk( con, (Uiccodedesc) v ) ).
                addSQL( Trackcategorydesc.class, new TextImporter<>( con,
                                                                     SCHEMA, "trackcategorydesc",
                                                                     Trackcategorydesc::getId,
                                                                     Trackcategorydesc::getText,
                                                                     Trackcategorydesc::getLastmodified ) ).
                addSQL( Stationcategorydesc.class, new TextImporter<>( con,
                                                                       SCHEMA, "stationcategorydesc",
                                                                       Stationcategorydesc::getId,
                                                                       Stationcategorydesc::getText,
                                                                       Stationcategorydesc::getLastmodified ) ).
                addSQL( Stationtypedesc.class, new TextImporter<>( con,
                                                                   SCHEMA, "stationtypedesc",
                                                                   Stationtypedesc::getId,
                                                                   Stationtypedesc::getText,
                                                                   Stationtypedesc::getLastmodified ) ).
                addSQL( Station.class, new StationImporter( con ) ).
                addSQL( Node.class, new NodeImporter( con ) ).
                addSQL( Signal.class, new SignalImporter( con ) );

        lastRoute = null;

        LOG.log( Level.INFO, () -> "Parsing " + path );
        try( InputStream is = new FileInputStream( path.toFile() ) ) {
            new XMLStreamParserBuilder().
                    setInputStream( is ).
                    setTriggerDepth( 2 ).
                    //setAppendDepth( 2 ).
                    createStream().
                    peek( monitor ).
                    map( new TPNMUnmarshaller() ).
                    filter( Objects::nonNull ).
                    //limit( 20 ).
                    forEach( o -> {
                        Consumer<Object> c = router.apply( o );
                        if( c != lastRoute && lastRoute != null ) {
                            try {
                                LOG.log( Level.INFO, "Committing work" );
                                con.commit();
                            }
                            catch( SQLException ex ) {
                                throw new UncheckedSQLException( ex );
                            }
                        }
                        lastRoute = c;

                        c.accept( o );
                    } );
        }
        finally {
            LOG.log( Level.INFO, () -> "Complete " + monitor );
        }
    }

    private void importUicCodeDesk( Connection con, Uiccodedesc v )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con,
                                                 "INSERT INTO " + SCHEMA + ".uiccodedesc VALUES (?,?,?,?)",
                                                 v.getId(),
                                                 v.getText(),
                                                 new Timestamp( v.getLastmodified().toGregorianCalendar().toInstant().getEpochSecond() ),
                                                 v.getVanillatext() ) ) {
            ps.executeUpdate();
        }

    }

}
