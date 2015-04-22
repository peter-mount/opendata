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

    private static final Logger log = Logger.getLogger( StationMessageRecorder.class.getName() );

    private final DataSource dataSource;

    public StationMessageRecorder( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    @Override
    public void accept( Pport t )
    {
        t.getUR().getOW().forEach( m -> accept( t, m ) );
    }

    private void accept( Pport t, StationMessage m )
    {
        int id = m.getId();

        try( Connection con = dataSource.getConnection() ) {
            if( m.isSetStation() ) {
                updateMessage( con, id, t, m );
            }
            else {
                removeMessage( con, id );
            }
        }
        catch( SQLException ex ) {
            log.log( Level.SEVERE, null, ex );
        }
    }

    /**
     * Updates or creates a station message in the database
     * <p>
     * @param con Connection
     * @param id  messageId
     * @param t   Pport
     * @param m   StationMessage to record
     * <p>
     * @throws SQLException
     */
    private void updateMessage( Connection con, int id, Pport t, StationMessage m )
            throws SQLException
    {
        log.log( Level.INFO, () -> "Updating station message " + id );

        // Clone with just this message present as we only want to persist this one message
        Pport dbPport = t.cloneMeta();
        dbPport.getUR().getOW().add( m );

        // CSV list of CRS this message applies to
        String crs = m.getStation().stream().map( StationMessage.Station::getCrs ).collect( Collectors.joining( "," ) );

        // Update the message
        try( PreparedStatement ps = SQL.prepare( con, "SELECT darwin.message(?,?,?,?,?,?)" ) ) {
            SQL.executeQuery( ps,
                              id,
                              Objects.toString( m.getCat(), "" ),
                              Objects.toString( m.getSev(), "" ),
                              m.getSuppress(),
                              DarwinJaxbContext.toXML.apply( dbPport ),
                              crs
            );
        }
    }

    /**
     * Removes the StationMessage
     * <p>
     * @param con Connection
     * @param id  messageId to remove
     * <p>
     * @throws SQLException
     */
    private void removeMessage( Connection con, int id )
            throws SQLException
    {
        log.log( Level.INFO, () -> "Removing station message " + id );

        try( PreparedStatement ps = SQL.prepare( con, "DELETE FROM darwin.message_station WHERE msgid=?" ) ) {
            SQL.executeUpdate( ps, id );
        }

        /* Originally we also deleted the message but we'll archive them from now on.
         * They will not appear due to the link in message_station being removed.
         * However the archive is accurate as we still have the XML present with all of the CRS information present at the time of the last update.
         * Won't be 100% accurate if stations were removed during it's lifetime but it'll do.
         try( PreparedStatement ps = SQL.prepare( con, "DELETE FROM darwin.message WHERE id=?" ) ) {
         SQL.executeUpdate( ps, id );
         }
         */
    }
}
