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
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.map.OpenMap;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class OpenAction
        extends AbstractAction
{

    public OpenAction()
    {
        super( "Open" );
        putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( 'O', InputEvent.CTRL_DOWN_MASK ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( MainFrame.fileChooser.showOpenDialog( MainFrame.frame ) == JFileChooser.APPROVE_OPTION ) {
            SignalMap map = Project.INSTANCE.getMap();
            map.setFile( MainFrame.fileChooser.getSelectedFile() );
            ThreadQueue.executeLater( new OpenMap( map ) );
        }
    }

}
