/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.Consumers;

/**
 * Default {@link RecordVisitor} which has the ability to check the CIF file date and optionally send some updates to a consumer.
 * <p>
 * @author Peter T Mount
 */
public class BasicRecordVisitor
        implements RecordVisitor
{

    protected final Logger log = Logger.getLogger( getClass().
            getName() );

    private final Consumer<TIPLOCAction> tiplocConsumer;
    private final LocalDate lastFileDate;
    private LocalDateTime start;

    public BasicRecordVisitor()
    {
        this( Consumers.sink(), null );
    }

    public BasicRecordVisitor( LocalDate lastFileDate )
    {
        this( Consumers.sink(), lastFileDate );
    }

    public BasicRecordVisitor( Consumer<TIPLOCAction> tiplocConsumer, LocalDate lastFileDate )
    {
        this.tiplocConsumer = Consumers.ensureNotNull( tiplocConsumer );
        this.lastFileDate = lastFileDate;
    }

    @Override
    public void visit( Header h )
    {
        start = LocalDateTime.now();

        log.log( Level.INFO,
                 () -> "Processing CIF FILE " + h.getCurrentFileRef() + " extracted " + h.getDateOfExtract()
                 + " Schedules run from " + h.getStartDate() + " until " + h.getEndDate() );

        // Check to see if the file is more recent than a previous run
        if( lastFileDate != null ) {
            if( !h.getDateOfExtract().
                    isAfter( lastFileDate ) ) {
                throw new IllegalStateException(
                        "Failed extract as it's not more recent than previous one. Current " + lastFileDate + " this file " + h.
                        getDateOfExtract() );
            }
        }
    }

    @Override
    public void visit( TrailerRecord t )
    {
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between( start, end );

        log.log( Level.INFO, () -> "Completed parsing of CIF file in " + duration );
    }

    @Override
    public void visit( TIPLOCInsert i )
    {
        tiplocConsumer.accept( i );
    }

    @Override
    public void visit( TIPLOCAmend a )
    {
        tiplocConsumer.accept( a );
    }

    @Override
    public void visit( TIPLOCDelete d )
    {
        tiplocConsumer.accept( d );
    }
}
