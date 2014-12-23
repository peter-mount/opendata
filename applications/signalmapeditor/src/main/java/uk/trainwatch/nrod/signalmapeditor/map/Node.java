/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import uk.trainwatch.nrod.signalmapeditor.Constants;

/**
 * A node within the map.
 * <p>
 * @author peter
 */
public abstract class Node
        extends MapObject
{

    /**
     * The Signal berth id
     */
    public static final String PROP_ID = "node_id";
    /**
     * The X Coordinate in the map
     */
    public static final String PROP_X = "node_x";
    /**
     * The Y Coordinate in the map
     */
    public static final String PROP_Y = "node_y";
    public static final String PROP_DIMENSION = "node_dimension";
    private String id;
    private int x;
    private int y;
    private volatile Dimension dimension;

    public Node( String id )
    {
        this.id = id;
    }

    public Node( String id, int x, int y, PropertyChangeListener l )
    {
        super( l );
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Node( String id, int x, int y )
    {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId( String id )
    {
        String oldId = this.id;
        this.id = id;
        firePropertyChange( PROP_ID, oldId, id );
    }

    public int getWidth()
    {
        return 1;
    }

    public int getHeight()
    {
        return 1;
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
        resetDimension();
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
        resetDimension();
        firePropertyChange( PROP_X, oldX, x );
    }

    /**
     * Returns the dimension for this Node
     * <p>
     * @return
     */
    public final Dimension getDimension()
    {
        if( dimension == null ) {
            resetDimension();
        }
        return dimension;
    }

    private void resetDimension()
    {
        Dimension oldDimension = dimension;
        dimension = new Dimension( Constants.getX( x + getWidth() + 1 ), Constants.getY( y + getHeight() + 1 ) );
        firePropertyChange( PROP_DIMENSION, oldDimension, dimension );
    }

    public final boolean contains( Point p )
    {
        return contains( (int) p.getX(), (int) p.getY() );
    }

    public boolean contains( int x, int y )
    {
        return x == this.x && y == this.y;
    }

}
