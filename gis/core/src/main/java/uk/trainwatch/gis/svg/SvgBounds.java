/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.svg;

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import org.postgis.LineString;
//import org.postgis.Point;
import uk.trainwatch.util.sql.SQL;

/**
 * Class that holds the bounds of a dataset and manages the transformation from source and svg coordinate space
 * <p>
 * @author peter
 */
public class SvgBounds
        extends Rectangle2D.Double
{

    private final double scale;
    private final int svgWidth, svgHeight;

    /**
     *
     * @param scale  The scale
     * @param x      native x coordinate of centre
     * @param y      native y coordinate of centre
     * @param width  width of svg
     * @param height height of svg
     */
    public SvgBounds( double scale, double x, double y, int width, int height )
    {
        super( x - (width * scale / 2), y - (height * scale / 2), width * scale, height * scale );
        this.scale = scale == 0.0 ? 1.0 : scale;
        this.svgWidth = width;
        this.svgHeight = height;
    }

    /**
     * The scale
     * <p>
     * @return
     */
    public double getScale()
    {
        return scale;
    }

    /**
     * The width of the svg element
     * <p>
     * @return
     */
    public int getSvgWidth()
    {
        return svgWidth;
    }

    /**
     * The height of the svg element
     * <p>
     * @return
     */
    public int getSvgHeight()
    {
        return svgHeight;
    }

    /**
     * Transform x from netx to output coordinates
     * <p>
     * @param netx
     *             <p>
     * @return
     */
    public double getX( double netx )
    {
        return transformScale( netx - getMinX() );
    }

    /**
     * Transform nety to output coordinates
     * <p>
     * @param nety
     *             <p>
     * @return
     */
    public double getY( double nety )
    {
        return transformScale( nety - getMinY() );
    }

    public double transformScale( double v )
    {
        return v / scale;
    }

    public int transformScale( int v )
    {
        return (int) (v / scale);
    }

    /**
     * Transform a PostGIS {@link Point} from map to output coordinates
     * <p>
     * @param p
     *          <p>
     * @return
     */
//    public Point transformPoint( Point p )
//    {
//        p.x = getX( p.x );
//        p.y = getY( p.y );
//        return p;
//    }

    /**
     * Transform a PostGIS {@link LineString} from map to output coordinates
     * <p>
     * @param l LineString to transform
     * <p>
     * @return l
     */
//    public LineString transformLineString( LineString l )
//    {
//        for( Point p: l.getPoints() ) {
//            transformPoint( p );
//        }
//        return l;
//    }

    public PreparedStatement prepare( PreparedStatement ps, Connection con, String fields, String table, String geom )
            throws SQLException
    {
        return ps == null ? prepare( con, fields, table, geom ) : SQL.setParameters( ps,
                                                                                     (int) getMinX(),
                                                                                     (int) getMinY(),
                                                                                     (int) getMaxX(),
                                                                                     (int) getMaxY() );
    }

    public PreparedStatement prepare( Connection con, String fields, String table, String geom )
            throws SQLException
    {
        return SQL.prepare( con,
                            "SELECT " + fields + "," + geom
                            + " FROM " + table
                            + " WHERE " + geom + " && (tpnm.ST_MakeEnvelope(?,?,?,?,4258)::geometry)",
                            (int) getMinX(),
                            (int) getMinY(),
                            (int) getMaxX(),
                            (int) getMaxY()
        );
    }

}
