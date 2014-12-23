/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;

/**
 *
 * @author peter
 */
public enum Project
        implements PropertyChangeListener
{

    INSTANCE;

    /**
     * Predicate that returns true if the value in a {@link PropertyChangeEvent} has not changed
     */
    public static final Predicate<PropertyChangeEvent> UNCHANGED = e -> Objects.equals( e.getOldValue(), e.getNewValue() );
    /**
     * Predicate that returns true if the value in a {@link PropertyChangeEvent} has changed
     */
    public static final Predicate<PropertyChangeEvent> CHANGED = UNCHANGED.negate();

    public static final String PROP_CHANGED = "projectChanged";
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );
    private final SignalMap map;

    private boolean changed = false;

    @SuppressWarnings("LeakingThisInConstructor")
    private Project()
    {
        map = new SignalMap();
        map.addPropertyChangeListener( this );
    }

    public void newMap()
    {
        map.setArea( "MA" );
        try {
            map.setAuthor( System.getProperty( "user.name" ) + "@" + InetAddress.getLocalHost().
                    getHostName() );
        }
        catch( UnknownHostException ex ) {
            map.setAuthor( System.getProperty( "user.name" ) + "@localhost" );
        }
        map.setTitle( "Untitled" );
        map.setVersion( "1.0" );
        map.clear();
        setChanged( false );
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        System.out.println( evt.getPropertyName() + " " + evt.getOldValue() + " -> " + evt.getNewValue() );
        setChanged( true );

        // Pass it on
        propertyChangeSupport.firePropertyChange( evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }

    /**
     * Get the value of changed
     *
     * @return the value of changed
     */
    public boolean isChanged()
    {
        return changed;
    }

    /**
     * Set the value of changed
     *
     * @param changed new value of changed
     */
    public void setChanged( boolean changed )
    {
        boolean oldChanged = this.changed;
        this.changed = changed;
        propertyChangeSupport.firePropertyChange( PROP_CHANGED, oldChanged, changed );
    }

    /**
     * Similar to {@link #setChanged(boolean)} but only sets it
     * <p>
     */
    public void markAsChanged()
    {
        setChanged( true );
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( propertyName, listener );
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( propertyName, listener );
    }

    /**
     * Enables an action if we are changed
     * <p>
     * @param action
     */
    public void enableActionWhenChanged( Action action )
    {
        addPropertyChangeListener( PROP_CHANGED, e -> action.setEnabled( CHANGED.test( e ) ) );
        action.setEnabled( changed );
    }

    public void disableActionWhenChanged( Action action )
    {
        addPropertyChangeListener( PROP_CHANGED, e -> action.setEnabled( UNCHANGED.test( e ) ) );
        action.setEnabled( !changed );
    }

    public void addTextField( JTextField f, String property, Supplier<String> getter, Consumer<String> setter )
    {
        // Set the field to the initial value
        f.setText( getter.get() );

        // Set the field when the property changes
        addPropertyChangeListener( property, e -> f.setText( getter.get() ) );

        // Set the property when the field changes - i.e. enter pressed
        f.addActionListener( e -> setter.accept( f.getText() ) );

        // Also when focus is lost - i.e. tab out does not get any change
        f.addFocusListener( new FocusAdapter()
        {
            @Override
            public void focusLost( FocusEvent e )
            {
                setter.accept( f.getText() );
            }
        } );
    }

    public void addEditorPane( JEditorPane f, String property, Supplier<String> getter, Consumer<String> setter )
    {
        // Set the field to the initial value
        f.setText( getter.get() );

        // Set the field when the property changes
        addPropertyChangeListener( property, e -> f.setText( getter.get() ) );

        // Also when focus is lost - i.e. tab out does not get any change
        f.addFocusListener( new FocusAdapter()
        {
            @Override
            public void focusLost( FocusEvent e )
            {
                setter.accept( f.getText() );
            }
        } );
    }

    public boolean isPresent()
    {
        return map != null;
    }

    public SignalMap getMap()
    {
        return map;
    }

}
