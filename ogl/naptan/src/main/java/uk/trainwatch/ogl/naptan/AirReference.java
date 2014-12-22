/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan;

import java.time.LocalDateTime;
import java.util.Objects;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
public class AirReference
{

    public static final SQLResultSetHandler<AirReference> fromSQL = rs -> new AirReference(
            rs.getLong( 1 ),
            rs.getString( "atcoCode" ),
            rs.getString( "iataCode" ),
            rs.getString( "nameCode" ),
            rs.getString( "nameLang" ),
            SQL.getLocalDateTime( rs, "creationDT" ),
            SQL.getLocalDateTime( rs, "modificationDT" ),
            rs.getInt( "revisionNumber" ),
            rs.getString( "modification" )
    );
    
    private final long id;
    private final String atcoCode;
    private final String iataCode;
    private final String name;
    private final String nameLang;
    private final LocalDateTime created;
    private final LocalDateTime modified;
    private final int revision;
    private final String modification;

    public AirReference( long id, String atcoCode, String iataCode, String name, String nameLang, LocalDateTime created, LocalDateTime modified, int revision,
                         String modification )
    {
        this.id = id;
        this.atcoCode = atcoCode;
        this.iataCode = iataCode;
        this.name = name;
        this.nameLang = nameLang;
        this.created = created;
        this.modified = modified;
        this.revision = revision;
        this.modification = modification;
    }

    public long getId()
    {
        return id;
    }

    public String getAtcoCode()
    {
        return atcoCode;
    }

    public String getIataCode()
    {
        return iataCode;
    }

    public String getName()
    {
        return name;
    }

    public String getNameLang()
    {
        return nameLang;
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
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 17 * hash + Objects.hashCode( this.atcoCode );
        hash = 17 * hash + Objects.hashCode( this.iataCode );
        hash = 17 * hash + Objects.hashCode( this.name );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final AirReference other = (AirReference) obj;
        return this.id == other.id
                || Objects.equals( this.atcoCode, other.atcoCode )
                || Objects.equals( this.iataCode, other.iataCode )
                || Objects.equals( this.name, other.name );
    }

}
