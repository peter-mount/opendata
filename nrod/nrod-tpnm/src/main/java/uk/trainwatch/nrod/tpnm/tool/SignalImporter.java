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
        extends AbstractWayImporter<Object, Signal>
        implements SQLConsumer<Object>
{

    private PreparedStatement signalPS;
    private PreparedStatement ssectPS;

    public SignalImporter( Connection con )
    {
        super( con, 1000 );
    }

    @Override
    protected void process( Signal s )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteTable( con, "tpnm", "security_way" );
            SQL.deleteTable( con, "tpnm", "securitysection" );
            SQL.deleteTable( con, "tpnm", "signal" );
        }

        Long directedId = importDirected( s.getDirected() );

        long signalId = s.getId();

        signalPS = SQL.prepareInsert( signalPS, con,
                                      "tpnm.signal",
                                      signalId,
                                      s.getInterlockingsysid(),
                                      s.getName(),
                                      s.getZoneid(),
                                      s.getTmpclosed(),
                                      s.getUsesecsectfreeingtime(),
                                      s.getSecsectfreeingtime(),
                                      directedId );
        signalPS.executeUpdate();

        if( !s.getSecuritysection().isEmpty() ) {
            s.getSecuritysection().
                    forEach( SQLConsumer.guard( sect -> {
                        ssectPS = SQL.prepare( ssectPS, con,
                                               "INSERT INTO tpnm.securitysection (signalid,vmax,accelerationattail) VALUES (?,?,?)",
                                               signalId,
                                               sect.getVmax(),
                                               sect.getAccelerationattail() );
                        ssectPS.executeUpdate();

                        long ssectId = SQL.currval( con, "tpnm.securitysection_id_seq" );

                        importWay( sect.getWay(), "security", ssectId );
                    } ) );
        }

    }
}
