/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Consumes a {@link CmsFile} and stores it on disk.
 * <p>
 * Once stored it's accessible by the main cms code
 * <p>
 * @author peter
 */
public class CmsWriter
        implements Consumer<CmsFile>
{

    private static final Logger LOG = Logger.getLogger( CmsWriter.class.getName() );
    private final String basePath;

    public CmsWriter( String basePath )
    {
        this.basePath = basePath;
    }

    @Override
    public void accept( CmsFile t )
    {
        Path path = t.toPath( basePath );

        LOG.log( Level.INFO, () -> "Writing " + path );

        try {
            path.getParent().toFile().mkdirs();

            Files.write( path, t.toBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING );
        }
        catch( IOException ex ) {
            LOG.log( Level.SEVERE, ex, () -> "Failed to write " + path );
            throw new UncheckedIOException( ex );
        }
    }

}
