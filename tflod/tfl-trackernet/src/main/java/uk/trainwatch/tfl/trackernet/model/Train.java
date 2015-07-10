/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.model;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author peter
 */
public class Train
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    private int set;
    private int trip;
    private String destination;
    private String nameOfDestination;
    private String timeToStation;
    private String currentLocation;

    @XmlAttribute( name = "S" )
    public int getSet()
    {
        return set;
    }

    public void setSet( int set )
    {
        this.set = set;
    }

    @XmlAttribute( name = "T" )
    public int getTrip()
    {
        return trip;
    }

    public void setTrip( int trip )
    {
        this.trip = trip;
    }

    @XmlAttribute( name = "D" )
    public String getDestination()
    {
        return destination;
    }

    public void setDestination( String destination )
    {
        this.destination = destination;
    }

    @XmlAttribute( name = "DE" )

    public String getNameOfDestination()
    {
        return nameOfDestination;
    }

    public void setNameOfDestination( String nameOfDestination )
    {
        this.nameOfDestination = nameOfDestination;
    }

    @XmlAttribute( name = "C" )

    public String getTimeToStation()
    {
        return timeToStation;
    }

    public void setTimeToStation( String timeToStation )
    {
        this.timeToStation = timeToStation;
    }

    @XmlAttribute( name = "L" )

    public String getCurrentLocation()
    {
        return currentLocation;
    }

    public void setCurrentLocation( String currentLocation )
    {
        this.currentLocation = currentLocation;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + this.set;
        hash = 23 * hash + this.trip;
        hash = 23 * hash + Objects.hashCode( this.destination );
        hash = 23 * hash + Objects.hashCode( this.nameOfDestination );
        hash = 23 * hash + Objects.hashCode( this.timeToStation );
        hash = 23 * hash + Objects.hashCode( this.currentLocation );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final Train other = (Train) obj;
        if( this.set != other.set )
        {
            return false;
        }
        if( this.trip != other.trip )
        {
            return false;
        }
        return Objects.equals( this.destination, other.destination )
                && Objects.equals( this.nameOfDestination, other.nameOfDestination )
                && Objects.equals( this.timeToStation, other.timeToStation )
                && Objects.equals( this.currentLocation, other.currentLocation );
    }

    @Override
    public String toString()
    {
        return "Train{" + "set=" + set + ", trip=" + trip + ", destination=" + destination + ", nameOfDestination=" + nameOfDestination + ", timeToStation=" + timeToStation + ", currentLocation=" + currentLocation + '}';
    }

}
