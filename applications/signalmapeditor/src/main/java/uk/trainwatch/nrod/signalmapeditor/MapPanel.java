/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.TextAlignment;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class MapPanel
        extends JPanel
{

    private Point hoverPoint;
    private String selectedBerth;
    private final float[] dash =
    {
        1.0f, 4.0f
    };

    public MapPanel()
    {
        // refresh & repaint if berth added/removed or moved
        Project.INSTANCE.addPropertyChangeListener( SignalMap.PROP_BERTHS, this::refresh );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_X, this::refresh );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_Y, this::refresh );
        // Repaint if id or text changes
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_ID, e -> repaint() );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_TEXT, e -> repaint() );

        addMouseMotionListener( new MouseMotionListener()
        {

            @Override
            public void mouseMoved( MouseEvent e )
            {
                Point p = e.getPoint();
                if( contains( p ) )
                {
                    hoverPoint = p;

                    Berth b = Project.INSTANCE.getMap().
                            getBerthAt( (int) p.getX() / 50, (int) p.getY() / 25 );
                    selectedBerth = b == null ? null : b.getId();

                    repaint();
                }
                else if( hoverPoint != null )
                {
                    hoverPoint = null;
                    repaint();
                }

            }

            @Override
            public void mouseDragged( MouseEvent e )
            {
                Point p = e.getPoint();
                if( contains( p ) && selectedBerth != null )
                {
                    SignalMap map = Project.INSTANCE.getMap();
                    Berth b = map.getBerth( selectedBerth );
                    if( b != null )
                    {
                        int x = (int) p.getX() / 50, y = (int) p.getY() / 25;
                        if( !map.present( x, y ) )
                        {
                            b.setX( x );
                            b.setY( y );
                            resizeCanvas();
                        }
                    }
                }
            }

        } );
    }

    @Override
    protected void paintComponent( Graphics g1 )
    {
        //super.paintComponent( g );
        Graphics2D g = (Graphics2D) g1.create();
        try
        {
            paintGrid( g );
            paintLines( g );
            paintBerths( g );
        }
        finally
        {
            g.dispose();
        }
    }

    private void paintGrid( Graphics2D g )
    {
        Insets i = getInsets();
        int y1 = i.top;
        int y2 = getHeight() - i.bottom;
        int x1 = i.left;
        int x2 = getWidth() - i.right;

        g.setColor( Color.WHITE );
        g.fillRect( x1, y1, x2 - x1, y2 - y1 );

        g.setColor( Color.LIGHT_GRAY );
        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 1.0f, dash, 0.0f ) );

        for( int y = y1 + 10; y < y2; y += 25 )
        {
            g.drawLine( x1, y, x2, y );
            for( int x = x1 + 10; x < x2; x += 50 )
            {
                g.drawLine( x, y1, x, y2 );
            }
        }
    }

    private static int getX( Berth b )
    {
        return 10 + (b.getX() * 50);
    }

    private static int getY( Berth b )
    {
        return 10 + (b.getY() * 25);
    }

    private void paintLines( Graphics2D g )
    {
        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND ) );
        g.setColor( Color.GRAY );
        Project.INSTANCE.getMap().
                forEach( ( i, b ) ->
                        {
                            int x1 = getX( b ) + 25, y1 = getY( b ) + 12;
                            b.forEachOutBerth( b2 -> g.drawLine( x1, y1, getX( b2 ) + 25, getY( b2 ) + 12 ) );
                } );
    }

    private void paintBerths( Graphics2D g )
    {
        g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND ) );
        g.setFont( new Font( Font.MONOSPACED, Font.PLAIN, 10 ) );
        FontMetrics fm = g.getFontMetrics();
        Project.INSTANCE.getMap().
                forEach( ( i, b ) -> paintBerth( g, fm, b ) );
    }

    private void paintBerth( Graphics2D g, FontMetrics fm, Berth b )
    {
        double x = getX( b );
        double y = getY( b );

        boolean occupied = b.getText() != null;

        Rectangle r = new Rectangle( (int) x + 10, (int) y + 5, 30, 15 );
        g.setColor( occupied ? Color.YELLOW : Color.LIGHT_GRAY );
        g.fill( r );

        g.setColor( hoverPoint == null || !r.contains( hoverPoint ) ? Color.BLACK : Color.RED );
        g.draw( r );

        TextAlignment.CENTER.drawString( g,
                                         fm,
                                         occupied ? b.getText() : b.getId(),
                                         x,
                                         y + 12 - ((fm.getAscent() + fm.getMaxDescent()) >>> 1),
                                         50.0 );
    }

    private void refresh( PropertyChangeEvent e )
    {
        if( Project.CHANGED.test( e ) )
        {
            resizeCanvas();
        }
    }

    private void resizeCanvas()
    {
        Dimension pd = getPreferredSize();
        Dimension d = new Dimension( getMinimumSize() );

        ThreadQueue.executeLater( () ->
        {
            Project.INSTANCE.getMap().
                    forEach( ( i, b ) -> d.setSize(
                                    Math.max( d.getWidth(), getX( b ) + 55 ),
                                    Math.max( d.getHeight(), getY( b ) + 30 )
                            ) );

            if( !d.equals( pd ) )
            {
                System.out.println( d );
                setPreferredSize( d );
                EventQueue.invokeLater( () ->
                {
                    invalidate();
                    getParent().
                            validate();
                } );
            }

            EventQueue.invokeLater( () -> repaint() );
        } );
    }
}
