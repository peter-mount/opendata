/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A representation of a Train as stored in the cache.
 * 
 * User code should access a Train reference from the cache or from {@link TrainFactory}.
 * 
 * @author peter
 */
public interface Train
        extends Serializable
{

    TimetableEntry findTime( Predicate<Integer> tplFilter );

    Stream<ForecastEntry> forecastEntries();

    List<Association> getAssociations();

    String getDest();

    ForecastEntry getDestinationForecast();

    Forecast getForecast();

    List<ForecastEntry> getForecastEntries();

    long getForecastId();

    ForecastEntry getLastReport();

    LocalDateTime getLastUpdate();

    String getOrigin();

    ForecastEntry getOriginForecast();

    String getRid();

    Schedule getSchedule();

    List<ScheduleEntry> getScheduleEntries();

    long getScheduleId();

    /**
     * If not null, where the train starts from.
     * <p>
     * This is usually caused by the train being cancelled at it's origin.
     * <p>
     * @return
     */
    ForecastEntry getStartsFrom();

    boolean isActivated();

    boolean isArchived();

    boolean isAssociationsPresent();

    boolean isDeactivated();

    boolean isDestinationForecastPresent();

    boolean isForecastPresent();

    boolean isOriginForecastPresent();

    boolean isSchedulePresent();

    boolean isStartsFromSet();

    boolean isValid();

    Train setArchived( boolean archived );

    void setAssociations( List<Association> associations );

    void setDestinationForecast( ForecastEntry destinationForecast );

    void setForecast( Forecast forecast );

    void setForecastEntries( List<ForecastEntry> forecastEntries );

    void setOriginForecast( ForecastEntry originForecast );

    void setSchedule( Schedule schedule );

    void setScheduleEntries( List<ScheduleEntry> scheduleEntries );

    void setStartsFrom( ForecastEntry startsFrom );

}
