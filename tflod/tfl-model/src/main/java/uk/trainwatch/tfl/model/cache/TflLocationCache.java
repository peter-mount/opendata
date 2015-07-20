/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.cache;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.tfl.model.Station;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "TflLocationCache")
public class TflLocationCache
{

    @Inject
    private StationCache stationCache;

    @CacheResult
    public TrainLocation get( @CacheKey String naptan )
    {
        try {
            Station station = stationCache.getLuCrs( naptan );
            if( station == null ) {
                return null;
            }
            
            // FIXME set crs correctly if present?
            TrainLocation loc = new TrainLocation(
                    station.getId(),
                    station.getName(),
                    // crs
                    naptan,
                    // nlc
                    naptan,
                    // tiploc
                    naptan,
                    0L,
                    "TfL Station"
            );
            loc.setTfl( true );
            return loc;
        }
        catch( SQLException ex ) {
            Logger.getLogger( getClass().getName() ).log( Level.SEVERE, ex, () -> "Failed to lookup " + naptan );
            return null;
        }
    }

}
