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
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.MapVisitor;
import uk.trainwatch.nrod.signalmapeditor.utils.TextAlignment;
import static uk.trainwatch.nrod.signalmapeditor.Constants.*;
import uk.trainwatch.nrod.signalmapeditor.map.Line;
import uk.trainwatch.nrod.signalmapeditor.map.LineNode;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.util.Functions;

/**
 * Handles the rendering of the map within the editor
 * <p>
 * @author peter
 */
class MapRenderer
        implements MapVisitor
{

    private final float[] dash = {
        1.0f, 4.0f
    };

    private final Insets insets;
    private final Dimension dimension;
    private final MapMouseHandler mouseHandler;
    private final Graphics2D g;
    private final Font font;
    private final FontMetrics fm;

    public MapRenderer( Insets insets, Dimension dimension, MapMouseHandler mouseHandler, Graphics2D g )
    {
        this.insets = insets;
        this.dimension = dimension;
        this.mouseHandler = mouseHandler;
        this.g = g;

        font = new Font( Font.MONOSPACED, Font.PLAIN, FONT_SIZE );
        g.setFont( font );
        fm = g.getFontMetrics();
    }

    private void paintGrid()
    {
        int y1 = insets.top;
        int y2 = (int) dimension.getHeight() - insets.bottom;
        int x1 = insets.left;
        int x2 = (int) dimension.getWidth() - insets.right;

        g.setColor( Color.WHITE );
        g.fillRect( x1, y1, x2 - x1, y2 - y1 );

        g.setColor( Color.LIGHT_GRAY );
        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1.0f, dash, 0.0f ) );

        g.setFont( new Font( Font.MONOSPACED, Font.PLAIN, FONT_SIZE ) );
        FontMetrics fm = g.getFontMetrics();

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
    }

    @Override
    public void visit( SignalMap m )
    {
        paintGrid();

        m.streamLines().
                forEach( this );

        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND ) );
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
        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND ) );
        g.setColor( Color.GRAY );

        Berth from = l.getFrom();
        Berth to = l.getTo();
        g.drawLine( Constants.getX( from ) + ROW_HEIGHT,
                    Constants.getY( from ) + ROW_CENTER,
                    Constants.getX( to ) + COLUMN_CENTER,
                    Constants.getY( to ) + ROW_CENTER );
    }
}
