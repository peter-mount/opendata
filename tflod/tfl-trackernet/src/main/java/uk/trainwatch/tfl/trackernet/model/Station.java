/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author peter
 */
public class Station
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String code;

    private String name;

    private final List<Platform> platforms = new ArrayList<>();

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

    @XmlElement( name = "P" )
    public List<Platform> getPlatforms()
    {
        return platforms;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode( this.code );
        hash = 29 * hash + Objects.hashCode( this.name );
        hash = 29 * hash + Objects.hashCode( this.platforms );
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
        final Station other = (Station) obj;
        return Objects.equals( this.code, other.code )
                && Objects.equals( this.name, other.name )
                && Objects.equals( this.platforms, other.platforms );
    }

    @Override
    public String toString()
    {
        return "Station{" + "code=" + code + ", name=" + name + ", platforms=" + platforms + '}';
    }

}
