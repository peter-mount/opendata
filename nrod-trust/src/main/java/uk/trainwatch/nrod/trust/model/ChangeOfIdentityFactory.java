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
public enum ChangeOfIdentityFactory
        implements Function<JsonObject, ChangeOfIdentity>
{

    INSTANCE;

    @Override
    public ChangeOfIdentity apply( JsonObject t )
    {
        ChangeOfIdentity c = new ChangeOfIdentity();
        c.setTrain_id( JsonUtils.getString( t, "train_id" ));
        
        c.setCurrent_train_id( JsonUtils.getString( t, "current_train_id" ) );
        c.setRevised_train_id( JsonUtils.getString( t, "revised_train_id" ) );
        c.setTrain_file_address( JsonUtils.getString( t, "train_file_address" ) );
        c.setTrain_service_code( JsonUtils.getString( t, "train_service_code" ) );
        c.setEvent_timestamp( JsonUtils.getLong( t, "event_timestamp" ) );
        return c;
    }

}
