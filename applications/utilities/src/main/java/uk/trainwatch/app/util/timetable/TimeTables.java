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
package uk.trainwatch.app.util.timetable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import uk.trainwatch.app.util.DBUtility;
import uk.trainwatch.nrod.timetable.cif.record.Association;
import uk.trainwatch.nrod.timetable.cif.record.BasicRecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.CIFParser;
import uk.trainwatch.nrod.timetable.cif.record.RecordVisitor;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.model.ScheduleBuilderVisitor;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.counter.CounterConsumer;

/**
 *
 * @author peter
 */
public class TimeTables
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( TimeTables.class.getName() );

    private List<Path> cifFiles;

    private boolean fullImport;

    private boolean includeTiploc;
    private boolean includeSchedules;

    public TimeTables()
    {
        super();
        getOptions().
                addOption( null, "full", false, "Full rather than Incremental import" ).
                addOption( "t", "tiploc", false, "Include tiploc updates" ).
                addOption( "s", "schedule", false, "Include schedule updates" );
    }

    @Override
    @SuppressWarnings( "ThrowableInstanceNeverThrown" )
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        fullImport = cmd.hasOption( "full" );

        includeTiploc = cmd.hasOption( "tiploc" );
        includeSchedules = cmd.hasOption( "schedule" );

        // Default to both
        if( !includeSchedules && !includeTiploc )
        {
            includeSchedules = true;
            includeTiploc = true;
        }

        // As commons-cli is pre-generics we have to do this first
        Collection<String> args = cmd.getArgList();

        // The first one will be the CIF name
        cifFiles = args.stream().
                map( File::new ).
                filter( File::exists ).
                filter( File::canRead ).
                map( File::toPath ).
                collect( Collectors.toList() );

        return !cifFiles.isEmpty();
    }

    @Override
    public void runUtility()
            throws Exception
    {
        if( fullImport )
        {
            fullImport();
        }

        cifFiles.forEach( this::parseFile );
    }

    @SuppressWarnings( "SleepWhileInLoop" )
    private void fullImport()
            throws InterruptedException,
                   SQLException
    {
        LOG.log( Level.SEVERE,
                 () -> "WARNING: This will perform a full import, wiping out anything currently in the database" );

        LOG.log( Level.SEVERE,
                 () -> "You have 10 seconds to abort this before proceding!" );

        for( int i = 10; i > 0; i-- )
        {
            LOG.log( Level.SEVERE, "{0} seconds remaining", i );
            Thread.sleep( 1000L );
        }

        LOG.log( Level.INFO, "Clearing down Schedule database" );

        try( Connection con = getConnection(); Statement s = con.createStatement() )
        {

            LOG.log( Level.INFO, "Deleting train associations" );
            s.execute( "DELETE FROM timetable.association" );

            LOG.log( Level.INFO, "Deleting tiplocs" );
            s.execute( "DELETE FROM timetable.tiploc" );
        }

        LOG.log( Level.INFO, "Schedule database is now clean" );
    }

    private void parseFile( Path cifFile )
    {
        Objects.requireNonNull( cifFile, "No CIF file provided" );

        try( Connection con = getConnection() )
        {
            // Strict mode so we fail on an invalid record type
            final CIFParser parser = new CIFParser( true );

            // Build schedules
            CounterConsumer<Schedule> scheduleCounter = Consumers.createIf( includeSchedules,
                                                                            () -> new CounterConsumer<>() );
            AssociationDBUpdate associations = Consumers.createIf( includeSchedules,
                                                                   () -> new AssociationDBUpdate( con ) );
            TiplocDBUpdate tiplocs = Consumers.createIf( includeTiploc, () -> new TiplocDBUpdate( con ) );

            // Pick the type of builder - if not forming schedules or associations then there's no need
            // to use the more expensive visitor
            final RecordVisitor builder = new ScheduleBuilderVisitor( scheduleCounter, associations, tiplocs, null );

            // Stream from the file
            LOG.log( Level.INFO, () -> "Parsing " + cifFile );

            Files.lines( cifFile ).
                    map( parser::parse ).
                    filter( Objects::nonNull ).
                    peek( Consumers.ifThen( l -> (parser.lineCount() % 10000) == 0,
                                            l -> LOG.log( Level.INFO, () -> "read " + parser.lineCount() + " records." )
                            ) ).
                    forEach( r -> r.accept( builder ) );

            LOG.log( Level.INFO, () -> "Processed " + parser.lineCount() + " records." );

            if( includeTiploc )
            {
                tiplocs.log();
            }

            if( includeSchedules )
            {
                LOG.log( Level.INFO, () -> "Associations " + associations.toString() );
                LOG.log( Level.INFO, () -> "Processed " + scheduleCounter.get() + " schedules, " );
            }
        }
        catch( IOException |
               SQLException ex )
        {
            LOG.log( Level.SEVERE, ex, () -> "While parsing " + cifFile );
        }

    }

}
