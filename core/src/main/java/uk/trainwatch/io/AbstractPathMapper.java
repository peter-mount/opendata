/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.io;

import java.io.File;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author Peter T Mount
 */
public abstract class AbstractPathMapper<T>
        implements Function<T, Path>
{

    private final String database;
    private final String prefix;
    private final String suffix;

    public AbstractPathMapper( String database, String prefix )
    {
        this( database, prefix, null );
    }

    public AbstractPathMapper( String database, String prefix, String suffix )
    {
        this.database = Objects.requireNonNull( database );
        this.prefix = Objects.requireNonNull( prefix );
        this.suffix = suffix == null ? "" : suffix;
    }

    protected final LocalDateTime getLocalDateTime()
    {
        return getLocalDateTime( Instant.now() );
    }

    protected final LocalDateTime getLocalDateTime( final long timestamp )
    {
        return getLocalDateTime( Instant.ofEpochMilli( timestamp ) );
    }

    protected final LocalDateTime getLocalDateTime( final Instant instant )
    {
        Clock clock = Clock.fixed( instant, ZoneId.of( "UTC" ) );
        return LocalDateTime.now( clock );
    }

    protected final Path getPath( final LocalDateTime dateTime, final boolean hours )
    {
        return getFile( dateTime, hours ).
                toPath();
    }

    protected final File getFile( final LocalDateTime dateTime, final boolean hours )
    {
        File dir = getDir( dateTime, hours );
        int id = hours ? dateTime.getMinute() : dateTime.getHour();
        return new File( dir, id + suffix );
    }

    protected final File getDir( final LocalDateTime dateTime, final boolean hours )
    {
        String fileName;
        if( hours )
        {
            fileName = String.join( "/",
                                    database,
                                    prefix,
                                    String.valueOf( dateTime.getYear() ),
                                    String.valueOf( dateTime.getMonth().
                                            getValue() ),
                                    String.valueOf( dateTime.getDayOfMonth() ),
                                    String.valueOf( dateTime.getHour() )
            );
        }
        else
        {
            fileName = String.join( "/",
                                    database,
                                    prefix,
                                    String.valueOf( dateTime.getYear() ),
                                    String.valueOf( dateTime.getMonth().
                                            getValue() )
            );
        }

        File file = new File( fileName.replaceAll( " ", "" ) );
        file.mkdirs();
        return file;
    }

}
