/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 *
 * @author Peter T Mount
 */
public abstract class TrustMovement
{

    private final TrustMovementType msg_type;
    private int toc_id;
    private String train_id;

    public TrustMovement( TrustMovementType msg_type )
    {
        this.msg_type = msg_type;
    }

    public TrustMovement( TrustMovementType msg_type, int toc_id, String train_id )
    {
        this.msg_type = msg_type;
        this.toc_id = toc_id;
        this.train_id = train_id;
    }

    public final TrustMovementType getMsg_type()
    {
        return msg_type;
    }

    public final int getToc_id()
    {
        return toc_id;
    }

    public final void setToc_id( int toc_id )
    {
        this.toc_id = toc_id;
    }

    public final String getTrain_id()
    {
        return train_id;
    }

    public final void setTrain_id( String train_id )
    {
        this.train_id = train_id;
    }

    public abstract void accept( TrustMovementVisitor v );
}
