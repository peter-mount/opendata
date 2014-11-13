/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonValue;

/**
 *
 * @author Peter T Mount
 */
public class TimeUtils
{

    private static final Logger LOG = Logger.getLogger( TimeUtils.class.
            getName() );

    public static final ZoneId UTC = ZoneId.of( "UTC" );

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
        return getLocalDateTime().
                toLocalDate();
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
    private static final DateTimeFormatter DATETIMES[] =
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
            for( DateTimeFormatter dtf: DATETIMES )
            {
                try
                {
                    return LocalDateTime.parse( s, dtf );
                }
                catch( DateTimeParseException ex )
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
            for( DateTimeFormatter dtf: DATES )
            {
                try
                {
                    return LocalDate.parse( s, dtf );
                }
                catch( DateTimeParseException ex )
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
                }
                catch( Exception ex )
                {
                    LOG.log( Level.SEVERE, "Parse fail for: " + s, ex );
                }
            }
            else if( s.length() == 4 )
            {
                // hhmm
                try
                {
                    return LocalTime.
                            of( Integer.parseInt( s.substring( 0, 2 ) ),
                                Integer.parseInt( s.substring( 2, 4 ) ) );
                }
                catch( Exception ex )
                {
                    LOG.log( Level.SEVERE, "Parse fail for: " + s, ex );
                }
            }

            // Check default formats
            for( DateTimeFormatter dtf: TIMES )
            {
                try
                {
                    return LocalTime.parse( s, dtf );
                }
                catch( DateTimeParseException ex )
                {
                    // Ignore
                }
            }
        }
        return null;
    }

    public static Date toDate( LocalDate ld )
    {
        if( ld == null )
        {
            return null;
        }
        return Date.from( ld.atStartOfDay( UTC ).
                toInstant() );
    }

    public static java.sql.Date toSqlDate( LocalDate ld )
    {
        return ld == null ? null : java.sql.Date.valueOf( ld );
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

    public static void setDateTime( PreparedStatement s, int col,
                                    LocalDateTime ld )
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
        return d == null ? JsonValue.NULL : JsonUtils.createJsonString( d.
                toString() );
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
        return t == null ? JsonValue.NULL : JsonUtils.createJsonString( t.
                toString() );
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
        return dt == null ? JsonValue.NULL : JsonUtils.createJsonString( dt.
                toString() );
    }

    /**
     * Converts a {@link LocalDate} into a database date used in the dim_date
     * table.
     * <p>
     * @param date
     *             <p>
     * @return
     */
    public static final Function<LocalDate, Long> toDBDate = date -> (long) ((date.getYear() * 400) + date.getDayOfYear());
    /**
     * Converts a database date from the dim_date table into a {@link LocalDate}
     */
    public static final Function<Long, LocalDate> fromDBDate = dt -> LocalDate.
            ofYearDay( (int) (dt / 400L), (int) (dt % 400L) );

    /**
     * Function to convert a LocalDateTime to a LocalDate representing rail days.
     * <p>
     * A Rail day starts at 0200, so 0159 is in the day before.
     */
    public static final Function<LocalDateTime, LocalDate> toRailDate = t -> t.minusHours( 2 ).
            toLocalDate();

}
