/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.svg;

import java.util.function.Supplier;
//import org.postgis.LineString;
//import org.postgis.Point;

/**
 * Accepts geometry of {@link LineString} or {@link Point} forming a single svg path.
 * <p>
 * This class will perform some optimisations:
 * <ul>
 * <li>If it sees a point with ".0" on the end of a coordinate, it will strip that. Over time that saves on average 4 chars per point reducing the output
 * size</li>
 * <li>For lines, if it sees the start or end point matches the last point drawn, it will continue the line. This does more than save space, but ensures the
 * line is continuous in the output, removing some ugly joins. If the end point matches it will render the line in reverse.</li>
 * </ul>
 * <p>
 * @author peter
 */
public class SvgLineAppender
        implements Supplier<String>
{

    private final StringBuilder sb = new StringBuilder();
//    private Point lastPoint;
    private int count;

//    private void append( Point p )
//    {
//        SvgUtils.append( sb, p.getX() );
//        sb.append( ',' );
//        SvgUtils.append( sb, p.getY() );
//    }

    /**
     * Is the appender empty
     * <p>
     * @return
     */
    public boolean isEmpty()
    {
        return sb.length() == 0 || count == 0;
    }

    /**
     * The number of points in this appender.
     * <p>
     * This is useful if you write a single point to the appender requiring alternate display
     * <p>
     * @return
     */
    public int getCount()
    {
        return count;
    }

    /**
     * Reset this instance
     * <p>
     * @return
     */
    public SvgLineAppender reset()
    {
        sb.setLength( 0 );
        count = 0;
        return this;
    }

    /**
     * Merge another appender to this one
     * <p>
     * @param b
     *          <p>
     * @return
     */
    public SvgLineAppender merge( SvgLineAppender b )
    {
        sb.append( b.sb );
//        lastPoint = null;
        count = count + b.count;
        return this;
    }

    /**
     * Merging function to merge two appenders together
     * <p>
     * @param a
     * @param b
     *          <p>
     * @return
     */
    public static SvgLineAppender merge( SvgLineAppender a, SvgLineAppender b )
    {
        return a.merge( b );
    }
//
//    /**
//     * Move to a point
//     * <p>
//     * @param p
//     *          <p>
//     * @return
//     */
//    public SvgLineAppender move( Point p )
//    {
//        sb.append( 'M' );
//        append( p );
//        lastPoint = p;
//        count++;
//        return this;
//    }
//
//    /**
//     * Appends a PostGIS {@link Point} to this path, drawing a line to it.
//     * <p>
//     * @param p
//     *          <p>
//     * @return
//     */
//    public SvgLineAppender lineTo( Point p )
//    {
//        if( p != null ) {
//            // No last point enforces move, otherwise ignore duplicate points
//            //if( lastPoint == null || !p.equals( lastPoint ) ) {
//                sb.append( lastPoint == null ? 'M' : 'L' );
//                append( p );
//                count++;
//            //}
//            lastPoint = p;
//        }
//        return this;
//    }
//
//    /**
//     * Append a PostGIS {@link LineString} to this path.
//     * <p>
//     * Note: If the first/last point of the path matches the last point in this path
//     * then we will continue from that point, optimising the result.
//     * <p>
//     * @param l
//     *          <p>
//     * @return
//     */
//    public SvgLineAppender append( LineString l )
//    {
//        // If last point in line matches lastPoint then draw in reverse
//        boolean reverse = false;//lastPoint != null && l.getLastPoint().equals( lastPoint );
//
//        // Start with a move if not matching
//        //if( lastPoint != null && !(lastPoint.equals( l.getFirstPoint() ) || lastPoint.equals( l.getLastPoint() )) ) {
//            lastPoint = null;
//        //}
//
//        if( reverse ) {
//            for( int i = l.numPoints() - 1; i >= 0; i-- ) {
//                lineTo( l.getPoint( i ) );
//            }
//        }
//        else {
//            for( Point p: l.getPoints() ) {
//                lineTo( p );
//            }
//        }
//
//        return this;
//    }

    /**
     * Close the current path.
     * <p>
     * @return
     */
    public SvgLineAppender closePath()
    {
        if( sb.length() > 0 && sb.charAt( sb.length() - 1 ) != 'Z' && count > 1 ) {
            sb.append( 'Z' );
        }
//        lastPoint = null;
        return this;
    }

    /**
     * The SVG path
     * <p>
     * @return
     */
    @Override
    public String toString()
    {
        return sb.toString();
    }

    /**
     * {@link Supplier} to return the current path
     * <p>
     * @return
     */
    @Override
    public String get()
    {
        return sb.toString();
    }

}
