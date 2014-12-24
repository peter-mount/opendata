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

    public static final String PROP_FROM = "line_from";
    public static final String PROP_TO = "line_to";

    private Berth from;
    private Berth to;

    public Line( Berth from, Berth to )
    {
        super( from.getX(), from.getY() );
        this.from = from;
        this.to = to;
    }

    @Override
    public int getWidth()
    {
        return (int) (to.getDimension().
                      getWidth() - from.getDimension().
                      getWidth());
    }

    @Override
    public int getHeight()
    {
        return (int) (to.getDimension().
                      getHeight() - from.getDimension().
                      getHeight());
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }

    public Berth getFrom()
    {
        return from;
    }

    public void setFrom( Berth from )
    {
        Berth oldFrom = from;
        this.from = from;
        firePropertyChange( PROP_FROM, oldFrom, from );
    }

    public Berth getTo()
    {
        return to;
    }

    public void setTo( Berth to )
    {
        Berth oldTo = to;
        this.to = to;
        firePropertyChange( PROP_TO, oldTo, to );
    }

}
