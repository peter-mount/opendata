/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.util.Objects;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
public class SignalArea
{

    public static final SQLResultSetHandler<SignalArea> fromSQL = rs -> new SignalArea(
            rs.getString( "area" ),
            rs.getString( "comment" ),
            rs.getBoolean( "enabled" )
    );
    
    private final String area;
    private final String comment;
    private final boolean enabled;

    public SignalArea( String area, String comment, boolean enabled )
    {
        this.area = area;
        this.comment = comment;
        this.enabled = enabled;
    }

    public String getArea()
    {
        return area;
    }

    public String getComment()
    {
        return comment;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode( this.area );
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
        final SignalArea other = (SignalArea) obj;
        if( !Objects.equals( this.area, other.area ) )
        {
            return false;
        }
        return true;
    }

}
