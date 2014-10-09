/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.nrod.td.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * The various message types in the TD feed
 * <p>
 * @author Peter T Mount
 */
public enum TDMessageType
{

    // C Class Messages
    /**
     * The CA message is a 'step' message.
     * <p>
     * This moves the description from the 'from' berth, in to the 'to' berth, cancelling the description in the 'from'
     * berth and overwriting any description in the 'to' berth.
     */
    CA( "CA", TDFactory.BERTH_STEP ),
    /**
     * The CB message is a 'cancel' message.
     * <p>
     * This cancels the description in the 'from' berth.
     */
    CB( "CB", TDFactory.BERTH_CANCEL ),
    /**
     * The CC message is an 'interpose' message.
     * <p>
     * This inserts the description in to the 'to' berth, overwriting any description in the 'to' berth.
     */
    CC( "CC", TDFactory.BERTH_INTERPOSE ),
    /**
     * The CT message is a 'heartbeat' message, periodically sent from a train describer.
     */
    CT( "CT", TDFactory.HEARTBEAT ),
    // S Class messages
    /**
     * Updates the signalling data for a specified set of signalling elements
     */
    SF( "SF", TDFactory.SIGNAL_UPDATE ),
    /**
     * Part of a set of SG messages which update the whole set of signalling data for TD area. Terminated by an SH
     * message.
     */
    SG( "SG", TDFactory.SIGNAL_REFRESH ),
    /**
     * Last message in a signalling refresh
     */
    SH( "SH", TDFactory.SIGNAL_REFRESH_END );

    private static final Map<String, TDMessageType> TYPES = new ConcurrentHashMap<>();

    static
    {
        for( TDMessageType mt : values() )
        {
            TYPES.put( mt.type, mt );
        }
    }

    /**
     * Lookup the TDMessageType by it's type
     * <p>
     * @param type TDMessageType type
     * <p>
     * @return TDMessageType or null if not found
     */
    public static TDMessageType lookup( String type )
    {
        return type == null ? null : TYPES.get( type );
    }

    /**
     * Lookup the TDMessageType from a {@link JsonValue}.
     * <p>
     * @param v JsonValue
     * <p>
     * @return TDMessageType or null if v is null, not a string or not found
     */
    public static TDMessageType lookup( JsonValue v )
    {
        if( v == null || v.getValueType() != JsonValue.ValueType.STRING )
        {
            return null;
        }
        else
        {
            return lookup( ((JsonString) v).getString() );
        }
    }

    public static TDMessageType lookup( JsonObject o, String n )
    {
        return lookup( o.get( n ) );
    }

    public static TDMessageType lookup( JsonObject o )
    {
        return lookup( o, "msg_type" );
    }

    /**
     * Convenience method to add a TDMessageType to a {@link JsonObjectBuilder}. If null is passed for the TDMessageType
     * then the field will be set to null.
     * <p>
     * @param b  builder
     * @param n  name of field
     * @param mt TDMessageType to add
     */
    public static void add( JsonObjectBuilder b, String n, TDMessageType mt )
    {
        Objects.requireNonNull( b );
        Objects.requireNonNull( n );
        if( mt == null )
        {
            b.addNull( n );
        }
        else
        {
            b.add( n, mt.getType() );
        }
    }

    private final String type;
    private final Function<JsonObject, TDMessage> factory;

    private TDMessageType( String type, Function<JsonObject, TDMessage> factory )
    {
        this.type = type;
        this.factory = factory;
    }

    /**
     * The message type
     * <p>
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     * Factory that can create this object
     * <p>
     * @return
     */
    public Function<JsonObject, TDMessage> getFactory()
    {
        return factory;
    }

}
