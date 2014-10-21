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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonObject;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordType;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum ScheduleJsonDecoder
        implements Function<JsonObject, Schedule>
{

    INSTANCE;

    @Override
    public Schedule apply( JsonObject t )
    {
        return new Schedule( BasicSchedule.fromJson.apply( t.getJsonObject( "schedule" ) ),
                             BasicScheduleExtras.fromJson.apply( t.getJsonObject( "extras" ) ),
                             decodeLocations( t.getJsonArray( "locations" ) ) );
    }

    private List<Location> decodeLocations( JsonArray a )
    {
        return a.stream().
                map( Functions.castTo( JsonObject.class ) ).
                filter( Objects::nonNull ).
                map( ScheduleJsonDecoder::decode ).
                filter( Objects::nonNull ).
                collect( Collectors.toList() );
    }

    private static Location decode( JsonObject o )
    {
        switch( JsonUtils.getEnum( RecordType.class, o, "type" ) )
        {
            case LO:
                return OriginLocation.fromJson.apply( o );

            case LI:
                return IntermediateLocation.fromJson.apply( o );

            case LT:
                return TerminatingLocation.fromJson.apply( o );

            case CR:
                return ChangesEnRoute.fromJson.apply( o );

            default:
                return (Location) null;
        }

    }
}
