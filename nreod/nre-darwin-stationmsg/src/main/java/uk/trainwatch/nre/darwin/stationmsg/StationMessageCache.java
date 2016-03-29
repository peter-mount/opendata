/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.stationmsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "DarwinStationMessagesCache")
public class StationMessageCache
{

    private static final Logger log = Logger.getLogger( StationMessageCache.class.getName() );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private DarwinJaxbContext darwinJaxbContext;

    private static final UnaryOperator<String> xmlWrapper = xml -> "<Pport xmlns=\"http://www.thalesgroup.com/rtti/PushPort/v12\">"
                                                                   + "<uR>"
                                                                   + "<OW xmlns:mg=\"http://www.thalesgroup.com/rtti/PushPort/StationMessages/v1\">" + xml + "</OW>"
                                                                   + "</uR>"
                                                                   + "</Pport>";

    @CacheResult
    public Collection<StationMessage> getMessages( @CacheKey String crs )
    {
        log.log( Level.INFO, () -> "Retrieving station messages for " + crs );

        if( crs != null && !crs.isEmpty() ) {
            String talpha = crs.toUpperCase();

            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con,
                                                         "SELECT m.xml FROM darwin.message m"
                                                         + " INNER JOIN darwin.message_station ms ON m.id=ms.msgid"
                                                         + " INNER JOIN darwin.crs s ON ms.crsid=s.id"
                                                         + " WHERE s.crs=?"
                                                         + " ORDER BY m.id DESC",
                                                         talpha ) ) {
                    return SQL.stream( ps, SQL.STRING_LOOKUP ).
                            map( xmlWrapper ).
                            map( darwinJaxbContext.fromXML() ).
                            filter( Objects::nonNull ).
                            flatMap( p -> p.getUR().getOW().stream() ).
                            //filter( ow -> ow.getStation().stream().filter( s -> s.getCrs().equals( talpha ) ).findAny().isPresent() ).
                            collect( Collectors.toList() );
                }
            }
            catch( SQLException ex ) {
                log.log( Level.SEVERE, ex, () -> "Retrieving messages for " + talpha );
            }
        }

        return Collections.emptyList();
    }

    @CacheResult
    public StationMessage getMessage( @CacheKey int id )
    {
        log.log( Level.SEVERE, () -> "Retrieving message id " + id );

        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT xml FROM darwin.message WHERE id=?", id ) ) {
                return SQL.stream( ps, SQL.STRING_LOOKUP ).
                        map( xmlWrapper ).
                        map( darwinJaxbContext.fromXML() ).
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
