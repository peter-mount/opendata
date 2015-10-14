/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.Predicates;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.web.ldb.model.Association;
import uk.trainwatch.web.ldb.model.Forecast;
import uk.trainwatch.web.ldb.model.ForecastEntry;
import uk.trainwatch.web.ldb.model.Schedule;
import uk.trainwatch.web.ldb.model.ScheduleEntry;
import uk.trainwatch.web.ldb.model.TimetableEntry;
import uk.trainwatch.web.ldb.model.Train;
import uk.trainwatch.web.ldb.model.TrainFactory;

/**
 * Cache holding Train details - used in searches but also allows reduction of size of queries
 * <p>
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "LDBTrainCache")
public class TrainCache
{

    private static final Logger LOG = Logger.getLogger( TrainCache.class.getName() );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private CrsTiplocCache crsTiplocCache;

    @Inject
    private TiplocCache tiplocCache;

    public void init()
    {
        LOG.log( Level.INFO, "Initialising TrainCache" );
    }

    @CacheRemove
    public void clear( @CacheKey String rid )
    {
        LOG.log( Level.FINE, () -> "Removing " + rid );
    }

    @CacheResult
    public Train get( @CacheKey String rid )
            throws SQLException
    {
        LOG.log( Level.INFO, () -> "Looking up Train " + rid );

        try( Connection con = dataSource.getConnection() ) {
            Train train = TrainFactory.newTrainInstance( rid );
            Schedule.populate.accept( con, train );
            ScheduleEntry.populate.accept( con, train );
            Forecast.populate.accept( con, train );
            ForecastEntry.populate.accept( con, train );
            Association.populate.accept( con, train );

//            if( train.getForecastEntries() != null ) {
//                Collections.sort( train.getForecastEntries() );
//            }
            if( train.isForecastPresent() && train.isSchedulePresent() ) {
                // Now link forecast & schedule entries - otherwise we'll have no idea of cancelled stops
                train.getForecastEntries().forEach( f -> {
                    train.getScheduleEntries().
                            stream().
                            filter( s -> s.getTpl().equals( f.getTpl() ) ).
                            filter( s -> Objects.equals( s.getPta(), f.getPta() ) ).
                            filter( s -> Objects.equals( s.getPtd(), f.getPtd() ) ).
                            findAny().
                            ifPresent( f::setScheduleEntry );
                } );

                // Map the origin/destination forecasts
                train.forecastEntries().
                        filter( getFilter( train.getSchedule().getOrigin() ) ).
                        findAny().ifPresent( train::setOriginForecast );

                train.forecastEntries().
                        filter( getFilter( train.getSchedule().getDest() ) ).
                        findAny().ifPresent( train::setDestinationForecast );

                // Cancelled at origin? Then find where the train starts from
                if( train.isOriginForecastPresent()
                    && train.getOriginForecast().isScheduleEntryPresent()
                    && train.getOriginForecast().getScheduleEntry().isCan() ) {
                    train.forecastEntries().
                            filter( fe -> !fe.isScheduleEntryPresent() || (fe.getWtp() != null && !fe.getScheduleEntry().isCan()) ).
                            findFirst().
                            ifPresent( train::setStartsFrom );
                }
            }

            return train;
        }
    }

    // Form a filter from all tiplocs for the crs
    private Predicate<TimetableEntry> getFilter( String tiploc )
    {
        try {
            return tiplocCache.get( tiploc ).
                    stream().
                    //peek( tpl -> LOG.log( Level.INFO, () -> "tiploc " + tiploc + " id " + tpl + ":" ) ).
                    map( tpl -> (Predicate<TimetableEntry>) tte -> tpl.equals( tte.getTplid() ) ).
                    reduce( null, Predicates::or, Predicates::or );
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return tte -> false;
        }
    }
}
