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

import java.util.function.Function;
import javax.json.JsonObject;

/**
 * Factory that creates the appropriate message object from a {@link JsonObject}
 * <p>
 * @author Peter T Mount
 */
public enum TDMessageFactory
        implements Function<JsonObject, TDMessage>
{

    INSTANCE;

    @Override
    public TDMessage apply( JsonObject t )
    {
        if( t == null )
        {
            return null;
        }

        TDMessageType type = TDMessageType.lookup( t );
        if( type == null )
        {
            return null;
        }

        return type.getFactory().
                apply( t );
    }

}
