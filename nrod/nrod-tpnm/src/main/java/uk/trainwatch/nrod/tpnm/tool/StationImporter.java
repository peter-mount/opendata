/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import uk.trainwatch.nrod.tpnm.model.Station;
import uk.trainwatch.nrod.tpnm.model.Track;
import uk.trainwatch.util.ParserUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class StationImporter
        extends AbstractWayImporter
        implements SQLConsumer<Object>
{

    public StationImporter( Connection con )
    {
        super( con );
    }

    @Override
    public void accept( Object o )
            throws SQLException
    {
        Station s = (Station) o;
        
        try( PreparedStatement ps = SQL.prepareInsert( con,
                                                       "tpnm.station",
                                                       s.getStationid(),
                                                       s.getUiccode(),
                                                       s.getAbbrev(),
                                                       s.getLongname(),
                                                       s.getCommentary(),
                                                       s.getStdstoppingtime(),
                                                       s.getStdconnectiontime(),
                                                       s.getStationtype(),
                                                       s.getStationcategory(),
                                                       s.getUicstationcode(),
                                                       s.getTransportassociation(),
                                                       s.getX(),
                                                       s.getY(),
                                                       s.getEasting(),
                                                       s.getNorthing(),
                                                       ParserUtils.parseInt( s.getStanox() ),
                                                       s.getLpbflag(),
                                                       s.getPeriodid(),
                                                       s.getCapitalsident(),
                                                       s.getNalco(),
                                                       new Timestamp( s.getLastmodified().toGregorianCalendar().toInstant().getEpochSecond() ),
                                                       s.getCrscode(),
                                                       s.getCompulsorystop()
        ) ) {
            ps.executeUpdate();
        }

        for( Track t: s.getTrack() ) {
            int trackId = t.getTrackID();

            try( PreparedStatement ps = SQL.prepareInsert( con,
                                                           "tpnm.track",
                                                           trackId,
                                                           s.getStationid(),
                                                           t.getName(),
                                                           0, // seq
                                                           t.getDescription(),
                                                           t.getTrackcategory(),
                                                           t.getEffectivelength(),
                                                           t.getRoplinecode(),
                                                           t.getSalinecode(),
                                                           t.getLinepropertytemplateid(),
                                                           t.getPeriodid(),
                                                           t.getPermissiveWorking(),
                                                           false, // directed
                                                           t.getTracktmpclosed()
            ) ) {
                ps.executeUpdate();
            }

            importWay( t.getWay(), "track", (long) trackId );
        }
        con.commit();
    }

}
