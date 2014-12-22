/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan.loader;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public abstract class BaseImporter<T>
        implements Importer<T>
{

    @SuppressWarnings( "NonConstantLogger" )
    protected final Logger log = Logger.getLogger( getClass().
            getName() );

    protected final Path path;

    public BaseImporter( Path path )
    {
        this.path = path;
    }

    @Override
    public final Path getPath()
    {
        return path;
    }

    @Override
    public final Logger getLog()
    {
        return log;
    }

}
