/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.beans.PropertyChangeListener;

/**
 * A line segment
 * <p>
 * @author peter
 */
public class Line
        extends LineNode
{

    public Line()
    {
        super();
    }

    public Line( int x, int y )
    {
        super( x, y );
    }

    public Line( int x, int y, PropertyChangeListener l )
    {
        super( x, y, l );
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }

}
