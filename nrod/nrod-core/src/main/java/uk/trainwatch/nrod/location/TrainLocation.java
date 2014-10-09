/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

/**
 *
 * @author peter
 */
public class TrainLocation
        implements Comparable<TrainLocation>
{

    private long id;
    private String location;
    private String crs;
    private String nlc;
    private String tiploc;
    private long stanox;
    private boolean data;
    private boolean station;
    private boolean delay_monitor_point;
    private String notes;
    int easting;
    int northing;

    public TrainLocation()
    {
    }

    TrainLocation( long id, String location, String crs, String nlc, String tiploc, long stanox,
                   boolean station,
                   boolean delay_monitor_point,
                   boolean data,
                   String notes,
                   int easting,
                   int norting )
    {
        this.id = id;
        this.location = location;
        this.crs = crs;
        this.nlc = nlc;
        this.tiploc = tiploc;
        this.stanox = stanox;
        this.data = data;
        this.station = station;
        this.delay_monitor_point = delay_monitor_point;
        this.notes = notes;
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
     */
    public boolean isStation()
    {
        return station;
    }

    /**
     * Does this location have data associated with it
     * <p/>
     * @return
     */
    public boolean isData()
    {
        return data;
    }

    /**
     * Is this location also a delayed monitoring point
     * <p/>
     * @return
     */
    public boolean isDelay_monitor_point()
    {
        return delay_monitor_point;
    }

    public long getId()
    {
        return id;
    }

    public String getLocation()
    {
        return location;
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

    public int getEasting()
    {
        return easting;
    }

    public int getNorthing()
    {
        return northing;
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
}
