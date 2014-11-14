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
package uk.trainwatch.gis.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 * Located here as not certain where this should go.
 * <p>
 * Imports a KML of station locations into the database
 * <p>
 * Based on KML attached to https://groups.google.com/d/msg/openraildata-talk/LOSy1oFi5CM/oqQt4Y46KPcJ
 * <p>
 * @author peter
 */
@MetaInfServices(Utility.class)
public class KMLImporter
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( KMLImporter.class.getName() );

    private static final String SCHEMA = "gis";

    private List<File> files;

    @Override
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        // As commons-cli is pre-generics we have to do this first
        Collection<String> args = cmd.getArgList();

        // The first one will be the CIF name
        files = args.stream().
                map( File::new ).
                filter( File::exists ).
                filter( File::canRead ).
                //map( File::toPath ).
                collect( Collectors.toList() );

        return !files.isEmpty();
    }

    @Override
    public void runUtility()
            throws Exception
    {
        try( Connection con = getConnection() )
        {
            con.setAutoCommit( false );
            try
            {
                SQL.deleteTable( con, SCHEMA, "stations" );

                for( File file: files )
                {
                    LOG.log( Level.INFO, () -> "Importing " + file );

                    importFile( con, file );
                }

                LOG.log( Level.OFF, "Committing" );
                con.commit();
            }
            catch( UncheckedIOException |
                   SQLException |
                   UncheckedSQLException |
                   ClassNotFoundException |
                   InstantiationException |
                   IllegalAccessException |
                   IOException ex )
            {
                LOG.log( Level.SEVERE, ex, () -> "Commit failed: " + ex.getMessage() );

                LOG.log( Level.INFO, () -> "Rolling back transaction" );
                con.rollback();
            }
        }
    }

    private void importFile( Connection con, File file )
            throws ClassNotFoundException,
                   InstantiationException,
                   IllegalAccessException,
                   IOException,
                   SQLException
    {
        try( PreparedStatement ps = con.prepareStatement( "INSERT INTO gis.stations (name,latitude,longitude) VALUES (?,?,?)" ) )
        {
            DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
            DOMImplementation impl = reg.getDOMImplementation( "XML 3.0" );
            DOMImplementationLS domImplLs = DOMImplementationLS.class.cast( impl.getFeature( "LS", "3.0" ) );
            LSInput in = domImplLs.createLSInput();
            try( InputStream is = new FileInputStream( file ) )
            {
                in.setByteStream( is );
                LSParser parser = domImplLs.createLSParser( DOMImplementationLS.MODE_SYNCHRONOUS, null );
                Document doc = parser.parse( in );

                Element kml = (Element) doc.getElementsByTagName( "kml" ).
                        item( 0 );
                Element document = getFirst( kml, "Document" );
                Element folder = getFirst( document, "Folder" );
                NodeList l = folder.getElementsByTagName( "Placemark" );
                for( int i = 0; i < l.getLength(); i++ )
                {
                    Element placemark = (Element) l.item( i );

                    ps.setString( 1, getFirst( placemark, "name" ).
                                  getTextContent().
                                  toUpperCase() );

                    Element point = getFirst( placemark, "Point" );
                    Element coord = getFirst( point, "coordinates" );
                    String c[] = coord.getTextContent().
                            split( ",", 2 );

                    ps.setDouble( 2, Double.parseDouble( c[1] ) );
                    ps.setDouble( 3, Double.parseDouble( c[0] ) );
                    ps.executeUpdate();
                }
            }
        }
    }

    private Element getFirst( Element e, String name )
    {
        return (Element) e.getElementsByTagName( name ).
                item( 0 );
    }
}
