/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
public enum TrainActivationFactory
        implements Function<JsonObject, TrainActivation>
{

    INSTANCE;

    @Override
    public TrainActivation apply( JsonObject t )
    {
        return new TrainActivation(
                JsonUtils.getString( t, "schedule_source" ),
                JsonUtils.getString( t, "train_file_address" ),
                JsonUtils.getDate( t, "schedule_end_date" ),
                JsonUtils.getString( t, "train_id" ),
                JsonUtils.getTimestamp( t, "tp_origin_timestamp" ),
                JsonUtils.getLong( t, "creation_timestamp" ),
                JsonUtils.getLong( t, "tp_origin_stanox" ),
                JsonUtils.getLong( t, "origin_dep_timestamp" ),
                JsonUtils.getString( t, "train_service_code" ),
                JsonUtils.getInt( t, "toc_id" ),
                JsonUtils.getString( t, "d1266_record_number" ),
                JsonUtils.getString( t, "train_call_type" ),
                JsonUtils.getString( t, "train_uid" ),
                JsonUtils.getString( t, "train_call_mode" ),
                JsonUtils.getString( t, "schedule_type" ),
                JsonUtils.getLong( t, "sched_origin_stanox" ),
                JsonUtils.getString( t, "schedule_wtt_id" ),
                JsonUtils.getDate( t, "schedule_start_date" ) );
    }

}
