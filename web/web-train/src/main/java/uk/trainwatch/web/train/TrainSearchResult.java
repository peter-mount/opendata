/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author peter
 */
public class TrainSearchResult
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final TrainLocation station;
    private final LocalDateTime searchDate;
    private final Collection<Train> trains;
    private final String error;

    public TrainSearchResult( TrainLocation station, LocalDateTime searchDate, Collection<Train> trains )
    {
        this.station = station;
        this.searchDate = searchDate;
        this.trains = trains;
        error = null;
    }

    public TrainSearchResult( String error )
    {
        this.station = null;
        this.searchDate = null;
        this.trains = null;
        this.error = error;
    }

    public boolean isError()
    {
        return error != null;
    }

    public String getError()
    {
        return error;
    }

    public TrainLocation getStation()
    {
        return station;
    }

    public LocalDateTime getSearchDate()
    {
        return searchDate;
    }

    public Collection<Train> getTrains()
    {
        return trains;
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
        final TrainSearchResult other = (TrainSearchResult) obj;
        if( !Objects.equals( this.station, other.station ) ) {
            return false;
        }
        if( !Objects.equals( this.searchDate, other.searchDate ) ) {
            return false;
        }
        return true;
    }

}
