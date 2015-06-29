/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObjectBuilder;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class TrainLocationFactory
{

    private static final Logger LOG = Logger.getLogger( TrainLocationFactory.class.getName() );

    private Map<LocationKey, TrainLocation> map = new ConcurrentHashMap<>();
    private List<String> stationIndex;
    private Map<String, List<TrainLocation>> stations;

    @Resource( name = "jdbc/rail" )
    private DataSource dataSource;

    @PostConstruct
    void start()
    {
        try
        {
            reload();
        } catch( SQLException ex )
        {
            LOG.log( Level.SEVERE, "Unable to load initial locations", ex );
            throw new UncheckedSQLException( ex );
        }
//        // By default use our old data
//        try
//        {
//            PsvReader.load( getClass().
//                    getResourceAsStream( "location.psv" ),
//                            s -> s.length < 12 ? null : new TrainLocation(
//                                            s[0].isEmpty() ? 0 : Long.parseLong( s[0] ),
//                                            s[1],
//                                            s[2],
//                                            s[3],
//                                            s[4],
//                                            s[5].isEmpty() ? 0 : Long.parseLong( s[5] ),
//                                            s[9]
//                                    )
//            ).
//                    forEach( loc ->
//                            {
//                                map.put( new TrainLocationID( loc.getId() ), loc );
//                                map.put( new CRS( loc.getCrs() ), loc );
//                                map.put( new NLC( loc.getNlc() ), loc );
//                                map.put( new Stanox( loc.getStanox() ), loc );
//                                map.put( new Tiploc( loc.getTiploc() ), loc );
//                    }
//                    );
//        } catch( IOException ex )
//        {
//            throw new UncheckedIOException( ex );
//        }
    }

    /**
     * Allows us to reload from the timetable
     * <p>
     * @param dataSource <p>
     * @throws SQLException
     */
    void reload()
            throws SQLException
    {
        LOG.log( Level.INFO, "Reloading TrainLocations" );
        try( Connection con = dataSource.getConnection() )
        {
            try( Statement s = con.createStatement() )
            {
                Map<LocationKey, TrainLocation> newMap = new ConcurrentHashMap<>();
                SQL.stream( s.executeQuery( "SELECT * FROM timetable.tiploc" ), TrainLocation.fromSQL ).
                        forEach( loc ->
                                {
                                    newMap.put( new TrainLocationID( loc.getId() ), loc );
                                    newMap.put( new CRS( loc.getCrs() ), loc );
                                    newMap.put( new NLC( loc.getNlc() ), loc );
                                    newMap.put( new Stanox( loc.getStanox() ), loc );
                                    newMap.put( new Tiploc( loc.getTiploc() ), loc );
                        } );
                map = newMap;

                // Create the station index
                stations = getStationStream().
                        collect( Collectors.groupingBy( TrainLocation::getLocationIndex ) );
                stationIndex = stations.keySet().
                        stream().
                        sorted().
                        collect( Collectors.toList() );
            }
        }
    }

    public TrainLocation get( LocationKey key )
    {
        return map.get( key );
    }

    /**
     * Resolve the train location by crs, tiploc, nlc then stanox in that order.
     * <p>
     * @param name <p>
     * @return
     */
    public TrainLocation resolveTrainLocation( String name )
    {
        if( name == null || name.isEmpty() )
        {
            return null;
        }

        TrainLocation loc = getTrainLocationByCrs( name );
        if( loc == null )
        {
            loc = getTrainLocationByTiploc( name );
        }
        if( loc == null )
        {
            loc = getTrainLocationByNlc( name );
        }
        if( loc == null )
        {
            try
            {
                loc = getTrainLocationByStanox( Long.parseLong( name ) );
            } catch( NumberFormatException |
                     NullPointerException ex )
            {
                loc = null;
            }
        }
        return loc;
    }

    /**
     * Returns a {@link TrainLocation} based on its internal id
     * <p/>
     * @param id < p/>
     * <p/>
     * @return TrainLocation or null if not found
     */
    public TrainLocation getTrainLocationById( long id )
    {
        return get( new TrainLocationID( id ) );
    }

    /**
     * Returns a {@link TrainLocation} based on its CRS
     * <p/>
     * @param crs key
     * <p/>
     * @return TrainLocation or null if not found
     */
    public TrainLocation getTrainLocationByCrs( String crs )
    {
        return get( new CRS( crs ) );
    }

    /**
     * Returns a {@link TrainLocation} based on its CRS
     * <p/>
     * @param nlc key
     * <p/>
     * @return TrainLocation or null if not found
     */
    public TrainLocation getTrainLocationByNlc( String nlc )
    {
        return get( new NLC( nlc ) );
    }

    /**
     * Returns a {@link TrainLocation} based on its CRS
     * <p/>
     * @param tiploc key
     * <p/>
     * @return TrainLocation or null if not found
     */
    public TrainLocation getTrainLocationByTiploc( String tiploc )
    {
        return get( new Tiploc( tiploc ) );
    }

    /**
     * Returns a {@link TrainLocation} based on its CRS
     * <p/>
     * @param stanox key
     * <p/>
     * @return TrainLocation or null if not found
     */
    public TrainLocation getTrainLocationByStanox( long stanox )
    {
        return get( new Stanox( stanox ) );
    }

    /**
     * Return all stations, e.g. those who have a CRS code
     * <p>
     * @return
     */
    public Collection<TrainLocation> getStations()
    {
        return getStationStream().
                collect( Collectors.toList() );
    }

    public List<String> getStationIndex()
    {
        return stationIndex;
    }

    public List<TrainLocation> getStationIndex( String code )
    {
        return stations.getOrDefault( code, Collections.emptyList() );
    }

    /**
     * Stream of all Stations
     * <p>
     * @return
     */
    public Stream<TrainLocation> getStationStream()
    {
        return map.entrySet().
                stream().
                filter( e -> e.getKey() instanceof CRS ).
                map( Map.Entry::getValue ).
                filter( TrainLocation::isStation ).
                sorted( TrainLocation.COMPARATOR );
    }

    public JsonObjectBuilder getJsonByStanox( long stanox )
    {
        TrainLocation l = getTrainLocationByStanox( stanox );
        return l == null ? null : l.toJson();
    }

    public JsonObjectBuilder getJsonByTiploc( String tiploc )
    {
        TrainLocation l = getTrainLocationByTiploc( tiploc );
        return l == null ? null : l.toJson();
    }

    public String getTiplocByStanox( long stanox )
    {
        TrainLocation l = getTrainLocationByStanox( stanox );
        return l == null ? Long.toString( stanox ) : l.getTiploc();
    }
}
