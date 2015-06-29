/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast.rec;

import java.sql.Connection;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.nre.darwin.model.ppt.schedules.AssocService;
import uk.trainwatch.nre.darwin.model.ppt.schedules.Association;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class AssociationRecorder
        extends AbstractRecorder<Association>
{

    @Override
    public void accept( Pport t )
    {
        accept( t, t.getUR().getAssociation() );
    }

    @Override
    protected void apply( Pport t, Association ts, Connection con )
            throws SQLException
    {
        // Add the association to both main and assoc trains
        apply( t, ts, ts.getMain(), con );
        apply( t, ts, ts.getAssoc(), con );
    }

    protected void apply( Pport t, Association ts, AssocService assoc, Connection con )
            throws SQLException
    {
        String rid = assoc.getRid();

        Pport dbPport = t.cloneMetaIfNull( getForecast( con, rid ) );
        dbPport.getUR().getAssociation().add( ts );

        recordForecast( con, dbPport );
    }

}
