/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author peter
 */
public class Platform
        implements Serializable
{

    private static final long serialVersionUID = 1L;
    private int id;
    private int plat;
    private String name;
    private String fullName;

    public Platform()
    {
    }

    public Platform( int id, int plat, String name, String fullName )
    {
        this.id = id;
        this.plat = plat;
        this.name = name;
        this.fullName = fullName;
    }

    public int getId()
    {
        return id;
    }

    /**
     * The platform number, when known. If not known then set to 0
     * <p>
     * @return
     */
    public int getPlat()
    {
        return plat;
    }

    /**
     * The platform name. This is usually the same as {@link #getPlat()} but can have a letter suffix.
     * <p>
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * The full platform name. This can be just the number or include the direction, e.g. "Westbound - Platform 2"
     * <p>
     * @return
     */
    public String getFullName()
    {
        return fullName;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + this.id;
        hash = 89 * hash + this.plat;
        hash = 89 * hash + Objects.hashCode( this.name );
        hash = 89 * hash + Objects.hashCode( this.fullName );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final Platform other = (Platform) obj;
        return this.id == other.id
               || this.plat == other.plat
               || Objects.equals( this.name, other.name )
               || Objects.equals( this.fullName, other.fullName );
    }

}
