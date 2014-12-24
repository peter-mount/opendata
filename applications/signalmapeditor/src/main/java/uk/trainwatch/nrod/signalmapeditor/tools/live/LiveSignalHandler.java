/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.tools.live;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.nrod.signalmapeditor.map.Berth;
import uk.trainwatch.nrod.signalmapeditor.map.SignalMap;
import uk.trainwatch.nrod.signalmapeditor.utils.ThreadQueue;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
public enum LiveSignalHandler
{

    INSTANCE;

    private final Action startAction;
    private final Action stopAction;
    private final Timer timer;
    private boolean run;

    private LiveSignalHandler()
    {
        this.startAction = new StartStopAction( "Start", "Starts running live data through the map", null, true );
        this.stopAction = new StartStopAction( "Stop", "Stops running live data through the map", null, false );
        timer = new Timer( 10000, this::pollLive );
        timer.setInitialDelay( 0 );
    }

    public Action getStartAction()
    {
        return startAction;
    }

    public Action getStopAction()
    {
        return stopAction;
    }

    private void setRun( boolean run )
    {
        ThreadQueue.executeSwing( () -> {
            if( this.run != run ) {

                this.run = run;
                startAction.setEnabled( !run );
                stopAction.setEnabled( run );

                ThreadQueue.executeSwingLater( () -> {
                    if( run ) {
                        timer.start();
                    }
                    else {
                        timer.stop();
                        clearBerths();
                    }
                } );
            }
        } );
    }

    private void pollLive( ActionEvent e )
    {
        ThreadQueue.executeLater( () -> {
            try {
                SignalMap map = Project.INSTANCE.getMap();

                JsonObject state = getCurrentStatus( map.getArea() );

                ThreadQueue.executeSwingLater( () -> {
                    clearBerths();

                    JsonObject berths = state.getJsonObject( "berths" );
                    berths.forEach( ( id, text ) -> {
                        Berth berth = map.getBerth( id );
                        if( berth != null ) {
                            berth.setText( JsonUtils.getString( text ) );
                        }
                    } );

                    MainFrame.setStatus( "Updated live running feed..." );
                } );
            }
            catch( IOException ex ) {
                MainFrame.setStatus( "Live feed failed: " + ex.getMessage() );
                ex.printStackTrace();
                setRun( false );
            }
        } );
    }

    private JsonObject getCurrentStatus( String area )
            throws IOException
    {
        try( InputStream is = new URL( "http", "uktra.in", "/signal/data/occupiedBerths/" + area ).openConnection().
                getInputStream() ) {
            return Json.createReader( is ).
                    readObject();
        }
    }

    private void clearBerths()
    {
        Project.INSTANCE.getMap().
                streamBerths().
                forEach( b -> b.setText( null ) );
    }

    private class StartStopAction
            extends AbstractAction
    {

        private final boolean run;

        public StartStopAction( String name, String tip, KeyStroke key, boolean run )
        {
            super( name );
            this.run = run;
            if( tip != null ) {
                putValue( SHORT_DESCRIPTION, tip );
            }
            if( key != null ) {
                putValue( ACCELERATOR_KEY, key );
            }
            setEnabled( run );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            setRun( run );
        }
    }
}
