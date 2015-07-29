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
                addSQL( Station.class, v -> importStation( con, (Station) v ) ).
                addSQL( Node.class, n -> importNode( con, (Node) n ) ).
                addSQL( Signal.class, n -> importSignal( con, (Signal) n ) );

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
                                                 new Timestamp( ts.apply( v ).toGregorianCalendar().toInstant().getEpochSecond() ) ) ) {
            ps.executeUpdate();
        }
    }

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
                                                       new Timestamp( s.getLastmodified().toGregorianCalendar().toInstant().getEpochSecond() ),
                                                       s.getCrscode(),
                                                       s.getCompulsorystop()
        ) ) {
            ps.executeUpdate();
        }

        for( Track t: s.getTrack() ) {
            int trackId = t.getTrackID();

            try( PreparedStatement ps = SQL.prepareInsert( con,
                                                           SCHEMA + ".track",
                                                           trackId,
                                                           s.getStationid(),
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
            ) ) {
                ps.executeUpdate();
            }

            importWay( con, t.getWay(), "track", (long) trackId );
        }
        con.commit();
    }

    private Long importDirected( Connection con, Directed d )
            throws SQLException
    {
        if( d == null ) {
            return null;
        }
        else {
            Point st = d.getStart().getPoint();
            Point ed = d.getEnd().getPoint();
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "INSERT INTO tpnm.directed (startid,endid) VALUES (?,?)",
                                                     st.getNodeid(),
                                                     ed.getNodeid()
            ) ) {
                ps.executeUpdate();
            }

            return SQL.currval( con, "tpnm.directed_id_seq" );
        }
    }

    private void importSignal( Connection con, Signal s )
            throws SQLException
    {
        Long directedId = importDirected( con, s.getDirected() );

        long signalId = s.getId();

        try( PreparedStatement signalPS = SQL.prepareInsert( con,
                                                             "tpnm.signal",
                                                             signalId,
                                                             s.getInterlockingsysid(),
                                                             s.getName(),
                                                             s.getZoneid(),
                                                             s.getTmpclosed(),
                                                             s.getUsesecsectfreeingtime(),
                                                             s.getSecsectfreeingtime(),
                                                             directedId );
             PreparedStatement ssectPS = SQL.prepare( con, "INSERT INTO tpnm.securitysection (signalid,vmax,accelerationattail) VALUES (?,?,?)" ) ) {
            signalPS.executeUpdate();

            if( !s.getSecuritysection().isEmpty() ) {
                s.getSecuritysection().forEach( SQLConsumer.guard( sect -> {
                    SQL.executeUpdate( ssectPS, signalId, sect.getVmax(), sect.getAccelerationattail() );
                    long ssectId = SQL.currval( con, "tpnm.securitysection_id_seq" );

                    importWay( con, sect.getWay(), "security", ssectId );
                } ) );
            }
        }

        con.commit();
    }

    private void importWay( Connection con, Way way, String type, Object... args )
            throws SQLException
    {
        // Don't create null/empty ways
        if( way != null && !way.getPoint().isEmpty() ) {
            try( PreparedStatement wayps = SQL.prepare( con, "SELECT tpnm.create" + type + "way(?)", args );
                 CallableStatement pointps = SQL.prepareCall( con, "{call tpnm.waypoint(?,?)}" ) ) {

                long wayId = SQL.stream( wayps, SQL.LONG_LOOKUP ).findAny().get();

                way.getPoint().
                        forEach( SQLConsumer.guard( pt -> SQL.executeBatch( pointps, wayId, (long) pt.getNodeid() ) ) );
                pointps.executeBatch();
            }
        }
    }

    private void importNode( Connection con, Node n )
            throws SQLException
    {
        if( n != null && n.getPoint() != null ) {
            try( PreparedStatement ps = SQL.prepareInsert( con,
                                                           SCHEMA + ".node",
                                                           n.getPoint().getNodeid(),
                                                           n.getPoint().getLineid(),
                                                           n.getNetx(),
                                                           n.getNety(),
                                                           n.getNetz(),
                                                           n.getLinex(),
                                                           n.getLiney(),
                                                           n.getLinez(),
                                                           n.getKmregionid(),
                                                           n.getKmvalue(),
                                                           n.getSecondkmregionid(),
                                                           n.getSecondkmvalue(),
                                                           n.getName(),
                                                           n.getAngle()
            ) ) {
                ps.executeUpdate();
            }
        }

        con.commit();
    }
}
