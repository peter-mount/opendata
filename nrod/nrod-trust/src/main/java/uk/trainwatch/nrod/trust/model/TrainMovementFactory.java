package uk.trainwatch.nrod.trust.model;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
public enum TrainMovementFactory
        implements Function<JsonObject, TrainMovement>
{

    INSTANCE;

    @Override
    public TrainMovement apply( JsonObject t )
    {
        return new TrainMovement(
                JsonUtils.getString( t, "event_type" ),
                JsonUtils.getLong( t, "gbtt_timestamp" ),
                JsonUtils.getLong( t, "original_loc_stanox" ),
                JsonUtils.getLong( t, "planned_timestamp" ),
                JsonUtils.getInt( t, "timetable_variation" ),
                JsonUtils.getLong( t, "original_loc_timestamp" ),
                JsonUtils.getString( t, "current_train_id" ),
                JsonUtils.getBoolean( t, "delay_monitoring_point" ),
                JsonUtils.getLong( t, "reporting_stanox" ),
                JsonUtils.getLong( t, "actual_timestamp" ),
                JsonUtils.getBoolean( t, "correction_ind" ),
                JsonUtils.getString( t, "event_source" ),
                JsonUtils.getString( t, "train_file_address" ),
                JsonUtils.getString( t, "platform" ),
                JsonUtils.getString( t, "division_code" ),
                JsonUtils.getBoolean( t, "train_terminated" ),
                JsonUtils.getString( t, "train_id" ),
                JsonUtils.getBoolean( t, "offroute_ind" ),
                JsonUtils.getString( t, "variation_status" ),
                JsonUtils.getString( t, "train_service_code" ),
                JsonUtils.getInt( t, "toc_id" ),
                JsonUtils.getLong( t, "loc_stanox" ),
                JsonUtils.getBoolean( t, "auto_expected" ),
                JsonUtils.getString( t, "direction_ind" ),
                JsonUtils.getString( t, "route" ),
                JsonUtils.getString( t, "planned_event_type" ),
                JsonUtils.getLong( t, "next_report_stanox" ),
                JsonUtils.getString( t, "line_ind" ) );
    }
}
