/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nrod.td.berth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import uk.trainwatch.nrod.td.model.BerthCancel;
import uk.trainwatch.nrod.td.model.BerthInterpose;
import uk.trainwatch.nrod.td.model.BerthStep;
import uk.trainwatch.nrod.td.model.TDMessage;
import uk.trainwatch.nrod.td.model.TDVisitor;

/**
 * Manages a map of each signalling area and what's contained in each berth
 * <p>
 * @author peter
 */
@ApplicationScoped
public class BerthAreaMap
{

    private final Map<String, BerthMap> berths = new ConcurrentHashMap<>();
    private final Consumer<TDMessage> consumer =         new TDVisitor()
        {

            @Override
            public void visit( BerthCancel s )
            {
                getArea( s.getAreaId() ).
                        cancel( s.getFrom(), s.getDescr() );
            }

            @Override
            public void visit( BerthInterpose s )
            {
                getArea( s.getAreaId() ).
                        put( s.getTo(), s.getDescr() );
            }

            @Override
            public void visit( BerthStep s )
            {
                // Note, put then cancel just incase someone queries it between the two, safer to show 2 berths occupied than none with a train in there :-P
                getArea( s.getAreaId() ).
                        put( s.getTo(), s.getDescr() ).
                        cancel( s.getFrom(), s.getDescr() );
            }

        };

    /**
     * Accept via CDI eventing a TD Message and add it to this map
     * @param t 
     */
    void accept( @Observes TDMessage t )
    {
        consumer.accept( t );
    }

    /**
     * Returns the {@link BerthMap} for a signalling area.
     * <p>
     * Note: Area is a 2 character code. Any
     * <p>
     * @param area Signalling area
     * <p>
     * @return BerthMap
     * <p>
     * @throws IllegalArgumentException if area is invalid.
     */
    public BerthMap getArea( String area )
    {
        if( area == null || area.length() != 2 )
        {
            throw new IllegalArgumentException( "Invalid signalling area " + area );
        }
        return berths.computeIfAbsent( area.toUpperCase(), k -> new BerthHashMap() );
    }

    public boolean containsArea( String area )
    {
        return area == null || area.length() != 2 ? false : berths.containsKey( area.toUpperCase() );
    }
}
