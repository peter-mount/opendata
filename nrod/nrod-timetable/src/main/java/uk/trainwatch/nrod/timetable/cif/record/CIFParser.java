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
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.ATSCode;
import uk.trainwatch.nrod.timetable.util.Activity;
import uk.trainwatch.nrod.timetable.util.AssociationCategory;
import uk.trainwatch.nrod.timetable.util.AssociationDateIndicator;
import uk.trainwatch.nrod.timetable.util.AssociationType;
import uk.trainwatch.nrod.timetable.util.BankHolidayRunning;
import uk.trainwatch.nrod.timetable.util.BusSec;
import uk.trainwatch.nrod.timetable.util.Catering;
import uk.trainwatch.nrod.timetable.util.DaysRun;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.PowerType;
import uk.trainwatch.nrod.timetable.util.Reservations;
import uk.trainwatch.nrod.timetable.util.STPIndicator;
import uk.trainwatch.nrod.timetable.util.ServiceBranding;
import uk.trainwatch.nrod.timetable.util.Sleepers;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainClass;
import uk.trainwatch.nrod.timetable.util.TrainStatus;
import uk.trainwatch.nrod.timetable.util.TrainUID;

/**
 * Parses a line
 * <p>
 * @author Peter T Mount
 */
public final class CIFParser
{

    private static final Logger LOG = Logger.getLogger( CIFParser.class.getName() );

    private final boolean strict;
    private String line;
    private int length;
    private int pos;
    private int lineCount;

    /**
     * Construct a non-strict parser
     */
    public CIFParser()
    {
        this( false );
    }

    /**
     * Constructor.
     * <p>
     * @param strict if true then an unknown record type will cause the parser to fail.
     */
    public CIFParser( boolean strict )
    {
        this.strict = strict;
    }

    /**
     * Reset the parser to a new record line
     * <p>
     * @param line New line
     * <p>
     * @return this instance
     */
    public CIFParser reset( String line )
    {
        this.line = Objects.requireNonNull( line );
        length = line.length();
        pos = 0;
        return this;
    }

    /**
     * Reset the parser and immediately parse the new record.
     * <p>
     * This method is useful within a stream, e.g. if {@code parser} refers to a parser instance then
     * {@code map(parser::parse)} will map a String to a {@link Record}.
     * <p>
     * @param line New line
     * <p>
     * @return parsed record, null if none generated
     * <p>
     * @throws IllegalArgumentException if the record type is unknown and strict mode is enabled
     */
    public Record parse( String line )
    {
        reset( line );
        return parse();
    }

    /**
     * Parse the current line and generate a record
     * <p>
     * @return parsed record, null if none generated
     * <p>
     * @throws IllegalArgumentException if the record type is unknown and strict mode is enabled
     */
    public Record parse()
    {
        lineCount++;
        final String t = getString( 2 );
        final RecordType rt = RecordType.lookup( t );
        if( rt == null )
        {
            if( strict )
            {
                throw new IllegalArgumentException( "Unsupported type \"" + t + "\" line " + lineCount );
            }
            return null;
        }
        else
        {
            Function<CIFParser, Record> factory = rt.getFactory();
            return factory == null ? null : factory.apply( this );
        }
    }

    /**
     * The line number in the source file. The first line is 1, but this will return 0 if no lines have been parsed.
     * <p>
     * @return Line number
     */
    public int lineCount()
    {
        return lineCount;
    }

    private void ensureAvailable( int l )
    {
        if( (pos + l) > length )
        {
            throw new IllegalStateException( "Requested " + l + " but only " + (length - pos) + " available" );
        }
    }

    /**
     * Utility used in debugging to return the current line.
     * <p>
     * This can be used in a stream with {@code map(parser::currentLine).} to bring back the current line.
     * <p>
     * @param o Ignored.
     * <p>
     * @return
     */
    public String currentLine( Object o )
    {
        return line;
    }

    public int position()
    {
        return pos;
    }

    /**
     * Get a String.
     * <p>
     * This string will always be l characters long.
     * <p>
     * @param l number of characters to read
     * <p>
     * @return String
     */
    public String getString( int l )
    {
        ensureAvailable( l );
        int s = pos;
        pos += l;
        return line.substring( s, pos );
    }

    /**
     * Get a String. If it's blank (all spaces) then null is returned.
     * <p>
     * This string will always be l characters long if not null.
     * <p>
     * @param l number of characters to read
     * <p>
     * @return String
     */
    public String getStringNull( int l )
    {
        if( isBlank( l ) )
        {
            skip( l );
            return null;
        }
        return getString( l );
    }

    /**
     * Peek at the next l characters
     * <p>
     * This does not move the current record position.
     * <p>
     * @param l <p>
     * @return String of l characters
     */
    public String peek( int l )
    {
        ensureAvailable( l );
        return line.substring( pos, pos + l );
    }

    /**
     * Is the next l characters blank (all spaces).
     * <p>
     * This does not move the current record position.
     * <p>
     * @param l number of characters
     * <p>
     * @return true if next l characters are all spaces
     */
    public boolean isBlank( int l )
    {
        ensureAvailable( l );
        for( int i = 0, p = pos; i < l; i++, p++ )
        {
            if( line.charAt( p ) != ' ' )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Get an int
     * <p>
     * @param l number of characters to read
     * <p>
     * @return int
     */
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

    /**
     * Get a long
     * <p>
     * @param l number of characters to read
     * <p>
     * @return long
     */
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
     * Parses a boolean from a single value
     * <p>
     * @param t The character expected for true
     * <p>
     * @return true if char matches t, false otherwise
     */
    public boolean getBoolean( String t )
    {
        Objects.requireNonNull( t );
        return t.equals( getString( 1 ) );
    }

    /**
     * Parse a date with the format "ddmmyy" accounting for Network Rail's Y2K rules.
     * <p>
     * @return
     */
    public LocalDate getDate_ddmmyy()
    {
        ensureAvailable( 6 );

        if( isBlank( 6 ) )
        {
            skip( 6 );
            return null;
        }

        int d = getInt( 2 );
        int m = getInt( 2 );
        int y = getInt( 2 );
        return of( y, m, d );
    }

    /**
     * Parse a date with the format "yymmdd" accounting for Network Rail's Y2K rules.
     * <p>
     * @return date
     */
    public LocalDate getDate_yymmdd()
    {
        ensureAvailable( 6 );

        if( isBlank( 6 ) )
        {
            skip( 6 );
            return null;
        }

        // Note 3 on P16 of the CIF guide
        int y = getInt( 2 );
        int m = getInt( 2 );
        int d = getInt( 2 );

        // Some dates have 999999 which means open ended, so use Jan 1 2100 as the date
        if( y == 99 && m == 99 && d == 99 )
        {
            return of( 2100, 1, 1 );
        }

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
     * Parse a public timetable time.
     * <p>
     * This will have the format "hhmm"
     * <p>
     * @return
     */
    public LocalTime getTime_hhmm()
    {
        ensureAvailable( 4 );

        // Check for blank, if so return null
        if( isBlank( 4 ) )
        {
            skip( 4 );
            return null;
        }

        return LocalTime.of( getInt( 2 ), getInt( 2 ) );
    }

    /**
     * Parse a working timetable time.
     * <p>
     * This will be either "hhmm " or "hhmmH" where H means half-minute so 30 is inserted as the second value.
     * <p>
     * @return
     */
    public LocalTime getTime_hhmmH()
    {
        ensureAvailable( 5 );

        // Check for blank, if so return null
        if( isBlank( 5 ) )
        {
            skip( 5 );
            return null;
        }

        return LocalTime.of( getInt( 2 ), getInt( 2 ), "H".equals( getString( 1 ) ) ? 30 : 0 );
    }

    /**
     * Get an allowance time in seconds. This is usually used for engineering, pathing or performance allowances where
     * the time is specified in minutes or with an H for Half a minute.
     * <p>
     * A blank represents half a minute
     * <p>
     * @return
     */
    public int getAllowance()
    {
        char a = getString( 1 ).
                charAt( 0 );

        int t = 0;
        if( Character.isDigit( a ) )
        {
            t = (int) (a - '0') * 60;
        }

        if( getBoolean( "H" ) )
        {
            t += 30;
        }
        return t;
    }

    public Activity[] getActivity()
    {
        return Activity.lookupAll( getString( 12 ) );
    }

    public AssociationCategory getAssociationCategory()
    {
        return AssociationCategory.lookup( getString( 2 ) );
    }

    public AssociationDateIndicator getAssociationDateIndicator()
    {
        return AssociationDateIndicator.lookup( getString( 1 ) );
    }

    public AssociationType getAssociationType()
    {
        return AssociationType.lookup( getString( 1 ) );
    }

    public ATOCCode getATOCCode()
    {
        return ATOCCode.lookup( getString( 2 ) );
    }

    public ATSCode getATSCode()
    {
        return ATSCode.lookup( getString( 1 ) );
    }

    public BusSec getBusSec()
    {
        return BusSec.lookup( getString( 1 ) );
    }

    public Catering[] getCatering()
    {
        return Catering.lookupAll( getString( 4 ) );
    }

    public DaysRun getDaysRun()
    {
        return new DaysRun( getString( 7 ) );
    }

    public BankHolidayRunning getBankHolidayRunning()
    {
        return BankHolidayRunning.lookup( getString( 1 ) );
    }

    public OperatingCharacteristics[] getOperatingCharacteristics()
    {
        return OperatingCharacteristics.lookupAll( getString( 6 ) );
    }

    public PowerType getPowerType()
    {
        return PowerType.lookup( getString( 3 ) );
    }

    public Reservations getReservations()
    {
        return Reservations.lookup( getString( 1 ) );
    }

    public ServiceBranding[] getServiceBranding()
    {
        return ServiceBranding.lookupAll( getString( 4 ) );
    }

    public Sleepers getSleepers()
    {
        return Sleepers.lookup( getString( 1 ) );
    }

    public STPIndicator getSTPIndicator()
    {
        return STPIndicator.lookup( getString( 1 ) );
    }

    public TimingLoad getTimingLoad()
    {
        return TimingLoad.lookup( getString( 4 ) );
    }

    public TrainStatus getTrainStatus()
    {
        return TrainStatus.lookup( getString( 1 ) );
    }

    public TrainCategory getTrainCategory()
    {
        return TrainCategory.lookup( getString( 2 ) );
    }

    public TrainClass getTrainClass()
    {
        return TrainClass.lookup( getString( 1 ) );
    }

    public TrainUID getTrainUID()
    {
        return new TrainUID( getString( 6 ) );
    }

    public TransactionType getTransactionType()
    {
        return TransactionType.lookup( getString( 1 ) );
    }

    /**
     * Get a 7 character Tiploc
     * <p>
     * @return
     */
    public Tiploc getTiploc()
    {
        return new Tiploc( getString( 7 ) );
    }

    /**
     * Get a tiploc and skip the suffix character
     * <p>
     * @return
     */
    public Tiploc getTiplocSuffix()
    {
        Tiploc t = new Tiploc( getString( 7 ) );
        skip( 1 );
        return t;
    }

    /**
     * Skip characters
     * <p>
     * @param l number of chars to skip
     * <p>
     * @return null
     */
    public Void skip( int l )
    {
        ensureAvailable( l );
        pos += l;
        return null;
    }

    public static CIFParser debug( CIFParser p )
    {
        System.out.println( p.line );
        return p;
    }
}
