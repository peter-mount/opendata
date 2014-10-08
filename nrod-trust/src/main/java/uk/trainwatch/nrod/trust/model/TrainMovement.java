/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.Date;
import java.util.Objects;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.nrod.util.TrainDate;
import uk.trainwatch.nrod.util.TrainId;
import uk.trainwatch.nrod.util.TrainTime;

/**
 * Java representation of the mvt_movement table and 0003 movement messages
 * <p/>
 * @author peter
 */
public class TrainMovement
        extends TrustMovement
        implements Comparable<TrainMovement>
{

    private long id;
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
    private String train_id;
    private boolean offroute_ind;
    private String variation_status;
    private String train_service_code;
    private int toc_id;
    private long loc_stanox;
    private boolean auto_expected;
    private String direction_ind;
    private String route;
    private String planned_event_type;
    private long next_report_stanox;
    private String line_ind;
    private transient TrainTime time;
    private transient TrainId trainId;

    TrainMovement()
    {
    }

    TrainMovement( long id, String event_type, long gbtt_timestamp, long original_loc_stanox,
                   long planned_timestamp, int timetable_variation, long original_loc_timestamp,
                   String current_train_id, boolean delay_monitoring_point, long reporting_stanox,
                   long actual_timestamp, boolean correction_ind, String event_source, String train_file_address,
                   String platform, String division_code, boolean train_terminated, String train_id,
                   boolean offroute_ind, String variation_status, String train_service_code, int toc_id,
                   long loc_stanox, boolean auto_expected, String direction_ind, String route,
                   String planned_event_type, long next_report_stanox, String line_ind )
    {
        this.id = id;
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
        this.train_id = train_id;
        this.offroute_ind = offroute_ind;
        this.variation_status = variation_status;
        this.train_service_code = train_service_code;
        this.toc_id = toc_id;
        this.loc_stanox = loc_stanox;
        this.auto_expected = auto_expected;
        this.direction_ind = direction_ind;
        this.route = route;
        this.planned_event_type = planned_event_type;
        this.next_report_stanox = next_report_stanox;
        this.line_ind = line_ind;
    }

    //<editor-fold defaultstate="collapsed" desc="Synthetic fields">
    public synchronized TrainId getTrainId()
    {
        if( trainId == null )
        {
            TrainDate td = new TrainDate( actual_timestamp ).//
                    clearTime().
                    setHour( 6 );
            trainId = new TrainId( td, train_id );
        }
        return trainId;
    }

//    public TrainOperator getToc()
//    {
//        return TrainOperatorFactory.INSTANCE.getById( toc_id );
//    }
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

    public synchronized TrainTime getTime()
    {
        if( time == null )
        {
            time = TrainTime.getTime( getTimestamp() );
        }
        return time;
    }

    /**
     * The delay in seconds
     */
    public String getHeadCode()
    {
        return getTrain_id().
                substring( 2, 6 );
    }

    public long getDelay()
    {
        return (actual_timestamp - planned_timestamp) / 1000L;
    }

    /**
     * The delay in a human format. Blank if lt 30s and include 1/2 symbol for 1/2 seconds
     * <p/>
     * @return
     */
    public String getDelayHtml()
    {
        return isOffroute_ind() ? "N/A" : TrainDate.getDelayHtml( getDelay(), false );
    }

    public TrainLocation getOriginal_loc()
    {
        return TrainLocationFactory.INSTANCE.getTrainLocationByStanox( original_loc_stanox );
    }

    public Date getPlanned_time()
    {
        return new Date( planned_timestamp );
    }

    public Date getOriginal_loc_time()
    {
        return new Date( original_loc_timestamp );
    }

    public TrainLocation getReporting()
    {
        return TrainLocationFactory.INSTANCE.getTrainLocationByStanox( reporting_stanox );
    }

    public Date getActual_time()
    {
        return new Date( actual_timestamp );
    }

    public TrainLocation getLoc()
    {
        return TrainLocationFactory.INSTANCE.getTrainLocationByStanox( loc_stanox );
    }

    public TrainLocation getNext_report()
    {
        return TrainLocationFactory.INSTANCE.getTrainLocationByStanox( next_report_stanox );
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bean getters">
    public long getId()
    {
        return id;
    }

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

    public String getTrain_id()
    {
        return train_id;
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

    public int getToc_id()
    {
        return toc_id;
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
        int hash = 3;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 41 * hash + (this.train_id != null ? this.train_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final TrainMovement other = (TrainMovement) obj;
        return this.id == other.id && Objects.equals( this.train_id, other.train_id );
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
        return "TrainMovement{" + "event_type=" + event_type + ", gbtt_timestamp=" + gbtt_timestamp + ", current_train_id=" + current_train_id + ", delay_monitoring_point=" + delay_monitoring_point + ", reporting_stanox=" + reporting_stanox + ", actual_timestamp=" + actual_timestamp + ", platform=" + platform + ", train_terminated=" + train_terminated + ", offroute_ind=" + offroute_ind + ", train_service_code=" + train_service_code + ", toc_id=" + toc_id + ", loc_stanox=" + loc_stanox + '}';
    }

    @Override
    public void accept( TrustMovementVisitor v )
    {
        v.visit( this );
    }
    
}
