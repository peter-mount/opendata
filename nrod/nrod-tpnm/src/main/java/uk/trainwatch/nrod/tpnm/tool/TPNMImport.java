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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import org.w3c.dom.Node;
import uk.trainwatch.nrod.tpnm.model.Station;
import uk.trainwatch.nrod.tpnm.model.Stationcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Stationtypedesc;
import uk.trainwatch.nrod.tpnm.model.Track;
import uk.trainwatch.nrod.tpnm.model.Trackcategorydesc;
import uk.trainwatch.nrod.tpnm.model.Uiccodedesc;
import uk.trainwatch.util.ParserUtils;
import uk.trainwatch.util.Router;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.counter.RateMonitor;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.xml.XMLStreamParserBuilder;

/**
 *
 * @author peter
 */
@MetaInfServices( Utility.class )
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
    @SuppressWarnings( "ThrowableInstanceNeverThrown" )
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
        importFiles( cifFiles, ( con, path ) ->
             {
                 try
                 {
                     importFile( con, path );
                 } catch( IOException ex )
                 {
                     throw new UncheckedIOException( ex );
                 } catch( ParserConfigurationException ex )
                 {
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
        RateMonitor<Node> monitor = RateMonitor.log( LOG, "Records" );

        Router<Class<?>, Object> router = Router.createClassRouter().
                addSQL( Uiccodedesc.class, v -> importUicCodeDesk( con, (Uiccodedesc) v ) ).
                addSQL( Trackcategorydesc.class, v -> importText( con,
                                                                  "trackcategorydesc",
                                                                  (Trackcategorydesc) v,
                                                                  Trackcategorydesc::getId,
                                                                  Trackcategorydesc::getText,
                                                                  Trackcategorydesc::getLastmodified ) ).
                addSQL( Stationcategorydesc.class, v -> importText( con,
                                                                    "stationcategorydesc",
                                                                    (Stationcategorydesc) v,
                                                                    Stationcategorydesc::getId,
                                                                    Stationcategorydesc::getText,
                                                                    Stationcategorydesc::getLastmodified ) ).
                addSQL( Stationtypedesc.class, v -> importText( con,
                                                                "stationtypedesc",
                                                                (Stationtypedesc) v,
                                                                Stationtypedesc::getId,
                                                                Stationtypedesc::getText,
                                                                Stationtypedesc::getLastmodified ) ).
                addSQL( Station.class, v -> importStation( con, (Station) v ) );

        lastRoute = null;

        LOG.log( Level.INFO, () -> "Parsing " + path );
        try( InputStream is = new FileInputStream( path.toFile() ) )
        {
            new XMLStreamParserBuilder().
                    setInputStream( is ).
                    setTriggerDepth( 2 ).
                    //setAppendDepth( 2 ).
                    createStream().
                    peek( monitor ).
                    map( new TPNMUnmarshaller() ).
                    filter( Objects::nonNull ).
                    //limit( 20 ).
                    forEach( o ->
                            {
                                Consumer<Object> c = router.apply( o );
                                if( c != lastRoute && lastRoute != null )
                                {
                                    try
                                    {
                                        LOG.log( Level.INFO, "Committing work" );
                                        con.commit();
                                    } catch( SQLException ex )
                                    {
                                        throw new UncheckedSQLException( ex );
                                    }
                                }
                                lastRoute = c;

                                c.accept( o );
                    } );
        }
        finally
        {
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
                                                 v.getVanillatext() ) )
        {
            ps.executeUpdate();
        }

    }

    private <T> void importText( Connection con, String table,
                                 T v,
                                 Function<T, Integer> id,
                                 Function<T, String> text,
                                 Function<T, XMLGregorianCalendar> ts )
            throws SQLException
    {

        try( PreparedStatement ps = SQL.prepare( con,
                                                 "INSERT INTO " + SCHEMA + "." + table + " (id,text,ts) VALUES (?,?,?)",
                                                 id.apply( v ),
                                                 text.apply( v ),
                                                 new Timestamp( ts.apply( v ).toGregorianCalendar().toInstant().getEpochSecond() ) ) )
        {
            ps.executeUpdate();
        }

    }

    private final AtomicInteger stcnt = new AtomicInteger();

    private void importStation( Connection con, Station s )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepareInsert( con,
                                                       SCHEMA + ".station",
                                                       s.getStationid(),
                                                       s.getUiccode(),
                                                       s.getAbbrev(),
                                                       s.getLongname(),
                                                       s.getCommentary(),
                                                       s.getStdstoppingtime(),
                                                       s.getStdconnectiontime(),
                                                       s.getStationtype(),
                                                       s.getStationcategory(),
                                                       s.getUicstationcode(),
                                                       s.getTransportassociation(),
                                                       s.getX(),
                                                       s.getY(),
                                                       s.getEasting(),
                                                       s.getNorthing(),
                                                       ParserUtils.parseInt( s.getStanox() ),
                                                       s.getLpbflag(),
                                                       s.getPeriodid(),
                                                       s.getCapitalsident(),
                                                       s.getNalco(),
                                                       new Timestamp( s.getLastmodified().toGregorianCalendar().toInstant().getEpochSecond() ) ) )
        {
            ps.executeUpdate();
        }

        for( Track t : s.getTrack() )
        {
            try( PreparedStatement ps = SQL.prepareInsert( con,
                                                           SCHEMA + ".track",
                                                           t.getTrackID(),
                                                           t.getName(),
                                                           0, // seq
                                                           t.getDescription(),
                                                           t.getTrackcategory(),
                                                           t.getEffectivelength(),
                                                           t.getRoplinecode(),
                                                           t.getSalinecode(),
                                                           t.getLinepropertytemplateid(),
                                                           t.getPeriodid(),
                                                           t.getPermissiveWorking(),
                                                           false, // directed
                                                           t.getTracktmpclosed()
            ) )
            {
                ps.executeUpdate();
            }
        }

        con.commit();
    }
}
