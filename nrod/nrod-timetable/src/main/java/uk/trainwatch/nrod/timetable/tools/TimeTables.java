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
package uk.trainwatch.nrod.timetable.tools;

import uk.trainwatch.nrod.timetable.sql.ScheduleDBUpdate;
import uk.trainwatch.nrod.timetable.sql.TiplocDBUpdate;
import uk.trainwatch.nrod.timetable.sql.ScheduleLocUpdate;
import uk.trainwatch.nrod.timetable.sql.AssociationDBUpdate;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nrod.timetable.cif.record.BasicRecordVisitor;
import uk.trainwatch.nrod.timetable.cif.record.CIFParser;
import uk.trainwatch.nrod.timetable.cif.record.Header;
import uk.trainwatch.nrod.timetable.cif.record.Record;
import uk.trainwatch.nrod.timetable.cif.record.TrailerRecord;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.timetable.model.ScheduleBuilderVisitor;
import uk.trainwatch.nrod.timetable.util.ATOCCode;
import uk.trainwatch.nrod.timetable.util.ATSCode;
import uk.trainwatch.nrod.timetable.util.Activity;
import uk.trainwatch.nrod.timetable.util.AssociationCategory;
import uk.trainwatch.nrod.timetable.util.AssociationDateIndicator;
import uk.trainwatch.nrod.timetable.util.AssociationType;
import uk.trainwatch.nrod.timetable.util.BankHolidayRunning;
import uk.trainwatch.nrod.timetable.util.BusSec;
import uk.trainwatch.nrod.timetable.util.Catering;
import uk.trainwatch.nrod.timetable.util.OperatingCharacteristics;
import uk.trainwatch.nrod.timetable.util.PowerType;
import uk.trainwatch.nrod.timetable.util.Reservations;
import uk.trainwatch.nrod.timetable.util.STPIndicator;
import uk.trainwatch.nrod.timetable.util.ServiceBranding;
import uk.trainwatch.nrod.timetable.util.Sleepers;
import uk.trainwatch.nrod.timetable.util.TimingLoad;
import uk.trainwatch.nrod.timetable.util.TrainCategory;
import uk.trainwatch.nrod.timetable.util.TrainClass;
import uk.trainwatch.nrod.timetable.util.TrainStatus;
import uk.trainwatch.util.Consumers;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.UncheckedSQLException;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@MetaInfServices( Utility.class )
public class TimeTables
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( TimeTables.class.getName() );
    private static final String SCHEMA = "timetable";
    private List<Path> cifFiles;

    private boolean fullImport;

    private boolean includeAssociations;
    private boolean includeTiploc;
    private boolean includeSchedules;

    public TimeTables()
    {
        super();
        getOptions().
                addOption( null, "full", false, "Full rather than Incremental import" ).
                addOption( "a", "associations", false, "Include train association updates" ).
                addOption( "t", "tiploc", false, "Include tiploc updates" ).
                addOption( "s", "schedule", false, "Include schedule updates" );
    }

    @Override
    @SuppressWarnings( "ThrowableInstanceNeverThrown" )
    public boolean parseArgs( CommandLine cmd )
    {
        super.parseArgs( cmd );

        fullImport = cmd.hasOption( "full" );

        includeAssociations = cmd.hasOption( "associations" );
        includeTiploc = cmd.hasOption( "tiploc" );
        includeSchedules = cmd.hasOption( "schedule" );

        // Default to both
        if( !includeAssociations && !includeSchedules && !includeTiploc )
        {
            includeAssociations = true;
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
        try( Connection con = getConnection() )
        {
            con.setAutoCommit( false );
            try
            {
                if( fullImport )
                {
                    fullImport( con );
                }

                cifFiles.forEach( f -> parseFile( con, f ) );

                // Now this may take a while ;-)
                LOG.log( Level.INFO, () -> "Committing to database" );
                con.commit();
                LOG.log( Level.INFO, () -> "Commit complete. Timetable is now live." );
            }
            catch( UncheckedIOException |
                   SQLException |
                   UncheckedSQLException ex )
            {
                LOG.log( Level.SEVERE, ex, () -> "Commit failed: " + ex.getMessage() );

                LOG.log( Level.INFO, () -> "Rolling back transaction" );
                con.rollback();
            }
        }
    }

    /**
     * Prepare the database for a full import.
     * <p>
     * This consists of deleting all data from the database and initialising the enum tables
     * <p>
     * @throws InterruptedException
     * @throws SQLException
     */
    private void fullImport( Connection con )
            throws SQLException
    {
        LOG.log( Level.INFO, "Clearing down Schedule database" );

        SQL.deleteTable( con, SCHEMA, "schedule_loc" );

        // Clear down our existing tables
        Arrays.asList(
                "schedule",
                "association",
                "tiploc",
                "trainuid",
                "lastupdate" ).
                forEach( t -> SQL.deleteIdTable( con, SCHEMA, t ) );

        // Initialise our enum tables
        Arrays.asList(
                ATOCCode.class,
                ATSCode.class,
                Activity.class,
                AssociationCategory.class,
                AssociationDateIndicator.class,
                AssociationType.class,
                BankHolidayRunning.class,
                BusSec.class,
                Catering.class,
                OperatingCharacteristics.class,
                PowerType.class,
                Reservations.class,
                STPIndicator.class,
                ServiceBranding.class,
                Sleepers.class,
                TimingLoad.class,
                TrainCategory.class,
                TrainClass.class,
                TrainStatus.class ).
                forEach( c -> SQL.updateEnumTable( con, SCHEMA, c ) );

        prepareDaysRun( con );

        LOG.log( Level.INFO, "Schedule database is now clean" );
    }

    /**
     * Prepares the daysrun table which maps days of week to a single id
     * <p>
     * @param con <p>
     * @throws SQLException
     */
    private void prepareDaysRun( Connection con )
            throws SQLException
    {
        String table = "daysrun";

        LOG.log( Level.INFO, () -> "Reinitialising " + table );

        SQL.deleteTable( con, SCHEMA, table );

        try( PreparedStatement s = con.prepareStatement(
                "INSERT INTO timetable." + table
                + " (id,monday,tuesday,wednesday,thursday,friday,saturday,sunday)"
                + " VALUES( ?,?,?,?,?,?,?,?)" ) )
        {
            for( int i = 0; i < 0x80; i++ )
            {
                s.setInt( 1, i );
                s.setBoolean( 2, (i & 0x01) != 0 );
                s.setBoolean( 3, (i & 0x02) != 0 );
                s.setBoolean( 4, (i & 0x04) != 0 );
                s.setBoolean( 5, (i & 0x08) != 0 );
                s.setBoolean( 6, (i & 0x10) != 0 );
                s.setBoolean( 7, (i & 0x20) != 0 );
                s.setBoolean( 8, (i & 0x40) != 0 );
                s.executeUpdate();
            }
        }

    }

    private void parseFile( Connection con, Path cifFile )
    {
        Objects.requireNonNull( cifFile, "No CIF file provided" );

        try
        {
            // Do the import in one massive transaction
            con.setAutoCommit( false );

            // Strict mode so we fail on an invalid record type
            final CIFParser parser = new CIFParser( true );

            // When did we last run an update
            LocalDateTime lastUpdate = null;
            try( Statement s = con.createStatement() )
            {
                try( ResultSet rs = s.executeQuery(
                        "SELECT extracted,imported FROM timetable.lastupdate ORDER BY extracted DESC LIMIT 1" ) )
                {
                    if( rs != null && rs.next() )
                    {
                        lastUpdate = rs.getTimestamp( 1 ).
                                toLocalDateTime();
                        LocalDateTime imported = rs.getTimestamp( 2 ).
                                toLocalDateTime();
                        LOG.log( Level.INFO, "Last file extracted {0}, imported {1}", new Object[]
                         {
                             lastUpdate, imported
                        } );
                    }
                }
            }

            // Record when we did the update
            Consumer<Header> header = h ->
            {
                try( PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO timetable.lastupdate (extracted,imported,filename) VALUES (?,?,?)" ) )
                {
                    ps.setTimestamp( 1, Timestamp.valueOf( h.getLocalDateTimeOfExtract() ) );
                    ps.setTimestamp( 2, Timestamp.valueOf( TimeUtils.getLocalDateTime() ) );
                    ps.setString( 3, h.getCurrentFileRef() );
                    ps.executeUpdate();
                }
                catch( SQLException ex )
                {
                    throw new UncheckedSQLException( ex );
                }
            };

            // The persistance consumers
            ScheduleDBUpdate schedules = Consumers.createIf( includeSchedules, () -> new ScheduleDBUpdate( con ) );
            ScheduleLocUpdate scheduleLocations = Consumers.createIf( includeSchedules,
                                                                      () -> new ScheduleLocUpdate( con ) );
            Consumer<Schedule> scheduleConsumer = Consumers.createIf( includeSchedules,
                                                                      () -> Consumers.andThen( schedules,
                                                                                               scheduleLocations ) );

            AssociationDBUpdate associations = Consumers.createIf( includeAssociations,
                                                                   () -> new AssociationDBUpdate( con ) );

            TiplocDBUpdate tiplocs = Consumers.createIf( includeTiploc, () -> new TiplocDBUpdate( con ) );

            // Now what to do at the end of the import
            Consumer<TrailerRecord> trailer = t -> LOG.log( Level.INFO,
                                                            () -> "Processed " + parser.lineCount() + " records." );

            if( includeTiploc )
            {
                trailer = trailer.andThen( t -> LOG.log( Level.INFO,
                                                         () -> "Tiplocs " + tiplocs ) );
            }

            if( includeAssociations )
            {
                trailer = trailer.andThen( t -> LOG.log( Level.INFO,
                                                         () -> "Associations " + associations ) );
            }

            if( includeSchedules )
            {
                trailer = trailer.andThen( t -> LOG.log( Level.INFO,
                                                         () -> "Schedules " + schedules + ", locations " + scheduleLocations ) );
            }

            // Pick the type of builder - if not forming schedules or associations then there's no need to use
            // the more expensive visitor
            BasicRecordVisitor builder;
            if( includeSchedules )
            {
                builder = new ScheduleBuilderVisitor(
                        header,
                        tiplocs,
                        associations,
                        scheduleConsumer,
                        trailer,
                        lastUpdate );
            }
            else
            {
                // No schedules so no point in building Schedule objects
                builder = new BasicRecordVisitor(
                        header,
                        tiplocs,
                        associations,
                        trailer,
                        lastUpdate );
            }

            // Progress counter for every 50k records
            Consumer<? super Record> recCount = Consumers.ifThen(
                    l -> (parser.lineCount() % 50000) == 0,
                    l -> LOG.log( Level.INFO, () -> "read " + parser.lineCount() + " records." )
            );

            Files.lines( cifFile ).
                    map( parser::parse ).
                    filter( Objects::nonNull ).
                    peek( recCount ).
                    forEach( r -> r.accept( builder ) );

        }
        catch( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
        catch( SQLException ex )
        {
            throw new UncheckedSQLException( ex );
        }

    }

}
