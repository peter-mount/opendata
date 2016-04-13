/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.reference.tools;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;

/**
 * Handles the daily import of the full darwin schedule
 * <p>
 * @author peter
 */
//@MetaInfServices(Utility.class)
@ApplicationScoped
public class DarwinDaily
//        extends DBUtility
{

    // The file suffix to retrieve
    private static final String SUFFIX = "_v8.xml.gz";

    // Time between attempts, in minutes
    private static final long RETRY_TIME = 10;

    private static final String DIR = "dir";
    private static final String RETRY = "retry";
    private static final String FORCEDOWNLOAD = "force-download";
    private static final String RETRIEVE = "retrieve";

    protected static final Logger LOG = Logger.getLogger( DarwinReference.class.getName() );
    private static final String SCHEMA = "darwin";
    private List<Path> cifFiles;
    private boolean force;
    private int retry;
    private boolean retrieve;
    private Path basePath;
  //  private final FTPUtilityHelper ftpHelper;

    @Inject
    private DarwinJaxbContext darwinJaxbContext;

    /*
    public DarwinDaily()
    {
        super();

        Options options = getOptions();

        // Retrieve option
        options.addOption( null, RETRIEVE, false, "Retrieve reference data before import into the specified path" ).
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
        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.darwin_cleanup()" ) ) {
            LOG.log( Level.INFO, "Cleaning up old trains..." );
            ps.executeQuery();
        }

        try {
            StringBuilder sb = new StringBuilder();

            // Uncompress
            LOG.log( Level.INFO, () -> "Reading " + p.toString() );

            try( InputStream is = new GZIPInputStream( new FileInputStream( p.toFile() ) ) ) {
                try( BufferedReader r = new BufferedReader( new InputStreamReader( is ) ) ) {
                    char b[] = new char[10240];
                    int s;
                    while( (s = r.read( b )) > -1 ) {
                        sb.append( b, 0, s );
                    }
                }
            }

            // Now import into postgresql
            LOG.log( Level.INFO, () -> "Importing " + p.toString() );
            try( PreparedStatement ps = con.prepareStatement( "SELECT darwin.darwintimetable(?::xml)" ) ) {
                ps.setString( 1, sb.toString() );
                ps.executeQuery();
                LOG.log( Level.INFO, () -> "Completed importing " + p.toString() );
            }
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }
*/
}
