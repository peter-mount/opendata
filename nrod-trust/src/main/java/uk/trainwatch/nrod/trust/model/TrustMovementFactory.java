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
 * Handles the decoding of JSON into specific objects defined by the inbound message type.
 * <p>
 * If the inbound message is null, has no header or is of an unsupported message type this will return null.
 * <p>
 * @author Peter T Mount
 */
public enum TrustMovementFactory
        implements Function<JsonObject, Object>
{

    INSTANCE;

    @Override
    public Object apply( JsonObject t )
    {
        if( t == null )
        {
            return null;
        }

        JsonObject header = t.getJsonObject( "header" );
        JsonObject body = t.getJsonObject( "body" );

        if( header == null || body == null )
        {
            return null;
        }

        switch( JsonUtils.getString( header, "msg_type", "INVD" ) )
        {
            case "0003":
                return TrainMovementFactory.INSTANCE.apply( body );
            default:
                return null;
        }
    }

}
