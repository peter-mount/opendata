/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.stationmsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.sql.SQL;

/**
 * Records a station message into the database
 * <p>
 * @author peter
 */
public class StationMessageRecorder
        implements Consumer<Pport>
{

    private final Logger log = Logger.getLogger( StationMessageRecorder.class.getName() );

    private final DataSource dataSource;

    public StationMessageRecorder( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    @Override
    public void accept( Pport t )
    {
        t.getUR().getOW().forEach( m -> {
            int id = m.getId();

            log.log( Level.INFO, () -> "Received station message " + id );

            try( Connection con = dataSource.getConnection() ) {
                try {
                    con.setAutoCommit( false );

                    if( m.getStation().isEmpty() ) {
                        removeMessage( con, id );
                    }
                    else {
                        updateMessage( con, id, t, m );
                    }

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
        } );
    }

    private void updateMessage( Connection con, int id, Pport t, StationMessage m )
            throws SQLException
    {
        // Update the message
        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.message(?,?,?,?,?,?)" ) ) {
            SQL.executeQuery( ps,
                              id,
                              Objects.toString( m.getCat(), "" ),
                              Objects.toString( m.getSev(), "" ),
                              m.getSuppress(),
                              DarwinJaxbContext.toXML.apply( t ),
                              // CSV list of CRS
                              m.getStation().stream().map( StationMessage.Station::getCrs ).collect( Collectors.joining( "," ) )
            );
        }
    }

    private void removeMessage( Connection con, int id )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "DELETE FROM darwin.message_station WHERE msgid=?" ) ) {
            SQL.executeUpdate( ps, id );
        }

        try( PreparedStatement ps = SQL.prepare( con, "DELETE FROM darwin.message WHERE id=?" ) ) {
            SQL.executeUpdate( ps, id );
        }
    }
}
