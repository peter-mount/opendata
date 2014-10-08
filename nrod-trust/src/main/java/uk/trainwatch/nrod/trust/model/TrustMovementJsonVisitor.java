/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public class TrustMovementJsonVisitor
        implements TrustMovementVisitor
{

    private JsonObjectBuilder b;

    public JsonObjectBuilder getJsonObjectBuilder()
    {
        return b;
    }

    @Override
    public void visit( TrainActivation t )
    {
        b = Json.createObjectBuilder().
                add( "id", t.getId() ).
                add( "schedule_source", t.getSchedule_source() ).
                add( "train_file_address", t.getTrain_file_address() ).
                add( "schedule_end_date", t.getSchedule_end_date().
                     getTime() ).
                add( "train_id", t.getTrain_id() ).
                add( "tp_origin_timestamp", t.getTp_origin_timestamp().
                     getTime() ).
                add( "creation_timestamp", t.getCreation_timestamp() ).
                add( "tp_origin_stanox", t.getTp_origin_stanox() ).
                add( "origin_dep_timestamp", t.getOrigin_dep_timestamp() ).
                add( "train_service_code", t.getTrain_service_code() ).
                add( "toc_id", t.getToc_id() ).
                add( "d1266_record_number", t.getD1266_record_number() ).
                add( "train_call_type", t.getTrain_call_type() ).
                add( "train_uid", t.getTrain_uid() ).
                add( "train_call_mode", t.getTrain_call_mode() ).
                add( "schedule_type", t.getSchedule_type() ).
                add( "sched_origin_stanox", t.getSched_origin_stanox() ).
                add( "schedule_wtt_id", t.getSchedule_wtt_id() ).
                add( "schedule_start_date", t.getSchedule_start_date().
                     getTime() );
    }
}
