/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.ejb.Schedules;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.timetable.model.Schedule;

/**
 *
 * @author peter
 */
public class TimeTableSearchResult
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final TrainLocation station;
    private final LocalDate searchDate;
    private final List<Schedule> schedules;
    private final String error;

    public TimeTableSearchResult( TrainLocation station, LocalDate searchDate, List<Schedule> schedules )
    {
        this.station = station;
        this.searchDate = searchDate;
        this.schedules = schedules;
        error = null;
    }

    public TimeTableSearchResult( String error )
    {
        this.station = null;
        this.searchDate = null;
        this.schedules = null;
        this.error = error;
    }

    public List<Schedule> getSchedules()
    {
        return schedules;
    }

    public String getError()
    {
        return error;
    }

    public boolean isError()
    {
        return error != null;
    }

    public LocalDate getSearchDate()
    {
        return searchDate;
    }

    public TrainLocation getStation()
    {
        return station;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode( this.station );
        hash = 83 * hash + Objects.hashCode( this.searchDate );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final TimeTableSearchResult other = (TimeTableSearchResult) obj;
        if( !Objects.equals( this.station, other.station ) ) {
            return false;
        }
        if( !Objects.equals( this.searchDate, other.searchDate ) ) {
            return false;
        }
        return true;
    }

}
