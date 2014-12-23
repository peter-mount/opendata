/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.function.Consumer;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 * A node within the map.
 * <p>
 * @author peter
 */
public abstract class MapObject
        implements PropertyChangeListener,
                   Consumer<MapVisitor>
{

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    public MapObject()
    {
    }

    public MapObject( PropertyChangeListener l )
    {
        addPropertyChangeListener( l );
    }

    public final PropertyChangeSupport getPropertyChangeSupport()
    {
        return propertyChangeSupport;
    }

    public final void addPropertyChangeListener( PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public final void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( propertyName, listener );
    }

    public final void removePropertyChangeListener( PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

    public final void removePropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( propertyName, listener );
    }

    protected final void firePropertyChange( String propertyName, Object oldValue, Object newValue )
    {
        if( oldValue != newValue ) {
            ThreadQueue.executeSwingLater( () -> propertyChangeSupport.firePropertyChange( propertyName, oldValue, newValue ) );
        }
    }

    protected final void firePropertyChange( String propertyName, int oldValue, int newValue )
    {
        if( oldValue != newValue ) {
            ThreadQueue.executeSwingLater( () -> propertyChangeSupport.firePropertyChange( propertyName, oldValue, newValue ) );
        }
    }

    protected final void firePropertyChange( String propertyName, boolean oldValue, boolean newValue )
    {
        if( oldValue != newValue ) {
            ThreadQueue.executeSwingLater( () -> propertyChangeSupport.firePropertyChange( propertyName, oldValue, newValue ) );
        }
    }

    protected final void firePropertyChange( PropertyChangeEvent event )
    {
        firePropertyChange( event.getPropertyName(), event.getOldValue(), event.getNewValue() );
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        // Pass it on
        firePropertyChange( evt );
    }

    @Override
    public abstract void accept( MapVisitor v );

}
