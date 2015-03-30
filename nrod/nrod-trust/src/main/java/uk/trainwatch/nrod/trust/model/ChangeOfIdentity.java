/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 * <strong>NOTE</strong> - Change of Identity messages are only sent for freight trains.
 * <p>
 * One or more Change of Identity messages may be sent for a freight train, after activation, where the class of the
 * train is to change. This will happen if the train will run without wagons (i.e. a Class 6 service runs as a Class 0),
 * or if the train is carrying a wagon with a defect and must run at a slower speed.
 * <p>
 * @author Peter T Mount
 */
public class ChangeOfIdentity
        extends TrustMovement
{

    private String current_train_id;
    private String revised_train_id;
    private String train_file_address;
    private String train_service_code;
    private long event_timestamp;

    public ChangeOfIdentity()
    {
        super( TrustMovementType.CHANGE_OF_IDENTITY );
    }

    @Override
    public long getTimestamp()
    {
        return getEvent_timestamp();
    }

    
    public String getCurrent_train_id()
    {
        return current_train_id;
    }

    public void setCurrent_train_id( String current_train_id )
    {
        this.current_train_id = current_train_id;
    }

    public String getRevised_train_id()
    {
        return revised_train_id;
    }

    public void setRevised_train_id( String revised_train_id )
    {
        this.revised_train_id = revised_train_id;
    }

    public String getTrain_file_address()
    {
        return train_file_address;
    }

    public void setTrain_file_address( String train_file_address )
    {
        this.train_file_address = train_file_address;
    }

    public String getTrain_service_code()
    {
        return train_service_code;
    }

    public void setTrain_service_code( String train_service_code )
    {
        this.train_service_code = train_service_code;
    }

    public long getEvent_timestamp()
    {
        return event_timestamp;
    }

    public void setEvent_timestamp( long event_timestamp )
    {
        this.event_timestamp = event_timestamp;
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }

}
