/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class SaveAction
        extends AbstractAction
{

    public SaveAction()
    {
        super( "Save" );
        putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( 'S', InputEvent.CTRL_DOWN_MASK ) );
        Project.INSTANCE.enableActionWhenChanged( this );

        // Enabled if changed 
        Project.INSTANCE.addPropertyChangeListener( SignalMap.PROP_FILE, e -> setEnabled( e.getNewValue() != null ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        SignalMap map = Project.INSTANCE.getMap();
        ThreadQueue.executeLater( () -> map.save() );
    }

    @Override
    public void setEnabled( boolean newValue )
    {
        super.setEnabled( Project.INSTANCE.getMap().
                getFile() != null && newValue );
    }

    
}
