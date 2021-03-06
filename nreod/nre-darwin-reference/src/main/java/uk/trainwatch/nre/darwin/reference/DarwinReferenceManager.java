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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.CISSource;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Reason;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.TocRef;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.Via;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;

/**
 * Front end to the Darwin reference data
 * <p>
 * @author peter
 */
@ApplicationScoped
public class DarwinReferenceManager
{

    private static final Logger LOG = Logger.getLogger( DarwinReferenceManager.class.getName() );

    @Inject
    @Database( "rail" )
    private DataSource dataSource;

    /**
     * Cache LocationRef as used often
     */
    private List<TrainLocation> locs;
    private final Map<String, TrainLocation> tiplocCache = new ConcurrentHashMap<>();
    private final Map<String, List<TrainLocation>> crsCache = new ConcurrentHashMap<>();
    private final Map<String, TocRef> tocCache = new ConcurrentHashMap<>();
    private final Map<String, CISSource> cisCache = new ConcurrentHashMap<>();
    private final Map<Integer, Reason> cancCache = new ConcurrentHashMap<>();
    private final Map<Integer, Reason> lateCache = new ConcurrentHashMap<>();
    /**
     * When the cache was last used
     */
    private volatile LocalDateTime locationCacheUpdated = LocalDateTime.MIN;

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
        if( refreshRequired() )
        {
            synchronized( this )
            {
                if( refreshRequired() )
                {
                    try( Connection con = dataSource.getConnection();
                            Statement s = con.createStatement() )
                    {

                        locs = SQL.stream( s.executeQuery(
                                "SELECT l.name, c.crs, t.tpl"
                                + " FROM darwin.location l"
                                + " INNER JOIN darwin.crs c ON l.crs=c.id"
                                + " INNER JOIN darwin.tiploc t ON l.tpl=t.id" ),
                                           rs -> new TrainLocation(
                                                   rs.getString( "name" ),
                                                   rs.getString( "crs" ),
                                                   rs.getString( "tpl" )
                                           ) ).
                                collect( Collectors.toList() );

                        updateMap( tiplocCache, locs.stream(), TrainLocation::getTiploc );

                        // CRS can map to multiple locations
                        updateMap( crsCache, locs.stream().
                                   filter( l -> l.isSetCrs() ).
                                   collect( Collectors.groupingBy( TrainLocation::getCrs ) ) );

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
                    } catch( SQLException ex )
                    {
                        LOG.log( Level.SEVERE, null, ex );
                    }
                }
            }
        }
    }

    /**
     * Search the location table for entries that match the given search term
     *
     * @param term Term to search fo. Minimum 3 charactersr
     * <p>
     * @return stream of {@link LocationRef} entries
     */
    public Stream<TrainLocation> searchLocations( String term )
    {
        if( term == null )
        {
            return Stream.empty();
        }

        String searchTerm = term.trim().toUpperCase();
        if( searchTerm.length() < 3 )
        {
            return Stream.empty();
        }

        refresh();

        // Search by location name
        Stream<TrainLocation> search = tiplocCache.values().
                stream().
                filter( l -> l.getLocation().toUpperCase().contains( searchTerm ) ).
                filter( TrainLocation::isSetCrs ).
                filter( TrainLocation::isSetLocation ).
                filter( l -> !l.getTiploc().equals( l.getLocation() ) ).
                sorted( ( a, b ) -> a.getLocation().compareToIgnoreCase( b.getLocation() ) ).
                distinct();

        // Check for crs match, only if term is 3 characters
        TrainLocation crsLocation = null;
        if( searchTerm.length() == 3 )
        {
            crsLocation = getLocationRefFromCrs( searchTerm );
        }

        // Now sort & make distinct by name
        // Merge crs result to front
        if( crsLocation != null )
        {
            search = Stream.concat( Stream.of( crsLocation ),
                                    // filter it out of main search if present
                                    search.filter( l -> !searchTerm.equals( l.getCrs() ) )
            );
        }

        return search;
    }

    public TrainLocation getLocationRefFromTiploc( String tpl )
    {
        refresh();
        return tiplocCache.get( tpl );
    }

    public TrainLocation getLocationRefFromCrs( String crs )
    {
        if( crs == null || crs.isEmpty() )
        {
            return null;
        }
        final List<TrainLocation> l = getLocationRefsFromCrs( crs );
        return l == null || l.isEmpty() ? null : l.get( 0 );
    }

    public List<TrainLocation> getLocationRefsFromCrs( String crs )
    {
        refresh();
        return crsCache.get( crs );
    }

    /**
     * The via text.
     * <p>
     * This will try to match via text from a specific crs location and a given destination tiploc to get the via text.
     * <p>
     * For example, getVia("VIC","CNTBW",list) with list containing "MSTONEE" and "ASHFKY" then we should get the via saying
     * "via Maidstone East & Ashford International".
     * <p>
     * @param at   The crs this text is defined for
     * @param dest The destination tiploc
     * @param locs List of locs in the schedule in the schedule order
     * <p>
     * @return Via or null
     */
    public Via getVia( String at, String dest, List<String> locs )
    {
        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM darwin.via WHERE at=? AND dest=?", at, dest ) )
            {
                return SQL.stream( ps, DarwinReferenceManager::readVia ).
                        filter( v -> locs.contains( v.getLoc1() ) ).
                        filter( v -> v.getLoc2() == null || locs.contains( v.getLoc2() ) ).
                        sorted( ( a, b ) ->
                                {
                                    int r = Integer.compare( locs.indexOf( a.getLoc1() ), locs.indexOf( b.getLoc1() ) );
                                    if( r == 0 )
                                    {
                                        String a2 = a.getLoc2(), b2 = a.getLoc2();
                                        if( a2 == null )
                                        {
                                            r = 1;
                                        }
                                        else if( b2 == null )
                                        {
                                            r = -1;
                                        }
                                        else
                                        {
                                            r = Integer.compare( locs.indexOf( a2 ), locs.indexOf( b2 ) );
                                        }
                                    }
                                    return r;
                        } ).
                        findAny().
                        orElse( null );
            }
        } catch( SQLException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    }

    public String getViaText( int via )
    {
        if( via == 0 )
        {
            return null;
        }
        try( Connection con = dataSource.getConnection();
                PreparedStatement ps = SQL.prepare( con, "SELECT text FROM darwin.via WHERE id=?", via ) )
        {
            return SQL.stream( ps, SQL.STRING_LOOKUP ).
                    findAny().
                    orElse( null );
        } catch( SQLException ex )
        {
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
     * @param code <p>
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
     * @param code <p>
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
     * @param code <p>
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

    public boolean isCrs( final String crs, final String tiploc )
    {
        TrainLocation l = getLocationRefFromTiploc( tiploc );
        return l == null ? false : crs.equalsIgnoreCase( l.getCrs() );
    }
}
