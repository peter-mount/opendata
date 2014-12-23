/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.ACCELERATOR_KEY;
import javax.swing.KeyStroke;

/**
 *
 * @author peter
 */
public class QuitAction
        extends AbstractAction
{

    public QuitAction()
    {
        super( "Quit" );
        putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( 'Q', InputEvent.CTRL_DOWN_MASK ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        // Check for project unsaved
        System.exit( 0 );
    }

}
