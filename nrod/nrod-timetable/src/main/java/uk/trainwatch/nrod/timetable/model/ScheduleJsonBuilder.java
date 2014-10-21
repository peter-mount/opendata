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
import uk.trainwatch.nrod.timetable.cif.record.Location;
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

    public JsonObject visit( Schedule t )
    {
        builder = Json.createObjectBuilder();
        locations = Json.createArrayBuilder();

        t.getBasicSchedule().
                accept( this );

        t.getBasicScheduleExtras().
                accept( this );

        t.forEach( l -> l.accept( this ) );

        builder.add( "locations", locations );

        return builder.build();
    }

    private JsonArrayBuilder getArray( Enum<?> ary[] )
    {
        JsonArrayBuilder b = Json.createArrayBuilder();
        if( ary != null && ary.length > 0 )
        {
            for( Enum<?> a : ary )
            {
                b.add( a.toString() );
            }
        }
        return b;
    }

    @Override
    public void visit( BasicSchedule s )
    {
        builder.add( "schedule", Json.createObjectBuilder().
                     add( "trainUid", s.getTrainUid().
                          getId() ).
                     add( "runsFrom", s.getRunsFrom().
                          toString() ).
                     add( "runsTo", s.getRunsTo().
                          toString() ).
                     add( "daysRun", s.getDaysRun().
                          getDaysRunning() ).
                     add( "bankHolidayRunning", s.getBankHolidayRunning().
                          toString() ).
                     add( "trainStatus", s.getTrainStatus().
                          toString() ).
                     add( "trainCategory", s.getTrainCategory().
                          toString() ).
                     add( "trainIdentity", s.getTrainIdentity() ).
                     add( "headCode", s.getHeadCode() ).
                     add( "serviceCode", s.getServiceCode() ).
                     add( "portionId", s.getPortionId().
                          toString() ).
                     add( "powerType", s.getPowerType().
                          toString() ).
                     add( "timingLoad", s.getTimingLoad().
                          toString() ).
                     add( "speed", s.getSpeed() ).
                     add( "operChars", getArray( s.getOperatingCharacteristics() ) ).
                     add( "trainClass", s.getTrainClass().
                          toString() ).
                     add( "sleepers", s.getSleepers().
                          toString() ).
                     add( "reservations", s.getReservations().
                          toString() ).
                     add( "catering", getArray( s.getCateringCode() ) ).
                     add( "branding", getArray( s.getServiceBranding() ) ).
                     add( "stpIndicator", s.getStpInd().
                          toString() )
        );
    }

    @Override
    public void visit( BasicScheduleExtras s )
    {
        builder.add( "extras", Json.createObjectBuilder().
                     add( "uicCode", s.getUicCode() ).
                     add( "atocCode", s.getAtocCode().
                          getCode() ).
                     add( "atsCode", s.getApplicableTimetableCode().
                          getCode() )
        );
    }

    private JsonObjectBuilder createBuilder( Location l )
    {
        return Json.createObjectBuilder().
                add( "type", l.getRecordType().
                     toString() ).
                add( "tiploc", l.getLocation().
                     getKey() );
    }

    @Override
    public void visit( OriginLocation ol )
    {
        locations.add( createBuilder( ol ).
                add( "workDeparture", ol.getWorkDeparture().
                     toString() ).
                add( "pubDeparture", ol.getPublicDeparture().
                     toString() ).
                add( "platform", ol.getPlatform() ).
                add( "line", ol.getLine() ).
                add( "engAllowance", ol.getEngAllowance() ).
                add( "pathAllowance", ol.getPathAllowance() ).
                add( "perfAllowance", ol.getPerfAllowance() ).
                add( "activity", getArray( ol.getActivity() ) ) );
    }

    @Override
    public void visit( IntermediateLocation il )
    {
        locations.add( createBuilder( il ).
                add( "workArrival", il.getWorkArrival().
                     toString() ).
                add( "workDeparture", il.getWorkDeparture().
                     toString() ).
                add( "workPass", il.getWorkPass().
                     toString() ).
                add( "pubArrival", il.getPublicArrival().
                     toString() ).
                add( "pubDeparture", il.getPublicDeparture().
                     toString() ).
                add( "platform", il.getPlatform() ).
                add( "line", il.getLine() ).
                add( "engAllowance", il.getEngAllowance() ).
                add( "pathAllowance", il.getPathAllowance() ).
                add( "perfAllowance", il.getPerfAllowance() ).
                add( "activity", getArray( il.getActivity() ) ) );
    }

    @Override
    public void visit( ChangesEnRoute s )
    {
        locations.add( createBuilder( s ).

                add( "trainCategory", s.getTrainCategory().
                     toString() ).
                add( "trainIdentity", s.getTrainIdentity() ).
                add( "headCode", s.getHeadCode() ).
                add( "serviceCode", s.getServiceCode() ).
                add( "portionId", s.getPortionId().
                     toString() ).
                add( "powerType", s.getPowerType().
                     toString() ).
                add( "timingLoad", s.getTimingLoad().
                     toString() ).
                add( "speed", s.getSpeed() ).
                add( "operChars", getArray( s.getOperatingCharacteristics() ) ).
                add( "trainClass", s.getTrainClass().
                     toString() ).
                add( "sleepers", s.getSleepers().
                     toString() ).
                add( "reservations", s.getReservations().
                     toString() ).
                add( "catering", getArray( s.getCateringCode() ) ).
                add( "branding", getArray( s.getServiceBranding() ) ).
                add( "uicCode", s.getUicCode() ) );
    }

    @Override
    public void visit( TerminatingLocation tl )
    {
        locations.add( createBuilder( tl ).
                add( "workArrival", tl.getWorkArrival().
                     toString() ).
                add( "pubArrival", tl.getPublicArrival().
                     toString() ).
                add( "platform", tl.getPlatform() ).
                add( "path", tl.getPath() ).
                add( "activity", getArray( tl.getActivity() ) ) );
    }

}
