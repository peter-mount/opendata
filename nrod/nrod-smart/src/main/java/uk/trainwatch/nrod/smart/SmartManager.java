/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.util.CollectionUtils;
import uk.trainwatch.util.cache.Cache;
import uk.trainwatch.util.sql.KeyValue;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
public enum SmartManager
{

    INSTANCE;
    private static final Logger LOG = Logger.getLogger( SmartManager.class.getName() );
    private DataSource dataSource;

    private final Map<Long, SmartArea> areas = new ConcurrentHashMap<>();
    private final Map<Long, String> berths = new ConcurrentHashMap<>();
    private final Map<Long, String> lines = new ConcurrentHashMap<>();
    private final Cache<SmartBerthMovement, Smart> cache = new Cache<>();

    void setDataSource( DataSource dataSource )
            throws SQLException
    {
        this.dataSource = dataSource;
        reload();
    }

    public void reload()
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             Statement s = con.createStatement() )
        {
            LOG.log( Level.INFO, "Loading Smart Train Describer Areas" );
            CollectionUtils.replace( areas,
                                     SQL.stream( s.executeQuery( "SELECT * FROM reference.smart_area" ), SmartArea.fromSQL ).
                                     collect( Collectors.toMap( SmartArea::getId, Function.identity() ) )
            );

            LOG.log( Level.INFO, "Loading Smart Train Berths" );
            CollectionUtils.replace( berths,
                                     SQL.stream( s.executeQuery( "SELECT id,berth FROM reference.smart_berth" ), KeyValue.LONG_STRING ).
                                     collect( KeyValue.toMap() )
            );

            LOG.log( Level.INFO, "Loading Smart Train Lines" );
            CollectionUtils.replace( lines,
                                     SQL.stream( s.executeQuery( "SELECT id,line FROM reference.smart_line" ), KeyValue.LONG_STRING ).
                                     collect( KeyValue.toMap() )
            );
        }
    }

    public SmartArea getArea( long id )
    {
        return areas.get( id );
    }

    public String getBerth( long id )
    {
        return berths.get( id );
    }

    public String getLine( long id )
    {
        return lines.get( id );
    }

    public Smart getSmart( SmartBerthMovement movement )
    {
        try
        {
            return cache.computeIfAbsent( movement, this::lookup );
        }
        catch( UncheckedSQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
            return null;
        }
    }

    public void perform( SmartBerthMovement movement, Consumer<Smart> c )
    {
        try
        {
            cache.compute( movement, this::lookup, c );
        }
        catch( UncheckedSQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
        }
    }

    public void perform( SmartBerthMovement movement, BiConsumer<SmartBerthMovement, Smart> c )
    {
        try
        {
            cache.compute( movement, this::lookup, c );
        }
        catch( UncheckedSQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
        }
    }

    public static Consumer<SmartBerthMovement> consume( Consumer<Smart> c )
    {
        return t -> INSTANCE.perform( t, c );
    }

    public static Consumer<SmartBerthMovement> consume( BiConsumer<SmartBerthMovement, Smart> c )
    {
        return t -> INSTANCE.perform( t, c );
    }

    private Smart lookup( SmartBerthMovement movement )
    {
        LOG.log( Level.INFO, () -> "Lookup " + movement );
        if( movement == null )
        {
            return null;
        }
        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = con.prepareStatement( "SELECT * FROM reference.smart WHERE area=? AND fromBerth=? AND toBerth=?" ) )
            {
                ps.setLong( 1, movement.getAreaId() );
                ps.setLong( 2, movement.getFromBerthId() );
                ps.setLong( 3, movement.getToBerthId() );
                return SQL.stream( ps, Smart.fromSQL ).
                        findFirst().
                        orElse( null );
            }
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }
}
