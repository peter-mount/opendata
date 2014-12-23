/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.beans.PropertyChangeListener;

/**
 * A node within the map.
 * <p>
 * @author peter
 */
public class Node
        extends AbstractMapObject
{

    /**
     * The X Coordinate in the map
     */
    public static final String PROP_X = "x";
    /**
     * The Y Coordinate in the map
     */
    public static final String PROP_Y = "y";
    private int x;
    private int y;

    public Node()
    {
    }

    public Node( int x, int y, PropertyChangeListener l )
    {
        super( l );
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public final int getY()
    {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public final void setY( int y )
    {
        int oldY = this.y;
        this.y = y;
        firePropertyChange( PROP_Y, oldY, y );
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public final int getX()
    {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public final void setX( int x )
    {
        int oldX = this.x;
        this.x = x;
        firePropertyChange( PROP_X, oldX, x );
    }

}
