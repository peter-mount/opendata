/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.Date;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

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

    private void add( String k, Date v )
    {
        if( v == null )
        {
            b.addNull( k );
        }
        else
        {
            b.add( k, v.getTime() );
        }
    }

    private void add( String k, String v )
    {
        if( v == null )
        {
            b.addNull( k );
        }
        else
        {
            b.add( k, v );
        }
    }

    @Override
    public void visit( TrainActivation t )
    {
        b = Json.createObjectBuilder().
                add( "id", t.getId() );

        add( "schedule_source", t.getSchedule_source() );
        add( "train_file_address", t.getTrain_file_address() );
        add( "schedule_end_date", t.getSchedule_end_date() );
        add( "train_id", t.getTrain_id() );
        add( "tp_origin_timestamp", t.getTp_origin_timestamp() );

        b.add( "creation_timestamp", t.getCreation_timestamp() ).
                add( "tp_origin_stanox", t.getTp_origin_stanox() ).
                add( "origin_dep_timestamp", t.getOrigin_dep_timestamp() );

        add( "train_service_code", t.getTrain_service_code() );
        b.add( "toc_id", t.getToc_id() );
        add( "d1266_record_number", t.getD1266_record_number() );
        add( "train_call_type", t.getTrain_call_type() );
        add( "train_uid", t.getTrain_uid() );
        add( "train_call_mode", t.getTrain_call_mode() );
        add( "schedule_type", t.getSchedule_type() );
        b.add( "sched_origin_stanox", t.getSched_origin_stanox() );
        add( "schedule_wtt_id", t.getSchedule_wtt_id() );
        add( "schedule_start_date", t.getSchedule_start_date() );
    }

    @Override
    public void visit( TrainMovement t )
    {
        b = Json.createObjectBuilder().
                add( "id", t.getId() );
        add( "event_type", t.getEvent_type() );
        b.add( "gbtt_timestamp", t.getGbtt_timestamp() ).
                add( "original_loc_stanox", t.getOriginal_loc_stanox() ).
                add( "planned_timestamp", t.getPlanned_timestamp() ).
                add( "timetable_variation", t.getTimetable_variation() ).
                add( "original_loc_timestamp", t.getOriginal_loc_timestamp() );
        add( "current_train_id", t.getCurrent_train_id() );
        b.add( "delay_monitoring_point", t.isDelay_monitoring_point() ).
                add( "reporting_stanox", t.getReporting_stanox() ).
                add( "actual_timestamp", t.getActual_timestamp() ).
                add( "correction_ind", t.isCorrection_ind() );
        add( "event_source", t.getEvent_source() );
        add( "train_file_address", t.getTrain_file_address() );
        add( "platform", t.getPlatform() );
        add( "division_code", t.getDivision_code() );
        b.add( "train_terminated", t.isTrain_terminated() );
        add( "train_id", t.getTrain_id() );
        b.add( "offroute_ind", t.isOffroute_ind() );
        add( "variation_status", t.getVariation_status() );
        add( "train_service_code", t.getTrain_service_code() );
        b.add( "toc_id", t.getToc_id() ).
                add( "loc_stanox", t.getLoc_stanox() ).
                add( "auto_expected", t.isAuto_expected() ).
                add( "direction_ind", t.getDirection_ind() );
        add( "route", t.getRoute() );
        add( "planned_event_type", t.getPlanned_event_type() );
        b.add( "next_report_stanox", t.getNext_report_stanox() );
        add( "line_ind", t.getLine_ind() );
    }
}
