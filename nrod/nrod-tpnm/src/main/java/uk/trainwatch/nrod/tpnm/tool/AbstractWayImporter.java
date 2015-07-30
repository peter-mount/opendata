/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import uk.trainwatch.nrod.tpnm.model.Directed;
import uk.trainwatch.nrod.tpnm.model.Point;
import uk.trainwatch.nrod.tpnm.model.Way;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 * @param <T>
 * @param <V>
 */
public abstract class AbstractWayImporter<T, V extends T>
        extends AbstractImporter<T, V>
{

    private String wayType;
    private PreparedStatement wayps;
    private CallableStatement pointps;
    private PreparedStatement directedPS;

    public AbstractWayImporter( Connection con )
    {
        super( con );
    }

    public AbstractWayImporter( Connection con, String name )
    {
        super( con, name );
    }

    public AbstractWayImporter( Connection con, int commit )
    {
        super( con, commit );
    }

    public AbstractWayImporter( Connection con, int commit, String name )
    {
        super( con, commit, name );
    }

    protected final void importWay( Way way, String type, Object... args )
            throws SQLException
    {
        // Don't create null/empty ways
        if( way != null && !way.getPoint().isEmpty() ) {

            if( wayType != null && !wayType.equals( type ) ) {
                if( wayps != null ) {
                    wayps.close();
                    wayps = null;
                }
                if( pointps != null ) {
                    pointps.close();
                    pointps = null;
                }
            }

            wayps = SQL.prepare( wayps, con, "SELECT tpnm.create" + type + "way(?)", args );

            long wayId = SQL.stream( wayps, SQL.LONG_LOOKUP ).findAny().get();

            // The sequence within the way
            AtomicInteger seq = new AtomicInteger();
            
            way.getPoint().
                    forEach( SQLConsumer.guard( pt -> {
                        pointps = SQL.prepareCall( pointps, con, "{call tpnm.waypoint(?,?,?)}", wayId, (long) pt.getNodeid(), seq.incrementAndGet() );

                        SQL.executeBatch( pointps );
                    } ) );

            if( pointps != null ) {
                pointps.executeBatch();
            }
        }
    }

    protected final Long importDirected( Directed d )
            throws SQLException
    {
        if( d == null ) {
            return null;
        }

        Point st = d.getStart().getPoint();
        Point ed = d.getEnd().getPoint();
        directedPS = SQL.prepare( directedPS, con,
                                  "INSERT INTO tpnm.directed (startid,endid) VALUES (?,?)",
                                  st.getNodeid(),
                                  ed.getNodeid() );
        directedPS.executeUpdate();

        return SQL.currval( con, "tpnm.directed_id_seq" );
    }

}
