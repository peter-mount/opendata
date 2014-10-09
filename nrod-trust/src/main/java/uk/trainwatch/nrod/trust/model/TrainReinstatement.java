/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 * When trains are cancelled, but then reinstated, a reinstatement message message is sent. This contains details of the
 * cancellation - the location to which it applies, the type of cancellation, and the reason code.
 * <p>
 * As with all other messages, cancellation messages will only be received for train schedules which have already been
 * activated.
 * <p>
 * @author Peter T Mount
 */
public class TrainReinstatement
        extends TrustAdjustment
{

    private int division_code_id;
    private long reinstatement_timestamp;

    public TrainReinstatement()
    {
        super( TrustMovementType.REINSTATEMENT );
    }

    public long getReinstatement_timestamp()
    {
        return reinstatement_timestamp;
    }

    public void setReinstatement_timestamp( long reinstatement_timestamp )
    {
        this.reinstatement_timestamp = reinstatement_timestamp;
    }

    public final int getDivision_code_id()
    {
        return division_code_id;
    }

    public final void setDivision_code_id( int division_code_id )
    {
        this.division_code_id = division_code_id;
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }
}
