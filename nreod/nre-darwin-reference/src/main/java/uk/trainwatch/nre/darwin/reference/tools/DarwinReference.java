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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBException;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.CISSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.PportTimetableRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.TocRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Imports/Updates darwin reference data
 * <p>
 * @author peter
 */
@MetaInfServices( Utility.class )
public class DarwinReference
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( DarwinReference.class.getName() );
    private static final String SCHEMA = "darwin";
    private List<Path> cifFiles;

    public DarwinReference()
    {
        super();
    }

    @Override
    @SuppressWarnings( "ThrowableInstanceNeverThrown" )
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
        importFiles( cifFiles, this::parse );
    }

    private void parse( Connection con, Path p )
            throws SQLException
    {
        try( InputStream is = new GZIPInputStream( new FileInputStream( p.toFile() ) ) )
        {
            try( Reader r = new InputStreamReader( is ) )
            {
                PportTimetableRef t = DarwinJaxbContext.INSTANCE.unmarshall( r );

                Map<String, Integer> tocs = parseTocRef( con, t.getTocRef() );
                parseLocation( con, t.getLocationRef(), tocs );

                parseReasons( con, t.getCancellationReasons().getReason(), "cancreason" );
                parseReasons( con, t.getLateRunningReasons().getReason(), "latereason" );

                parseCISSource( con, t.getCISSource() );

                parseVia( con, t.getVia() );
            }
        } catch( IOException ex )
        {
            throw new UncheckedIOException( ex );
        } catch( JAXBException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    private void parseCISSource( Connection con, List<CISSource> sources )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "cissource" );

        LOG.log( Level.INFO, () -> "Importing cissource" );
        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + ".cissource (code,name) VALUES (?,?)" ) )
        {
            sources.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getCode(), l.getName() ) ) );
        }
        LOG.log( Level.INFO, () -> "Imported " + sources.size() + " cissource" );

    }

    private void parseLocation( Connection con, List<LocationRef> locs, Map<String, Integer> tocs )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "location" );

        LOG.log( Level.INFO, () -> "Importing location" );
        try( PreparedStatement ps = SQL.prepare( con,
                "INSERT INTO " + SCHEMA + ".location"
                + " (tpl,crs,toc,name)"
                + " VALUES (darwin.tiploc(?),darwin.crs(?),?,?)" ) )
        {
            locs.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                    l.getTpl(),
                    l.getCrs(),
                    tocs.get( l.getToc() ),
                    l.getLocname()
            ) ) );
        }
        LOG.log( Level.INFO, () -> "Imported " + locs.size() + " locations" );
    }

    private void parseReasons( Connection con, List<Reason> reasons, String table )
            throws SQLException
    {
        SQL.deleteTable( con, SCHEMA, table );

        LOG.log( Level.INFO, () -> "Importing " + table );
        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + "." + table + "(id,name) VALUES (?,?)" ) )
        {
            reasons.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getCode(), l.getReasontext() ) ) );
        }
    }

    private Map<String, Integer> parseTocRef( Connection con, List<TocRef> tocs )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "toc" );

        LOG.log( Level.INFO, () -> "Importing toc" );
        try( PreparedStatement ps = SQL.prepare( con, "INSERT INTO " + SCHEMA + ".toc (code,name,url) VALUES (?,?,?)" ) )
        {
            tocs.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps, l.getToc(), l.getTocname(), l.getUrl() ) ) );
        }
        LOG.log( Level.INFO, () -> "Imported " + tocs.size() + " tocs" );

        try( PreparedStatement ps = SQL.prepare( con, "SELECT id,code FROM " + SCHEMA + ".toc" ) )
        {
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
        SQL.deleteIdTable( con, SCHEMA, "via" );

        LOG.log( Level.INFO, () -> "Importing via" );
        try( PreparedStatement ps = SQL.prepare( con,
                "INSERT INTO " + SCHEMA + ".via"
                + " (at,dest,loc1,loc2,text)"
                + " VALUES (darwin.crs(?),darwin.tiploc(?),darwin.tiploc(?),darwin.tiploc(?),?)" ) )
        {
            vias.forEach( SQLConsumer.guard( l -> SQL.executeUpdate( ps,
                    l.getAt(),
                    l.getDest(),
                    l.getLoc1(),
                    l.getLoc2(),
                    l.getViatext()
            ) ) );
        }
        LOG.log( Level.INFO, () -> "Imported " + vias.size() + " vias" );
    }
}
