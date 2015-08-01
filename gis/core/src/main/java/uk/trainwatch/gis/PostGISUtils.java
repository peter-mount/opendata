/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.PGConnection;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class PostGISUtils
{

    /**
     * Get a {@link PGgeometry} from a result set
     * <p>
     * @param rs  ResultSet
     * @param col column
     * <p>
     * @return PGGeometry or null if not
     * <p>
     * @throws SQLException
     */
    public static PGgeometry getPGgeometry( ResultSet rs, int col )
            throws SQLException
    {
        return (PGgeometry)rs.getObject( col );
//        Object o = rs.getObject( col );
//        return o instanceof PGgeometry ? (PGgeometry) o : null;
    }

    /**
     * Get a {@link Point} from a result set
     * <p>
     * @param rs  ResultSet
     * @param col column
     * <p>
     * @return Point or null if not
     * <p>
     * @throws SQLException
     */
    public static Point getPoint( ResultSet rs, int col )
            throws SQLException
    {
        Geometry geom = getPGgeometry( rs, col ).getGeometry();
        return geom instanceof Point ? (Point) geom : null;
    }

    /**
     * Get a {@link LineString} from a result set
     * <p>
     * @param rs  ResultSet
     * @param col column
     * <p>
     * @return LineString or null if not
     * <p>
     * @throws SQLException
     */
    public static LineString getLineString( ResultSet rs, int col )
            throws SQLException
    {
        Geometry geom = getPGgeometry( rs, col ).getGeometry();
        return geom instanceof LineString ? (LineString) geom : null;
    }

    /**
     * See http://postgis.net/docs/ch06.html
     * <p>
     * @param dataSource
     *                   <p>
     * @return
     *         <p>
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection getPostgisConnection( DataSource dataSource )
            throws SQLException,
                   ClassNotFoundException
    {
        return postgisEnable( dataSource.getConnection() );
    }

    public static Connection postgisEnable( Connection con )
            throws SQLException,
                   ClassNotFoundException
    {
        // Add geometry types to the connection
        PGConnection pgcon = (PGConnection) con;
        pgcon.addDataType( "geometry", Class.forName( "org.postgis.PGgeometry" ) );
        pgcon.addDataType( "box3d", Class.forName( "org.postgis.PGbox3d" ) );

        Statement s = con.createStatement();
        s.executeUpdate( "SET search_path = tpnm" );

        return con;
    }

    /**
     * Get the bounding box of a PostGIS feature table
     * <p>
     * @param con
     * @param feature
     *                <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public static PGbox3d getBounds( Connection con, String schema, String feature )
            throws SQLException
    {
        try( PreparedStatement ps = SQL.prepare( con, "SELECT " + schema + ".Box3D(geom) FROM " + schema + "." + feature ) ) {
            return SQL.stream( ps, rs -> (PGbox3d) rs.getObject( 1 ) ).
                    findAny().
                    orElse( null );
        }
    }

    public static Rectangle2D getMapBounds( Connection con, String schema, String... tables )
            throws SQLException
    {
        return Stream.of( tables ).
                map( SQLFunction.guard( table -> getBounds( con, schema, table ) ) ).
                reduce(
                        new Rectangle2D.Double(),
                        ( r, b ) -> {
                            Point p = b.getLLB();
                            r.add( p.getX(), p.getY() );
                            p = b.getLLB();
                            r.add( p.getX(), p.getY() );
                            return r;
                        },
                        ( a, b ) -> {
                            a.add( b );
                            return a;
                        } );
    }
}
