/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQL;

/**
 * Utility to refresh the PostGIS tables based on the current state of the TPNM tables.
 * <p>
 * @author peter
 */
@MetaInfServices(Utility.class)
public class TPNMGeometry
        extends DBUtility
{

    protected static final Logger LOG = Logger.getLogger( TPNMImport.class.getName() );
    private static final String SCHEMA = "tpnm";

    @Override
    public void runUtility()
            throws Exception
    {
        try( Connection con = getConnection();
             Statement s = con.createStatement() ) {

            LOG.log( Level.INFO, "Refreshing nodes" );
            SQL.deleteTable( con, SCHEMA, "feat_node" );
            s.executeUpdate( "INSERT INTO tpnm.feat_node"
                            + " SELECT id, netx::INTEGER, nety::INTEGER, tpnm.ST_MakePoint(netx::INTEGER, nety::INTEGER)"
                            + " FROM tpnm.node" );

            LOG.log( Level.INFO, "Refreshing Tracks" );
            SQL.deleteTable( con, SCHEMA, "feat_track" );
            s.executeUpdate( "INSERT INTO tpnm.feat_track"
                            + " SELECT  t.id, t.name, t.descr, tpnm.ST_MakeLine(n.geom)"
                            + " FROM tpnm.track t"
                            + " INNER JOIN tpnm.track_way tw ON t.id=tw.trackid"
                            + " INNER JOIN tpnm.waypoint wp ON tw.id=wp.wayid"
                            + " INNER JOIN tpnm.feat_node n ON wp.nodeid=n.id"
                            + " GROUP BY t.id" );
        }
    }

}
