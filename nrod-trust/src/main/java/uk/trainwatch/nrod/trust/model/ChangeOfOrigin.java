/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 * When a train is due to start from a location other than the first location in the schedule, a change of origin
 * message will be sent.
 * <p>
 * Trains may start from alternate locations for two reasons:
 * <ul>
 * <li>
 * When the previous working is terminated short of its destination and the return working will start from that location
 * </li>
 * <li>
 * When the train starts from, for example, Doncaster North Yard rather than the schedule location of Doncaster South
 * Yard
 * </li>
 * </ul>
 * <p>
 * @author Peter T Mount
 */
public class ChangeOfOrigin
        extends TrustAdjustment
{

    private String reason_code;
    private long coo_timestamp;
    private int division_code;

    public ChangeOfOrigin()
    {
        super( TrustMovementType.CHANGE_OF_ORIGIN );
    }

    public String getReason_code()
    {
        return reason_code;
    }

    public void setReason_code( String reason_code )
    {
        this.reason_code = reason_code;
    }

    public long getCoo_timestamp()
    {
        return coo_timestamp;
    }

    public void setCoo_timestamp( long coo_timestamp )
    {
        this.coo_timestamp = coo_timestamp;
    }

    public int getDivision_code()
    {
        return division_code;
    }

    public void setDivision_code( int division_code )
    {
        this.division_code = division_code;
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }

}
