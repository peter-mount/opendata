/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.web.ldb.model.Forecast;
import uk.trainwatch.web.ldb.model.ForecastEntry;
import uk.trainwatch.web.ldb.model.Schedule;
import uk.trainwatch.web.ldb.model.ScheduleEntry;
import uk.trainwatch.web.ldb.model.Train;

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

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

    @CacheResult
    public Train get( @CacheKey String rid )
            throws SQLException
    {
        LOG.log( Level.INFO, () -> "Looking up Train " + rid );

        try( Connection con = dataSource.getConnection() ) {
            Train train = new Train( rid );

            // Archive flag may be set if we already know we are searching in the past
            if( !train.isSchedulePresent() && !train.isArchived() ) {
                Schedule.populate.accept( con, train );
            }
            if( !train.isSchedulePresent() ) {
                Schedule.populateArc.accept( con, train );
            }

            // Only get entries if we have a schedule
            if( train.isSchedulePresent() ) {
                if( train.isArchived() ) {
                    ScheduleEntry.populateArc.accept( con, train );
                }
                else {
                    ScheduleEntry.populate.accept( con, train );
                }
            }

            // No need to search live table if archived
            if( !train.isArchived() ) {
                Forecast.populate.accept( con, train );
            }
            if( !train.isForecastPresent() ) {
                Forecast.populateArc.accept( con, train );
            }

            // Only get entries if we have a forecast
            if( train.isForecastPresent() ) {
                if( train.isArchived() ) {
                    ForecastEntry.populateArc.accept( con, train );
                }
                else {
                    ForecastEntry.populate.accept( con, train );
                }
            }

            // Now link forecast & schedule entries - otherwise we'll have no idea of cancelled stops
            if( train.isForecastPresent() && train.isSchedulePresent() ) {
                train.getForecastEntries().forEach( f -> {
                    train.getScheduleEntries().
                            stream().
                            filter( s -> s.getTpl().equals( f.getTpl() ) ).
                            filter( s -> Objects.equals( s.getPta(), f.getPta() ) ).
                            filter( s -> Objects.equals( s.getPtd(), f.getPtd() ) ).
                            findAny().
                            ifPresent( f::setScheduleEntry );
                } );
            }

            return train;
        }
    }

}
