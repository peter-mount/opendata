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
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.ScheduleID;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
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

    @Resource( name = "jdbc/rail" )
    private DataSource dataSource;

    @Inject
    private DarwinJaxbContext darwinJaxbContext;

    protected final void accept( Pport t, Collection<S> s )
    {
        if( t != null )
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
    }

    protected abstract void apply( Pport t, S ts, Connection con )
            throws SQLException;

    protected final Pport getForecast( Connection con, String rid )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.forecast WHERE rid=?", rid ) )
        {
            return SQL.stream( ps, SQL.STRING_LOOKUP ).
                    map( darwinJaxbContext.fromXML() ).
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

        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.forecast(?,?::xml)" ) )
        {
            SQL.executeQuery( ps,
                              schedule.getRid(),
                              darwinJaxbContext.toXML().apply( p )
            );
        }
    }

    protected final <T> List<T> replace( List<T> l, T v )
    {
        l.clear();
        l.add( v );
        return l;
    }
}
