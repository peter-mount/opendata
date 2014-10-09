/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.cif.record.Record;
import uk.trainwatch.nrod.timetable.cif.record.RecordType;

/**
 * Parses a line
 * <p>
 * @author Peter T Mount
 */
public final class CIFParser
{

    private final boolean strict;
    private String line;
    private int length;
    private int pos;

    public CIFParser()
    {
        this( false );
    }

    public CIFParser( boolean strict )
    {
        this.strict = strict;
    }

    public CIFParser setLine( String line )
    {
        this.line = Objects.requireNonNull( line );
        length = line.length();
        pos = 0;
        return this;
    }

    public Record parse( String line )
    {
        setLine( line );
        return parse();
    }

    public Record parse()
    {
        final String t = getString( 2 );
        final RecordType rt = RecordType.lookup( t );
        if( rt == null )
        {
            if( strict )
            {
                throw new IllegalArgumentException( "Unsupported type \"" + t + "\"" );
            }
            return null;
        }
        else
        {
            Function<CIFParser, Record> factory = rt.getFactory();
            return factory == null ? null : factory.apply( this );
        }
    }

    private void ensureAvailable( int l )
    {
        if( (pos + l) > length )
        {
            throw new IllegalStateException( "Requested " + l + " but only " + (length - pos) + " available" );
        }
    }

    public String getString( int l )
    {
        ensureAvailable( l );
        int s = pos;
        pos += l;
        return line.substring( s, pos );
    }

    public int getInt( int l )
    {
        try
        {
            return Integer.parseInt( getString( l ).
                    trim() );
        }
        catch( NumberFormatException ex )
        {
            return 0;
        }
    }

    public long getLong( int l )
    {
        try
        {
            return Long.parseLong( getString( l ).
                    trim() );
        }
        catch( NumberFormatException ex )
        {
            return 0L;
        }
    }

    /**
     * Return a {@link LocalDate} from ddmmyy string
     * <p>
     * @return
     */
    public LocalDate getDate_ddmmyy()
    {
        ensureAvailable( 6 );
        int d = getInt( 2 );
        int m = getInt( 2 );
        int y = getInt( 2 );
        return of( y, m, d );
    }

    public LocalDate getDate_yymmdd()
    {
        ensureAvailable( 6 );

        // Note 3 on P16 of the CIF guide
        int y = getInt( 2 );
        int m = getInt( 2 );
        int d = getInt( 2 );
        return of( y, m, d );
    }

    /**
     * Fixes y for y2k purposes using NR's standard
     * <p>
     * @param y1
     * @param m
     * @param d  <p>
     * @return
     */
    private LocalDate of( int y1, int m, int d )
    {
        int y = y1;
        if( y >= 60 )
        {
            y = y + 1900;
        }
        else
        {
            y = y + 2000;
        }
        return LocalDate.of( y, m, d );
    }

    /**
     * Gets a time of format hhmm
     * <p>
     * @return
     */
    public LocalTime getTime2()
    {
        ensureAvailable( 4 );
        return LocalTime.of( getInt( 2 ), getInt( 2 ) );
    }

    /**
     * hhmm or hhmmH where H means half-minute
     * <p>
     * @return
     */
    public LocalTime getTime2H()
    {
        ensureAvailable( 5 );
        return LocalTime.of( getInt( 2 ), getInt( 2 ), "H".equals( getString( 1 ) ) ? 30 : 0 );
    }

    public TransactionType getTransactionType()
    {
        return TransactionType.lookup( getString( 1 ) );
    }
}
