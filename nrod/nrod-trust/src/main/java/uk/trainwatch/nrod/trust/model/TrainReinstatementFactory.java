/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum TrainReinstatementFactory
    implements Function<JsonObject,TrainReinstatement>
{
    INSTANCE;

    @Override
    public TrainReinstatement apply( JsonObject t )
    {
        TrainReinstatement r = new TrainReinstatement();
        r.setToc_id( JsonUtils.getInt( t, "toc_id" ) );
        r.setTrain_id( JsonUtils.getString( t, "train_id" ));

        // TrustAdjustment fields
        // TODO make this common?
        r.setCurrent_train_id( JsonUtils.getString( t, "current_train_id" ) );
        r.setOriginal_loc_timestamp( JsonUtils.getLong( t, "original_loc_timestamp"));
        r.setDep_timestamp(JsonUtils.getLong( t, "dep_timestamp"));
        r.setLoc_stanox(JsonUtils.getLong( t, "loc_stanox"));
        r.setOriginal_loc_stanox(JsonUtils.getLong( t, "original_loc_stanox"));
        r.setTrain_file_address(JsonUtils.getString( t, "train_file_address" ) );
        r.setTrain_service_code(JsonUtils.getString( t, "train_service_code" ) );
        
        r.setReinstatement_timestamp(JsonUtils.getLong( t, "reinstatement_timestamp"));
        r.setDivision_code_id( JsonUtils.getInt( t, "division_code_id"));
        return r;
    }
    
    
}
