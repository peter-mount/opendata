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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public enum StationMessageManager
{

    INSTANCE;

    private final Logger log = Logger.getLogger( StationMessageManager.class.getName() );

    private DataSource dataSource;

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    /**
     * Get all messages for a crs sorted by id
     * <p>
     * @param crs 3alpha code for a station
     * <p>
     * @return Stream of StationMessages, may be empty but never null
     */
    public Stream<StationMessage> getMessages( String crs )
    {
        if( crs == null || crs.isEmpty() ) {
            return Stream.empty();
        }
        
        String talpha = crs.toUpperCase();

        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT m.xml FROM darwin.message m"
                                                     + " INNER JOIN darwin.message_station ms ON m.id=ms.msgid"
                                                     + " INNER JOIN darwin.station s ON ms.stationid=s.id"
                                                     + " WHERE s.crs=?"
                                                     + " ORDER BY m.id DESC",
                                                     talpha ) ) {
                return SQL.stream( ps, SQL.STRING_LOOKUP ).
                        map( DarwinJaxbContext.fromXML ).
                        filter( Objects::nonNull ).
                        flatMap( p -> p.getUR().getOW().stream() ).
                        filter( ow -> ow.getStation().stream().filter( s -> s.getCrs().equals( talpha ) ).findAny().isPresent() ).
                        collect( Collectors.toList() ).
                        stream();
            }
        }
        catch( SQLException ex ) {
            log.log( Level.SEVERE, ex, () -> "Retrieving messages for " + talpha );
        }
        return null;
    }

    public StationMessage getMessage( int id )
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.message WHERE id=?", id ) ) {
                return SQL.stream( ps, SQL.STRING_LOOKUP ).
                        map( DarwinJaxbContext.fromXML ).
                        filter( Objects::nonNull ).
                        flatMap( p -> p.getUR().getOW().stream() ).
                        filter( s -> s.getId() == id ).
                        findAny().
                        orElse( null );
            }
        }
        catch( SQLException ex ) {
            log.log( Level.SEVERE, ex, () -> "Retrieving message id " + id );
        }
        return null;
    }

}
