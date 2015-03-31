/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.timetable.ScheduleSQL;

/**
 *
 * @author peter
 */
public class TimetableResolver
        implements Consumer<JsonObject>
{

    private static final Logger LOG = Logger.getLogger( TimetableResolver.class.getName() );

    @Override
    public void accept( JsonObject t )
    {
        if( t.getInt( "toc_id" ) != 80 ) {
            return;
        }

        try {
            String trainUid = t.getString( "train_uid" );
            LocalDate date = TimeUtils.getLocalDate( JsonUtils.getLong( t, "time" ) );
            Trust trust = TrustCache.INSTANCE.getTrustIfPresent( t.getInt( "toc_id" ), t.getString( "train_id" ) );

            // use ifPresent so if queue has old data we do nothing with it. Also no point if we have already resolved it
            if( trust == null || Trust.isScheduled( trust ) || trust.isScheduleFail() ) {
                LOG.log( Level.INFO, () -> "Ignoring activation for " + trainUid + " on " + date + " no entry in cache, stale?" );
                return;
            }

            LOG.log( Level.INFO, () -> "Resolving schedule for " + trainUid + " on " + date );

            trust.setSchedule( ScheduleSQL.getSchedule( date, trainUid ) );

            if( !Trust.isScheduled( trust ) ) {
                LOG.log( Level.INFO, () -> "Unable to retrieve schedule for " + trainUid + " on " + date );
            }
        }
        catch( SQLException ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }

    }

}
