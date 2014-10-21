/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import uk.trainwatch.io.format.PsvReader;

/**
 *
 * @author peter
 */
public enum TrainLocationFactory
{

    INSTANCE;

    private static final Logger LOG = Logger.getLogger( TrainLocationFactory.class.getName() );

    private final Map<LocationKey, TrainLocation> map = new ConcurrentHashMap<>();

    private TrainLocationFactory()
    {
        try
        {
            PsvReader.load( getClass().
                    getResourceAsStream( "location.psv" ),
                            s -> s.length < 12 ? null : new TrainLocation(
                                            s[0].isEmpty() ? 0 : Long.parseLong( s[0] ),
                                            s[1],
                                            s[2],
                                            s[3],
                                            s[4],
                                            s[5].isEmpty() ? 0 : Long.parseLong( s[5] ),
                                            "t".equals( s[6] ),
                                            "t".equals( s[7] ),
                                            "t".equals( s[8] ),
                                            s[9],
                                            s[10].isEmpty() ? 0 : Integer.parseInt( s[10] ),
                                            s[11].isEmpty() ? 0 : Integer.parseInt( s[11] )
                                    )
            ).
                    forEach( loc ->
                            {
                                map.put( new TrainLocationID( loc.getId() ), loc );
                                map.put( new CRS( loc.getCrs() ), loc );
                                map.put( new NLC( loc.getNlc() ), loc );
                                map.put( new Stanox( loc.getStanox() ), loc );
                                map.put( new Tiploc( loc.getTiploc() ), loc );
                    }
                    );
        }
        catch( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    public TrainLocation get( LocationKey key )
    {
        return map.get( key );
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
        return get( new TrainLocationID( stanox ) );
    }

    /**
     * Return all stations, e.g. those who have a CRS code
     * <p>
     * @return
     */
    public Collection<TrainLocation> getStations()
    {
        return map.entrySet().
                stream().
                // Filter just CRS
                filter( e -> e.getKey() instanceof CRS ).
                // Filter out X?? codes as they are not stations
                filter( e -> !e.getKey().
                        getKey().
                        startsWith( "X" ) ).
                // Sort
                sorted( (a, b) -> a.getKey().
                        compareTo( b.getKey() ) ).
                map( Map.Entry::getValue ).
                collect( Collectors.toList() );
    }
}
