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
        implements Function<JsonObject, TrustMovement>
{

    INSTANCE;

    @Override
    public TrustMovement apply( JsonObject t )
    {
        if( t == null )
        {
            return null;
        }

        // Figure out if this is a Trust object or our own format
        JsonObject header = t.getJsonObject( "header" );
        boolean trustSource = header != null;
        JsonObject body = trustSource ? t.getJsonObject( "body" ) : t;
        if( body == null )
        {
            return null;
        }

        // Decode msg_type
        final TrustMovementType type = TrustMovementType.getType(
                JsonUtils.getString( trustSource ? header : body, "msg_type" ) );
        if( type == null )
        {
            // Unsupported type?
            return null;
        }

        // Apply any special manipulation between the two formats?
        if( trustSource )
        {
            if( type == TrustMovementType.CANCELLATION )
            {
                // We also take a field from the header as it determines the type of cancellation
                String original_data_source = JsonUtils.getString( header, "original_data_source", "" );

                body = JsonUtils.createObjectBuilder( body ).
                        add( "original_data_source", original_data_source ).
                        build();
            }
        }

        // Now build the object. If factory is null it means we don't implement it
        Function<JsonObject, ? extends TrustMovement> factory = type.getFactory();
        return factory==null?null:factory.apply( body );
    }

}
