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
public class MoveMapDownAction
        extends AbstractAction
{

    public MoveMapDownAction()
    {
        super( "Move Map Down" );
        putValue( SHORT_DESCRIPTION, "Move everything in the map down one row" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        ThreadQueue.executeLater( () ->
        {
            Project.INSTANCE.getMap().
                    forEach( ( i, b ) -> b.setY( b.getY() + 1 ) );

            ThreadQueue.executeSwingLater( () -> MainFrame.frame.repaint() );
        } );
    }

}
