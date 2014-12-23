/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.beans.PropertyChangeListener;
import java.util.UUID;

/**
 * A line segment
 * <p>
 * @author peter
 */
public abstract class LineNode
        extends Node
{

    public LineNode()
    {
        super( UUID.randomUUID().
                toString() );
    }

    public LineNode( int x, int y )
    {
        super( UUID.randomUUID().
                toString(), x, y );
    }

    public LineNode( int x, int y, PropertyChangeListener l )
    {
        super( UUID.randomUUID().
                toString(), x, y, l );
    }

}
