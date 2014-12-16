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

import java.util.function.Consumer;

/**
 * Visitor used by all TD message types. This visitor is also a {@link Consumer} which will pass the visitor to the received object.
 * <p>
 * @author Peter T Mount
 */
public interface TDVisitor
        extends Consumer<TDMessage>
{

    @Override
    default void accept( TDMessage t )
    {
        if( t != null )
        {
            t.accept( this );
        }
    }

    default void visit( BerthStep s )
    {
    }

    default void visit( BerthCancel s )
    {
    }

    default void visit( BerthInterpose s )
    {
    }

    default void visit( Heartbeat s )
    {
    }

    default void visit( SignalUpdate s )
    {
    }

    default void visit( SignalRefresh s )
    {
    }

    default void visit( SignalEndRefresh s )
    {
    }
}
