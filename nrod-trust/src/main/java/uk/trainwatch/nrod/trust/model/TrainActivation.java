/*
 * To change this template, choose Tools ;
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.nrod.util.TrainDate;
import uk.trainwatch.nrod.util.TrainId;

/**
 * Java representation of the mvt_activation table and 0001 movement messages
 * <p/>
 * @author peter
 */
@XmlRootElement( name = "activation" )
@XmlAccessorType( XmlAccessType.FIELD )
public class TrainActivation
        implements Serializable,
                   Comparable<TrainActivation>
{

    private static final long serialVersionUID = -2358185935042140248L;
    private int id;
    private String schedule_source;
    private String train_file_address;
    private Date schedule_end_date;
    private String train_id;
    private Date tp_origin_timestamp;
    private long creation_timestamp;
    private long tp_origin_stanox;
    private long origin_dep_timestamp;
    private String train_service_code;
    private int toc_id;
    private String d1266_record_number;
    private String train_call_type;
    private String train_uid;
    private String train_call_mode;
    private String schedule_type;
    private long sched_origin_stanox;
    private String schedule_wtt_id;
    private Date schedule_start_date;
    private transient TrainId trainId;

    TrainActivation()
    {
    }

    TrainActivation( int id, String schedule_source, String train_file_address, Date schedule_end_date,
                     String train_id, Date tp_origin_timestamp, long creation_timestamp, long tp_origin_stanox,
                     long origin_dep_timestamp, String train_service_code, int toc_id, String d1266_record_number,
                     String train_call_type, String train_uid, String train_call_mode, String schedule_type,
                     long sched_origin_stanox, String schedule_wtt_id, Date schedule_start_date )
    {
        this.id = id;
        this.schedule_source = schedule_source;
        this.train_file_address = train_file_address;
        this.schedule_end_date = schedule_end_date;
        this.train_id = train_id;
        this.tp_origin_timestamp = tp_origin_timestamp;
        this.creation_timestamp = creation_timestamp;

        /*
         * If the train is due to start from a location other than the origin (i.e. it is part-cancelled), the STANOX of
         * the location at which the train starts, otherwise the STANOX of the scheduled origin location. If this field
         * is populated, it will be typically be in response to a VAR issued through VSTP or SCHEDULE.
         */
        if( tp_origin_stanox > 0 )
        {
            this.tp_origin_stanox = tp_origin_stanox;
        }
        else
        {
            this.tp_origin_stanox = sched_origin_stanox;
        }

        this.origin_dep_timestamp = origin_dep_timestamp;
        this.train_service_code = train_service_code;
        this.toc_id = toc_id;
        this.d1266_record_number = d1266_record_number;
        this.train_call_type = train_call_type;
        this.train_uid = train_uid;
        this.train_call_mode = train_call_mode;
        this.schedule_type = schedule_type;
        this.sched_origin_stanox = sched_origin_stanox;
        this.schedule_wtt_id = schedule_wtt_id;
        this.schedule_start_date = schedule_start_date;
    }

    public synchronized TrainId getTrainId()
    {
        if( trainId == null )
        {
            TrainDate td = new TrainDate( schedule_start_date.getTime() ).//
                    clearTime().
                    setHour( 6 );
            trainId = new TrainId( td, train_id );
        }
        return trainId;
    }

    public int getId()
    {
        return id;
    }

    public Date getCreation()
    {
        return new Date( getCreation_timestamp() );
    }

    public Date getOrigin_dep()
    {
        return new Date( getOrigin_dep_timestamp() );
    }

    public String getSchedule_source()
    {
        return schedule_source;
    }

    public String getTrain_file_address()
    {
        return train_file_address;
    }

    public Date getSchedule_end_date()
    {
        return schedule_end_date;
    }

    public String getTrain_id()
    {
        return train_id;
    }

    public Date getTp_origin_timestamp()
    {
        return tp_origin_timestamp;
    }

    public long getCreation_timestamp()
    {
        return creation_timestamp;
    }

    public long getTp_origin_stanox()
    {
        return tp_origin_stanox;
    }

    public long getOrigin_dep_timestamp()
    {
        return origin_dep_timestamp;
    }

    public String getTrain_service_code()
    {
        return train_service_code;
    }

    public int getToc_id()
    {
        return toc_id;
    }

    public String getD1266_record_number()
    {
        return d1266_record_number;
    }

    public String getTrain_call_type()
    {
        return train_call_type;
    }

    public String getTrain_uid()
    {
        return train_uid;
    }

    public String getTrain_call_mode()
    {
        return train_call_mode;
    }

    public String getSchedule_type()
    {
        return schedule_type;
    }

    public long getSched_origin_stanox()
    {
        return sched_origin_stanox;
    }

    public String getSchedule_wtt_id()
    {
        return schedule_wtt_id;
    }

    public Date getSchedule_start_date()
    {
        return schedule_start_date;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + this.id;
        hash = 89 * hash + (this.schedule_source != null ? this.schedule_source.hashCode() : 0);
        hash = 89 * hash + (this.train_file_address != null ? this.train_file_address.hashCode() : 0);
        hash = 89 * hash + (this.schedule_end_date != null ? this.schedule_end_date.hashCode() : 0);
        hash = 89 * hash + (this.train_id != null ? this.train_id.hashCode() : 0);
        hash = 89 * hash + (this.tp_origin_timestamp != null ? this.tp_origin_timestamp.hashCode() : 0);
        hash = 89 * hash + (int) (this.creation_timestamp ^ (this.creation_timestamp >>> 32));
        hash = 89 * hash + (int) (this.tp_origin_stanox ^ (this.tp_origin_stanox >>> 32));
        hash = 89 * hash + (int) (this.origin_dep_timestamp ^ (this.origin_dep_timestamp >>> 32));
        hash = 89 * hash + (this.train_service_code != null ? this.train_service_code.hashCode() : 0);
        hash = 89 * hash + this.toc_id;
        hash = 89 * hash + (this.d1266_record_number != null ? this.d1266_record_number.hashCode() : 0);
        hash = 89 * hash + (this.train_call_type != null ? this.train_call_type.hashCode() : 0);
        hash = 89 * hash + (this.train_uid != null ? this.train_uid.hashCode() : 0);
        hash = 89 * hash + (this.train_call_mode != null ? this.train_call_mode.hashCode() : 0);
        hash = 89 * hash + (this.schedule_type != null ? this.schedule_type.hashCode() : 0);
        hash = 89 * hash + (int) (this.sched_origin_stanox ^ (this.sched_origin_stanox >>> 32));
        hash = 89 * hash + (this.schedule_wtt_id != null ? this.schedule_wtt_id.hashCode() : 0);
        hash = 89 * hash + (this.schedule_start_date != null ? this.schedule_start_date.hashCode() : 0);
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
        final TrainActivation other = (TrainActivation) obj;
        if( this.id != other.id )
        {
            return false;
        }
        if( (this.schedule_source == null) ? (other.schedule_source != null) : !this.schedule_source.equals(
                other.schedule_source ) )
        {
            return false;
        }
        if( (this.train_file_address == null) ? (other.train_file_address != null) : !this.train_file_address.equals(
                other.train_file_address ) )
        {
            return false;
        }
        if( this.schedule_end_date != other.schedule_end_date && (this.schedule_end_date == null || !this.schedule_end_date.
                                                                  equals(
                                                                  other.schedule_end_date )) )
        {
            return false;
        }
        if( (this.train_id == null) ? (other.train_id != null) : !this.train_id.equals( other.train_id ) )
        {
            return false;
        }
        if( this.tp_origin_timestamp != other.tp_origin_timestamp && (this.tp_origin_timestamp == null || !this.tp_origin_timestamp.
                                                                      equals(
                                                                      other.tp_origin_timestamp )) )
        {
            return false;
        }
        if( this.creation_timestamp != other.creation_timestamp )
        {
            return false;
        }
        if( this.tp_origin_stanox != other.tp_origin_stanox )
        {
            return false;
        }
        if( this.origin_dep_timestamp != other.origin_dep_timestamp )
        {
            return false;
        }
        if( (this.train_service_code == null) ? (other.train_service_code != null) : !this.train_service_code.equals(
                other.train_service_code ) )
        {
            return false;
        }
        if( this.toc_id != other.toc_id )
        {
            return false;
        }
        if( (this.d1266_record_number == null) ? (other.d1266_record_number != null) : !this.d1266_record_number.equals(
                other.d1266_record_number ) )
        {
            return false;
        }
        if( (this.train_call_type == null) ? (other.train_call_type != null) : !this.train_call_type.equals(
                other.train_call_type ) )
        {
            return false;
        }
        if( (this.train_uid == null) ? (other.train_uid != null) : !this.train_uid.equals( other.train_uid ) )
        {
            return false;
        }
        if( (this.train_call_mode == null) ? (other.train_call_mode != null) : !this.train_call_mode.equals(
                other.train_call_mode ) )
        {
            return false;
        }
        if( (this.schedule_type == null) ? (other.schedule_type != null) : !this.schedule_type.equals(
                other.schedule_type ) )
        {
            return false;
        }
        if( this.sched_origin_stanox != other.sched_origin_stanox )
        {
            return false;
        }
        if( (this.schedule_wtt_id == null) ? (other.schedule_wtt_id != null) : !this.schedule_wtt_id.equals(
                other.schedule_wtt_id ) )
        {
            return false;
        }
        if( this.schedule_start_date != other.schedule_start_date && (this.schedule_start_date == null || !this.schedule_start_date.
                                                                      equals(
                                                                      other.schedule_start_date )) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo( TrainActivation o )
    {
        int r = train_id.compareTo( o.train_id );
        if( r == 0 )
        {
            r = schedule_start_date.compareTo( o.schedule_start_date );
        }
        return r;
    }
}
