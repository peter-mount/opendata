/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.util;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 * A train's id, used as a key in db or cache calls
 * <p/>
 * @author peter
 */
public class TrainId
{

    public static final Function<JsonObject, TrainId> FACTORY
                                                      = o -> new TrainId( TrainDate.FACTORY.apply( o ),
                                                                          JsonUtils.getString( o, "trainId", "" ) );

    private TrainDate trainDate;
    private String trainId;

    public TrainId()
    {
    }

    public TrainId( TrainDate trainDate, String trainId )
    {
        this.trainDate = trainDate;
        this.trainId = trainId;
    }

    public TrainDate getTrainDate()
    {
        return trainDate;
    }

    public String getTrainId()
    {
        return trainId;
    }

    public String getHeadCode()
    {
        return getTrainId().
                substring( 2, 6 );
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 31 * hash + (this.trainDate != null ? this.trainDate.hashCode() : 0);
        hash = 31 * hash + (this.trainId != null ? this.trainId.hashCode() : 0);
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
        final TrainId other = (TrainId) obj;
        if( this.trainDate != other.trainDate && (this.trainDate == null || !this.trainDate.equals( other.trainDate )) )
        {
            return false;
        }
        if( (this.trainId == null) ? (other.trainId != null) : !this.trainId.equals( other.trainId ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "TrainId[trainDate=" + trainDate + ", trainId=" + trainId + ']';
    }
}
