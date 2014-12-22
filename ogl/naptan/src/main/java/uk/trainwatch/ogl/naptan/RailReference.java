/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
public class RailReference
{

    public static final SQLResultSetHandler<RailReference> fromSQL = rs -> new RailReference(
            rs.getLong( "id" ),
            rs.getString( "atcocode" ),
            rs.getString( "tiploccode" ),
            rs.getString( "crscode" ),
            rs.getString( "stationname" ),
            rs.getString( "stationnamelang" ),
            rs.getString( "gridtype" ),
            rs.getInt( "easting" ),
            rs.getInt( "northing" ),
            SQL.getLocalDateTime( rs, "creationdatetime" ),
            SQL.getLocalDateTime( rs, "modificationdatetime" ),
            rs.getInt( "revisionnumber" ),
            rs.getString( "modification" )
    );

    private final int hashCode;
    private final long id;
    private final String atcoCode;
    private final String tiplocCode;
    private final String crsCode;
    private final String stationName;
    private final String stationLang;
    private final String gridType;
    private final int easting;
    private final int northing;
    private final LocalDateTime creationDateTime;
    private final LocalDateTime modificationDateTime;
    private final int revisionNumber;
    private final String modification;

    public RailReference( long id, String atcoCode, String tiplocCode, String crsCode, String stationName, String stationLang, String gridType, int easting,
                          int northing,
                          LocalDateTime creationDateTime,
                          LocalDateTime modificationDateTime,
                          int revisionNumber, String modification )
    {
        this.id = id;
        this.atcoCode = atcoCode;
        this.tiplocCode = tiplocCode;
        this.crsCode = crsCode;
        this.stationName = stationName;
        this.stationLang = stationLang;
        this.gridType = gridType;
        this.easting = easting;
        this.northing = northing;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
        this.revisionNumber = revisionNumber;
        this.modification = modification;

        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode( this.atcoCode );
        hash = 89 * hash + Objects.hashCode( this.tiplocCode );
        hash = 89 * hash + Objects.hashCode( this.crsCode );
        hash = 89 * hash + Objects.hashCode( this.stationName );
        hashCode = hash;
    }

    public long getId()
    {
        return id;
    }

    public String getAtcoCode()
    {
        return atcoCode;
    }

    public String getTiplocCode()
    {
        return tiplocCode;
    }

    public String getCrsCode()
    {
        return crsCode;
    }

    public String getStationName()
    {
        return stationName;
    }

    public String getStationLang()
    {
        return stationLang;
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

    public LocalDateTime getCreationDateTime()
    {
        return creationDateTime;
    }

    public LocalDateTime getModificationDateTime()
    {
        return modificationDateTime;
    }

    public int getRevisionNumber()
    {
        return revisionNumber;
    }

    public String getModification()
    {
        return modification;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final RailReference other = (RailReference) obj;
        return this.id == other.id
                || Objects.equals( this.atcoCode, other.atcoCode )
                || Objects.equals( this.tiplocCode, other.tiplocCode )
                || Objects.equals( this.crsCode, other.crsCode )
                || Objects.equals( this.stationName, other.stationName );
    }

}
