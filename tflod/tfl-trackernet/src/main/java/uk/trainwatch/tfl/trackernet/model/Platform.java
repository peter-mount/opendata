/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author peter
 */
public class Platform
{

    private String code;
    private String name;
    private final List<Train> trains = new ArrayList<>();

    @XmlAttribute( name = "Code" )
    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    @XmlAttribute( name = "N" )

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @XmlElement( name = "T" )

    public List<Train> getTrains()
    {
        return trains;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode( this.code );
        hash = 47 * hash + Objects.hashCode( this.name );
        hash = 47 * hash + Objects.hashCode( this.trains );
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
        final Platform other = (Platform) obj;
        return Objects.equals( this.code, other.code )
                && Objects.equals( this.name, other.name )
                && Objects.equals( this.trains, other.trains );
    }

    @Override
    public String toString()
    {
        return "Platform{" + "code=" + code + ", name=" + name + ", trains=" + trains + '}';
    }

}
