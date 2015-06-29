/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast.rec;

import java.sql.Connection;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.nre.darwin.model.ppt.schedules.DeactivatedSchedule;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class DeactivationRecorder
        extends AbstractRecorder<DeactivatedSchedule>
{

    @Override
    public void accept( Pport t )
    {
        accept( t, t.getUR().getDeactivated() );
    }

    @Override
    protected void apply( Pport t, DeactivatedSchedule ts, Connection con )
            throws SQLException
    {
        String rid = ts.getRid();

        Pport dbPport = t.cloneMetaIfNull( getForecast( con, rid ) );
        replace( dbPport.getUR().getDeactivated(), ts );

        recordForecast( con, dbPport );
    }

}
