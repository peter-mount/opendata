/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.smart;

import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
@XmlRootElement(name = "smart")
public class Smart
{

    public static final SQLResultSetHandler<Smart> fromSQL = rs -> new Smart(
            rs.getLong( 1 ),
            SmartManager.INSTANCE.getArea( rs.getLong( 2 ) ),
            SmartManager.INSTANCE.getBerth( rs.getLong( 3 ) ),
            SmartManager.INSTANCE.getBerth( rs.getLong( 4 ) ),
            SmartManager.INSTANCE.getLine( rs.getLong( 5 ) ),
            SmartManager.INSTANCE.getLine( rs.getLong( 6 ) ),
            rs.getInt( 7 ),
            rs.getString( 8 ),
            SmartEvent.getId( rs.getInt( 9 ) ),
            rs.getString( 10 ),
            rs.getInt( 11 ),
            rs.getString( 12 ),
            StepType.getId( rs.getInt( 13 ) ),
            rs.getString( 14 )
    );

    private final long id;
    private final SmartArea area;
    private final String fromBerth;
    private final String toBerth;
    private final String fromLine;
    private final String toLine;
    private final int berthOffset;
    private final String platform;
    private final SmartEvent event;
    private final String route;
    private final int stanox;
    private final String stanme;
    private final StepType stepType;
    private final String comment;

    public Smart( long id, SmartArea area, String fromBerth, String toBerth, String fromLine, String toLine, int berthOffset, String platform, SmartEvent event,
                  String route, int stanox, String stanme, StepType stepType, String comment )
    {
        this.id = id;
        this.area = area;
        this.fromBerth = fromBerth;
        this.toBerth = toBerth;
        this.fromLine = fromLine;
        this.toLine = toLine;
        this.berthOffset = berthOffset;
        this.platform = platform;
        this.event = event;
        this.route = route;
        this.stanox = stanox;
        this.stanme = stanme;
        this.stepType = stepType;
        this.comment = comment;
    }

    public long getId()
    {
        return id;
    }

    public SmartArea getArea()
    {
        return area;
    }

    public String getFromBerth()
    {
        return fromBerth;
    }

    public String getToBerth()
    {
        return toBerth;
    }

    public String getFromLine()
    {
        return fromLine;
    }

    public String getToLine()
    {
        return toLine;
    }

    public int getBerthOffset()
    {
        return berthOffset;
    }

    public String getPlatform()
    {
        return platform;
    }

    public SmartEvent getEvent()
    {
        return event;
    }

    public String getRoute()
    {
        return route;
    }

    public int getStanox()
    {
        return stanox;
    }

    public String getStanme()
    {
        return stanme;
    }

    public StepType getStepType()
    {
        return stepType;
    }

    public String getComment()
    {
        return comment;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode( this.area );
        hash = 89 * hash + Objects.hashCode( this.fromBerth );
        hash = 89 * hash + Objects.hashCode( this.toBerth );
        hash = 89 * hash + Objects.hashCode( this.fromLine );
        hash = 89 * hash + Objects.hashCode( this.toLine );
        hash = 89 * hash + this.berthOffset;
        hash = 89 * hash + Objects.hashCode( this.platform );
        hash = 89 * hash + Objects.hashCode( this.event );
        hash = 89 * hash + Objects.hashCode( this.route );
        hash = 89 * hash + this.stanox;
        hash = 89 * hash + Objects.hashCode( this.stanme );
        hash = 89 * hash + Objects.hashCode( this.stepType );
        hash = 89 * hash + Objects.hashCode( this.comment );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final Smart other = (Smart) obj;
        return this.id == other.id
               && Objects.equals( this.area, other.area )
               && Objects.equals( this.fromBerth, other.fromBerth )
               && Objects.equals( this.toBerth, other.toBerth )
               && Objects.equals( this.fromLine, other.fromLine )
               && Objects.equals( this.toLine, other.toLine )
               && this.berthOffset == other.berthOffset
               && Objects.equals( this.platform, other.platform )
               && this.event == other.event
               && Objects.equals( this.route, other.route )
               && this.stanox == other.stanox
               && Objects.equals( this.stanme, other.stanme )
               && this.stepType == other.stepType
               && Objects.equals( this.comment, other.comment );
    }

    @Override
    public String toString()
    {
        return "Smart{" + "id=" + id + ", area=" + getArea() + ", fromBerth=" + getFromBerth() + ", toBerth=" + getToBerth() + ", fromLine=" + getFromLine() + ", toLine=" + getToLine() + ", berthOffset=" + berthOffset + ", platform=" + platform + ", event=" + event + ", route=" + route + ", stanox=" + stanox + ", stanme=" + stanme + ", stepType=" + stepType + ", comment=" + comment + '}';
    }

}
