/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter
 */
@XmlRootElement( name = "ROOT" )
public class Root
{

    private Time time;

    private final List<Station> stations = new ArrayList<>();

    @XmlElement( name = "S" )
    public List<Station> getStations()
    {
        return stations;
    }

    @XmlElement( name = "Time" )
    public Time getTime()
    {
        return time;
    }

    public void setTime( Time time )
    {
        this.time = time;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode( this.time );
        hash = 89 * hash + Objects.hashCode( this.stations );
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
        final Root other = (Root) obj;
        return Objects.equals( this.time, other.time )
                && Objects.equals( this.stations, other.stations );
    }

    @Override
    public String toString()
    {
        return "Root{" + "time=" + time + ", stations=" + stations + '}';
    }

}
