/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.corpus;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility to load the corpus data
 * <p>
 * @author peter
 */
//@MetaInfServices(Utility.class)
public class CorpusLoader
//        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( CorpusLoader.class.getName() );
    private static final String SCHEMA = "reference";
    private static final String INSERT_SQL = "INSERT INTO " + SCHEMA + ".corpus (stanox,uic,talpha,tiploc,nlc,nlcdesc,nlcdesc16) VALUES (?,?,?,?,?,?,?)";
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
//        LOG.log( Level.INFO, "Clearing down Corpus database" );
//        SQL.deleteIdTable( con, SCHEMA, "corpus" );
//    }
//
//    /**
//     * Parses a JSON file
//     */
//    private static class Parser
//            implements SQLBiConsumer<Connection, Path>
//    {
//
//        @Override
//        public void accept( Connection con, Path corpusFile )
//                throws SQLException
//        {
//            Objects.requireNonNull( corpusFile, "No Corpus file provided" );
//
//            CounterConsumer<JsonObject> counter = new CounterConsumer<>();
//
//            try( Importer importer = new Importer( con ) )
//            {
//                LOG.log( Level.INFO, () -> "Reading " + corpusFile );
//
//                try( JsonReader r = Json.createReader( new FileReader( corpusFile.toFile() ) ) )
//                {
//                    LOG.log( Level.INFO, () -> "Loading " + corpusFile );
//
//                    Consumer<JsonObject> consumer = importer.guard().
//                            andThen( counter );
//
//                    r.readObject().
//                            getJsonArray( "TIPLOCDATA" ).
//                            stream().
//                            map( Functions.castTo( JsonObject.class ) ).
//                            filter( Objects::nonNull ).
//                            forEach( consumer );
//                }
//            }
//            catch( IOException ex )
//            {
//                throw new UncheckedIOException( ex );
//            }
//
//            LOG.log( Level.INFO, () -> "Imported " + counter.get() + " records" );
//        }
//    }
//
//    /**
//     * Used by Parser, imports a Corpus JsonObject into the database
//     */
//    private static class Importer
//            implements SQLConsumer<JsonObject>,
//                       AutoCloseable
//    {
//
//        private final PreparedStatement ps;
//
//        public Importer( Connection con )
//                throws SQLException
//        {
//            this.ps = con.prepareStatement( INSERT_SQL );
//        }
//
//        @Override
//        public void accept( JsonObject t )
//                throws SQLException
//        {
//            SQL.setInt( ps, 1, JsonUtils.getInt( t, "STANOX", null ) );
//            SQL.setInt( ps, 2, JsonUtils.getInt( t, "UIC", null ) );
//            ps.setString( 3, JsonUtils.getString( t, "3ALPHA" ) );
//            ps.setString( 4, JsonUtils.getString( t, "TIPLOC" ) );
//            SQL.setInt( ps, 5, JsonUtils.getInt( t, "NLC", null ) );
//            ps.setString( 6, JsonUtils.getString( t, "NLCDESC" ).
//                          trim() );
//            
//            String s = JsonUtils.getString( t, "NLCDESC16" ).
//                    trim();
//            if( s.isEmpty() )
//            {
//                ps.setNull( 7, Types.VARCHAR );
//            }
//            else
//            {
//                ps.setString( 7, s );
//            }
//            ps.executeUpdate();
//        }
//
//        @Override
//        public void close()
//                throws SQLException
//        {
//            ps.close();
//        }
//
//    }
}
