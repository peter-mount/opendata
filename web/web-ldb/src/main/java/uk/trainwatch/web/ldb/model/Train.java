/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
public class Train
{

    private final String rid;
    private Schedule schedule;
    private List<ScheduleEntry> scheduleEntries = Collections.emptyList();
    private Forecast forecast;
    private List<ForecastEntry> forecastEntries = Collections.emptyList();
    private boolean archived;

    private ForecastEntry lastReport;

    public Train( String rid )
    {
        this.rid = rid;
    }

    public boolean isArchived()
    {
        return archived;
    }

    public Train setArchived( boolean archived )
    {
        this.archived = archived;
        return this;
    }

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

    public ForecastEntry getLastReport()
    {
        return lastReport;
    }

    public String getRid()
    {
        return rid;
    }

    public boolean isValid()
    {
        return isSchedulePresent() || isForecastPresent();
    }

    public boolean isSchedulePresent()
    {
        return schedule != null;
    }

    public long getScheduleId()
    {
        return isSchedulePresent() ? schedule.getId() : 0;
    }

    public Schedule getSchedule()
    {
        return schedule;
    }

    public void setSchedule( Schedule schedule )
    {
        this.schedule = schedule;
    }

    public List<ScheduleEntry> getScheduleEntries()
    {
        return scheduleEntries;
    }

    public void setScheduleEntries( List<ScheduleEntry> scheduleEntries )
    {
        this.scheduleEntries = scheduleEntries;
    }

    public boolean isForecastPresent()
    {
        return forecast != null;
    }

    public long getForecastId()
    {
        return isForecastPresent() ? forecast.getId() : 0;
    }

    public boolean isActivated()
    {
        return !archived && (isForecastPresent() && forecast.isActivated());
    }

    public boolean isDeactivated()
    {
        return archived || (isForecastPresent() && forecast.isDeactivated());
    }

    public Forecast getForecast()
    {
        return forecast;
    }

    public void setForecast( Forecast forecast )
    {
        this.forecast = forecast;
    }

    public List<ForecastEntry> getForecastEntries()
    {
        return forecastEntries;
    }

    public Stream<ForecastEntry> forecastEntries()
    {
        return Streams.stream( forecastEntries );
    }

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
