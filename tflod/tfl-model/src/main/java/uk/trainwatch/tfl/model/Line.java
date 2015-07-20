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
public class Line
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;

    public Line()
    {
    }

    public Line( String code, String name )
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode( this.code );
        hash = 97 * hash + Objects.hashCode( this.name );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final Line other = (Line) obj;
        return Objects.equals( this.code, other.code )
               || Objects.equals( this.name, other.name );
    }

}
