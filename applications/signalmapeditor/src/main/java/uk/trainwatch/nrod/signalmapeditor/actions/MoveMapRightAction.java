/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class MoveMapRightAction
        extends AbstractAction
{

    public MoveMapRightAction()
    {
        super( "Move Map Right" );
        putValue( SHORT_DESCRIPTION, "Move everything in the map right one column" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        ThreadQueue.executeLater( () ->
        {
            Project.INSTANCE.getMap().
                    forEach( ( i, b ) -> b.setX( b.getX() + 1 ) );

            ThreadQueue.executeSwingLater( () -> MainFrame.frame.repaint() );
        } );
    }

}
