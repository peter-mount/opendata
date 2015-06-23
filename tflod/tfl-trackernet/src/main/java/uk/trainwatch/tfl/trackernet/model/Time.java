/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.model;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author peter
 */
public class Time
{

    private String timeStamp;

    @XmlAttribute( name = "TimeStamp" )
    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp( String timeStamp )
    {
        this.timeStamp = timeStamp;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode( this.timeStamp );
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
        final Time other = (Time) obj;
        return Objects.equals( this.timeStamp, other.timeStamp );
    }

    @Override
    public String toString()
    {
        return "Time{" + "timeStamp=" + timeStamp + '}';
    }

}
