/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TSLocation;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public class ForecastRecorder
        implements Consumer<Pport>
{

    private static final Logger log = Logger.getLogger( ForecastRecorder.class.getName() );

    private final DataSource dataSource;

    public ForecastRecorder( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    @Override
    public void accept( Pport t )
    {
        t.getUR().getTS().forEach(
                ts -> {
                    // NB: INFO only if debugging as averages 20 messages per second!
                    log.log( Level.FINE, () -> "Received TS " + ts.getRid() );

                    try( Connection con = dataSource.getConnection() ) {
                        try {
                            con.setAutoCommit( false );

                            mergeForecast( con, t, ts );
                            recordForecast( con, t, ts );

                            con.commit();
                        }
                        catch( SQLException ex ) {
                            log.log( Level.SEVERE, null, ex );
                            con.rollback();
                        }
                    }
                    catch( SQLException ex ) {
                        log.log( Level.SEVERE, null, ex );
                    }
                }
        );
    }

    private void mergeForecast( Connection con, Pport p, TS t )
            throws SQLException
    {
        Optional<Pport> existing;
        try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.forecast WHERE rid=?", t.getRid() ) ) {
            existing = SQL.stream( ps, SQL.STRING_LOOKUP ).
                    map( DarwinJaxbContext.fromXML ).
                    findAny();
        }

        if( existing.isPresent() ) {
            List<TSLocation> list = t.getLocation();

            // Set of new tiplocs
            Set<String> tpl0 = list.stream().
                    map( TSLocation::getTpl ).
                    collect( Collectors.toSet() );

            // Merge existing tiploc entries
            existing.get().getUR().getTS().
                    stream().
                    flatMap( t0 -> t0.getLocation().stream() ).
                    filter( t0 -> !tpl0.contains( t0.getTpl() ) ).
                    forEach( list::add );
        }
    }

    private void recordForecast( Connection con, Pport p, TS t )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.forecast(?,?,?,?,?,?,?)" ) ) {
            SQL.executeQuery( ps,
                              t.getRid(),
                              t.getUid(),
                              t.getSsd().toString(),
                              true,
                              false,
                              DarwinJaxbContext.toXML.apply( p ),
                              // CSV list of tpl
                              t.getLocation().stream().map( TSLocation::getTpl ).collect( Collectors.joining( "," ) )
            );
        }
    }

}
