/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.forecast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.forecasts.TS;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public enum ForecastManager
{

    INSTANCE;

    private final Logger log = Logger.getLogger( ForecastManager.class.getName() );

    private DataSource dataSource;

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    /**
     * Returns a copy of the rid's at a specific location
     * <p>
     * @param tpl
     *            <p>
     * @return
     */
    public Collection<String> getRids( String tpl )
    {
        if( tpl != null && !tpl.isEmpty() ) {
            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT f.rid FROM darwin.forecast f"
                                                              + " INNER JOIN darwin.forecast_entry fe ON f.id=fe.fid"
                                                              + " INNER JOIN darwin.tiploc t ON fe.tpl=t.id"
                                                              + " WHERE t.tpl=?",
                                                         tpl.toUpperCase() ) ) {
                    return SQL.stream( ps, SQL.STRING_LOOKUP ).collect( Collectors.toList() );
                }
            }
            catch( SQLException ex ) {
                log.log( Level.SEVERE, ex, () -> "getLocation " + tpl );
            }
        }
        return Collections.emptyList();
    }

    public Stream<TS> getForecasts( String tpl )
    {
        if( tpl != null && !tpl.isEmpty() ) {
            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT f.xml FROM darwin.forecast f"
                                                              + " INNER JOIN darwin.forecast_entry fe ON f.id=fe.fid"
                                                              + " INNER JOIN darwin.tiploc t ON fe.tpl=t.id"
                                                              + " WHERE t.tpl=?",
                                                         tpl.toUpperCase() ) ) {
                    return SQL.stream( ps, SQL.STRING_LOOKUP ).
                            map( DarwinJaxbContext.fromXML ).
                            flatMap( p -> p.getUR().getTS().stream() ).
                            // Sort data into correct order
                            peek( ts -> ts.getLocation().sort( TSLocationComparator.INSTANCE ) ).
                            collect( Collectors.toList() ).
                            stream();
                }
            }
            catch( SQLException ex ) {
                log.log( Level.SEVERE, ex, () -> "getLocation " + tpl );
            }
        }
        return Stream.empty();
    }

    /**
     * Return the TS for a rid
     * <p>
     * @param rid RTTI unique Train Identifier to retrieve
     * <p>
     * @return latest TS or null if not present
     */
    public Pport get( String rid )
    {
        if( rid != null && !rid.isEmpty() ) {
            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.forecast WHERE rid=?", rid ) ) {
                    return SQL.stream( ps, SQL.STRING_LOOKUP ).
                            map( DarwinJaxbContext.fromXML ).
                            findAny().
                            orElse( null );
                }
            }
            catch( SQLException ex ) {
                log.log( Level.SEVERE, ex, () -> "getLocation " + rid );
            }
        }
        return null;
    }
}
