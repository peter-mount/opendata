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
public enum TrainCancellationFactory
        implements Function<JsonObject, TrainCancellation>
{

    INSTANCE;

    @Override
    public TrainCancellation apply( JsonObject t )
    {
        TrainCancellation c = new TrainCancellation();
        c.setToc_id( JsonUtils.getInt( t, "toc_id" ) );
        c.setTrain_id( JsonUtils.getString( t, "train_id" ));
        
        c.setOrig_loc_stanox( JsonUtils.getLong( t, "orig_loc_stanox" ));
        c.setOrig_loc_timestamp(JsonUtils.getLong( t, "orig_loc_timestamp" ));
        c.setDep_timestamp(JsonUtils.getLong( t, "dep_timestamp" ));
        c.setDivision_code(JsonUtils.getInt( t, "division_code" ));
        c.setLoc_stanox(JsonUtils.getLong( t, "loc_stanox" ));
        c.setCanx_timestamp(JsonUtils.getLong( t, "canx_timestamp" ));
        c.setCanx_reason_code(JsonUtils.getString( t, "canx_reason_code" ));
        c.setCanx_type(JsonUtils.getString( t, "canx_type" ));
        c.setTrain_service_code(JsonUtils.getString( t, "train_service_code" ));
        c.setTrain_file_address(JsonUtils.getString( t, "train_file_address" ));

        // Not in trust body but is in the header & in our simpler format
        c.setOriginal_data_source( JsonUtils.getString( t, "original_data_source" ));
        return c;
    }

}
