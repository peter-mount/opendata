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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class SmartManager
{

    private static final Logger LOG = Logger.getLogger( SmartManager.class.getName() );

    @Resource( name = "jdbc/rail" )
    private DataSource dataSource;

    private final Map<Long, SmartArea> areas = new ConcurrentHashMap<>();
    private final Map<String, Long> areaIndex = new ConcurrentHashMap<>();
    private final Map<Long, String> berths = new ConcurrentHashMap<>();
    private final Map<String, Long> berthIndex = new ConcurrentHashMap<>();
    private final Map<Long, String> lines = new ConcurrentHashMap<>();
    private final Cache<SmartBerthMovement, Smart> cache = new Cache<>();

    @PostConstruct
    void start()
    {
        try
        {
            reload();
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
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
            CollectionUtils.replace( areaIndex,
                                     areas.entrySet().
                                     stream().
                                     collect( Collectors.toMap( e -> e.getValue().
                                                     getArea(), Map.Entry::getKey ) ) );

            LOG.log( Level.INFO, "Loading Smart Train Berths" );
            CollectionUtils.replace( berths,
                                     SQL.stream( s.executeQuery( "SELECT id,berth FROM reference.smart_berth" ), KeyValue.LONG_STRING ).
                                     collect( KeyValue.toMap() )
            );
            CollectionUtils.replace( berthIndex, CollectionUtils.invertMap( berths ) );

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

    public SmartBerthMovement getSmartBerthMovement( String area, String from, String to )
    {
        Long areaId = areaIndex.get( area );
        Long fromBerthId = berthIndex.get( from );
        Long toBerthId = berthIndex.get( to );
        if( areaId == null || fromBerthId == null || toBerthId == null )
        {
            return null;
        }
        return new SmartBerthMovement( areaId, fromBerthId, toBerthId, area, from, to );
    }

    public Smart getSmart( SmartBerthMovement movement )
    {
        if( movement != null )
        {
            try
            {
                return cache.computeIfAbsent( movement, this::lookup );
            } catch( UncheckedSQLException ex )
            {
                LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
            }
        }

        return null;
    }

    public void perform( SmartBerthMovement movement, Consumer<Smart> c )
    {
        try
        {
            cache.compute( movement, this::lookup, c );
        } catch( UncheckedSQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
        }
    }

    public void perform( SmartBerthMovement movement, BiConsumer<SmartBerthMovement, Smart> c )
    {
        try
        {
            cache.compute( movement, this::lookup, c );
        } catch( UncheckedSQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "Failed to lookup " + movement );
        }
    }

    public Consumer<SmartBerthMovement> consume( Consumer<Smart> c )
    {
        return t -> perform( t, c );
    }

    public Consumer<SmartBerthMovement> consume( BiConsumer<SmartBerthMovement, Smart> c )
    {
        return t -> perform( t, c );
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
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT * FROM reference.smart WHERE area=? AND fromBerth=? AND toBerth=?",
                                                     movement.getAreaId(),
                                                     movement.getFromBerthId(),
                                                     movement.getToBerthId()
            ) )
            {
                return SQL.stream( ps, Smart.fromSQL( this ) ).
                        findFirst().
                        orElse( null );
            }
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    public Collection<String> getBerths( String area )
    {
        return getBerths( areaIndex.get( area ) );
    }

    public Collection<String> getBerths( Long areaId )
    {
        if( areaId == null )
        {
            return Collections.emptyList();
        }

        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT b.berth FROM reference.smart s INNER JOIN reference.smart_berth b ON s.fromBerth=b.id WHERE area=?"
                                                     + " UNION "
                                                     + "SELECT b.berth FROM reference.smart s INNER JOIN reference.smart_berth b ON s.toBerth=b.id WHERE area=?",
                                                     areaId,
                                                     areaId
            ) )
            {
                return SQL.stream( ps, SQL.STRING_LOOKUP ).
                        sorted().
                        distinct().
                        collect( Collectors.toList() );
            }
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    public Collection<Smart> getSmart( String area )
    {
        LOG.log( Level.INFO, () -> "Lookup " + area );
        if( area == null || !areaIndex.containsKey( area ) )
        {
            return null;
        }
        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM reference.smart WHERE area=?", area ) )
            {
                return SQL.stream( ps, Smart.fromSQL( this ) ).
                        collect( Collectors.toList() );
            }
        } catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }
    }

    public Collection<SmartArea> getAreas()
    {
        List<SmartArea> l = new ArrayList<>( areas.values() );
        Collections.sort( l, SmartArea.COMPARATOR );
        return l;
    }
}
