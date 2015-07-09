/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.cache;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author peter
 */
public class SearchDepartureKey
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String crs;
    private LocalDateTime time;

    public SearchDepartureKey()
    {
    }

    public SearchDepartureKey( String crs, LocalDateTime time )
    {
        this.crs = crs;
        this.time = time;
    }

    public String getCrs()
    {
        return crs;
    }

    public void setCrs( String crs )
    {
        this.crs = crs;
    }

    public LocalDateTime getTime()
    {
        return time;
    }

    public void setTime( LocalDateTime time )
    {
        this.time = time;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode( this.crs );
        hash = 37 * hash + Objects.hashCode( this.time );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final SearchDepartureKey other = (SearchDepartureKey) obj;
        return Objects.equals( this.crs, other.crs )
               && Objects.equals( this.time, other.time );
    }

}
