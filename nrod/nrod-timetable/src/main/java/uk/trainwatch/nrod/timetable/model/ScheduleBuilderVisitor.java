/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.nrod.timetable.cif.record.BasicRecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.BasicSchedule;
import uk.trainwatch.nrod.timetable.cif.record.BasicScheduleExtras;
import uk.trainwatch.nrod.timetable.cif.record.ChangesEnRoute;
import uk.trainwatch.nrod.timetable.cif.record.Header;
import uk.trainwatch.nrod.timetable.cif.record.IntermediateLocation;
import uk.trainwatch.nrod.timetable.cif.record.Location;
import uk.trainwatch.nrod.timetable.cif.record.OriginLocation;
import uk.trainwatch.nrod.timetable.cif.record.RecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.TIPLOCAction;
import uk.trainwatch.nrod.timetable.cif.record.TerminatingLocation;
import uk.trainwatch.nrod.timetable.cif.record.TrailerRecord;
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

    /**
     *
     * @param headerConsumer   consume {@link Header}'s or null
     * @param tiplocConsumer   consume {@link TIPLOCAction}'s or null
     * @param assocConsumer    consume {@link Association}'s or null
     * @param scheduleConsumer consume built {@link Schedule}'s or null
     * @param trailerConsumer  consume {@link TrailerRecord}'s or null
     * @param lastFileDate     timestamp of last extract or null
     */
    public ScheduleBuilderVisitor( Consumer<Header> headerConsumer,
                                   Consumer<TIPLOCAction> tiplocConsumer,
                                   Consumer<Association> assocConsumer,
                                   Consumer<Schedule> scheduleConsumer,
                                   Consumer<TrailerRecord> trailerConsumer,
                                   LocalDateTime lastFileDate )
    {
        super( headerConsumer, tiplocConsumer, assocConsumer, trailerConsumer, lastFileDate );
        this.scheduleConsumer = Consumers.ensureNotNull( scheduleConsumer );
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
