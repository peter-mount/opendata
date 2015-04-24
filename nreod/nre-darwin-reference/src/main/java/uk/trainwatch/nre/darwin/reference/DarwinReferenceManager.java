/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.CISSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.TocRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.util.sql.SQL;

/**
 * Front end to the Darwin reference data
 * <p>
 * @author peter
 */
public enum DarwinReferenceManager
{

    INSTANCE;

    private final Logger LOG = Logger.getLogger( getClass().getName() );
    private DataSource dataSource;
    /**
     * Cache LocationRef as used often
     */
    private final Map<String, LocationRef> tiplocCache = new ConcurrentHashMap<>();
    private final Map<String, List<LocationRef>> crsCache = new ConcurrentHashMap<>();
    private final Map<String, TocRef> tocCache = new ConcurrentHashMap<>();
    private final Map<String, CISSource> cisCache = new ConcurrentHashMap<>();
    private final Map<Integer, Reason> cancCache = new ConcurrentHashMap<>();
    private final Map<Integer, Reason> lateCache = new ConcurrentHashMap<>();
    /**
     * When the cache was last used
     */
    private volatile LocalDateTime locationCacheUpdated = LocalDateTime.MIN;

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    private boolean refreshRequired()
    {
        return LocalDateTime.now().minusHours( 1 ).isAfter( locationCacheUpdated );
    }

    private <K, V> void updateMap( Map<K, V> cache, Map<K, V> map )
    {
        cache.putAll( map );
        cache.keySet().removeIf( k -> !map.containsKey( k ) );
    }

    private <K, V> void updateMap( Map<K, V> cache, Stream<V> stream, Function<V, K> keyMapper )
    {
        Map<K, V> map = stream.collect( Collectors.toMap( keyMapper, Function.identity(), ( a, b ) -> a ) );
        updateMap( cache, map );
    }

    private void refresh()
    {
        if( refreshRequired() ) {
            synchronized( this ) {
                if( refreshRequired() ) {
                    try( Connection con = dataSource.getConnection();
                         Statement s = con.createStatement() ) {

                        List<LocationRef> locs = SQL.stream( s.executeQuery( "SELECT * FROM darwin.location" ), DarwinReferenceManager::readLocationRef ).
                                collect( Collectors.toList() );

                        updateMap( tiplocCache, locs.stream(), LocationRef::getTpl );

                        // CRS can map to multiple locations
                        updateMap( crsCache, locs.stream().
                                   filter( l -> l.isSetCrs() ).
                                   collect( Collectors.groupingBy( LocationRef::getCrs ) ) );

                        updateMap( cisCache,
                                   SQL.stream( s.executeQuery( "SELECT * FROM darwin.cissource" ), DarwinReferenceManager::readCISSource ),
                                   CISSource::getCode );

                        updateMap( tocCache, SQL.stream( s.executeQuery( "SELECT * FROM darwin.toc" ), DarwinReferenceManager::readTocRef ), TocRef::getToc );

                        updateMap( cancCache,
                                   SQL.stream( s.executeQuery( "SELECT * FROM darwin.cancreason" ), DarwinReferenceManager::readReason ),
                                   Reason::getCode );

                        updateMap( lateCache,
                                   SQL.stream( s.executeQuery( "SELECT * FROM darwin.latereason" ), DarwinReferenceManager::readReason ),
                                   Reason::getCode );

                        locationCacheUpdated = LocalDateTime.now();
                    }
                    catch( SQLException ex ) {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
            }
        }
    }

    public LocationRef getLocationRefFromTiploc( String tpl )
    {
        refresh();
        return tiplocCache.get( tpl );
    }

    public Collection<LocationRef> getLocationRefFromCrs( String crs )
    {
        if( refreshRequired() ) {
            refresh();
        }
        return crsCache.get( crs );
    }

    /**
     * The via text.
     * <p>
     * This will try to match via text from a specific crs location and a given destination tiploc to get the via text.
     * <p>
     * For example, getVia("VIC","CNTBW",list) with list containing "MSTONEE" and "ASHFKY" then we should get the via saying "via Maidstone East & Ashford
     * International".
     * <p>
     * @param at   The crs this text is defined for
     * @param dest The destination tiploc
     * @param locs List of locs in the schedule in the schedule order
     * <p>
     * @return Via or null
     */
    public Via getVia( String at, String dest, List<String> locs )
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM darwin.via WHERE at=? AND dest=?", at, dest ) ) {
                return SQL.stream( ps, DarwinReferenceManager::readVia ).
                        filter( v -> locs.contains( v.getLoc1() ) ).
                        filter( v -> v.getLoc2() == null || locs.contains( v.getLoc2() ) ).
                        sorted( ( a, b ) -> {
                            int r = Integer.compare( locs.indexOf( a.getLoc1() ), locs.indexOf( b.getLoc1() ) );
                            if( r == 0 ) {
                                String a2 = a.getLoc2(), b2 = a.getLoc2();
                                if( a2 == null ) {
                                    r = 1;
                                }
                                else if( b2 == null ) {
                                    r = -1;
                                }
                                else {
                                    r = Integer.compare( locs.indexOf( a2 ), locs.indexOf( b2 ) );
                                }
                            }
                            return r;
                        } ).
                        findAny().
                        orElse( null );
            }
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    }

    /**
     * The CIS source
     * <p>
     * @param code cis code
     * <p>
     * @return
     */
    public CISSource getCISSource( String code )
    {
        refresh();
        return cisCache.get( code );
    }

    /**
     * The toc ref from the atoc code
     * <p>
     * @param code
     *             <p>
     * @return
     */
    public TocRef getTocRef( String code )
    {
        refresh();
        return tocCache.get( code );
    }

    /**
     * The cancel reason
     * <p>
     * @param code
     *             <p>
     * @return
     */
    public Reason getCancelReason( int code )
    {
        refresh();
        return cancCache.get( code );
    }

    /**
     * The late reason
     * <p>
     * @param code
     *             <p>
     * @return
     */
    public Reason getLateReason( int code )
    {
        refresh();
        return lateCache.get( code );
    }

    private static CISSource readCISSource( ResultSet rs )
            throws SQLException
    {
        CISSource c = new CISSource();
        c.setCode( rs.getString( "code" ) );
        c.setName( rs.getString( "name" ) );
        return c;
    }

    private static LocationRef readLocationRef( ResultSet rs )
            throws SQLException
    {
        LocationRef c = new LocationRef();
        c.setTpl( rs.getString( "tpl" ) );
        c.setCrs( rs.getString( "crs" ) );
        c.setToc( rs.getString( "toc" ) );
        c.setLocname( rs.getString( "name" ) );
        return c;
    }

    private static TocRef readTocRef( ResultSet rs )
            throws SQLException
    {
        TocRef c = new TocRef();
        c.setToc( rs.getString( "code" ) );
        c.setTocname( rs.getString( "name" ) );
        c.setUrl( rs.getString( "url" ) );
        return c;
    }

    private static Reason readReason( ResultSet rs )
            throws SQLException
    {
        Reason c = new Reason();
        c.setCode( rs.getInt( "id" ) );
        c.setReasontext( rs.getString( "name" ) );
        return c;
    }

    private static Via readVia( ResultSet rs )
            throws SQLException
    {
        Via c = new Via();
        c.setAt( rs.getString( "at" ) );
        c.setDest( rs.getString( "dest" ) );
        c.setLoc1( rs.getString( "loc1" ) );
        c.setLoc2( rs.getString( "loc2" ) );
        c.setViatext( rs.getString( "text" ) );
        return c;
    }
}
