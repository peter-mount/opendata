/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import static uk.trainwatch.nrod.signalmapeditor.Constants.*;

/**
 * Handles the mouse within the {@link MapPanel}
 * <p>
 * @author peter
 */
class MapMouseHandler
        implements MouseMotionListener
{

    private final MapPanel panel;

    /**
     * The Point on the canvas the mouse is currently over
     */
    private Point hoverPoint;
    /**
     * The
     */
    private String selectedBerth;

    @SuppressWarnings("LeakingThisInConstructor")
    public MapMouseHandler( MapPanel panel )
    {
        this.panel = panel;
        panel.addMouseMotionListener( this );
    }

    /**
     * Are we hovering over the supplied {@link Rectangle}
     * <p>
     * @param r
     *          <p>
     * @return
     */
    public boolean isHoveringOver( Rectangle r )
    {
        return hoverPoint != null && r.contains( hoverPoint );
    }

    //<editor-fold defaultstate="collapsed" desc="MouseMotionListener">
    @Override
    public void mouseMoved( MouseEvent e )
    {
        Point p = e.getPoint();
        if( panel.contains( p ) ) {
            hoverPoint = p;

            Berth b = Project.INSTANCE.getMap().
                    getBerthAt( getGridX( p ), getGridY( p ) );
            selectedBerth = b == null ? null : b.getId();

            panel.repaint();
        }
        else if( hoverPoint != null ) {
            hoverPoint = null;
            panel.repaint();
        }

    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
        Point p = e.getPoint();
        if( panel.contains( p ) && selectedBerth != null ) {
            SignalMap map = Project.INSTANCE.getMap();
            Berth b = map.getBerth( selectedBerth );
            if( b != null ) {
                int x = getGridX( p ), y = getGridY( p );
                if( !map.present( x, y ) ) {
                    b.setX( x );
                    b.setY( y );
                    panel.resizeCanvas();
                }
            }
        }
    }
//</editor-fold>

}
