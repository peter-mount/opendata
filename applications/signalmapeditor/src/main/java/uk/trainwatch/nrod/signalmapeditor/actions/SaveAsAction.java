/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import javax.swing.AbstractAction;
import static javax.swing.Action.ACCELERATOR_KEY;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class SaveAsAction
        extends AbstractAction
{

    public SaveAsAction()
    {
        super( "Save As" );
        putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( 'A', InputEvent.CTRL_DOWN_MASK ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( MainFrame.fileChooser.showSaveDialog( MainFrame.frame ) == JFileChooser.APPROVE_OPTION ) {
            File file = MainFrame.fileChooser.getSelectedFile();
            SignalMap map = Project.INSTANCE.getMap();
            map.setFile( file );
            // Run the import in a background thread, leaving Swing to run the UI
            ThreadQueue.executeLater( () -> map.save() );
        }
    }
}
