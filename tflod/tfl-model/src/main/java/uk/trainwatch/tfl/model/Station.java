/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author peter
 */
public class Station
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private String naptan;
    private String name;
    private List<Line> lines;
    private List<Platform> platforms;

    public Station()
    {
    }

    public Station( int id, String naptan, String name )
    {
        this.id = id;
        this.naptan = naptan;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getNaptan()
    {
        return naptan;
    }

    public String getName()
    {
        return name;
    }

    public boolean isLinesPresent()
    {
        return lines != null && !lines.isEmpty();
    }

    public List<Line> getLines()
    {
        if( lines == null ) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    public boolean isPlatformPresent()
    {
        return platforms != null && !platforms.isEmpty();
    }

    public List<Platform> getPlatforms()
    {
        if( platforms == null ) {
            platforms = new ArrayList<>();
        }
        return platforms;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 61 * hash + id;
        hash = 61 * hash + Objects.hashCode( this.naptan );
        hash = 61 * hash + Objects.hashCode( this.name );
        hash = 61 * hash + Objects.hashCode( this.lines );
        hash = 61 * hash + Objects.hashCode( this.platforms );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final Station other = (Station) obj;
        return this.id == other.id
               || Objects.equals( this.naptan, other.naptan )
               || Objects.equals( this.name, other.name );
    }

}
