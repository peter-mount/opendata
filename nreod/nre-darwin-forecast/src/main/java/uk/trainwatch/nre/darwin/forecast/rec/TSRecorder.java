/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast.rec;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.nre.darwin.forecast.TSLocationComparator;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSLocation;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * Handles the recording of a TD record, merging it as necessary
 *
 * @author peter
 */
@ApplicationScoped
public class TSRecorder
        extends AbstractRecorder<TS>
{

    @Override
    public void accept( Pport t )
    {
        accept( t, t.getUR().getTS() );
    }

    @Override
    protected void apply( Pport t, TS ts, Connection con )
            throws SQLException
    {
        String rid = ts.getRid();

        Pport dbPport = t.cloneMetaIfNull( getForecast( con, rid ) );

        List<TS> existing = dbPport.getUR().getTS();

        if( !existing.isEmpty() ) {
            // Merge any existing TSLocation's not in the new one into it
            List<TSLocation> existingTpl = existing.get( 0 ).getLocation();
            List<TSLocation> list = ts.getLocation();

            // Set of new tiplocs by name
            Set<String> set = list.stream().
                    map( TSLocation::getTpl ).
                    collect( Collectors.toSet() );

            // Add existing entries that are not in the new list into the new list
            existingTpl.stream().
                    filter( tl -> !set.contains( tl.getTpl() ) ).
                    forEach( list::add );

            // Not necessary but useful to sort it now, saves doing it multiple times later when processing
            list.sort( TSLocationComparator.INSTANCE );

        }

        // Replace the existing TS element with the new one. This means the new meta-data is now in use
        replace( existing, ts );
        
        recordForecast( con, dbPport, ts );
    }

}
