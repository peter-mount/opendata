/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonValue;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Peter T Mount
 */
public class TimeUtils
{

    private static final Logger LOG = Logger.getLogger( TimeUtils.class.getName() );

    /**
     * {@link ZoneId} for UTC which everything in the system runs under
     */
    public static final ZoneId UTC = ZoneId.of( "UTC" );

    /**
     * {@link Zoneid} for London used in the front end - i.e. shows correct schedules when Daylight Savings Time is in effect
     * not those one hour earlier
     */
    public static final ZoneId LONDON = ZoneId.of( "Europe/London" );

    public static Instant getInstant( long timestamp )
    {
        return Instant.ofEpochMilli( timestamp );
    }

    /**
     * Gets the {@link LocalDateTime} for a timestamp in UTC
     * <p>
     * @param timestamp <p>
     * @return
     */
    public static final LocalDateTime getLocalDateTime( final long timestamp )
    {
        return getLocalDateTime( getInstant( timestamp ) );
    }

    /**
     * Gets the current time in UTC
     * <p>
     * @return
     */
    public static final LocalDateTime getLocalDateTime()
    {
        return getLocalDateTime( Instant.now() );
    }

    public static final LocalDateTime getLondonDateTime()
    {
        return LocalDateTime.now( LONDON );
    }

    /**
     * Get's the {@link LocalDateTime} for an {@link Instant} in UTC
     * <p>
     * @param instant <p>
     * @return
     */
    public static final LocalDateTime getLocalDateTime( final Instant instant )
    {
        Clock clock = Clock.fixed( instant, UTC );
        return LocalDateTime.now( clock );
    }

    public static LocalDateTime getLocalDateTime( final LocalDate date )
    {
        return LocalDateTime.of( date, LocalTime.MIN );
    }

    public static final LocalDate getLocalDate()
    {
        return getLocalDateTime().toLocalDate();
    }

    public static final LocalDate getLondonDate()
    {
        return LocalDate.now( LONDON );
    }

    public static final LocalDate getLocalDate( final long timestamp )
    {
        return getLocalDate( getInstant( timestamp ) );
    }

    public static final LocalDate getLocalDate( final Instant instant )
    {
        Clock clock = Clock.fixed( instant, UTC );
        return LocalDate.now( clock );
    }

    /**
     * The formatters we'll use to get a LocalDateTime.
     * <p>
     * TODO order these so the most used one is first
     */
    private static final DateTimeFormatter DATETIMES[]
            =
            {
                DateTimeFormatter.ISO_DATE_TIME,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ISO_INSTANT,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_ZONED_DATE_TIME,
                DateTimeFormatter.RFC_1123_DATE_TIME
            };

    /**
     * The formatters we'll use to get a LocalDate.
     * <p>
     * TODO order these so the most used one is first
     */
    private static final DateTimeFormatter DATES[]
            =
            {
                DateTimeFormatter.ISO_DATE,
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ISO_OFFSET_DATE,
                DateTimeFormatter.BASIC_ISO_DATE
            };

    /**
     * The formatters we'll use to get a LocalDateTime.
     * <p>
     * TODO order these so the most used one is first
     */
    private static final DateTimeFormatter TIMES[]
            =
            {
                DateTimeFormatter.ISO_TIME,
                DateTimeFormatter.ISO_LOCAL_TIME,
                DateTimeFormatter.ISO_OFFSET_TIME
            };

    /**
     * Attempt to parse a string
     * <p>
     * @param s <p>
     * @return
     */
    public static final LocalDateTime getLocalDateTime( final String s )
    {
        if( s != null && !s.isEmpty() )
        {
            for( DateTimeFormatter dtf : DATETIMES )
            {
                try
                {
                    return LocalDateTime.parse( s, dtf );
                } catch( DateTimeParseException ex )
                {
                    // Ignore
                }
            }
        }
        return null;
    }

    /**
     * Attempt to parse a string
     * <p>
     * @param s <p>
     * @return
     */
    public static final LocalDate getLocalDate( final String s )
    {
        if( s != null && !s.isEmpty() )
        {
            for( DateTimeFormatter dtf : DATES )
            {
                try
                {
                    return LocalDate.parse( s, dtf );
                } catch( DateTimeParseException ex )
                {
                    // Ignore
                }
            }
        }
        return null;
    }

    /**
     * Returns the LocalTime based on the second of the day
     * <p>
     * @param secondOfDay <p>
     * @return
     */
    public static final LocalTime getLocalTime( final long secondOfDay )
    {
        return LocalTime.ofSecondOfDay( secondOfDay );
    }

    /**
     * Attempt to parse a string
     * <p>
     * @param s <p>
     * @return
     */
    public static final LocalTime getLocalTime( final String s )
    {
        if( s != null && !s.isEmpty() )
        {
            // Custom formats
            if( s.length() == 6 )
            {
                // hhmmss
                try
                {
                    return LocalTime.of(
                            Integer.parseInt( s.substring( 0, 2 ) ),
                            Integer.parseInt( s.substring( 2, 4 ) ),
                            Integer.parseInt( s.substring( 4, 6 ) ) );
                } catch( Exception ex )
                {
                    LOG.log( Level.SEVERE, "Parse fail for: " + s, ex );
                }
            }
            else if( s.length() == 4 )
            {
                // hhmm
                try
                {
                    return LocalTime.of(
                            Integer.parseInt( s.substring( 0, 2 ) ),
                            Integer.parseInt( s.substring( 2, 4 ) ) );
                } catch( Exception ex )
                {
                    LOG.log( Level.SEVERE, "Parse fail for: " + s, ex );
                }
            }

            // Check default formats
            for( DateTimeFormatter dtf : TIMES )
            {
                try
                {
                    return LocalTime.parse( s, dtf );
                } catch( DateTimeParseException ex )
                {
                    // Ignore
                }
            }
        }
        return null;
    }

    public static LocalTime getLocalTime( ResultSet rs, String col )
            throws SQLException
    {
        Time t = rs.getTime( col );
        if( t == null )
        {
            return null;
        }
        return t.toLocalTime();
    }

    /**
     * Parses a PostgreSQL Interval type into a {@link Duration}. This is valid only for up to 24 hours"
     *
     * @param s Interval in "hh:mm:ss" format. A negative duration will have '-' first.
     * <p>
     * @return <p>
     * @throws SQLException
     */
    public static Duration getDuration( String s )
            throws SQLException
    {
        if( s == null || s.isEmpty() )
        {
            return null;
        }
        boolean negative = s.startsWith( "-" );
        LocalTime t = LocalTime.parse( negative ? s.substring( 1 ) : s );
        return Duration.of( negative ? -t.toSecondOfDay() : t.toSecondOfDay(), ChronoUnit.SECONDS );
    }

    public static Duration getDuration( ResultSet rs, String col )
            throws SQLException
    {
        return getDuration( rs.getString( col ) );
    }

    public static Date toDate( LocalDate ld )
    {
        if( ld == null )
        {
            return null;
        }
        return Date.from( ld.atStartOfDay( UTC ).toInstant() );
    }

    public static java.sql.Date toSqlDate( LocalDate ld )
    {
        return ld == null ? null : java.sql.Date.valueOf( ld );
    }

    public static java.sql.Time toSqlTime( LocalTime lt )
    {
        return lt == null ? null : java.sql.Time.valueOf( lt );
    }

    public static void setDate( PreparedStatement s, int col, LocalDate ld )
            throws SQLException
    {
        if( ld == null )
        {
            s.setNull( col, Types.DATE );
        }
        else
        {
            s.setDate( col, java.sql.Date.valueOf( ld ) );
        }
    }

    public static void setDateTime( PreparedStatement s, int col, LocalDateTime ld )
            throws SQLException
    {
        if( ld == null )
        {
            s.setNull( col, Types.TIMESTAMP );
        }
        else
        {
            s.setTimestamp( col, Timestamp.valueOf( ld ) );
        }
    }

    public static LocalDate getLocalDate( ResultSet rs, String n )
            throws SQLException
    {
        Date d = rs.getDate( n );
        if( rs.wasNull() )
        {
            return null;
        }
        return getLocalDate( d.getTime() );
    }

    public static LocalDate getLocalDate( ResultSet rs, int i )
            throws SQLException
    {
        Date d = rs.getDate( i );
        if( rs.wasNull() )
        {
            return null;
        }
        return getLocalDate( d.getTime() );
    }

    /**
     * Return the string of a {@link LocalDate} or null if it's null
     * <p>
     * @param d date
     * <p>
     * @return String or null
     */
    public static String toString( LocalDate d )
    {
        return d == null ? null : d.toString();
    }

    /**
     * Return the string of a {@link LocalTime} or null if it's null
     * <p>
     * @param t time
     * <p>
     * @return String or null
     */
    public static String toString( LocalTime t )
    {
        return t == null ? null : t.toString();
    }

    /**
     * Return the string of a {@link LocalDateTime} or null if it's null
     * <p>
     * @param dt date time
     * <p>
     * @return String or null
     */
    public static String toString( LocalDateTime dt )
    {
        return dt == null ? null : dt.toString();
    }

    /**
     * Convert a {@link LocalDate} to Json handling nulls
     * <p>
     * @param d date
     * <p>
     * @return JsonValue
     */
    public static JsonValue toJson( LocalDate d )
    {
        return d == null ? JsonValue.NULL : JsonUtils.createJsonString( d.toString() );
    }

    /**
     * Convert a {@link LocalTime} to Json handling nulls
     * <p>
     * @param t time
     * <p>
     * @return JsonValue
     */
    public static JsonValue toJson( LocalTime t )
    {
        return t == null ? JsonValue.NULL : JsonUtils.createJsonString( t.toString() );
    }

    /**
     * Convert a {@link LocalDateTime} to Json handling nulls
     * <p>
     * @param dt date time
     * <p>
     * @return JsonValue
     */
    public static JsonValue toJson( LocalDateTime dt )
    {
        return dt == null ? JsonValue.NULL : JsonUtils.createJsonString( dt.toString() );
    }

    public static JsonValue toJson( long t )
    {
        return toJson( getLocalDateTime( t ) );
    }

    /**
     * Converts a {@link LocalDate} into a database date used in the dim_date table.
     */
    public static final Function<LocalDate, Long> toDBDate = date -> (long) ((date.getYear() * 400) + date.getDayOfYear());
    /**
     * Converts a database date from the dim_date table into a {@link LocalDate}
     */
    public static final Function<Long, LocalDate> fromDBDate = dt -> LocalDate.ofYearDay( (int) (dt / 400L), (int) (dt % 400L) );

    /**
     * Function to convert a LocalDateTime to a LocalDate representing rail days.
     * <p>
     * A Rail day starts at 0200, so 0159 is in the day before.
     */
    public static final Function<LocalDateTime, LocalDate> toRailDate = t -> t.minusHours( 2 ).toLocalDate();

    /**
     * Comparator to compare two {@link LocalTime}'s into ascending order accounting for times crossing midnight as per Darwin
     * rules.
     * <p>
     * Darwin uses the following rules to handle these cases:
     * <table>
     * <tr><td>Time difference</td><td>Interpret as</td></tr>
     * <tr><td>Less than -6 hours</td><td>Crossed midnight</td></tr>
     * <tr><td>Between -6 and 0 hours</td><td>Back in time</td></tr>
     * <tr><td>Between 0 and +18 hours</td><td>Normal increasing time</td></tr>
     * <tr><td>Greater than +18 hours</td><td>Back in time and crossed midnight</td></tr>
     * </table>
     * However we only look for |Δhour| &gt; 18 hours as that handles both crossed midnight cases here.
     */
    public static final Comparator<LocalTime> compareLocalTimeDarwin = ( a, b ) -> Math.abs( a.getHour() - b.getHour() ) < 18 ? a.compareTo( b ) : -a.
            compareTo( b );

    /**
     * Returns a {@link Predicate} that returns true if a {@link LocalDateTime} is between two others.
     * <p>
     * @param start Start time
     * @param end   End time
     * <p>
     * @return predicate
     * <p>
     * @throws IllegalArgumentException if start is not before end
     */
    public static Predicate<LocalDateTime> isWithin( LocalDateTime start, LocalDateTime end )
    {
        if( !start.isBefore( end ) )
        {
            throw new IllegalArgumentException( "Start " + start + " must be before end " + end );
        }

        return dt -> !dt.isBefore( start ) && dt.isBefore( end );
    }

    public static Instant parseXMLInstant( String s )
    {
        return DatatypeConverter.parseDateTime( s ).toInstant();
    }

    public static LocalDateTime parseXMLLocalDateTime( String s )
    {
        return getLocalDateTime( parseXMLInstant( s ) );
    }

    public static String toDateHeader( LocalDateTime dt )
    {
        return String.format( "%s, %02d %s %d %s GMT",
                              dt.getDayOfWeek().getDisplayName( TextStyle.SHORT, Locale.ENGLISH ),
                              dt.getDayOfMonth(),
                              dt.getMonth().getDisplayName( TextStyle.SHORT, Locale.ENGLISH ),
                              dt.getYear(),
                              dt.toLocalTime()
        );
    }

    public static LocalDate parseLocalDate( String s )
    {
        if( s == null || s.isEmpty() )
        {
            return null;
        }
        try
        {
            return LocalDate.parse( s );
        } catch( DateTimeParseException ex )
        {
            LOG.log( Level.WARNING, () -> "Failed to parse date \"%s\"" );
            return null;
        }
    }

    public static LocalTime parseLocalTime( String s )
    {
        if( s == null || s.isEmpty() )
        {
            return null;
        }
        try
        {
            return LocalTime.parse( s );
        } catch( DateTimeParseException ex )
        {
            LOG.log( Level.WARNING, () -> "Failed to parse time \"%s\"" );
            return null;
        }
    }

}
