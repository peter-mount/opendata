/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.PlatformData;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSTimeData;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinDispatcherBuilder;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.cache.Cache;

/**
 *
 * @author peter
 */
public enum ForecastManager
        implements Consumer<Pport>
{

    INSTANCE;

    private final Logger log = Logger.getLogger( ForecastManager.class.getName() );

    /**
     * Map of rid to current forecast
     */
    private final Cache<String, TS> forecasts;

    /**
     * Cache
     */
    private final Map<String, Collection<String>> locations = new ConcurrentHashMap<>();

    private ForecastManager()
    {
        // Cache of all forecasts. If they expire we need to clean up locations
        forecasts = new Cache<>( 100000, Duration.ofHours( 12 ) );
        forecasts.setEvicted( this::remove );
    }

    private Collection<String> getLocation( String tpl )
    {
        return locations.computeIfAbsent( tpl, k -> new CopyOnWriteArraySet<>() );
    }

    /**
     * Returns a copy of the rid's at a specific location
     * <p>
     * @param tpl
     *            <p>
     * @return
     */
    public Collection<String> getRids( String tpl )
    {
        Collection<String> col = locations.get( tpl );
        return col == null ? Collections.emptyList() : new ArrayList<>( col );
    }

    public Stream<TS> getForecasts( String tpl )
    {
        Collection<String> col = locations.get( tpl );
        if( col == null ) {
            return Stream.empty();
        }

        return col.stream().
                map( forecasts::get ).
                filter( Objects::nonNull );
    }

    private static <T> void forEach( Collection<T> col, Consumer<T> c )
    {
        if( !col.isEmpty() ) {
            col.forEach( c );
        }
    }

    /**
     * Accept a {@link Pport} for processing
     * <p>
     * @param p
     */
    @Override
    public void accept( Pport p )
    {
        // Insert all TS entries
        forEach( p.getUR().getTS(), t -> add( p, t ) );

        // Deactivation? then remove the rid
        forEach( p.getUR().getDeactivated(), s -> remove( s.getRid() ) );
    }

    private void add( Pport p, TS t )
    {
        String id = t.getRid();

        // The master forecast
        TS oldT = forecasts.put( id, t );

        // TODO make this more efficient, we could have a brief moment when a rid is not visible
        if( oldT == null ) {
            log.log( Level.FINE, () -> "Creating " + id );
        }
        else {
            log.log( Level.FINE, () -> "Updating " + id );
            // Remove any xrefs in tpl if we have an update
            oldT.getLocation().forEach( tl -> getLocation( tl.getTpl() ).remove( id ) );
        }

        // Add locations from t
        t.getLocation().forEach( tl -> getLocation( tl.getTpl() ).add( id ) );
        log.log( Level.FINE, () -> "Complete " + forecasts.size() );
    }

    /**
     * Return the TS for a rid
     * <p>
     * @param rid RTTI unique Train Identifier to retrieve
     * <p>
     * @return latest TS or null if not present
     */
    public TS get( String rid )
    {
        return forecasts.get( rid );
    }

    /**
     * Remove a rid from the cache
     * <p>
     * @param rid
     */
    public void remove( String rid )
    {
        forecasts.computeIfPresent( rid, this::remove );
    }

    private TS remove( String rid, TS ts )
    {
        log.log( Level.FINE, () -> "Removing " + rid );

        // Remove all xrefs
        ts.getLocation().forEach( tl -> getLocation( tl.getTpl() ).remove( rid ) );

        // Now remove from forecasts
        return null;
    }

    public static void main( String... args )
            throws Exception
    {
        Consumer<Pport> consumer = new DarwinDispatcherBuilder().
                addDeactivatedSchedule( INSTANCE::accept ).
                addTs( INSTANCE::accept ).
                build();

        final File dir = new File( "/home/peter/nre" );
        Stream.of( dir.listFiles( n -> n.getName().startsWith( "pPortData.log." ) ) ).
                sorted( ( a, b ) -> a.getName().compareTo( b.getName() ) ).
                limit( 20 ).
                flatMap( Functions::fileLines ).
                map( DarwinJaxbContext.fromXML ).
                filter( Objects::nonNull ).
                forEach( consumer );

        INSTANCE.log.log( Level.WARNING, () -> "Completed " + INSTANCE.forecasts.size() );

        INSTANCE.getLocation( "MSTONEE" ).forEach( rid -> {
            System.out.printf( "%10s ", rid );
            TS t = INSTANCE.get( rid );
            System.out.printf( "%s %s\n",
                               t.getSsd(),
                               t.getLateReason()
            );
            t.getLocation().forEach(
                    l -> System.out.printf( "  %8s %8s %8s %8s %3s %s %s %s\n",
                                            l.getTpl(),
                                            time( l.getArr() ),
                                            time( l.getDep() ),
                                            time( l.getPass() ),
                                            platform( l.getPlat() ),
                                            l.getPta(),
                                            l.getPtd(),
                                            l.getSuppr()
                    )
            );
        } );
        throw new RuntimeException();
    }

    private static String platform( PlatformData p )
    {
        StringBuilder s = new StringBuilder();

        if( p != null ) {
            s.append( p.getValue() );
            if( p.getCisPlatsup() ) {
                s.append( " CIS" );
            }
            if( p.getPlatsup() ) {
                s.append( " Plat" );
            }
            if( p.getConf() ) {
                s.append( " Conf" );
            }
        }

        return s.toString();
    }

    private static String time( TSTimeData t )
    {
        if( t == null ) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        if( t.getEtUnknown() == true ) {
            s.append( "Unknown" );
        }
        else {
            s.append( t.getEt() );
        }
        if( t.getDelayed() == true ) {
            s.append( " Delayed" );
        }
        if( t.getAtRemoved() == true ) {
            s.append( " AtRemvd" );
        }
        return s.toString();
    }
}
