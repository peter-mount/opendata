/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.counter.CounterConsumer;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
//@MetaInfServices(Utility.class)
public class SmartLoader
//        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( SmartLoader.class.getName() );
    private static final String SCHEMA = "reference";
    private static final String INSERT_SQL
                                = "INSERT INTO " + SCHEMA + ".smart ("
                                  + "area,"
                                  + "fromBerth,toBerth,"
                                  + "fromLine,toLine,"
                                  + "berthOffset,"
                                  + "platform,event,route,stanox,stanme,steptype,comment"
                                  + ") VALUES ("
                                  + SCHEMA + ".smart_area(?),"
                                  + SCHEMA + ".smart_berth(?),"
                                  + SCHEMA + ".smart_berth(?),"
                                  + SCHEMA + ".smart_line(?),"
                                  + SCHEMA + ".smart_line(?),"
                                  + "?,?,?,?,?,?,?,?)";
    private List<Path> files;

//    @Override
//    public boolean parseArgs( CommandLine cmd )
//    {
//        super.parseArgs( cmd );
//
//        files = Utility.getArgFileList( cmd );
//
//        return !files.isEmpty();
//    }
//
//    @Override
//    public void runUtility()
//            throws Exception
//    {
//        importFiles( files, new Parser() );
//    }
//
//    @Override
//    protected void initDB( Connection con )
//            throws SQLException
//    {
//        LOG.log( Level.INFO, "Clearing down SMART database" );
//        SQL.deleteIdTable( con, SCHEMA, "smart" );
//        SQL.deleteIdTable( con, SCHEMA, "smart_area" );
//        SQL.deleteIdTable( con, SCHEMA, "smart_berth" );
//        SQL.deleteIdTable( con, SCHEMA, "smart_line" );
//    }

    private static class Parser
            implements SQLBiConsumer<Connection, Path>
    {

        @Override
        public void accept( Connection con, Path smartFile )
                throws SQLException
        {
            Objects.requireNonNull( smartFile, "No SMART file provided" );

            CounterConsumer<JsonObject> counter = new CounterConsumer<>();

            try( Importer importer = new Importer( con ) )
            {
                LOG.log( Level.INFO, () -> "Reading " + smartFile );

                try( JsonReader r = Json.createReader( new FileReader( smartFile.toFile() ) ) )
                {
                    LOG.log( Level.INFO, () -> "Loading " + smartFile );

                    Consumer<JsonObject> consumer = importer.guard().
                            andThen( counter ).
                            andThen( counter.logEvery( 5000, LOG ) );

                    r.readObject().
                            getJsonArray( "BERTHDATA" ).
                            stream().
                            map( Functions.castTo( JsonObject.class ) ).
                            filter( Objects::nonNull ).
                            forEach( consumer );
                }
            }
            catch( IOException ex )
            {
                throw new UncheckedIOException( ex );
            }

            LOG.log( Level.INFO, () -> "Imported " + counter.get() + " records" );
        }
    }

    private static class Importer
            implements SQLConsumer<JsonObject>,
                       AutoCloseable
    {

        private final PreparedStatement ps;

        public Importer( Connection con )
                throws SQLException
        {
            this.ps = con.prepareStatement( INSERT_SQL );
        }

        @Override
        public void accept( JsonObject t )
                throws SQLException
        {
            int i = 1;
            ps.setString( i++, t.getString( "TD" ) );
            ps.setString( i++, t.getString( "FROMBERTH" ) );
            ps.setString( i++, t.getString( "TOBERTH" ) );
            ps.setString( i++, t.getString( "FROMLINE" ) );
            ps.setString( i++, t.getString( "TOLINE" ) );
            ps.setInt( i++, JsonUtils.getInt( t, "BERTHOFFSET" ) );
            ps.setString( i++, t.getString( "PLATFORM" ) );
            ps.setInt( i++, SmartEvent.getType( t.getString( "EVENT" ) ).
                       ordinal() );
            ps.setString( i++, t.getString( "ROUTE" ) );
            ps.setInt( i++, JsonUtils.getInt( t, "STANOX" ) );
            ps.setString( i++, t.getString( "STANME" ) );
            ps.setInt( i++, StepType.getType( t.getString( "STEPTYPE" ) ).
                       ordinal() );
            ps.setString( i++, t.getString( "COMMENT" ) );
            ps.executeUpdate();
        }

        @Override
        public void close()
                throws SQLException
        {
            ps.close();
        }

    }
}
