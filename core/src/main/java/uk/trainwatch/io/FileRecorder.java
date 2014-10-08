/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A consumer that will record the passed value to a file
 * <p>
 * @author Peter T Mount
 * @param <T>
 */
public class FileRecorder<T>
        implements Consumer<T>
{

    private static final Logger LOG = Logger.getLogger( FileRecorder.class.getName() );

    private final Function<T, Path> fileNameMapper;
    private final Function<T, String> encoder;
    private final OpenOption options[];

    /**
     * Create a consumer that will append to the file at the specified path, creating it as necessary
     * <p>
     * @param <T>     Type
     * @param path    Path to record to
     * @param encoder mapping function to encode the type to a String
     * <p>
     * @return consumer
     */
    public static <T> Consumer<T> recordTo( final Path path, final Function<T, String> encoder )
    {
        return recordTo( v -> path, encoder );
    }

    /**
     * Create a consumer that will append to the file at the path provided by the pathMapper, creating it as necessary
     * <p>
     * @param <T>        Type
     * @param pathMapper mapping function to determine the path based on the type
     * @param encoder    mapping function to encode the type to a String
     * <p>
     * @return consumer
     */
    public static <T> Consumer<T> recordTo( final Function<T, Path> pathMapper, final Function<T, String> encoder )
    {
        return new FileRecorder<>( pathMapper, encoder );
    }

    /**
     * Create a consumer that will append the given String to a file.
     * <p>
     * @param path Path of the file to append to
     * <p>
     * @return consumer
     */
    public static Consumer<String> recordTo( final Path path )
    {
        return recordTo( path, Function.identity() );
    }

    /**
     * Create a consumer that will append the given String to a file.
     * <p>
     * @param pathMapper mapping function to determine the path based on the type
     * <p>
     * @return consumer
     */
    public static Consumer<String> recordTo( final Function<String, Path> pathMapper )
    {
        return recordTo( pathMapper, Function.identity() );
    }

    /**
     * Construct a FileRecorder that will create the file if it does not exist, otherwise it will append to it.
     * <p>
     * @param pathMapper mapping function that can generate a Path based on the value being passed
     * @param encoder    mapping function that encodes the value to a String
     */
    public FileRecorder( final Function<T, Path> pathMapper, final Function<T, String> encoder )
    {
        this( pathMapper, encoder, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE );
        // Don't use StandardOpenOption.DSYNC as standard as it causes IO contention, more so in the cloud
    }

    /**
     * Constructor allowing alternate {@link OpenOption}'s to be provided.
     * <p>
     * @param pathMapper mapping function that can generate a Path based on the value being passed
     * @param encoder    mapping function that encodes the value to a String
     * @param options    {@link OpenOption}'s to apply to the file
     */
    public FileRecorder( final Function<T, Path> pathMapper, final Function<T, String> encoder, OpenOption... options )
    {
        this.fileNameMapper = pathMapper;
        this.encoder = encoder;
        this.options = options;
    }

    @Override
    public void accept( T t )
    {
        if( t != null )
        {
            try
            {
                final Path path = fileNameMapper.apply( t );
                final String record = encoder.apply( t );

                if( path != null && encoder != null )
                {
                    LOG.log( Level.FINE, () -> "Writing to " + path );
                    Files.write( path, record.getBytes( Charset.defaultCharset() ), options );
                }
            }
            catch( IOException ex )
            {
                LOG.log( Level.SEVERE, null, ex );
            }
        }
    }

}
