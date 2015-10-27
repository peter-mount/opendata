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
package uk.trainwatch.nrod.feed.trust;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nrod.trust.model.ChangeOfOrigin;
import uk.trainwatch.nrod.trust.model.TrainActivation;
import uk.trainwatch.nrod.trust.model.TrainCancellation;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.nrod.trust.model.TrainReinstatement;
import uk.trainwatch.nrod.trust.model.TrustMovement;
import uk.trainwatch.nrod.trust.model.TrustMovementFactory;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * Invokes the report.trust() function in the database against all trust messages
 * <p>
 * @author Peter T Mount
 */
@ApplicationScoped
public class TrustLogger
        implements SQLConsumer<String>
{

    /**
     * Max delay to accept, here 1 day is excessive, although not the actual max delay ever on the rail network - that
     * was 2 weeks!
     */
    private static final int MAX_DELAY = 86400;

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Override
    public void accept( final String json )
            throws SQLException
    {
        TrustMovement t = TrustMovementFactory.INSTANCE.apply( JsonUtils.parseJsonObject.apply( json ) );
        if( t == null ) {
            return;
        }

        try( Connection con = dataSource.getConnection() ) {
            final String trainId = t.getTrain_id();
            final int trainClass = Integer.parseInt( trainId.substring( 2, 3 ) );

            Date date;
            String trainUid = null;
            long stanox = 0;
            int operatorId = 0;
            int delay = 0;

            switch( t.getMsg_type() ) {
                case ACTIVATION:
                    TrainActivation act = (TrainActivation) t;
                    date = new Date( act.getOrigin_dep_timestamp() );
                    trainUid = act.getTrain_uid();
                    break;

                case CANCELLATION:
                    TrainCancellation can = (TrainCancellation) t;
                    // FIXME not certain if this is correct
                    date = new Date( System.currentTimeMillis() );
                    break;

                case MOVEMENT:
                    TrainMovement mvt = (TrainMovement) t;
                    date = new Date( mvt.getTimestamp() );
                    stanox = mvt.getLoc_stanox();
                    // Fix bug where nrod sends stanox twice in the field
                    while( stanox >= 100000 ) {
                        stanox -= 100000;
                    }
                    operatorId = mvt.getToc_id();

                    delay = (int) mvt.getDelay();

                    // Force delay into reasonable ranges, ignoring stupid values
                    if( delay > MAX_DELAY || delay < -MAX_DELAY ) {
                        delay = 0;
                    }

                    break;

                case REINSTATEMENT:
                    TrainReinstatement rei = (TrainReinstatement) t;
                    date = new Date( rei.getReinstatement_timestamp() );
                    break;

                case CHANGE_OF_ORIGIN:
                    ChangeOfOrigin coo = (ChangeOfOrigin) t;
                    date = new Date( coo.getCoo_timestamp() );
                    break;

                case CHANGE_OF_IDENTITY:
                // Currently ignored, we don't track freight trains
                default:
                    return;
            }

            if( date == null ) {
                return;
            }

            try( PreparedStatement s = con.prepareStatement( "SELECT report.trust(?, ?, ?, ?, ?, ?, ?, ?, ?)" ) ) {
                int i = 1;

                s.setDate( i++, date );

                if( trainUid == null ) {
                    s.setNull( i++, Types.VARCHAR );
                }
                else {
                    s.setString( i++, trainUid );
                }

                s.setString( i++, trainId );
                s.setInt( i++, trainClass );
                s.setInt( i++, t.getMsg_type().getType() );
                s.setLong( i++, stanox );
                s.setInt( i++, operatorId );
                s.setInt( i++, delay );
                s.setString( i++, json );
                s.executeQuery();
            }
        }
    }

}
