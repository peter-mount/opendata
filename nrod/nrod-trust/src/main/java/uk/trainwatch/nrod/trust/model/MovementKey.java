/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.io.Serializable;
import uk.trainwatch.nrod.util.TrainId;

/**
 *
 * @author peter
 */
public final class MovementKey
        implements Serializable
{

    private final TrainId trainId;
    private final long stanox;

    public MovementKey( TrainMovement movement )
    {
        this( movement.getTrainId(), movement.getLoc_stanox() );
    }

    public MovementKey( TrainId trainId, long stanox )
    {
        this.trainId = trainId;
        this.stanox = stanox;
    }

    public TrainId getTrainId()
    {
        return trainId;
    }

    public long getStanox()
    {
        return stanox;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 79 * hash + (this.trainId != null ? this.trainId.hashCode() : 0);
        hash = 79 * hash + (int) (this.stanox ^ (this.stanox >>> 32));
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
        final MovementKey other = (MovementKey) obj;
        if( this.trainId != other.trainId && (this.trainId == null || !this.trainId.equals( other.trainId )) )
        {
            return false;
        }
        if( this.stanox != other.stanox )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "MovementKey[" + "trainId=" + trainId + ", stanox=" + stanox + ']';
    }
}
