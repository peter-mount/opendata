/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.Objects;

/**
 * A train movement message is sent whenever a train arrives, passes or departs a location monitored by TRUST. It
 * records the time at which the event happens.
 * <p>
 * Reports may be automatically generated, or manually entered.
 * <p/>
 * @author peter
 */
public class TrainMovement
        extends TrustMovement
        implements Comparable<TrainMovement>
{

    private String event_type;
    private long gbtt_timestamp;
    private long original_loc_stanox;
    private long planned_timestamp;
    private int timetable_variation;
    private long original_loc_timestamp;
    private String current_train_id;
    private boolean delay_monitoring_point;
    private long reporting_stanox;
    private long actual_timestamp;
    private boolean correction_ind;
    private String event_source;
    private String train_file_address;
    private String platform;
    private String division_code;
    private boolean train_terminated;
    private boolean offroute_ind;
    private String variation_status;
    private String train_service_code;
    private long loc_stanox;
    private boolean auto_expected;
    private String direction_ind;
    private String route;
    private String planned_event_type;
    private long next_report_stanox;
    private String line_ind;

    TrainMovement()
    {
        super( TrustMovementType.MOVEMENT );
    }

    TrainMovement( String event_type, long gbtt_timestamp, long original_loc_stanox,
                   long planned_timestamp, int timetable_variation, long original_loc_timestamp,
                   String current_train_id, boolean delay_monitoring_point, long reporting_stanox,
                   long actual_timestamp, boolean correction_ind, String event_source, String train_file_address,
                   String platform, String division_code, boolean train_terminated, String train_id,
                   boolean offroute_ind, String variation_status, String train_service_code, int toc_id,
                   long loc_stanox, boolean auto_expected, String direction_ind, String route,
                   String planned_event_type, long next_report_stanox, String line_ind )
    {
        super( TrustMovementType.MOVEMENT, toc_id, train_id );
        this.event_type = event_type;
        this.gbtt_timestamp = gbtt_timestamp;
        this.original_loc_stanox = original_loc_stanox;
        this.planned_timestamp = planned_timestamp;
        this.timetable_variation = timetable_variation;
        this.original_loc_timestamp = original_loc_timestamp;
        this.current_train_id = current_train_id;
        this.delay_monitoring_point = delay_monitoring_point;
        this.reporting_stanox = reporting_stanox;
        this.actual_timestamp = actual_timestamp;
        this.correction_ind = correction_ind;
        this.event_source = event_source;
        this.train_file_address = train_file_address;
        this.platform = platform;
        this.division_code = division_code;
        this.train_terminated = train_terminated;
        this.offroute_ind = offroute_ind;
        this.variation_status = variation_status;
        this.train_service_code = train_service_code;
        this.loc_stanox = loc_stanox;
        this.auto_expected = auto_expected;
        this.direction_ind = direction_ind;
        this.route = route;
        this.planned_event_type = planned_event_type;
        this.next_report_stanox = next_report_stanox;
        this.line_ind = line_ind;
    }

    //<editor-fold defaultstate="collapsed" desc="Synthetic fields">
    /**
     * The timestamp for this movement.
     * <p/>
     * Specifically this is the planned timestamp unless the movement is offroute when it's the actual one as it's not
     * planned
     * <p/>
     * @return
     */
    public long getTimestamp()
    {
        return isOffroute_ind() ? getActual_timestamp() : getPlanned_timestamp();
    }

    /**
     * The delay in seconds
     * <p>
     * @return
     */
    public String getHeadCode()
    {
        return getTrain_id().
                substring( 2, 6 );
    }

    /**
     * The delay in seconds
     * <p>
     * @return delay in seconds
     */
    public long getDelay()
    {
        return (actual_timestamp - planned_timestamp) / 1000L;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bean getters">
    public String getEvent_type()
    {
        return event_type;
    }

    public long getGbtt_timestamp()
    {
        return gbtt_timestamp;
    }

    public long getOriginal_loc_stanox()
    {
        return original_loc_stanox;
    }

    public long getPlanned_timestamp()
    {
        return planned_timestamp;
    }

    public int getTimetable_variation()
    {
        return timetable_variation;
    }

    public long getOriginal_loc_timestamp()
    {
        return original_loc_timestamp;
    }

    public String getCurrent_train_id()
    {
        return current_train_id;
    }

    public boolean isDelay_monitoring_point()
    {
        return delay_monitoring_point;
    }

    public long getReporting_stanox()
    {
        return reporting_stanox;
    }

    public long getActual_timestamp()
    {
        return actual_timestamp;
    }

    public boolean isCorrection_ind()
    {
        return correction_ind;
    }

    public String getEvent_source()
    {
        return event_source;
    }

    public String getTrain_file_address()
    {
        return train_file_address;
    }

    public String getPlatform()
    {
        return platform;
    }

    public String getDivision_code()
    {
        return division_code;
    }

    public boolean isTrain_terminated()
    {
        return train_terminated;
    }

    public boolean isOffroute_ind()
    {
        return offroute_ind;
    }

    public String getVariation_status()
    {
        return variation_status;
    }

    public String getTrain_service_code()
    {
        return train_service_code;
    }

    public long getLoc_stanox()
    {
        return loc_stanox;
    }

    public boolean isAuto_expected()
    {
        return auto_expected;
    }

    public String getDirection_ind()
    {
        return direction_ind;
    }

    public String getRoute()
    {
        return route;
    }

    public String getPlanned_event_type()
    {
        return planned_event_type;
    }

    public long getNext_report_stanox()
    {
        return next_report_stanox;
    }

    public String getLine_ind()
    {
        return line_ind;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="equals/hashcode">
    @Override
    public int hashCode()
    {
        return Objects.hashCode( getTrain_id() );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final TrainMovement other = (TrainMovement) obj;
        return Objects.equals( this.getTrain_id(), other.getTrain_id() );
    }
    //</editor-fold>

    /**
     * {@link Comparable} where we compare against the timestamp returned by {@link #getTimestamp() }. This means we use
     * that fields algorithm, specifically the planned timestamp if the train is on route or the actual timestamp when
     * they are off route.
     * <p/>
     * @param t < p/>
     * <p/>
     * @return
     */
    @Override
    public int compareTo( TrainMovement t )
    {
        long thisVal = getTimestamp();
        long anotherVal = t.getTimestamp();
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

    @Override
    public String toString()
    {
        return "TrainMovement{" + "event_type=" + event_type + ", gbtt_timestamp=" + gbtt_timestamp + ", current_train_id=" + current_train_id + ", delay_monitoring_point=" + delay_monitoring_point + ", reporting_stanox=" + reporting_stanox + ", actual_timestamp=" + actual_timestamp + ", platform=" + platform + ", train_terminated=" + train_terminated + ", offroute_ind=" + offroute_ind + ", train_service_code=" + train_service_code + ", toc_id=" + getToc_id() + ", loc_stanox=" + loc_stanox + '}';
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }

}
