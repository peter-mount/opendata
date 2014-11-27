/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import java.sql.ResultSet;
import java.util.Comparator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
@XmlRootElement(name = "stationPerformance")
public class StationPerfStat
{

    public static SQLFunction<ResultSet, StationPerfStat> fromSQL_ALL = rs -> new StationPerfStat(
            rs.getLong( "dt_stanox" ),
            "All",
            0,
            rs.getInt( "traincount" ),
            rs.getInt( "delaycount" ),
            rs.getInt( "totaldelay" ),
            rs.getInt( "mindelay" ),
            rs.getInt( "maxdelay" ),
            rs.getInt( "earlycount" ),
            rs.getInt( "maxEarly" ),
            rs.getInt( "ontime" )
    );

    public static SQLFunction<ResultSet, StationPerfStat> fromSQL_TOC = rs -> new StationPerfStat(
            rs.getLong( "dt_stanox" ),
            rs.getString( "name" ),
            0,
            rs.getInt( "traincount" ),
            rs.getInt( "delaycount" ),
            rs.getInt( "totaldelay" ),
            rs.getInt( "mindelay" ),
            rs.getInt( "maxdelay" ),
            rs.getInt( "earlycount" ),
            rs.getInt( "maxEarly" ),
            rs.getInt( "ontime" )
    );

    public static SQLFunction<ResultSet, StationPerfStat> fromSQL_TOC_CLASS = rs -> new StationPerfStat(
            rs.getLong( "dt_stanox" ),
            rs.getString( "name" ),
            rs.getInt( "trainclass" ),
            rs.getInt( "traincount" ),
            rs.getInt( "delaycount" ),
            rs.getInt( "totaldelay" ),
            rs.getInt( "mindelay" ),
            rs.getInt( "maxdelay" ),
            rs.getInt( "earlycount" ),
            rs.getInt( "maxEarly" ),
            rs.getInt( "ontime" )
    );

    /**
     * Sort statistics by operator name then class, with operator "All" first
     */
    public static Comparator<StationPerfStat> COMPARATOR = ( a, b ) ->
    {
        // Sort with "All" first then by operator name
        String an = a.getOperator();
        String bn = b.getOperator();
        int r = "All".equals( an ) ? -1 : "All".equals( bn ) ? 1 : an.compareTo( bn );
        return r == 0 ? Integer.compare( a.getTrainClass(), b.getTrainClass() ) : r;
    };

    private long stanox;
    private String operator;
    private int trainClass;
    private int trainCount;
    private int delayCount;
    private int totalDelay;
    private int minDelay;
    private int maxDelay;
    private int earlyCount;
    private int maxEarly;
    private int ontime;

    public StationPerfStat( long stanox, String operator, int trainClass,
                            int trainCount,
                            int delayCount, int totalDelay, int minDelay, int maxDelay,
                            int earlyCount, int maxEarly,
                            int ontime )
    {
        this.stanox = stanox;
        this.operator = operator;
        this.trainClass = trainClass;
        this.trainCount = trainCount;
        this.delayCount = delayCount;
        this.totalDelay = totalDelay;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.earlyCount = earlyCount;
        this.maxEarly = maxEarly;
        this.ontime = ontime;
    }

    public long getStanox()
    {
        return stanox;
    }

    public void setStanox( long stanox )
    {
        this.stanox = stanox;
    }

    public int getTrainCount()
    {
        return trainCount;
    }

    public void setTrainCount( int trainCount )
    {
        this.trainCount = trainCount;
    }

    public int getTotalDelay()
    {
        return totalDelay;
    }

    public void setTotalDelay( int totalDelay )
    {
        this.totalDelay = totalDelay;
    }

    public int getMinDelay()
    {
        return minDelay;
    }

    public void setMinDelay( int minDelay )
    {
        this.minDelay = minDelay;
    }

    public int getMaxDelay()
    {
        return maxDelay;
    }

    public void setMaxDelay( int maxDelay )
    {
        this.maxDelay = maxDelay;
    }

    @XmlTransient
    public int getAveDelay()
    {
        return delayCount == 0 ? 0 : totalDelay / delayCount;
    }

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }

    public int getTrainClass()
    {
        return trainClass;
    }

    public void setTrainClass( int trainClass )
    {
        this.trainClass = trainClass;
    }

    public int getDelayCount()
    {
        return delayCount;
    }

    public void setDelayCount( int delayCount )
    {
        this.delayCount = delayCount;
    }

    public int getEarlyCount()
    {
        return earlyCount;
    }

    public void setEarlyCount( int earlyCount )
    {
        this.earlyCount = earlyCount;
    }

    public int getMaxEarly()
    {
        return maxEarly;
    }

    public void setMaxEarly( int maxEarly )
    {
        this.maxEarly = maxEarly;
    }

    public int getOntime()
    {
        return ontime;
    }

    public void setOntime( int ontime )
    {
        this.ontime = ontime;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 73 * hash + (int) (this.stanox ^ (this.stanox >>> 32));
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
        final StationPerfStat other = (StationPerfStat) obj;
        if( this.stanox != other.stanox )
        {
            return false;
        }
        return true;
    }

}
