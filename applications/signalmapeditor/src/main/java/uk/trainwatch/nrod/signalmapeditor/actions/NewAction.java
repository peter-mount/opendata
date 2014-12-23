/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import uk.trainwatch.nrod.signalmapeditor.Project;

/**
 *
 * @author peter
 */
public class NewAction
        extends AbstractAction
{

    public NewAction()
    {
        super( "Name" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        Project.INSTANCE.newMap();
    }

}
