/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import uk.trainwatch.nrod.timetable.cif.record.BasicRecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAction;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.util.Consumers;

/**
 * A {@link RecordVisitor} which can be used to build schedules.
 * <p>
 * As each {@link Schedule} is built it's passed to a consumer for further processing.
 * <p>
 * @author Peter T Mount
 */
public class ScheduleBuilderVisitor
        extends BasicRecordVisitor
{

    private final Consumer<Schedule> scheduleConsumer;
    private BasicSchedule basicSchedule;
    private BasicScheduleExtras basicScheduleExtras;
    private List<Location> locations;

    public ScheduleBuilderVisitor( Consumer<Schedule> scheduleConsumer )
    {
        this( scheduleConsumer, Consumers.sink(), null );
    }

    public ScheduleBuilderVisitor( Consumer<Schedule> scheduleConsumer, LocalDate lastFileDate )
    {
        this( scheduleConsumer, Consumers.sink(), lastFileDate );
    }

    public ScheduleBuilderVisitor( Consumer<Schedule> scheduleConsumer, Consumer<TIPLOCAction> tiplocConsumer )
    {
        this( scheduleConsumer, tiplocConsumer, null );
    }

    public ScheduleBuilderVisitor( Consumer<Schedule> scheduleConsumer,
                                   Consumer<TIPLOCAction> tiplocConsumer,
                                   LocalDate lastFileDate )
    {
        super( tiplocConsumer, lastFileDate );
        this.scheduleConsumer = Objects.requireNonNull( scheduleConsumer );
    }

    @Override
    public void visit( BasicSchedule s )
    {
        basicSchedule = s;
        locations = new ArrayList<>();
    }

    @Override
    public void visit( BasicScheduleExtras e )
    {
        basicScheduleExtras = e;
    }

    @Override
    public void visit( OriginLocation ol )
    {
        locations.add( ol );
    }

    @Override
    public void visit( IntermediateLocation il )
    {
        locations.add( il );
    }

    @Override
    public void visit( ChangesEnRoute c )
    {
        locations.add( c );
    }

    @Override
    public void visit( TerminatingLocation tl )
    {
        locations.add( tl );

        scheduleConsumer.accept( new Schedule( basicSchedule, basicScheduleExtras, locations ) );
    }

}
