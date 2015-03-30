/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 * A cancellation message is sent when the train does not, or will not, complete its scheduled journey.
 * <p>
 * A train may be cancelled in one of four ways:
 * <ul>
 * <li>
 * At activation time ("ON CALL"), usually where the applicable schedule has a STP indicator of "C" - see Planned
 * Cancellations. Trains may be cancelled for other reasons before train has been activated, and when activation occurs,
 * the train will be immediately cancelled with the appropriate reason code
 * </li>
 * <li>At the train's planned origin ("AT ORIGIN")</li>
 * <li>En-route ("EN ROUTE")</li>
 * <li>Off-route ("OUT OF PLAN")</li>
 * </ul>
 * <p>
 * As with all other messages, cancellation messages will only be received for train schedules which have already been
 * activated.
 * <p>
 * @author Peter T Mount
 */
public class TrainCancellation
        extends TrustMovement
{

    private String original_data_source;
    private boolean plannedCancellation;

    private long orig_loc_stanox;
    private long orig_loc_timestamp;
    private long dep_timestamp;
    private int division_code;
    private long loc_stanox;
    private long canx_timestamp;
    private String canx_reason_code;
    private String canx_type;
    private String train_service_code;
    private String train_file_address;

    public TrainCancellation()
    {
        super( TrustMovementType.CANCELLATION );
    }

    public String getOriginal_data_source()
    {
        return original_data_source;
    }

    @Override
    public long getTimestamp(){
        return getCanx_timestamp();
    }
    public void setOriginal_data_source( String original_data_source )
    {
        this.original_data_source = original_data_source;
        plannedCancellation = original_data_source == null || original_data_source.isEmpty();
    }

    /**
     * Virtual, based on original_data_source value, is this a planned cancellation
     * <p>
     * @return
     */
    public boolean isPlannedCancellation()
    {
        return plannedCancellation;
    }

    /**
     * Virtual, based on original_data_source, is this a reactionary cancellation
     * <p>
     * @return
     */
    public boolean isReactionaryCancellation()
    {
        return !isPlannedCancellation();
    }

    public long getOrig_loc_stanox()
    {
        return orig_loc_stanox;
    }

    public void setOrig_loc_stanox( long orig_loc_stanox )
    {
        this.orig_loc_stanox = orig_loc_stanox;
    }

    public long getOrig_loc_timestamp()
    {
        return orig_loc_timestamp;
    }

    public void setOrig_loc_timestamp( long orig_loc_timestamp )
    {
        this.orig_loc_timestamp = orig_loc_timestamp;
    }

    public long getDep_timestamp()
    {
        return dep_timestamp;
    }

    public void setDep_timestamp( long dep_timestamp )
    {
        this.dep_timestamp = dep_timestamp;
    }

    public int getDivision_code()
    {
        return division_code;
    }

    public void setDivision_code( int division_code )
    {
        this.division_code = division_code;
    }

    public long getLoc_stanox()
    {
        return loc_stanox;
    }

    public void setLoc_stanox( long loc_stanox )
    {
        this.loc_stanox = loc_stanox;
    }

    public long getCanx_timestamp()
    {
        return canx_timestamp;
    }

    public void setCanx_timestamp( long canx_timestamp )
    {
        this.canx_timestamp = canx_timestamp;
    }

    public String getCanx_reason_code()
    {
        return canx_reason_code;
    }

    public void setCanx_reason_code( String canx_reason_code )
    {
        this.canx_reason_code = canx_reason_code;
    }

    public String getCanx_type()
    {
        return canx_type;
    }

    public void setCanx_type( String canx_type )
    {
        this.canx_type = canx_type;
    }

    public String getTrain_service_code()
    {
        return train_service_code;
    }

    public void setTrain_service_code( String train_service_code )
    {
        this.train_service_code = train_service_code;
    }

    public String getTrain_file_address()
    {
        return train_file_address;
    }

    public void setTrain_file_address( String train_file_address )
    {
        this.train_file_address = train_file_address;
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }

}
