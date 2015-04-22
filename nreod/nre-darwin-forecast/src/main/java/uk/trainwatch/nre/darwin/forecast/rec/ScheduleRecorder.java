/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast.rec;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Schedule;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;

/**
 *
 * @author peter
 */
public class ScheduleRecorder
        extends AbstractRecorder<Schedule>
{

    public ScheduleRecorder( DataSource dataSource )
    {
        super( dataSource );
    }

    @Override
    public void accept( Pport t )
    {
        accept( t, t.getUR().getSchedule() );
    }

    @Override
    protected void apply( Pport t, Schedule schedule, Connection con )
            throws SQLException
    {
        String rid = schedule.getRid();

        Pport dbPport = t.cloneMetaIfNull( getForecast( con, rid ) );

        dbPport.getUR().getSchedule().add( schedule );

        recordForecast( con, dbPport, schedule );
    }

}
