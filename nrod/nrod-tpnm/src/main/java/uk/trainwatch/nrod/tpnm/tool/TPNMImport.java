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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nrod.tpnm.model.Node;
import uk.trainwatch.nrod.tpnm.model.Projectinfo;
import uk.trainwatch.nrod.tpnm.model.Projecttype;
import uk.trainwatch.nrod.tpnm.model.Schedversiontype;
import uk.trainwatch.nrod.tpnm.model.Signal;
import uk.trainwatch.nrod.tpnm.model.Station;
import uk.trainwatch.nrod.tpnm.model.Stationcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Stationtypedesc;
import uk.trainwatch.nrod.tpnm.model.Trackcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Uiccodedesc;
import uk.trainwatch.nrod.tpnm.model.Version;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.Router;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLConsumer;
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

    private final RateMonitor<org.w3c.dom.Node> monitor;
    private final Function<org.w3c.dom.Node, Object> unmarshaller;

    private List<Path> cifFiles;
    private boolean fullImport;
    private boolean textImport, stationImport, nodeImport, signalImport;

    public TPNMImport()
    {
        super();
        getOptions().
                addOption( null, "full", false, "Full rather than Incremental import" ).
                addOption( null, "text", false, "Import text only" ).
                addOption( null, "station", false, "Import stations" ).
                addOption( null, "node", false, "Import nodes" ).
                addOption( null, "signal", false, "Import signals" );

        monitor = RateMonitor.log( LOG, "Records" );

        unmarshaller = new TPNMUnmarshaller();
    }

    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        fullImport = cmd.hasOption( "full" );

        textImport = fullImport || cmd.hasOption( "text" );
        stationImport = fullImport || cmd.hasOption( "station" );
        nodeImport = fullImport || cmd.hasOption( "node" );
        signalImport = fullImport || cmd.hasOption( "signal" );

        if( !(fullImport || textImport || stationImport || nodeImport || signalImport) ) {
            LOG.log( Level.SEVERE, "No options set" );
            return false;
        }

        // The first one will be the CIF name
        cifFiles = Utility.getArgFileList( cmd );

        return !cifFiles.isEmpty();
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

    private void importFile( Connection con, Path path )
            throws SQLException,
                   IOException,
                   ParserConfigurationException
    {
        monitor.reset();

        // Sink for disabled supported types
        SQLConsumer<Object> sink = Consumers.sqlSink();

        Router<Class<?>, Object> router = Router.createClassRouter();
        Set<Class<?>> unsupported = new HashSet<>();

        // Commit between root object types but not any that are sinks
        router.setKeyChangeConsumer( SQLBiConsumer.guard( ( k, v ) -> {
            if( router.isRouted( k ) ) {
                LOG.log( Level.INFO, () -> "Committing " + k.getSimpleName() );
                con.commit();
            }
        } ) );

        // Log anything not supported then sink the rest
        router.setSink( o -> {
            Class<?> c = o.getClass();
            if( !unsupported.contains( c ) ) {
                LOG.log( Level.INFO, () -> "Unsupported " + o.getClass().getSimpleName() );
                unsupported.add( c );
            }
        } );

        // These are always unsupported or we have no use for them
        unsupported.add( Version.class );
        unsupported.add( Projecttype.class );
        unsupported.add( Schedversiontype.class );
        unsupported.add( Projectinfo.class );

        // Text imports
        if( textImport ) {
            router.addSQL( Uiccodedesc.class, new UiccodedescImporter( con ) ).
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
                                                                       Stationtypedesc::getLastmodified ) );
        }
        else {
            // Disabled so sink them
            unsupported.add( Uiccodedesc.class );
            unsupported.add( Trackcategorydesc.class );
            unsupported.add( Stationcategorydesc.class );
            unsupported.add( Stationtypedesc.class );
        }

        // The custom importers
        if( stationImport ) {
            router.addSQL( Station.class, new StationImporter( con ) );
        }
        else {
            unsupported.add( Station.class );
        }

        if( nodeImport ) {
            router.addSQL( Node.class, new NodeImporter( con ) );
        }
        else {
            unsupported.add( Node.class );
        }

        if( signalImport ) {
            router.addSQL( Signal.class, new SignalImporter( con ) );
        }
        else {
            unsupported.add( Signal.class );
        }

        LOG.log( Level.INFO, () -> "Parsing " + path );
        try( InputStream is = new FileInputStream( path.toFile() ) ) {
            new XMLStreamParserBuilder().
                    setInputStream( is ).
                    setTriggerDepth( 2 ).
                    createStream().
                    peek( monitor ).
                    map( unmarshaller ).
                    filter( Objects::nonNull ).
                    forEach( router );
        }
        finally {
            LOG.log( Level.INFO, () -> "Complete " + monitor );
        }
    }

}
