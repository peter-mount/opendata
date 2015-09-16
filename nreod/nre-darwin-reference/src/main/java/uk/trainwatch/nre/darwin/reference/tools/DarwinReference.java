/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nre.darwin.reference.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.net.ftp.FTPFile;
import org.kohsuke.MetaInfServices;
import static org.omg.IOP.IORHelper.insert;
import uk.trainwatch.io.ftp.FTPClient;
import uk.trainwatch.io.ftp.FTPClientBuilder;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.CISSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.PportTimetableRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.TocRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.LoggingUtils;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.FTPUtilityHelper;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Imports/Updates darwin reference data
 * <p>
 * @author peter
 */
@MetaInfServices(Utility.class)
@ApplicationScoped
public class DarwinReference
        extends DBUtility
{

    // The file suffix to retrieve
    private static final String SUFFIX = "_ref_v3.xml.gz";

    // Time between attempts, in minutes
    private static final long RETRY_TIME = 10;

    private static final String DIR = "dir";
    private static final String RETRY = "retry";
    private static final String FORCEDOWNLOAD = "force-download";
    private static final String RETRIEVE = "retrieve";
    private static final String FULL = "full";

    protected static final Logger LOG = Logger.getLogger( DarwinReference.class.getName() );
    private static final String SCHEMA = "darwin";
    private List<Path> cifFiles;
    private boolean full;
    private boolean force;
    private int retry;
    private boolean retrieve;
    private Path basePath;
    private final FTPUtilityHelper ftpHelper;

    @Inject
    private DarwinJaxbContext darwinJaxbContext;

    public DarwinReference()
    {
        super();

        Options options = getOptions();

        // Retrieve option
        options.addOption( null, FULL, false, "Perform a full import (do not use)" ).
                addOption( null, RETRIEVE, false, "Retrieve reference data before import into the specified path" ).
                addOption( null, DIR, true, "Base directory to download to" ).
                addOption( null, FORCEDOWNLOAD, false, "Force retrieval rather than check" ).
                addOption( null, RETRY, false, "Number of times to retry" );
        ftpHelper = new FTPUtilityHelper( options );
    }

    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        full = cmd.hasOption( FULL );

        force = cmd.hasOption( FORCEDOWNLOAD );
        retry = cmd.hasOption( RETRY ) ? Integer.parseInt( cmd.getOptionValue( RETRY ) ) : 0;
        retrieve = cmd.hasOption( RETRIEVE );
        basePath = cmd.hasOption( DIR ) ? Paths.get( cmd.getOptionValue( DIR ) ) : null;

        if( retrieve && !ftpHelper.parseArgs( cmd ) ) {
            return false;
        }

        // The first one will be the CIF name
        cifFiles = Utility.getArgFileList( cmd );

        // cifFiles must be empty if in retrieve mode
        if( retrieve ) {
            return cifFiles.isEmpty();
        }

        // No files, then look for todays one
        if( cifFiles.isEmpty() ) {
            String prefix = getPrefix();
            Path dir = getDir( prefix );
            File d = dir.toFile();
            if( d.exists() && d.isDirectory() ) {
                for( File f: d.listFiles( f -> f.isFile() && f.getName().startsWith( prefix ) && f.getName().endsWith( SUFFIX ) ) ) {
                    if( f.isFile() && f.canRead() ) {
                        cifFiles = Arrays.asList( dir.resolve( f.getName() ) );
                    }
                }
            }
        }

        return !cifFiles.isEmpty();
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void runUtility()
            throws Exception
    {
        if( retrieve ) {
            while( cifFiles.isEmpty() ) {
                try {
                    cifFiles = Arrays.asList( retrieveFile() );
                }
                catch( Exception ex ) {
                    if( retry > 0 ) {
                        LOG.log( Level.SEVERE, ex, () -> "Failed to retrieve, will retry in " + RETRY_TIME + " minutes. " + retry + " retries left" );
                        retry--;
                        Thread.sleep( TimeUnit.MINUTES.toMillis( RETRY_TIME ) );
                    }
                    else {
                        throw ex;
                    }
                }
            }
        }

        importFiles( cifFiles, this::parse );
    }

    private String getPrefix()
    {
        Calendar cal = Calendar.getInstance();
        return String.format( "%04d%02d%02d", cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) + 1, cal.get( Calendar.DAY_OF_MONTH ) );
    }

    private Path getDir( String prefix )
    {
        return basePath.resolve( prefix.substring( 0, 4 ) ).
                resolve( prefix.substring( 4, 6 ) );
    }

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private Path retrieveFile()
            throws IOException
    {
        // Today's date forms part of the file name
        String prefix = getPrefix();
        LOG.log( Level.INFO, () -> "Retrieving config for " + prefix );

        Path dir = getDir( prefix );

        File dirFile = dir.toFile();
        if( dirFile.mkdirs() ) {
            LOG.log( Level.INFO, () -> "Created " + dir );
        }

        try( FTPClient ftp = new FTPClientBuilder().
                logger( s -> LOG.log( Level.INFO, s ) ).
                enableDebugging().
                printCommands().
                build() ) {

            ftpHelper.connect( ftp );
            ftpHelper.login( ftp );

            // Now see if the file exists & retrieve it
            FTPFile file = Streams.stream( ftp.listFiles( f -> f.isFile() && f.getName().startsWith( prefix ) && f.getName().endsWith( SUFFIX ) ) ).
                    findAny().
                    orElseThrow( () -> new FileNotFoundException( "Cannot find a file to download: " + prefix ) );

            if( !force ) {
                File f = new File( dirFile, file.getName() );
                if( f.exists() && f.length() == file.getSize() ) {
                    LOG.log( Level.WARNING, () -> "Not retrieving " + file.getName() + " as file appears identical" );
                    return dir.resolve( file.getName() );
                }
            }

            return ftp.retrieve( file, dir, StandardCopyOption.REPLACE_EXISTING );
        }
        catch( Exception ex ) {
            // Remove any existing file incase it's corrupt
            for( File f: dirFile.listFiles( f -> f.isFile() && f.getName().startsWith( prefix ) && f.getName().endsWith( SUFFIX ) ) ) {
                if( f.delete() ) {
                    LOG.log( Level.WARNING, () -> "Deleted " + f + " as possibly corrupt" );
                }
            }
            throw ex;
        }
    }

    private void parse( Connection con, Path p )
            throws SQLException
    {
        LoggingUtils.logBanner( "Importing " + p.toString() );

        try( InputStream is = new GZIPInputStream( new FileInputStream( p.toFile() ) ) ) {
            try( Reader r = new InputStreamReader( is ) ) {

                PportTimetableRef t = darwinJaxbContext.unmarshall( r );

                Map<String, Integer> tocs = parseTocRef( con, t.getTocRef() );
                parseLocation( con, t.getLocationRef(), tocs );

                parseReasons( con, t.getCancellationReasons().getReason(), "cancreason" );
                parseReasons( con, t.getLateRunningReasons().getReason(), "latereason" );

                parseCISSource( con, t.getCISSource() );

                parseVia( con, t.getVia() );
            }
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
        catch( JAXBException ex ) {
            throw new RuntimeException( ex );
        }
        finally {
            LOG.log( Level.INFO, "Completed importing " + p.toString() );
        }
    }

    private void parseCISSource( Connection con, List<CISSource> sources )
            throws SQLException
    {
        if( full ) {
            SQL.deleteIdTable( con, SCHEMA, "cissource" );
        }

        LOG.log( Level.INFO, () -> "Importing cissource" );

        Set<String> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT code FROM " + SCHEMA + ".cissource" ) ) {
            existing = SQL.stream( ps, SQL.STRING_LOOKUP ).collect( Collectors.toSet() );
        }

        // Now strip out duplicates
        List<CISSource> src = sources.stream().
                filter( l -> !existing.contains( l.getCode() ) ).
                collect( Collectors.toList() );

        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + ".cissource (code,name) VALUES (?,?)" ) ) {
            src.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getCode(), l.getName() ) ) );
        }

        LOG.log( Level.INFO, () -> "Imported " + src.size() + " cissource" );

    }

    private void parseLocation( Connection con, List<LocationRef> locs, Map<String, Integer> tocs )
            throws SQLException
    {
        class Entry
        {

            String tpl;
            String crs;

            public Entry( ResultSet rs )
                    throws SQLException
            {
                this.tpl = rs.getString( "tpl" );
                this.crs = rs.getString( "crs" );
            }

            public Entry( LocationRef l )
            {
                this.tpl = l.getTpl();
                this.crs = l.getCrs();
            }

            @Override
            public int hashCode()
            {
                return Objects.hash( tpl, crs );
            }

            @Override
            public boolean equals( Object obj )
            {
                if( obj instanceof Entry ) {
                    Entry e = (Entry) obj;
                    return Objects.equals( tpl, e.tpl ) && Objects.equals( crs, e.crs );
                }
                return false;
            }

        }

        if( full ) {
            SQL.deleteIdTable( con, SCHEMA, "location" );
        }

        LOG.log( Level.INFO, () -> "Importing location" );

        Set<Entry> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT tpl,crs FROM " + SCHEMA + ".location" ) ) {
            existing = SQL.stream( ps, rs -> new Entry( rs ) ).collect( Collectors.toSet() );
        }

        Map<Boolean, List<LocationRef>> partition = locs.stream().
                collect( Collectors.partitioningBy( l -> existing.contains( new Entry( l ) ) ) );

        // Insert new entries
        List<LocationRef> inserts = partition.getOrDefault( false, Collections.emptyList() );
        if( !inserts.isEmpty() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "INSERT INTO " + SCHEMA + ".location"
                                                     + " (tpl,crs,toc,name)"
                                                     + " VALUES (darwin.tiploc(?),darwin.crs(?),?,?)" ) ) {
                inserts.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                                                                            l.getTpl(),
                                                                            l.getCrs(),
                                                                            tocs.get( l.getToc() ),
                                                                            l.getLocname()
                ) ) );
            }
            LOG.log( Level.INFO, () -> "Imported " + inserts.size() + " locations" );
        }

        // Update existing entries
        List<LocationRef> updates = partition.getOrDefault( true, Collections.emptyList() );
        if( !updates.isEmpty() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "UPDATE " + SCHEMA + ".location"
                                                     + " SET toc=?,name?"
                                                     + " WHERE tpl=darwin.tiploc(?) AND crs=darwin.crs(?)" ) ) {
                updates.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                                                                            tocs.get( l.getToc() ),
                                                                            l.getLocname(),
                                                                            l.getTpl(),
                                                                            l.getCrs()
                ) ) );
            }
            LOG.log( Level.INFO, () -> "Updated " + updates.size() + " locations" );
        }
    }

    private void parseReasons( Connection con, List<Reason> reasons, String table )
            throws SQLException
    {
        if( full ) {
            SQL.deleteTable( con, SCHEMA, table );
        }

        LOG.log( Level.INFO, () -> "Importing " + table );

        Set<Integer> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT id FROM " + SCHEMA + "." + table ) ) {
            existing = SQL.stream( ps, SQL.INT_LOOKUP ).collect( Collectors.toSet() );
        }

        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + "." + table + "(id,name) VALUES (?,?)" ) ) {
            reasons.stream().
                    filter( r -> !existing.contains( r.getCode() ) ).
                    forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getCode(), l.getReasontext() ) ) );
        }

        try( PreparedStatement ps = SQL.prepare( con, "UPDATE " + SCHEMA + "." + table + " SET name=? WHERE id=?" ) ) {
            reasons.stream().
                    filter( r -> existing.contains( r.getCode() ) ).
                    forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getReasontext(), l.getCode() ) ) );
        }
    }

    private Map<String, Integer> parseTocRef( Connection con, List<TocRef> tocs )
            throws SQLException
    {
        if( full ) {
            SQL.deleteIdTable( con, SCHEMA, "toc" );
        }

        LOG.log( Level.INFO, () -> "Importing toc" );

        Set<String> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT code FROM " + SCHEMA + ".toc" ) ) {
            existing = SQL.stream( ps, SQL.STRING_LOOKUP ).collect( Collectors.toSet() );
        }

        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + ".toc (code,name,url) VALUES (?,?,?)" ) ) {
            tocs.stream().
                    filter( t -> !existing.contains( t.getToc() ) ).
                    forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getToc(), l.getTocname(), l.getUrl() ) ) );
        }

        try( PreparedStatement ps = SQL.prepare( con, "UPDATE " + SCHEMA + ".toc SET name=?, url=? WHERE code=?" ) ) {
            tocs.stream().
                    filter( t -> existing.contains( t.getToc() ) ).
                    forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getTocname(), l.getUrl(), l.getToc() ) ) );
        }

        try( PreparedStatement ps = SQL.prepare( con, "SELECT id,code FROM " + SCHEMA + ".toc" ) ) {
            return SQL.stream( ps,
                               rs -> new Object()
                               {
                                   Integer id = rs.getInt( "id" );
                                   String code = rs.getString( "code" );
                               }
            ).collect( Collectors.toMap( o -> o.code, o -> o.id ) );
        }
    }

    private void parseVia( Connection con, List<Via> vias )
            throws SQLException
    {
        class V
        {

            String at;
            String dest;
            String loc1;
            String loc2;

            V( ResultSet rs )
                    throws SQLException
            {
                at = rs.getString( "at" );
                dest = rs.getString( "dest" );
                loc1 = rs.getString( "loc1" );
                loc2 = rs.getString( "loc2" );
            }

            V( Via v )
            {
                at = v.getAt();
                dest = v.getDest();
                loc1 = v.getLoc1();
                loc2 = v.getLoc2();
            }

            @Override
            public int hashCode()
            {
                return Objects.hash( at, dest, loc1, loc2 );
            }

            @Override
            public boolean equals( Object obj )
            {
                if( obj instanceof V ) {
                    V o = (V) obj;
                    return Objects.equals( at, o.at )
                           && Objects.equals( dest, o.dest )
                           && Objects.equals( loc1, o.loc1 )
                           && Objects.equals( loc2, o.loc2 );
                }
                return false;
            }
        }

        if( full ) {
            SQL.deleteIdTable( con, SCHEMA, "via" );
        }

        LOG.log( Level.INFO, () -> "Importing via" );

        Set<V> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM " + SCHEMA + ".via" ) ) {
            existing = SQL.stream( ps, rs -> new V( rs ) ).collect( Collectors.toSet() );
        }

        Map<Boolean, List<Via>> partition = vias.stream().collect( Collectors.partitioningBy( v -> existing.contains( new V( v ) ) ) );

        List<Via> inserts = partition.getOrDefault( false, Collections.emptyList() );
        if( !inserts.isEmpty() ) {
            LOG.log( Level.INFO, () -> "Importing " + inserts.size() + " vias" );
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "INSERT INTO " + SCHEMA + ".via"
                                                     + " (at,dest,loc1,loc2,text)"
                                                     + " VALUES (darwin.crs(?),darwin.tiploc(?),darwin.tiploc(?),darwin.tiploc(?),?)" ) ) {
                inserts.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                                                                            l.getAt(),
                                                                            l.getDest(),
                                                                            l.getLoc1(),
                                                                            l.getLoc2(),
                                                                            l.getViatext()
                ) ) );
            }

            LOG.log( Level.INFO, () -> "Imported " + inserts.size() + " vias" );
        }

        List<Via> updates = partition.getOrDefault( true, Collections.emptyList() );
        if( !inserts.isEmpty() ) {
            LOG.log( Level.INFO, () -> "Updating " + updates.size() + " vias" );
            
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "UPDATE " + SCHEMA + ".via"
                                                     + " SET text=?"
                                                     + " WHERE at=darwin.crs(?) AND dest=darwin.tiploc(?) AND loc1=darwin.tiploc(?) AND loc2=darwin.tiploc(?)" ) ) {
                updates.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                                                                            l.getViatext(),
                                                                            l.getAt(),
                                                                            l.getDest(),
                                                                            l.getLoc1(),
                                                                            l.getLoc2()
                ) ) );
            }

            LOG.log( Level.INFO, () -> "Updated " + updates.size() + " vias" );
        }
    }
}
