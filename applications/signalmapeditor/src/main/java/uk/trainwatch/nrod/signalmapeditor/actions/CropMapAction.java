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
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;

/**
 *
 * @author peter
 */
public class CropMapAction
        extends AbstractAction
{

    public CropMapAction()
    {
        super( "Crop map" );
        putValue( SHORT_DESCRIPTION, "Crops the map so there's no wasted space around the borders" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        ThreadQueue.executeLater( () -> {
            SignalMap map = Project.INSTANCE.getMap();

            // Find the minimum X & Y coordinates
            int minX = map.streamBerths().
                    map( Berth::getX ).
                    min( Integer::compare ).
                    orElse( 0 );

            int minY = map.streamBerths().
                    map( Berth::getY ).
                    min( Integer::compare ).
                    orElse( 0 );

            // Move all nodes up and left by that amount
            map.forEach( ( i, b ) -> {
                b.setX( b.getX() - minX );
                b.setY( b.getY() - minY );
            } );

            // Reset the map dimension allowing for the reduced size
            map.resetDimension( true );
        } );
    }

}
