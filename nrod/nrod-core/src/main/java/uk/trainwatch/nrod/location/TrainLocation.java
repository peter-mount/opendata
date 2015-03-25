/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.sql.ResultSet;
import java.util.Comparator;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
@XmlRootElement
public class TrainLocation
        implements Comparable<TrainLocation>
{

    public static final SQLFunction<ResultSet, TrainLocation> fromSQL = rs -> new TrainLocation(
            rs.getLong( 1 ),
            rs.getString( 6 ),
            rs.getString( 8 ),
            rs.getString( 4 ),
            rs.getString( 2 ),
            rs.getLong( 7 ),
            rs.getString( 9 )
    );

    public static final Comparator<TrainLocation> COMPARATOR = ( a, b ) -> a.getLocation().
            compareTo( b.getLocation() );

    private long id;
    private String location;
    private String crs;
    private String nlc;
    private String tiploc;
    private long stanox;
    private String notes;

    public TrainLocation()
    {
    }

    TrainLocation( long id, String location, String crs, String nlc, String tiploc, long stanox, String notes )
    {
        this.id = id;
        this.location = location == null ? null : location.trim();
        this.crs = crs == null ? null : crs.trim();
        this.nlc = nlc == null ? null : nlc.trim();
        this.tiploc = tiploc == null ? null : tiploc.trim();
        this.stanox = stanox;
        this.notes = notes == null ? null : notes.trim();
    }

    /**
     * Optional notes for this location
     * <p/>
     * @return
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * Is this a station or some other point on the rail network
     * <p/>
     * @return true for a station, false for an intermediate point
     * <p>
     */
    public boolean isStation()
    {
        return crs != null && !crs.startsWith( "X" );
    }

    /**
     * Does this location have data associated with it
     * <p/>
     * @return @deprecated
     */
    @Deprecated
    public boolean isData()
    {
        return false;
    }

    /**
     * Is this location also a delayed monitoring point
     * <p/>
     * @return @deprecated
     */
    @Deprecated
    public boolean isDelay_monitor_point()
    {
        return false;
    }

    public long getId()
    {
        return id;
    }

    public String getLocation()
    {
        return location;
    }

    public String getLocationIndex()
    {
        return location == null || location.isEmpty() ? "?" : location.substring( 0, 1 );
    }

    public String getCrs()
    {
        return crs;
    }

    public String getNlc()
    {
        return nlc;
    }

    public String getTiploc()
    {
        return tiploc;
    }

    public long getStanox()
    {
        return stanox;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TrainLocation other = (TrainLocation) obj;
        if( this.id != other.id )
        {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo( TrainLocation o )
    {
        return location.compareToIgnoreCase( o.getLocation() );
    }

    /**
     * Returns this instance as a {@link JsonObjectBuilder}
     *
     * @return
     */
    public JsonObjectBuilder toJson()
    {
        JsonObjectBuilder o = Json.createObjectBuilder().
                add( "location", (location == null || location.isEmpty()) ? "" : location ).
                add( "stanox", stanox );
        if( crs != null && !crs.isEmpty() )
        {
            o.add( "crs", crs );
        }
        if( tiploc != null && !tiploc.isEmpty() )
        {
            o.add( "tiploc", tiploc );
        }
        return o;
    }
}
