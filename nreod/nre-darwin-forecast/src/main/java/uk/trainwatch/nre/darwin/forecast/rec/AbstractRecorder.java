/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast.rec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.ScheduleID;
import uk.trainwatch.nre.darwin.model.util.TplLocation;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.sql.SQL;

/**
 * Handles the recording of a TD record, merging it as necessary
 *
 * @author peter
 * @param <S>
 */
public abstract class AbstractRecorder<S>
        implements Consumer<Pport>
{

    protected static final Logger LOG = Logger.getLogger( AbstractRecorder.class.getName() );

    private final DataSource dataSource;

    public AbstractRecorder( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    protected final void accept( Pport t, Collection<S> s )
    {
        s.forEach( ts ->
        {
            try( Connection con = dataSource.getConnection() )
            {
                try
                {
                    con.setAutoCommit( false );
                    apply( t, ts, con );
                    con.commit();
                } catch( SQLException ex )
                {
                    LOG.log( Level.SEVERE, null, ex );
                    con.rollback();
                }
            } catch( SQLException ex )
            {
                LOG.log( Level.SEVERE, null, ex );
            }
        }
        );
    }

    protected abstract void apply( Pport t, S ts, Connection con )
            throws SQLException;

    protected final Pport getForecast( Connection con, String rid )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.forecast WHERE rid=?", rid ) )
        {
            return SQL.stream( ps, SQL.STRING_LOOKUP ).
                    map( DarwinJaxbContext.fromXML ).
                    filter( Objects::nonNull ).
                    findAny().
                    orElse( null );
        }
    }

    protected final void recordForecast( Connection con, Pport p )
            throws SQLException
    {
        recordForecast( con, p, ScheduleID.getScheduleID( p ) );
    }

    protected final void recordForecast( Connection con, Pport p, ScheduleID schedule )
            throws SQLException
    {
        if( p == null || schedule == null )
        {
            return;
        }
        
        Pport.UR ur = p.getUR();

        // Generate CSV of all tiplocs in schedule & forecast from the TplLocation's
        Stream<TplLocation> stream = null;
        if( ur.isSetSchedule() )
        {
            stream = Streams.concat( stream,
                    ur.getSchedule().stream().
                    flatMap( s -> s.getOROrOPOROrIP().stream() ).
                    map( TplLocation::castTplLocation )
            );
        }

        if( ur.isSetTS() )
        {
            stream = Streams.concat( stream,
                    p.getUR().
                    getTS().
                    stream().
                    flatMap( t -> t.getLocation().stream() )
            );
        }

        String tpl = stream == null ? "" : stream.
                filter( Objects::nonNull ).
                map( TplLocation::getTpl ).
                //sorted().
                collect( Collectors.joining( "," ) );

        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.forecast(?,?,?,?,?,?,?)" ) )
        {
            SQL.executeQuery( ps,
                    schedule.getRid(),
                    schedule.getUid(),
                    Objects.toString( schedule.getSsd(), null ),
                    // Activated?
                    ur.isSetSchedule(),
                    // Deactivated?
                    ur.isSetDeactivated(),
                    // Copy of the final XML
                    DarwinJaxbContext.toXML.apply( p ),
                    // Tiploc list for crossreferencing
                    tpl
            );
        }
    }

}
