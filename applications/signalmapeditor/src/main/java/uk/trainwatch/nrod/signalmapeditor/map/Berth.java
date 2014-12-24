/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import uk.trainwatch.nrod.signalmapeditor.Constants;

/**
 *
 * @author peter
 */
public class Berth
        extends Node
{

    /**
     * The current occupier (not stored in the final xml)
     */
    public static final String PROP_TEXT = "berth_text";
    private transient String text;
    private final Set<LineNode> outBerths = new HashSet<>();

    public Berth( String id )
    {
        super( id );
    }

    public Berth( String id, int x, int y )
    {
        super( id, x, y );
    }

    public Berth( String id, int x, int y, PropertyChangeListener l )
    {
        super( id, x, y, l );
    }

    public Set<? super LineNode> getOutBerths()
    {
        return outBerths;
    }

    public void forEachOutBerth( Consumer<? super LineNode> c )
    {
        outBerths.forEach( c );
    }

    public Stream<? super LineNode> streamOutBerths()
    {
        return outBerths.stream();
    }

    public void join( LineNode line )
    {
        outBerths.add( line );
    }

    public void unjoin( LineNode to )
    {
        outBerths.remove( to );
    }

    /**
     * Get the value of text
     *
     * @return the value of text
     */
    public String getText()
    {
        return text;
    }

    /**
     * Set the value of text
     *
     * @param text new value of text
     */
    public void setText( String text )
    {
        String oldText = this.text;
        this.text = text;
        firePropertyChange( PROP_TEXT, oldText, text );
    }

    @Override
    protected Rectangle createRectangle()
    {
        // This represents the bounds of the berth not the actual grid
        return new Rectangle( Constants.getX( getX() ) + 10, Constants.getY( getY() ) + 5,
                              (getWidth() * Constants.COLUMN_WIDTH) - 20, (getHeight() * Constants.ROW_HEIGHT) - 10 );
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }
}
