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
package uk.trainwatch.nrod.timetable.model;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;

/**
 *
 * @author Peter T Mount
 */
public class ScheduleJsonBuilder
        implements RecordVisitor
{

    private JsonObjectBuilder builder;
    private JsonArrayBuilder locations;

    public JsonObjectBuilder getBuilder()
    {
        return builder;
    }

    public JsonArrayBuilder getLocations()
    {
        return locations;
    }

    public JsonObject visit( Schedule t )
    {
        return visitBuilder( t ).
                build();
    }

    public JsonObjectBuilder visitBuilder( Schedule t )
    {
        builder = Json.createObjectBuilder();
        locations = Json.createArrayBuilder();

        t.getBasicSchedule().
                accept( this );

        t.getBasicScheduleExtras().
                accept( this );

        t.forEach( l -> l.accept( this ) );

        builder.add( "locations", locations );

        return builder;
    }

    @Override
    public void visit( BasicSchedule s )
    {
        builder.add( "schedule", BasicSchedule.toJson.apply( s ) );
    }

    @Override
    public void visit( BasicScheduleExtras s )
    {
        builder.add( "extras", BasicScheduleExtras.toJson.apply( s ) );
    }

    @Override
    public void visit( OriginLocation ol )
    {
        locations.add( OriginLocation.toJson.apply( ol ) );
    }

    @Override
    public void visit( IntermediateLocation il )
    {
        locations.add( IntermediateLocation.toJson.apply( il ) );
    }

    @Override
    public void visit( ChangesEnRoute s )
    {
        locations.add( ChangesEnRoute.toJson.apply( s ) );
    }

    @Override
    public void visit( TerminatingLocation tl )
    {
        locations.add( TerminatingLocation.toJson.apply( tl ) );
    }

}
