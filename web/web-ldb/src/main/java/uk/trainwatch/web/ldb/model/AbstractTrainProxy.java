/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.inject.Inject;
import uk.trainwatch.util.CDIUtils;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.web.ldb.cache.TrainCache;

/**
 *
 * @author peter
 */
public abstract class AbstractTrainProxy
        implements Train
{

    private static final long serialVersionUID = 1L;

    @Inject
    private transient volatile TrainCache trainCache;
    private final String rid;

    public AbstractTrainProxy( String rid )
    {
        this.rid = rid;
    }

    @Override
    public final int hashCode()
    {
        return rid.hashCode();
    }

    @Override
    public final boolean equals( Object obj )
    {
        return obj instanceof Train && rid.equals( ((Train) obj).getRid() );
    }

    protected Train getCachedTrain()
    {
        if( trainCache == null ) {
            synchronized( this ) {
                if( trainCache == null ) {
                    CDIUtils.inject( this );
                }
            }
        }
        try {
            return trainCache.get( rid );
        }
        catch( SQLException ex ) {
            throw new UncheckedSQLException( ex );
        }
    }

    protected abstract Train getTrain();

    @Override
    public final String getRid()
    {
        return rid;
    }

    @Override
    public final TimetableEntry findTime( Predicate<Integer> tplFilter )
    {
        return getTrain().findTime( tplFilter );
    }

    @Override
    public final Stream<ForecastEntry> forecastEntries()
    {
        return getTrain().forecastEntries();
    }

    @Override
    public final List<Association> getAssociations()
    {
        return getTrain().getAssociations();
    }

    @Override
    public final String getDest()
    {
        return getTrain().getDest();
    }

    @Override
    public final ForecastEntry getDestinationForecast()
    {
        return getTrain().getDestinationForecast();
    }

    @Override
    public final Forecast getForecast()
    {
        return getTrain().getForecast();
    }

    @Override
    public final List<ForecastEntry> getForecastEntries()
    {
        return getTrain().getForecastEntries();
    }

    @Override
    public final long getForecastId()
    {
        return getTrain().getForecastId();
    }

    @Override
    public final ForecastEntry getLastReport()
    {
        return getTrain().getLastReport();
    }

    @Override
    public final LocalDateTime getLastUpdate()
    {
        return getTrain().getLastUpdate();
    }

    @Override
    public final String getOrigin()
    {
        return getTrain().getOrigin();
    }

    @Override
    public final ForecastEntry getOriginForecast()
    {
        return getTrain().getOriginForecast();
    }

    @Override
    public final Schedule getSchedule()
    {
        return getTrain().getSchedule();
    }

    @Override
    public final List<ScheduleEntry> getScheduleEntries()
    {
        return getTrain().getScheduleEntries();
    }

    @Override
    public final long getScheduleId()
    {
        return getTrain().getScheduleId();
    }

    @Override
    public final ForecastEntry getStartsFrom()
    {
        return getTrain().getStartsFrom();
    }

    @Override
    public final boolean isActivated()
    {
        return getTrain().isActivated();
    }

    @Override
    public final boolean isArchived()
    {
        return getTrain().isArchived();
    }

    @Override
    public final boolean isAssociationsPresent()
    {
        return getTrain().isAssociationsPresent();
    }

    @Override
    public final boolean isDeactivated()
    {
        return getTrain().isDeactivated();
    }

    @Override
    public final boolean isDestinationForecastPresent()
    {
        return getTrain().isDestinationForecastPresent();
    }

    @Override
    public final boolean isForecastPresent()
    {
        return getTrain().isForecastPresent();
    }

    @Override
    public final boolean isOriginForecastPresent()
    {
        return getTrain().isOriginForecastPresent();
    }

    @Override
    public final boolean isSchedulePresent()
    {
        return getTrain().isSchedulePresent();
    }

    @Override
    public final boolean isStartsFromSet()
    {
        return getTrain().isStartsFromSet();
    }

    @Override
    public final boolean isValid()
    {
        return getTrain().isValid();
    }

    @Override
    public final Train setArchived( boolean archived )
    {
        return getTrain().setArchived( archived );
    }

    @Override
    public final void setAssociations( List<Association> associations )
    {
        getTrain().setAssociations( associations );
    }

    @Override
    public final void setDestinationForecast( ForecastEntry destinationForecast )
    {
        getTrain().setDestinationForecast( destinationForecast );
    }

    @Override
    public final void setForecast( Forecast forecast )
    {
        getTrain().setForecast( forecast );
    }

    @Override
    public final void setForecastEntries( List<ForecastEntry> forecastEntries )
    {
        getTrain().setForecastEntries( forecastEntries );
    }

    @Override
    public final void setOriginForecast( ForecastEntry originForecast )
    {
        getTrain().setOriginForecast( originForecast );
    }

    @Override
    public final void setSchedule( Schedule schedule )
    {
        getTrain().setSchedule( schedule );
    }

    @Override
    public final void setScheduleEntries( List<ScheduleEntry> scheduleEntries )
    {
        getTrain().setScheduleEntries( scheduleEntries );
    }

    @Override
    public final void setStartsFrom( ForecastEntry startsFrom )
    {
        getTrain().setStartsFrom( startsFrom );
    }

}
