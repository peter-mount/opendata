/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author peter
 */
public class Berth
        extends Node
{

    /**
     * The Signal berth id
     */
    public static final String PROP_ID = "berth_id";
    /**
     * The current occupier (not stored in the final xml)
     */
    public static final String PROP_TEXT = "berth_text";
    private String id;
    private transient String text;
    private final Set<Berth> outBerths = new HashSet<>();
    private final Set<Berth> inBerths = new HashSet<>();

    public Berth()
    {
    }

    public Berth( String id )
    {
        this.id = id;
    }

    public Berth( String id, int x, int y )
    {
        this( id );
        setX( x );
        setY( y );
    }

    public Berth( String id, int x, int y, PropertyChangeListener l )
    {
        super( x, y, l );
        this.id = id;
    }

    public Set<Berth> getOutBerths()
    {
        return outBerths;
    }

    public void forEachOutBerth( Consumer<Berth> c )
    {
        outBerths.forEach( c );
    }

    public Set<Berth> getInBerths()
    {
        return inBerths;
    }

    public void forEachInBerth( Consumer<Berth> c )
    {
        inBerths.forEach( c );
    }

    public void join( Berth to )
    {
        outBerths.add( to );
        to.inBerths.add( this );
    }

    public void unjoin( Berth to )
    {
        outBerths.remove( to );
        to.inBerths.remove( this );
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

}
