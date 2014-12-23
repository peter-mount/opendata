/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 * The actual editor/viewer panel
 * <p>
 * @author peter
 */
public class MapPanel
        extends JPanel
{

    private final MapMouseHandler mouseHandler;

    public MapPanel()
    {
        // refresh & repaint if berth added/removed or moved
        Project.INSTANCE.addPropertyChangeListener( SignalMap.PROP_NODES, this::refresh );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_X, this::refresh );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_Y, this::refresh );
        // Repaint if id or text changes
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_ID, e -> repaint() );
        Project.INSTANCE.addPropertyChangeListener( Berth.PROP_TEXT, e -> repaint() );

        // Resize if dimension changes or a new berth is added/removed
        Project.INSTANCE.addPropertyChangeListener( SignalMap.PROP_NODES, e -> resizeCanvas() );
        Project.INSTANCE.addPropertyChangeListener( SignalMap.PROP_DIMENSION, e -> resizeCanvas() );

        mouseHandler = new MapMouseHandler( this );
    }

    @Override
    protected void paintComponent( Graphics g1 )
    {
        //super.paintComponent( g );
        Graphics2D g = (Graphics2D) g1.create();
        try {
            Project.INSTANCE.getMap().
                    accept( new MapRenderer( getInsets(), getSize(), mouseHandler, g ) );
        }
        finally {
            g.dispose();
        }
    }

    private void refresh( PropertyChangeEvent e )
    {
        if( Project.CHANGED.test( e ) ) {
            resizeCanvas();
        }
    }

    public final void resizeCanvas()
    {
        ThreadQueue.executeSwing( () -> {
            // Get current state from within the Swing dispatch thread
            Dimension ms = getMinimumSize();
            Dimension pd = getPreferredSize();

            // Now hand over to a background thread to allow Swing to continue responding whilst we do thos
            ThreadQueue.executeLater( () -> {

                // Find the overall dimension of the map, i.e. contain's all berths
                Dimension nd = Project.INSTANCE.getMap().
                        getDimension();

                // Hand back to swing
                EventQueue.invokeLater( () -> {
                    setPreferredSize( nd );
                    // Tell the JScrollPane we have changed size
                    invalidate();
                    getParent().
                            validate();

                    // In either case do a repaint
                    repaint();
                } );
            } );
        } );
    }
}
