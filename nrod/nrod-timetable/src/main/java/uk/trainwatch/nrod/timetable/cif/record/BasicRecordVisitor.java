/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.TimeUtils;

/**
 * Default {@link RecordVisitor} which has the ability to check the CIF file date and optionally send some updates to a
 * consumer.
 * <p>
 * @author Peter T Mount
 */
public class BasicRecordVisitor
        implements RecordVisitor
{

    protected final Logger log = Logger.getLogger( getClass().
            getName() );

    private final Consumer<Header> headerConsumer;
    private final Consumer<TIPLOCAction> tiplocConsumer;
    private final Consumer<Association> assocConsumer;
    private final Consumer<TrailerRecord> trailerConsumer;
    private LocalDateTime startTime;

    /**
     *
     * @param headerConsumer      consume {@link Header}'s or null
     * @param tiplocConsumer      consume {@link TIPLOCAction}'s or null
     * @param assocConsumer       consume {@link Association}'s or null
     * @param trailerConsumer     consume {@link TrailerRecord}'s or null
     * @param lastExtractDateTime timestamp of last extract or null
     */
    public BasicRecordVisitor( Consumer<Header> headerConsumer,
                               Consumer<TIPLOCAction> tiplocConsumer,
                               Consumer<Association> assocConsumer,
                               Consumer<TrailerRecord> trailerConsumer,
                               LocalDateTime lastExtractDateTime )
    {
        // The header consumer, log details -> check lastFileDate -> user defined consumers
        // note lastFileDate is only there if it's not null
        Consumer<Header> hc = headerConsumer;
        if( lastExtractDateTime != null )
        {
            // we have lastFileDate so prefix with a check to ensure the extract is more recent
            hc = Consumers.andThen(
                    h ->
                    {
                        LocalDateTime extractDateTime = h.getLocalDateTimeOfExtract();

                        if( !lastExtractDateTime.isBefore( extractDateTime ) )
                        {
                            throw new IllegalStateException(
                                    "Failed extract as it's not more recent than previous one."
                                    + " Last extract was at " + lastExtractDateTime
                                    + " this extract was at " + extractDateTime
                            );
                        }
                    },
                    hc
            );
        }
        // Prefix with the logging of this CIF files details
        this.headerConsumer = Consumers.andThen(
                // This is our start
                h -> startTime = TimeUtils.getLocalDateTime(),
                // CIF details
                h -> log.log( Level.INFO,
                              () -> "Processing CIF FILE " + h.getCurrentFileRef()
                                    + " extracted " + h.getLocalDateTimeOfExtract() ),
                // Period this CIF file covers
                h -> log.log( Level.INFO, () -> "Schedules run from " + h.getStartDate()
                                                + " until " + h.getEndDate() ),
                // Chain to the rest
                hc
        );

        // Log the parse duration then include any supplied consumers
        this.trailerConsumer = Consumers.andThen(
                t ->
                {
                    LocalDateTime end = LocalDateTime.now();
                    Duration duration = Duration.between( startTime, end );

                    log.log( Level.INFO, () -> "Completed parsing of CIF file in " + duration );

                },
                trailerConsumer
        );

        this.tiplocConsumer = Consumers.ensureNotNull( tiplocConsumer );
        this.assocConsumer = Consumers.ensureNotNull( assocConsumer );
    }

    @Override
    public void visit( Header h )
    {
        headerConsumer.accept( h );
    }

    @Override
    public void visit( TrailerRecord t )
    {
        trailerConsumer.accept( t );
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

    @Override
    public void visit( Association a )
    {
        assocConsumer.accept( a );
    }

}
