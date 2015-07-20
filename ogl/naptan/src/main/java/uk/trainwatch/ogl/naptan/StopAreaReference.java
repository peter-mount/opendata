/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author peter
 */
public class StopAreaReference
{

    private final long id;
    private final String code;
    private final String name;
    private final String nameLang;
    private final String adminAreaCode;
    private final String stopAreaType;

    private final String gridType;
    private final int easting;
    private final int northing;
    private final LocalDateTime created;
    private final LocalDateTime modified;
    private final int revision;
    private final String modification;

    public StopAreaReference( long id, String code, String name, String nameLang, String adminAreaCode, String stopAreaType, String gridType, int easting,
                              int northing, LocalDateTime created, LocalDateTime modified, int revision, String modification )
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.nameLang = nameLang;
        this.adminAreaCode = adminAreaCode;
        this.stopAreaType = stopAreaType;
        this.gridType = gridType;
        this.easting = easting;
        this.northing = northing;
        this.created = created;
        this.modified = modified;
        this.revision = revision;
        this.modification = modification;
    }

    public long getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getNameLang()
    {
        return nameLang;
    }

    public String getAdminAreaCode()
    {
        return adminAreaCode;
    }

    public String getStopAreaType()
    {
        return stopAreaType;
    }

    public String getGridType()
    {
        return gridType;
    }

    public int getEasting()
    {
        return easting;
    }

    public int getNorthing()
    {
        return northing;
    }

    public LocalDateTime getCreated()
    {
        return created;
    }

    public LocalDateTime getModified()
    {
        return modified;
    }

    public int getRevision()
    {
        return revision;
    }

    public String getModification()
    {
        return modification;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode( this.code );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final StopAreaReference other = (StopAreaReference) obj;
        return this.id == other.id
               || Objects.equals( this.code, other.code );
    }

}
