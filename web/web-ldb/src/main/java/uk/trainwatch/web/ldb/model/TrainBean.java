/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.TimeUtils;

/**
 * A concrete representation of a Train as stored in the cache
 * <p>
 * @author peter
 */
class TrainBean
        implements Train
{

    private static final long serialVersionUID = 1L;

    private final String rid;

    private Schedule schedule;
    private List<ScheduleEntry> scheduleEntries = Collections.emptyList();
    private List<Association> associations = Collections.emptyList();
    private Forecast forecast;
    private List<ForecastEntry> forecastEntries = Collections.emptyList();
    private boolean archived;

    private ForecastEntry lastReport, startsFrom, originForecast, destinationForecast;

    TrainBean( String rid )
    {
        this.rid = rid;
    }

    @Override
    public boolean isArchived()
    {
        return archived;
    }

    @Override
    public Train setArchived( boolean archived )
    {
        this.archived = archived;
        return this;
    }

    @Override
    public LocalDateTime getLastUpdate()
    {
        Timestamp t = null;
        if( forecast != null ) {
            t = forecast.getTs();
        }
        else if( schedule != null ) {
            t = schedule.getTs();
        }
        if( t == null ) {
            return LocalDateTime.now();
        }

        LocalDateTime dt = t.toLocalDateTime();

        // Fix as DB time is wrong
        dt.plus( TimeUtils.LONDON.getRules().getDaylightSavings( t.toInstant() ) );

        return dt;
    }

    @Override
    public ForecastEntry getLastReport()
    {
        return lastReport;
    }

    @Override
    public String getRid()
    {
        return rid;
    }

    @Override
    public boolean isValid()
    {
        return isSchedulePresent() || isForecastPresent();
    }

    @Override
    public String getOrigin()
    {
        if( isSchedulePresent() ) {
            return schedule.getOrigin();
        }

        if( forecastEntries != null && !forecastEntries.isEmpty() ) {
            return forecastEntries.get( 0 ).getTpl();
        }

        return "";
    }

    @Override
    public String getDest()
    {
        if( isSchedulePresent() ) {
            return schedule.getDest();
        }

        if( forecastEntries != null && !forecastEntries.isEmpty() ) {
            return forecastEntries.get( forecastEntries.size() - 1 ).getTpl();
        }

        return "";
    }

    @Override
    public boolean isSchedulePresent()
    {
        return schedule != null;
    }

    @Override
    public long getScheduleId()
    {
        return isSchedulePresent() ? schedule.getId() : 0;
    }

    @Override
    public Schedule getSchedule()
    {
        return schedule;
    }

    @Override
    public void setSchedule( Schedule schedule )
    {
        this.schedule = schedule;
    }

    @Override
    public List<ScheduleEntry> getScheduleEntries()
    {
        return scheduleEntries;
    }

    @Override
    public void setScheduleEntries( List<ScheduleEntry> scheduleEntries )
    {
        this.scheduleEntries = scheduleEntries;
    }

    @Override
    public boolean isForecastPresent()
    {
        return forecast != null;
    }

    @Override
    public long getForecastId()
    {
        return isForecastPresent() ? forecast.getId() : 0;
    }

    @Override
    public boolean isActivated()
    {
        return !archived && (isForecastPresent() && forecast.isActivated());
    }

    @Override
    public boolean isDeactivated()
    {
        return archived || (isForecastPresent() && forecast.isDeactivated());
    }

    @Override
    public Forecast getForecast()
    {
        return forecast;
    }

    @Override
    public void setForecast( Forecast forecast )
    {
        this.forecast = forecast;
    }

    @Override
    public List<ForecastEntry> getForecastEntries()
    {
        return forecastEntries;
    }

    @Override
    public Stream<ForecastEntry> forecastEntries()
    {
        return Streams.stream( forecastEntries );
    }

    @Override
    public void setForecastEntries( List<ForecastEntry> forecastEntries )
    {
        this.forecastEntries = forecastEntries;
        if( forecastEntries != null && !forecastEntries.isEmpty() ) {
            // Find last arr/dep/pass
            lastReport = forecastEntries.stream().
                    filter( e -> e.getArr() != null || e.getDep() != null || e.getPass() != null ).
                    sorted( ForecastEntry.SORT_REVERSE ).
                    findAny().
                    orElse( null );
        }
        else {
            lastReport = null;
        }

    }

    @Override
    public boolean isAssociationsPresent()
    {
        return associations != null && !associations.isEmpty();
    }

    @Override
    public List<Association> getAssociations()
    {
        return associations;
    }

    @Override
    public void setAssociations( List<Association> associations )
    {
        this.associations = associations;
    }

    /**
     * If not null, where the train starts from.
     * <p>
     * This is usually caused by the train being cancelled at it's origin.
     * <p>
     * @return
     */
    @Override
    public ForecastEntry getStartsFrom()
    {
        return startsFrom;
    }

    @Override
    public boolean isStartsFromSet()
    {
        return startsFrom != null;
    }

    @Override
    public void setStartsFrom( ForecastEntry startsFrom )
    {
        this.startsFrom = startsFrom;
    }

    @Override
    public boolean isOriginForecastPresent()
    {
        return originForecast != null;
    }

    @Override
    public ForecastEntry getOriginForecast()
    {
        return originForecast;
    }

    @Override
    public void setOriginForecast( ForecastEntry originForecast )
    {
        this.originForecast = originForecast;
    }

    @Override
    public boolean isDestinationForecastPresent()
    {
        return destinationForecast != null;
    }

    @Override
    public ForecastEntry getDestinationForecast()
    {
        return destinationForecast;
    }

    @Override
    public void setDestinationForecast( ForecastEntry destinationForecast )
    {
        this.destinationForecast = destinationForecast;
    }

    @Override
    public TimetableEntry findTime( Predicate<Integer> tplFilter )
    {
        TimetableEntry e = null;

        if( isForecastPresent() ) {
            e = TimetableEntry.findTime( forecastEntries, tplFilter );
        }
        if( e == null && isSchedulePresent() ) {
            e = TimetableEntry.findTime( scheduleEntries, tplFilter );
        }

        return e;
    }
}
