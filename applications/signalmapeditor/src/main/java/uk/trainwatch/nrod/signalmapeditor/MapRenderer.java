/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.MapVisitor;
import uk.trainwatch.nrod.signalmapeditor.utils.TextAlignment;
import static uk.trainwatch.nrod.signalmapeditor.Constants.*;
import uk.trainwatch.nrod.signalmapeditor.map.Node;
import uk.trainwatch.nrod.signalmapeditor.map.Line;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.map.Text;

/**
 * Handles the rendering of the map within the editor
 * <p>
 * @author peter
 */
class MapRenderer
        implements MapVisitor
{

    private static final float[] dash = {
        1.0f, 4.0f
    };

    // The normal font
    private static final Font FONT_NORMAL = new Font( Font.MONOSPACED, Font.PLAIN, FONT_SIZE );
    // 0.8em font based on FONT_SIZE - this is based on the uktra.in signal map prototype
    private static final Font FONT_SMALL = new Font( Font.SANS_SERIF, Font.PLAIN, (int) (FONT_SIZE * 0.8) );

    private static final Stroke STROKE = new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND );
    private static final Stroke DASH_0 = new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1.0f, dash, 0.0f );
    private static final Stroke DASH_1 = new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f );

    private final Insets insets;
    private final Dimension dimension;
    private final MapMouseHandler mouseHandler;
    private final Graphics2D g;

    private Font font;
    private FontMetrics fm;

    public MapRenderer( Insets insets, Dimension dimension, MapMouseHandler mouseHandler, Graphics2D g )
    {
        this.insets = insets;
        this.dimension = dimension;
        this.mouseHandler = mouseHandler;
        this.g = g;

        setFont( FONT_NORMAL );
    }

    private void setFont( Font font )
    {
        this.font = font;
        g.setFont( font );
        fm = g.getFontMetrics();
    }

    /**
     * Paints the grid for the map
     */
    private void paintGrid()
    {
        int y1 = insets.top;
        int y2 = (int) dimension.getHeight() - insets.bottom;
        int x1 = insets.left;
        int x2 = (int) dimension.getWidth() - insets.right;

        g.setColor( Color.LIGHT_GRAY );
        g.setStroke( DASH_0 );

        setFont( FONT_NORMAL );

        for( int x = x1 + TOP_LEFT_OFFSET; x < x2; x += COLUMN_WIDTH ) {
            TextAlignment.CENTER.drawString( g, fm, String.valueOf( (x - TOP_LEFT_OFFSET) / COLUMN_WIDTH ), x, y1 - fm.getDescent() + 1, COLUMN_WIDTH );
        }

        for( int y = y1 + TOP_LEFT_OFFSET; y < y2; y += ROW_HEIGHT ) {
            g.drawLine( x1, y, x2, y );
            TextAlignment.LEFT.drawString( g, fm, String.valueOf( (y - TOP_LEFT_OFFSET) / ROW_HEIGHT ), x1 + 1, y + ROW_CENTER - fm.getDescent(), COLUMN_WIDTH );
            for( int x = x1 + TOP_LEFT_OFFSET; x < x2; x += COLUMN_WIDTH ) {
                g.drawLine( x, y1, x, y2 );
            }
        }

        // A vertical line at position 10 to mark the 'preferred' width on uktra.in maps as that works on most devices (i.e. my Phone)
        g.setColor( Color.GREEN.brighter().
                brighter() );
        int x = Constants.getX( 11 );
        g.drawLine( x, y1, x, y2 );
    }

    @Override
    public void visit( SignalMap m )
    {
        // Clear the canvas
        g.setColor( Color.WHITE );
        g.fillRect( insets.left, insets.top, (int) dimension.getWidth() - insets.right, (int) dimension.getHeight() - insets.bottom );

        paintGrid();

        // The map bounds
        Dimension d = m.getDimension();
        g.setColor( Color.GREEN.brighter() );
        g.setStroke( DASH_1 );
        g.drawRect( insets.left, insets.top, (int) d.getWidth(), (int) d.getHeight() );

        // Draw the rail lines
        m.streamLines().
                forEach( this );

        // Draw the signal berths
        g.setStroke( STROKE );
        m.streamBerths().
                forEach( this );
    }

    @Override
    public void visit( Berth b )
    {
        double x = getX( b );
        double y = getY( b );

        boolean occupied = b.getText() != null;

        Rectangle r = new Rectangle( (int) x + 10, (int) y + 5, 30, 15 );
        g.setColor( occupied ? Color.YELLOW : Color.LIGHT_GRAY );
        g.fill( r );

        g.setColor( mouseHandler.isHoveringOver( r ) ? Color.RED : Color.BLACK );
        g.draw( r );

        setFont( FONT_NORMAL );
        g.setColor( Color.BLACK );
        TextAlignment.CENTER.drawString( g,
                                         fm,
                                         occupied ? b.getText() : b.getId(),
                                         x,
                                         y + ROW_CENTER - ((fm.getAscent() + fm.getMaxDescent()) >>> 1),
                                         50.0 );
    }

    @Override
    public void visit( Line l )
    {
        g.setStroke( STROKE );
        g.setColor( Color.GRAY );

        Node from = l.getFrom();
        Node to = l.getTo();
        g.drawLine( Constants.getX( from ) + ROW_HEIGHT,
                    Constants.getY( from ) + ROW_CENTER,
                    Constants.getX( to ) + COLUMN_CENTER,
                    Constants.getY( to ) + ROW_CENTER );
    }

    @Override
    public void visit( Text t )
    {
        g.setStroke( STROKE );

        Rectangle r = t.getRectangle();
        if( mouseHandler.isHoveringOver( r ) ) {
            g.setColor( Color.RED );
            g.draw( r );
        }

        setFont( FONT_SMALL );
        g.setColor( Color.BLACK );
        TextAlignment.CENTER.drawString( g, fm, t.getText(), getX( t ), getY( t ) + ROW_CENTER, fm.stringWidth( t.getText() ) );
    }
}
