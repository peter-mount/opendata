/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.io;

import java.nio.file.Path;

/**
 *
 * @author Peter T Mount
 */
public class DatePathMapper<T>
        extends AbstractPathMapper<T>
{

    private final boolean hours;

    public DatePathMapper( String database, String prefix )
    {
        this( database, prefix, false );
    }

    public DatePathMapper( String database, String prefix, String suffix )
    {
        this( database, prefix, suffix, false );
    }

    public DatePathMapper( String database, String prefix, boolean hours )
    {
        super( database, prefix );
        this.hours = hours;
    }

    public DatePathMapper( String database, String prefix, String suffix, boolean hours )
    {
        super( database, prefix, suffix );
        this.hours = hours;
    }

    @Override
    public Path apply( T t )
    {
        return getPath( getLocalDateTime(), hours );
    }

}
