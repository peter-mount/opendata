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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class StationMessageManager
{

    private static final Logger log = Logger.getLogger( StationMessageManager.class.getName() );

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Inject
    private DarwinJaxbContext darwinJaxbContext;

    @Inject
    private StationMessageCache stationMessageCache;

    public Collection<StationMessage> getMessages()
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT xml FROM darwin.message"
                                                     + " WHERE dt between now()-'6 hours'::interval AND now()"
                                                     + " ORDER BY dt DESC" ) ) {
                return SQL.stream( ps, SQL.STRING_LOOKUP ).
                        map( darwinJaxbContext.fromXML() ).
                        filter( Objects::nonNull ).
                        flatMap( p -> p.getUR().getOW().stream() ).
                        collect( Collectors.toList() );
            }
        }
        catch( SQLException ex ) {
            log.log( Level.SEVERE, ex, () -> "Retrieving messages" );
        }
        return null;
    }

    /**
     * Get all messages for a crs sorted by id
     * <p>
     * @param crs 3alpha code for a station
     * <p>
     * @return Stream of StationMessages, may be empty but never null
     */
    public Collection<StationMessage> getMessages( String crs )
    {
        return stationMessageCache.getMessages( crs );
    }

    public StationMessage getMessage( int id )
    {
        return stationMessageCache.getMessage( id );
    }

}
