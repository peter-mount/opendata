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
import uk.trainwatch.util.RecentList;

/**
 * Handles a RecentList of the last 10 movements for each area
 * <p>
 * @author peter
 */
@ApplicationScoped
public class RecentAreaMap
{

    private final Map<String, RecentList<TDMessage>> areas = new ConcurrentHashMap<>();
    private final Consumer<TDMessage> consumer = new TDVisitor()
    {

        @Override
        public void visit( BerthCancel s )
        {
            add( s );
        }

        @Override
        public void visit( BerthInterpose s )
        {
            add( s );
        }

        @Override
        public void visit( BerthStep s )
        {
            add( s );
        }

    };

    /**
     * Accept via CDI eventing a TD Message and add it to this map
     * <p>
     * @param t
     */
    void accept( @Observes TDMessage t )
    {
        consumer.accept( t );
    }

    public RecentList getRecentList( String area )
    {
        return areas.computeIfAbsent( area, k -> new RecentList<>() );
    }

    private void add( TDMessage m )
    {
        getRecentList( m.getAreaId() ).
                add( m );
    }

}
