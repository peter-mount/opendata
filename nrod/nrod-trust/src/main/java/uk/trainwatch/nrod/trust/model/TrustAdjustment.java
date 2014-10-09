/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 * Common fields shared by several messages
 * <p>
 * @author Peter T Mount
 */
public abstract class TrustAdjustment
        extends TrustMovement
{

    private String current_train_id;
    private long original_loc_timestamp;
    private long dep_timestamp;
    private long loc_stanox;
    private long original_loc_stanox;
    private String train_file_address;
    private String train_service_code;

    public TrustAdjustment( TrustMovementType msg_type )
    {
        super( msg_type );
    }

    public final String getCurrent_train_id()
    {
        return current_train_id;
    }

    public final void setCurrent_train_id( String current_train_id )
    {
        this.current_train_id = current_train_id;
    }

    public final long getOriginal_loc_timestamp()
    {
        return original_loc_timestamp;
    }

    public final void setOriginal_loc_timestamp( long original_loc_timestamp )
    {
        this.original_loc_timestamp = original_loc_timestamp;
    }

    public final long getDep_timestamp()
    {
        return dep_timestamp;
    }

    public final void setDep_timestamp( long dep_timestamp )
    {
        this.dep_timestamp = dep_timestamp;
    }

    public final long getLoc_stanox()
    {
        return loc_stanox;
    }

    public final void setLoc_stanox( long loc_stanox )
    {
        this.loc_stanox = loc_stanox;
    }

    public final long getOriginal_loc_stanox()
    {
        return original_loc_stanox;
    }

    public final void setOriginal_loc_stanox( long original_loc_stanox )
    {
        this.original_loc_stanox = original_loc_stanox;
    }

    public final String getTrain_file_address()
    {
        return train_file_address;
    }

    public final void setTrain_file_address( String train_file_address )
    {
        this.train_file_address = train_file_address;
    }

    public final String getTrain_service_code()
    {
        return train_service_code;
    }

    public final void setTrain_service_code( String train_service_code )
    {
        this.train_service_code = train_service_code;
    }
}
