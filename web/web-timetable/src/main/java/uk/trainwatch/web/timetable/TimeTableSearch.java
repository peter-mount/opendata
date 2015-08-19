/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "timeTableSearch")
public class TimeTableSearch
{

    @Inject
    protected TrainLocationFactory trainLocationFactory;

    @CacheResult
    public TimeTableSearchResult search( @CacheKey String station, @CacheKey LocalDate date )
    {

        TrainLocation loc = trainLocationFactory.resolveTrainLocation( station );
        if( loc == null ) {
            return new TimeTableSearchResult( "The station you have requested is unknown" );
        }
        else {
            try {
                return new TimeTableSearchResult( loc, date, ScheduleSQL.getSchedules( loc, date ) );
            }
            catch( SQLException ex ) {
                Logger.getLogger( TimeTableSearch.class.getName() ).log( Level.SEVERE, ex, () -> "Search " + loc + " " + date );
                return new TimeTableSearchResult( "Something unexpected just went wrong, please try again later." );
            }
        }
    }

}
