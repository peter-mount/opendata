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
package uk.trainwatch.nrod.trust.perf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import uk.trainwatch.io.report.ReportBuilder;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementJsonVisitor;
import uk.trainwatch.util.DaemonThreadFactory;

/**
 * Monitor's everything about a train
 * <p>
 * @author Peter T Mount
 */
public class TrainReporter
        implements Consumer<TrustMovement>
{

    private static final Logger LOG = Logger.getLogger( TrainReporter.class.getName() );

    /**
     * Expire items from memory when last movement was 3 hours ago
     */
    private static final long EXPIRY_TIME = 3L * 3600L * 1000L;
    /**
     * Delay between receiving data and reporting it - to keep load down
     */
    private static final long RECORD_DELAY = 10L * 60L * 1000L;

    private final Map<Integer, ConcurrentHashMap<String, Train>> trains = new ConcurrentHashMap<>();

    private final Consumer<? super JsonStructure> reportingConsumer;

    public TrainReporter( Consumer<? super JsonStructure> reportingConsumer )
    {
        this.reportingConsumer = reportingConsumer;

        DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( this::backgroundProcessing, 1L, 1L, TimeUnit.MINUTES );
    }

    @Override
    public synchronized void accept( TrustMovement t )
    {
        trains.computeIfAbsent( t.getToc_id(), toc -> new ConcurrentHashMap<>() ).
                computeIfAbsent( t.getTrain_id(), tid -> new Train( tid, t.getToc_id() ) ).
                add( t );
    }

    private synchronized void backgroundProcessing()
    {
        LOG.log( Level.INFO, "Starting background processing run" );
        AtomicInteger before = new AtomicInteger();
        AtomicInteger after = new AtomicInteger();
        AtomicInteger reports = new AtomicInteger();

        try
        {
            trains.forEach( (toc, trains) ->
            {
                before.addAndGet( trains.size() );

                // Expire any old entries
                trains.values().
                        removeIf( t -> t.isExpired() );

                after.addAndGet( trains.size() );

                trains.forEach( (tid, t) ->
                {
                    if( t.isReportable() )
                    {
                        reports.incrementAndGet();
                        report( tid, t );
                    }
                }
                );

            }
            );
        }
        catch( Throwable t )
        {
            LOG.log( Level.SEVERE, "Failed background processing", t );
        }

        LOG.log( Level.INFO,
                 () -> "After run, before " + before.get()
                       + ", after expires " + after.get()
                       + ", expired " + (before.get() - after.get())
                       + " reports generated " + reports.get() );
    }

    private void report( String trainId, Train train )
    {
        LOG.log( Level.FINE, () -> "Generating report for " + trainId + " toc " + train.getTocId() );

        LocalDateTime today = LocalDateTime.now();

        JsonArrayBuilder movements = Json.createArrayBuilder();
        TrustMovementJsonVisitor visitor = new TrustMovementJsonVisitor();
        train.forEach( movement ->
        {
            movement.accept( visitor );
            
            // Null check incase the visitor didn't do the conversion
            JsonObjectBuilder b = visitor.getJsonObjectBuilder();
            if( b != null )
            {
                movements.add( b );
            }
        } );

        reportingConsumer.accept( new ReportBuilder().
                setReportId( "TrustTrainMovement" ).
                setTitle( "Train movement for " + trainId ).
                setRecordPath( "train/%d/%d/%d/%d/%s",
                               today.getYear(),
                               today.getMonth().
                               getValue(),
                               today.getDayOfMonth(),
                               train.getTocId(),
                               trainId
                ).
                add( "movements", movements ).
                build() );
    }

    private static class Train
    {

        private final String trainId;
        private final int tocId;
        private final List<TrustMovement> movements = new ArrayList<>();
        private long lastUpdate;
        private long lastReport;

        public Train( String trainId, int tocId )
        {
            this.trainId = trainId;
            this.tocId = tocId;
        }

        public long getLastUpdate()
        {
            return lastUpdate;
        }

        public boolean isExpired()
        {
            return (System.currentTimeMillis() - lastUpdate) > EXPIRY_TIME;
        }

        public boolean isReportable()
        {
            boolean rep = lastReport != 0L && (System.currentTimeMillis() - lastReport) > RECORD_DELAY;
            if( rep )
            {
                lastReport = 0L;
            }
            return rep;
        }

        public String getTrainId()
        {
            return trainId;
        }

        public int getTocId()
        {
            return tocId;
        }

        public boolean add( TrustMovement e )
        {
            lastUpdate = System.currentTimeMillis();
            if( lastReport == 0L )
            {
                lastReport = lastUpdate;
            }
            return movements.add( e );
        }

        public Stream<TrustMovement> stream()
        {
            return movements.stream();
        }

        public void forEach( Consumer<? super TrustMovement> action )
        {
            movements.forEach( action );
        }

        @Override
        public int hashCode()
        {
            return 29 * Objects.hashCode( this.trainId );
        }

        @Override
        public boolean equals( Object obj )
        {
            if( obj == null || getClass() != obj.getClass() )
            {
                return false;
            }
            final Train other = (Train) obj;
            return Objects.equals( this.trainId, other.trainId );
        }

    }
}
