/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.util.Collections;
import java.util.List;

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

    public Train( String rid )
    {
        this.rid = rid;
    }

    public String getRid()
    {
        return rid;
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

    public void setForecastEntries( List<ForecastEntry> forecastEntries )
    {
        this.forecastEntries = forecastEntries;
    }

}
