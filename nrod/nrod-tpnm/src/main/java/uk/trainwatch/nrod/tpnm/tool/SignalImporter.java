/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uk.trainwatch.nrod.tpnm.model.Signal;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class SignalImporter
        extends AbstractWayImporter
        implements SQLConsumer<Object>
{

    public SignalImporter( Connection con )
    {
        super( con );
    }

    @Override
    public void accept( Object o )
            throws SQLException
    {
        Signal s = (Signal) o;

        Long directedId = importDirected( s.getDirected() );

        long signalId = s.getId();

        try( PreparedStatement signalPS = SQL.prepareInsert( con,
                                                             "tpnm.signal",
                                                             signalId,
                                                             s.getInterlockingsysid(),
                                                             s.getName(),
                                                             s.getZoneid(),
                                                             s.getTmpclosed(),
                                                             s.getUsesecsectfreeingtime(),
                                                             s.getSecsectfreeingtime(),
                                                             directedId );
             PreparedStatement ssectPS = SQL.prepare( con, "INSERT INTO tpnm.securitysection (signalid,vmax,accelerationattail) VALUES (?,?,?)" ) ) {
            signalPS.executeUpdate();

            if( !s.getSecuritysection().isEmpty() ) {
                s.getSecuritysection().forEach( SQLConsumer.guard( sect -> {
                    SQL.executeUpdate( ssectPS, signalId, sect.getVmax(), sect.getAccelerationattail() );
                    long ssectId = SQL.currval( con, "tpnm.securitysection_id_seq" );

                    importWay( sect.getWay(), "security", ssectId );
                } ) );
            }
        }

        con.commit();
    }

}
